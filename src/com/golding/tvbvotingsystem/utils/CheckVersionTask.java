package com.golding.tvbvotingsystem.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.golding.tvbcloud.Upgrade;
import com.golding.tvbvotingsystem.TVBLogoActivity;
import com.golding.tvbvotingsystem.TVBVotingApp;
import com.golding.tvbvotingsystem.bean.UpdataInfo;
import com.golding.tvbvotingsystem.contant.Contant;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jallen on 2017/12/23 0023 09:08.
 */

public class CheckVersionTask  implements Runnable{
    private static final String TAG = "CheckVersionTask";
    private Upgrade.CUpgrade mUpgradeInfo;
    private Context mContext;
    private static final int UPDATA_CLIENT = 0;
    private static final int UPDATA_ERROR = 1;
    private static final int DOWNLOAD_ERROR = 2;
    public static boolean isDownLoad = false;

    public CheckVersionTask(Context ct,Upgrade.CUpgrade upgradeInfo) {
        mContext = ct;
        mUpgradeInfo = upgradeInfo;
		isDownLoad = true;
    }

    public void run() {
        try {
            Message msg = new Message();
            msg.what = UPDATA_CLIENT;
            msg.obj = mUpgradeInfo.getDownloadUrl();
            handler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            Message msg = new Message();
            msg.what = UPDATA_ERROR;
            handler.sendMessage(msg);
        }
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_CLIENT:
                    String url = (String) msg.obj;
                    downLoadFile(url);
                    break;
                case UPDATA_ERROR:
                    //服务器超时
                    isDownLoad = false;
                    Log.e(TAG,"获取服务器更新信息失败");
                    break;
                case DOWNLOAD_ERROR:
                    //下载apk失败
                    isDownLoad = false;
                    Log.e(TAG,"下载新版本失败");
                    Utils.ToastMessage(TVBLogoActivity.mInstance,"Download error");
                    break;
            }
        }
    };


      /*
     * 从服务器中下载APK
     */
    protected void downLoadFile(final String url) {
        final ProgressDialog pd;	//进度条对话框
        pd = new  ProgressDialog(mContext);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    File file;
                    if(url.contains(".zip")){
                        file = Utils.getFileFromServer(url, pd,"update.zip");
                        sleep(3000);
                        updateSystem(file);
						isDownLoad = false;
                    }else{
                        file = Utils.getFileFromServer(url, pd,"GoldingTVBVoting.apk");
                        sleep(3000);
                        installApk(file);
						isDownLoad = false;
                    }
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Log.e(TAG,"下载新版本失败");
                    e.printStackTrace();
                    pd.dismiss(); //结束掉进度条对话框
                    Message msg = new Message();
                    msg.what = DOWNLOAD_ERROR;
                    handler.sendMessage(msg);
                }
                
            }}.start();
    }

    //安装apk  /storage/emulated/0/GoldingTVBVoting.apk
    protected void installApk(File file) {
        if(file == null || !mUpgradeInfo.getMD5().equals(Utils.getFileMD5(file))){
            Log.e(TAG,"下载新版本失败");
            return;
        }
        Log.d(TAG,"下载新版本成功:"+file.getAbsolutePath());
        try {
            TVBVotingApp.getInstance().getIgoldingSysService().installApk(file.getAbsolutePath());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //update  /storage/emulated/0/update.zip
    protected void updateSystem(File file) {
        if(file == null){
            Log.e(TAG,"下载新版本失败");
            return;
        }
        Log.d(TAG,"下载新版本成功:path="+file.getPath());
        try {
            TVBVotingApp.getInstance().getIgoldingSysService().updateSystem(file.getAbsolutePath(),mUpgradeInfo.getMD5());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}