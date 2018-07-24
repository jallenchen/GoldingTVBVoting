package com.golding.tvbvotingsystem;

import java.util.HashMap;
import java.util.Locale;

import com.golding.tvbcloud.VotingResultInfo.*;
import com.golding.tvbcloud.VotingInfo.*;
import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.socket.SocketService;
import com.golding.tvbvotingsystem.utils.BitmapCache;
import com.golding.tvbvotingsystem.utils.Utils;
import com.golding.tvbvotingsystem.utils.CrashHandler;


import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import aidl.IGoldingSysService;

/** 
 * @author  :Jallen 
 * @date    :
 * @version :1.0 
 * @desc :
 */
@SuppressLint("UseSparseArrays")
public class TVBVotingApp extends Application {
	private static TVBVotingApp mInstance;
	private SocketService mService;
    private HashMap<Integer, CContestantRecord> ContestantRecordMap = new HashMap<Integer,  CContestantRecord>();
	private BitmapCache bitmapCache = new BitmapCache();
	public CVotingInfo mVotingInfo;
	public IGoldingSysService IgoldingSysService;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		Contant.seatNum = Utils.getSeatNum();
		if(Contant.seatNum.equals("")){
			SharedPreferences mShare = getSharedPreferences(Contant.KEY_SHARE_NAME, Context.MODE_PRIVATE);
			Contant.seatNum = mShare.getString("seatNum","");
		}
		Utils.changeLanguage(Locale.TAIWAN);
        CrashHandler.getInstance().init(mInstance);
		//mVotingInfo = testData();
		startAidlService();
	}

	private void startAidlService(){
		try {
			Intent intent = new Intent();
			intent.setAction("com.goldingmedia.basesystemservice.BaseTVBServer");
			Intent eintent = new Intent(Utils.createExplicitFromImplicitIntent(this,intent));
			bindService(eintent,mServiceConnection, Service.BIND_AUTO_CREATE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			IgoldingSysService = IGoldingSysService.Stub.asInterface(service);
			Log.e("TVBVotingApp", "onServiceConnected");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.e("TVBVotingApp", "onServiceDisconnected");
			IgoldingSysService = null;
		}
	};

	private int waitCnt = 0;
	public IGoldingSysService getIgoldingSysService(){
		if(IgoldingSysService == null){
			startAidlService();
		}
		while(IgoldingSysService == null){
			try {
			Thread.sleep(3000);  //wait connect finish
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
			if(waitCnt >3){
				waitCnt = 0;
				break;
			}
		
		}
		
		return IgoldingSysService;
	}
	
	
	public static TVBVotingApp getInstance(){
		return mInstance;
	}
	
	public void setSocketService(SocketService service){
		this.mService = service;
	}
	
	public SocketService getSocketService(){
		if(this.mService == null){
			this.mService = new SocketService();
		}
		return this.mService;
	}
	
	public void setVotingInfo(CVotingInfo votingInfo) {
        mVotingInfo = votingInfo;
	}
	
	public CVotingInfo getVotingInfo() {
		return mVotingInfo;
	}

    public HashMap<Integer, CContestantRecord> getContestantRecordMap() {
        if(ContestantRecordMap == null) {
			ContestantRecordMap = new HashMap<Integer, CContestantRecord>();
        }
        return ContestantRecordMap;
    }

    public void setContestantRecordMap(HashMap<Integer, CContestantRecord> mRecordValueMap) {
        this.ContestantRecordMap = mRecordValueMap;
    }

	public BitmapCache getBitmapCache(){
		if(bitmapCache == null){
			bitmapCache =  new BitmapCache();
		}
		return bitmapCache;
	}
	
	public void clearData(){
		ContestantRecordMap.clear();
		TVBVotingActivity.players.clear();
		this.bitmapCache = null;
        mVotingInfo = null;
        Utils.PrintLogE("App","---clearData()");
	}
}
