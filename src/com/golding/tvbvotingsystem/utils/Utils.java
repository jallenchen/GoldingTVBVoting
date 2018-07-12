package com.golding.tvbvotingsystem.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import com.bumptech.glide.signature.StringSignature;
import com.bumptech.glide.Glide;
import com.golding.tvbvotingsystem.R;
import com.golding.tvbvotingsystem.TVBVotingApp;
import com.golding.tvbvotingsystem.contant.Contant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.SystemProperties;

/**
 * @author :Jallen
 * @date ：2017-6-16 2:39:06
 * @version :1.0
 * @desc :
 */
public class Utils {
	private static String TAG = "Utils";
	private static boolean logTag = true;
	private static Toast toast = new Toast(TVBVotingApp.getInstance());
	public static void PrintLogE(String tag, String txt) {
		if (logTag) {
			Log.e(tag, txt);
		}
	}
	
	public static void PrintLogD(String tag, String txt) {
		if (logTag) {
			Log.d(tag, txt);
		}
	}

	public static void ToastMessage(FragmentActivity ct,int resid){
		LayoutInflater inflater = ct.getLayoutInflater();//调用Activity的getLayoutInflater()
		View view = inflater.inflate(R.layout.toast, null); //加載layout下的布局
		TextView messages = (TextView) view.findViewById(R.id.tvToast);
		messages.setText(resid); //toast内容
		toast.setGravity(Gravity.CENTER, 12, 20);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
		toast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
		toast.setView(view); //添加视图文件
		toast.show();

	}
	public static void ToastMessage(FragmentActivity ct,String txt){
		LayoutInflater inflater = ct.getLayoutInflater();//调用Activity的getLayoutInflater()
		View view = inflater.inflate(R.layout.toast, null); //加載layout下的布局
		TextView messages = (TextView) view.findViewById(R.id.tvToast);
		messages.setText(txt); //toast内容
		toast.setGravity(Gravity.CENTER, 12, 20);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
		toast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
		toast.setView(view); //添加视图文件
		toast.show();

	}

	public static String countTimeToString(int totalsec) {
		String timeTxt ;
		int min = totalsec / 60;
		int sec = totalsec % 60;
		timeTxt = String.format("%02d", min) + ":" + String.format("%02d", sec);
		return timeTxt;
	}
	/**
	 * 校验评分是否符合要求(0.0-10)
	 * 
	 * @param score
	 * @return boolean
	 */
	public static boolean checkScoring(String score) {
		if (score.matches("^(([4-9])([0-9]{1}))|100$")) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 获取终端系列号
	 * @return 返回终端系列号
	 */
	public static String getSerialNumber() {
		return Build.SERIAL;
	}
	
	
	/**
	 * 判断网络是否可用
	 * @return
	 */
	public static boolean isNetConnect() {
        ConnectivityManager connectivity = (ConnectivityManager) TVBVotingApp.getInstance()
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

	public static String getLocalHostIp() {
		String ipaddress = "";
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();
				Enumeration<InetAddress> inet = nif.getInetAddresses();
				while (inet.hasMoreElements()) {
					InetAddress ip = inet.nextElement();
					//if (!ip.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ip.getHostAddress())) {
						if (!ip.isLoopbackAddress() && (ip instanceof Inet4Address)) {
						ipaddress = ip.getHostAddress();
						PrintLogD(TAG,ipaddress);
						return ipaddress;
					}
				}
			}
		} catch(SocketException e) {
			e.printStackTrace();
		}
		return ipaddress;
	}

	public static  void setImage(String url,ImageView view){
		Glide.with(TVBVotingApp.getInstance()).load(url).signature(new StringSignature(Contant.SystenTime)).into(view);
	}
	
	
	public static boolean setSeatNum2File(String seatnum){
		String path = Contant.getSeatNumFolderTxt();
		FileUtils file = new FileUtils();
		
		return file.writeTxtToFile(seatnum, path, Contant.fILE_SEATNUM);
	}


	public static String getSeatNum() {
		String path = Contant.getSeatNumFolderTxt();
		String fileName = path+Contant.fILE_SEATNUM;
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			//res = EncodingUtils.getString(buffer, "UTF-8");
			res = new String(buffer, "UTF-8");
			fin.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 需要系统权限，带系统签名后完成 2017-6-17
	 * 
	 * @param type
	 *            void
	 */
	public static void changeLanguage(Locale type) {
		  Configuration config = TVBVotingApp.getInstance().getResources().getConfiguration();
          DisplayMetrics dm = TVBVotingApp.getInstance().getResources() .getDisplayMetrics();
          config.locale = type;
          TVBVotingApp.getInstance().getResources().updateConfiguration(config, dm);
	}
	
	public static void confirmDialog(final FragmentActivity activity) throws Exception{
		LayoutInflater inflaterDl = LayoutInflater.from(activity);
        RelativeLayout layout = (RelativeLayout)inflaterDl.inflate(R.layout.dialog_submit, null );
       
        //对话框
        final Dialog dialog = new AlertDialog.Builder(activity).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(false);
        
        //取消按钮
        Button btnCancel = (Button) layout.findViewById(R.id.cancel);
        btnCancel.setOnClickListener(new OnClickListener() {
         
          @Override
          public void onClick(View v) {
        	  dialog.dismiss();
          }
        });
       
       
        //确定按钮
        Button btnOK = (Button) layout.findViewById(R.id.confirm);
        btnOK.setOnClickListener(new OnClickListener() {
         
          @Override
          public void onClick(View v) {
        	  dialog.dismiss();
        	  activity.setResult(-1);
        	  activity.finish();           
          }
        });
	}
	public static void errorDialog(final FragmentActivity activity) throws Exception{
		LayoutInflater inflaterDl = LayoutInflater.from(activity);
		RelativeLayout layout = (RelativeLayout)inflaterDl.inflate(R.layout.dialog_submit, null );

		//对话框
		final Dialog dialog = new AlertDialog.Builder(activity).create();
		dialog.show();
		dialog.getWindow().setContentView(layout);
		dialog.setCanceledOnTouchOutside(false);

		TextView txt = (TextView) layout.findViewById(R.id.dialog_tx);
		txt.setText(R.string.scoring_error);
		//取消按钮
		Button btnCancel = (Button) layout.findViewById(R.id.cancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		//确定按钮
		Button btnOK = (Button) layout.findViewById(R.id.confirm);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				activity.getSupportFragmentManager().popBackStack();
			}
		});
	}
	/**
	 * 返回 指定的 String 是否是 有效的 IP 地址.
	 */
	public static boolean isValidIpAddress(String value) {

		int start = 0;
		int end = value.indexOf('.');
		int num = 0;

		while (start < value.length()) {
			if (-1 == end) {
				end = value.length();
			}

			try {
				int block = Integer.parseInt(value.substring(start, end));
				if ((block > 255) || (block < 0)) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
			num++;
			start = end + 1;
			end = value.indexOf('.', start);
		}
		return num == 4;
	}

	/**
	 * 返回指定的座位是否有效  ^[A-Z0-9]+$
	 */
	public static boolean isValidSeatNum(String value) {
		if (value.matches("^(([A-P])([0-3])([0-9]))|([A-P])40$") && !value.matches("^([A-P])00$")) {
			return true;
		} else {
			return false;
		}
	}

	public static File getFileFromServer(String path, ProgressDialog pd,String filename) throws Exception{
		//如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			URL url = new URL(path);
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			//获取到文件的大小
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(), filename);
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len ;
			int total=0;
			while((len =bis.read(buffer))!=-1){
				fos.write(buffer, 0, len);
				total+= len;
				//获取当前下载量
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		}
		else{
			return null;
		}
	}

	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public static String getAppVersionName(Context ct) {
		try {
			PackageManager manager = ct.getPackageManager();
			PackageInfo info = manager.getPackageInfo(ct.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getSysOsVersion(){
		return SystemProperties.get("ro.product.firmware", "Unknown");
		//return  android.os.Build.VERSION.RELEASE;
	}

	public static String getFileMD5(File file) {
		if (file == null || !file.isFile() || !file.exists()) {
			return "";
		}
		FileInputStream in = null;
		String result = "";
		byte buffer[] = new byte[8192];
		int len;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer)) != -1) {
				md5.update(buffer, 0, len);
			}
			byte[] bytes = md5.digest();

			for (byte b : bytes) {
				String temp = Integer.toHexString(b & 0xff);
				if (temp.length() == 1) {
					temp = "0" + temp;
				}
				result += temp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(null!=in){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
		public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
		// Retrieve all services that can match the given intent
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

		// Make sure only one match was found
		if (resolveInfo == null || resolveInfo.size() != 1) {
			return null;
		}

		// Get component info and create ComponentName
		ResolveInfo serviceInfo = resolveInfo.get(0);
		String packageName = serviceInfo.serviceInfo.packageName;
		String className = serviceInfo.serviceInfo.name;
		ComponentName component = new ComponentName(packageName, className);

		// Create a new intent. Use the old one for extras and such reuse
		Intent explicitIntent = new Intent(implicitIntent);

		// Set the component to be explicit
		explicitIntent.setComponent(component);

		return explicitIntent;
	}

	public static  boolean isNetworkOnline() {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process ipProcess = runtime.exec("ping -c 3 114.114.114.114");
			int exitValue = ipProcess.waitFor();
			return (exitValue == 0);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

}
