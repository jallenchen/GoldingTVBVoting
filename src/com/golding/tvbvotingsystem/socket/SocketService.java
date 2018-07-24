package com.golding.tvbvotingsystem.socket;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bumptech.glide.signature.StringSignature;
import com.bumptech.glide.Glide;
import com.golding.tvbcloud.Logon.CLogon;
import com.golding.tvbcloud.Command.*;
import com.golding.tvbcloud.Upgrade.*;
import com.golding.tvbcloud.VotingInfo.*;
import com.golding.tvbvotingsystem.TVBLogoActivity;
import com.golding.tvbvotingsystem.TVBVotingApp;
import com.golding.tvbvotingsystem.bean.EventBusCMD;
import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.interfaces.IStopVotingCmd;
import com.golding.tvbvotingsystem.interfaces.IVotingInfoDisplay;
import com.golding.tvbvotingsystem.utils.CheckVersionTask;
import com.golding.tvbvotingsystem.utils.HandlerUtils;
import com.golding.tvbvotingsystem.utils.Utils;

import android.app.Service;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import de.greenrobot.event.EventBus;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * @author :Jallen
 * @date ：2017-6-21 上午10:33:29
 * @version :1.0
 * @desc :
 */
public class SocketService extends Service implements HandlerUtils.OnReceiveMessageListener{
	private static final String TAG = "SocketService";
	private static IVotingInfoDisplay iVotingInfo;
	private static IStopVotingCmd iVotingStop;
	public WebSocketConnection mConnect = new WebSocketConnection();
	private static int RECONNECTINGTIME = 5000;
	public static boolean isVoting = false;
	public UDPClient client;
	public ExecutorService exec;
	private HandlerUtils.HandlerHolder mHandlerHolder;
	private final ISocketService.Stub iSocketImpl = new ISocketService.Stub() {
//		@Override
//		public boolean sendMessage(String message) throws RemoteException {
//			return sendRespMessage(message);
//		}


		@Override
		public boolean sendByteMessage(int category, int categorySub, byte[] data) throws RemoteException {
			// TODO Auto-generated method stub
			return sendProtoMessage(category,categorySub,data);
		}
	};

	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return (IBinder) iSocketImpl;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Utils.PrintLogD(TAG, "SocketService onCreate");
		SharedPreferences share = getSharedPreferences(Contant.KEY_SHARE_NAME, Context.MODE_PRIVATE);
		Contant.address = share.getString("serverUrl", Contant.address);
		mHandlerHolder = new HandlerUtils.HandlerHolder(this);
		initSocket();
		TVBVotingApp.getInstance().setSocketService(this);

		//建立线程池  调用
		exec = Executors.newCachedThreadPool();
		client = new UDPClient();
		exec.execute(client);
	}

	public static void setVotingInfoLisenter(IVotingInfoDisplay lisenter){
		iVotingInfo = lisenter;
	}
	
	
	public static void setVotingStopLisenter(IStopVotingCmd lisenter){
		iVotingStop = lisenter;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	public synchronized void initSocket() {
		if(Contant.address == null || Contant.address.equals("")){
			return;
		}
		if(mConnect != null && mConnect.isConnected()){
            mConnect.disconnect();
            return;
        }
        try {
            Thread.sleep(1000);  //wait connect finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		if(mConnect == null){
			mConnect = new WebSocketConnection();
		}

		try {
			mConnect.connect(Contant.address, new WebSocketHandler() {

				@Override
				public void onClose(int code, String reason) {
					super.onClose(code, reason);
					EventBus.getDefault().post(new EventBusCMD(Contant.CHECKNET_NG));
					client.setUdpLife(true);
					Utils.PrintLogD(TAG, "----onClose mConnect： " + mConnect);
					exec.execute(client);
					Utils.PrintLogE(TAG,"Status:onClose:" +reason);
					mHandlerHolder.sendEmptyMessageDelayed(0, RECONNECTINGTIME);
				}

				@Override
				public void onOpen() {
					super.onOpen();
					EventBus.getDefault().post(new EventBusCMD(Contant.CHECKNET_OK));
					client.setUdpLife(false);
					Utils.PrintLogD(TAG, "----onOpen mConnect： " + mConnect);
					Utils.PrintLogE(TAG, "Status:Connect to " + Contant.address);
					sendProtoMessage(Contant.CATEGORY_CLIENT_INFO_ID,Contant.PROPERTY_CLIENT_LOGIN_ID,login().build().toByteArray());
				}

				@Override
				public void onTextMessage(String payload) {
					super.onTextMessage(payload);
				}

				@Override
				public void onBinaryMessage(byte[] payload) {
					super.onBinaryMessage(payload);
					reciverData(payload);
					Utils.PrintLogE(TAG, "onBinaryMessage："+payload.length);

				}

			});
		} catch (WebSocketException e) {
			e.printStackTrace();
		}
	}

	private CLogon.Builder login(){
		CLogon.Builder login =  CLogon.newBuilder();
		login.setDevId(Utils.getSerialNumber());
		login.setSeatNum(Contant.seatNum);
		login.setTerminal(1);              //1.android device
		login.setIp(Utils.getLocalHostIp());
		login.setApkVer(Utils.getAppVersionName(TVBLogoActivity.mInstance));
		login.setSysVer(Utils.getSysOsVersion());
		return login;
	}
	
	private void reConnect(){
		initSocket();
	}



	
	private void reciverData(byte[] data) {
		int category = data[0];
		int categorySub = data[1];
		byte[] proto = new  byte[data.length - 3];
		
		System.arraycopy(data, 3, proto, 0, data.length - 3);
		try {
			parserData(category,categorySub,proto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void parserData(int category,int categorySub,byte[] protoData) throws Exception {
		Utils.PrintLogD(TAG,"category:"+category+"  categorySub:"+categorySub);
		switch (category) {
		case Contant.CATEGORY_VOTING_ID:
			if(categorySub == Contant.PROPERTY_VOTING_CMD_ID) {
				parseResquestCMD(protoData);
			}else if(categorySub == Contant.PROPERTY_VOTING_INFO_ID) {
				parseVotingInfo(protoData);
			}
			break;

		case Contant.CATEGORY_VOTING_RESPOND_ID:
			if(categorySub == Contant.PROPERTY_RESPOND_ID) {
			}
			break;
		case Contant.CATEGORY_CLIENT_INFO_ID:
			if(categorySub == Contant.PROPERTY_CLIENT_UPGRADE_ID) {
				paeseUpgrade(protoData);
			}
			break;
		default:
			break;
		}
	}
	
	private String updateMd5="";

	/**
	 * 升级更新
	 */
	private void paeseUpgrade(byte[] data) throws Exception{
		CUpgrade upgradeInfo = CUpgrade.parseFrom(data);
	
		//判断发送的apk版本是否一样
		 if(mUpgradeInfo.getUpdateType().equals("apk")
			 && mUpgradeInfo.getVersion().equals(Utils.getAppVersionName(TVBLogoActivity.mInstance))){
				  Log.d(TAG,"版本号相同无需升级");
				 return;
		 }
		 
		 //判断发送的系统版本是否一样
		  if(mUpgradeInfo.getUpdateType().equals("system")
			 && mUpgradeInfo.getVersion().equals(Utils.getSysOsVersion())){
				  Log.d(TAG,"版本号相同无需升级");
				 return;
		 }
		 
		 //判断发送过来的升级文件的md5是否和上一次发送的一致
		if(mUpgradeInfo.getMD5().equals(updateMd5)){
			 Log.d(TAG,"MD5相同无需处理");
			return;
		}else{
			updateMd5 = mUpgradeInfo.getMD5();
		}
		
		//判断是否已经在下载中
		if(CheckVersionTask.isDownLoad){
			Utils.PrintLogD("paeseUpgrade","new version id downloading");
			return;
		}
		Utils.PrintLogD("paeseUpgrade","new version is "+upgradeInfo.getVersion());
		Thread thread = new Thread(new CheckVersionTask(TVBLogoActivity.mInstance,upgradeInfo));
		thread.start();
	}

	private void parseVotingInfo(byte[] data) throws Exception {
		TVBVotingApp.getInstance().clearData();
		CVotingInfo votingInfo = CVotingInfo.parseFrom(data);
		TVBVotingApp.getInstance().setVotingInfo(votingInfo);
		saveImageCache(votingInfo);
		iVotingInfo.setVotingInfoDisplay(votingInfo.getActivityInfo().getVotingName(), votingInfo.getActivityInfo().getVotingLogoL());
	}
	
	private void parseResquestCMD(byte[] data) throws Exception {
		CCommand votingCMD = CCommand.parseFrom(data);
		Utils.PrintLogE(TAG,"cmd:"+votingCMD.getCommandCode());
		if(votingCMD.getCommandCode() == Contant.VOTING_START) {
			if(TVBVotingApp.getInstance().getVotingInfo() != null && !isVoting) {
				iVotingInfo.startVoting();
				isVoting = true;
			}else {
			}
		}else if(votingCMD.getCommandCode() == Contant.VOTING_STOP){
			iVotingStop.stopVoting();
			isVoting = false;
		}else if(votingCMD.getCommandCode() == Contant.VOTING_CHECKPSW) {
			if(!votingCMD.getCommandParam().equals(Contant.SUCESS)) {
			}
		}else if(votingCMD.getCommandCode() == Contant.VOTING_DIAGNOSE) {
			sendProtoMessage(Contant.CATEGORY_CLIENT_INFO_ID,Contant.PROPERTY_CLIENT_LOGIN_ID,login().build().toByteArray());
		}
	}

    private void saveImageCache(CVotingInfo votingservice){
		//LoadImageFromServer loadImageFromServer = new LoadImageFromServer();
		//new LoadImageFromServer().getImageData(votingservice.getActivityInfo().getVotingName(), votingservice.getActivityInfo().getVotingLogoL(),"L", null);
		//new LoadImageFromServer().getImageData(votingservice.getActivityInfo().getVotingName(), votingservice.getActivityInfo().getVotingLogoS(),"S", null);
        Contant.SystenTime = System.nanoTime()+"";
		Glide.with(TVBVotingApp.getInstance()).load(votingservice.getActivityInfo().getVotingLogoL()).signature(new StringSignature(Contant.SystenTime));
		Glide.with(TVBVotingApp.getInstance()).load(votingservice.getActivityInfo().getVotingLogoS()).signature(new StringSignature(Contant.SystenTime));
		List<CVotingPlayer> players = votingservice.getPlayersList();
		Utils.PrintLogE(TAG,"players:"+players.size());
		for(int i = 0; i< players.size(); i++){
			//new LoadImageFromServer().getImageData(players.get(i).getName(), players.get(i).getImageS(), "S",null);
			//new LoadImageFromServer().getImageData(players.get(i).getName(), players.get(i).getImageL(), "L",null);
			Glide.with(TVBVotingApp.getInstance()).load(players.get(i).getImageL()).signature(new StringSignature(Contant.SystenTime));
			Glide.with(TVBVotingApp.getInstance()).load(players.get(i).getImageS()).signature(new StringSignature(Contant.SystenTime));
		}
	}
/*
*大类-子类-路由-proto
 */
	private boolean sendProtoMessage(int category,int categorySub,byte[] proto) {
		if (mConnect.isConnected()) {
			byte[] message = new byte[proto.length+3];
			message[0] = (byte) category;
			message[1] =  (byte) categorySub;
			message[2] = 0x01;
			
			System.arraycopy(proto, 0, message, 3, proto.length);
			
			mConnect.sendBinaryMessage(message);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void handlerMessage(Message msg) {
		reConnect();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mConnect.disconnect();
		client.setUdpLife(false);
		Utils.PrintLogD(TAG, "SocketService onDestroy");
	}

}
