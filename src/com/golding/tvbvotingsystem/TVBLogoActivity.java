package com.golding.tvbvotingsystem;

import com.golding.tvbcloud.VotingResultInfo.*;
import com.golding.tvbvotingsystem.bean.EventBusCMD;
import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.interfaces.IVotingInfoDisplay;
import com.golding.tvbvotingsystem.socket.ISocketService;
import com.golding.tvbvotingsystem.socket.SocketService;
import com.golding.tvbvotingsystem.ui.MTextView;
import com.golding.tvbvotingsystem.utils.PermissionUtils;
import com.golding.tvbvotingsystem.utils.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collection;
import java.util.Iterator;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class TVBLogoActivity extends TVBBaseActivity implements
		IVotingInfoDisplay {
	private static final String TAG = "TVBLogoActivity";

	/** The Messenger for sending commands to the Service */

	private ImageView mIcon;
	private MTextView mVotingName;
	private TextView mThanks,mTitle,mNetTv;
	private Intent mServiceIntent;
	private ISocketService iService;
	public static TVBLogoActivity mInstance;
	public int NET_ETHERNET = 1;
	public int NET_NOCONNECT = 0;
	private boolean isNetWorkOK = false;

	private final ServiceConnection  conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// 未连接为空
			iService = null;
			Utils.PrintLogE(TAG, "iService disconnect");
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// 已连接
			iService = ISocketService.Stub.asInterface(service);
			SocketService.setVotingInfoLisenter(TVBLogoActivity.this);

			Utils.PrintLogD(TAG, "iService connected");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tvblogo);
		mInstance = this;
		initView();
		registerNet();
		PermissionUtils.requestMultiPermissions(mInstance,mPermissionGrant);
		Utils.PrintLogD(TAG, "onCreate");
	}

	private void registerNet(){
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mNetworkStateReceiver, filter);
	}

	private void initView() {
		mIcon = (ImageView) findViewById(R.id.voting_icon);
		mVotingName = (MTextView) findViewById(R.id.voting_name);
		mThanks = (TextView) findViewById(R.id.thanks);
		mTitle = (TextView) findViewById(R.id.tile);
		mNetTv = (TextView) findViewById(R.id.net_tv);
		mServiceIntent = new Intent(this, SocketService.class);
		bindService(mServiceIntent, conn, BIND_AUTO_CREATE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		onShowSeatNumDialog();
	}
	private void onShowSeatNumDialog(){
		if(Contant.seatNum.equals("")){
			try {
				seatNumDialog(TVBLogoActivity.this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void onSetting(View view){
		try {
			pswDialog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void pswDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog_setting_psw,
				(ViewGroup) findViewById(R.id.dialog));

		final EditText eText = (EditText) layout.findViewById(R.id.psw);
		final  Dialog alertDialog = new AlertDialog.Builder(this)
				.setView(layout)
				.create();
		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(false);
		//确定按钮
		Button btnOK = (Button) layout.findViewById(R.id.confirm);
		btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(Contant.PSW.equals(eText.getEditableText().toString())){
					alertDialog.dismiss();
					Intent intent = new Intent(TVBLogoActivity.this, TVBSettingActivity.class);
					startActivityForResult(intent, 1);
				}else{
					Utils.ToastMessage(TVBLogoActivity.this,R.string.psw_error);
					eText.setText("");
				}
			}
		});
		//取消按钮
		Button btnCancel = (Button) layout.findViewById(R.id.cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				onShowSeatNumDialog();
			}
		});

	}

	public  void seatNumDialog(final FragmentActivity activity) throws Exception{
		LayoutInflater inflaterDl = LayoutInflater.from(activity);
		RelativeLayout layout = (RelativeLayout)inflaterDl.inflate(R.layout.dialog_setting_seatnum, null );

		//对话框
		final Dialog dialog = new AlertDialog.Builder(activity).create();
		dialog.show();
		dialog.getWindow().setContentView(layout);
		dialog.setCanceledOnTouchOutside(false);

		//确定按钮
		Button btnOK = (Button) layout.findViewById(R.id.confirm);
		btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					dialog.dismiss();
					pswDialog();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		Utils.PrintLogD(TAG, "onDestroy");
		unbindService(conn);
		unregisterReceiver(mNetworkStateReceiver);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode){
		case  0:
			mThanks.setVisibility(View.VISIBLE);
			votingResult(TVBVotingApp.getInstance().getVotingInfo().getActivityInfo().getVotingTypeId());
			break;
		case 1:
		//	Utils.PrintLogE(TAG, data.getStringExtra("serverUrl"));
		//	Contant.address = data.getStringExtra("serverUrl");
			TVBVotingApp.getInstance().getSocketService().initSocket();
			break;
		}
		Utils.PrintLogD(TAG, "votingResult requestCode:"+requestCode);
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void votingResult(int type) {
		if(type == Contant.VOTING_SCORE || type == Contant.VOTING_LICKORUNLIKE){
			if(TVBVotingApp.getInstance().getVotingInfo().getPlayersList().size() != TVBVotingApp.getInstance().getContestantRecordMap().size()){
				mThanks.setVisibility(View.VISIBLE);
				SocketService.isVoting = false;
				Utils.ToastMessage(this,R.string.not_finish);
				return;
			}
		}else{
			if(TVBVotingApp.getInstance().getVotingInfo().getActivityInfo().getVotingLimit() > TVBVotingApp.getInstance().getContestantRecordMap().size()){
				mThanks.setVisibility(View.VISIBLE);
				SocketService.isVoting = false;
				Utils.ToastMessage(this,R.string.not_finish);
				return;
			}
		}


		CVotingResultInfo.Builder votingResult = CVotingResultInfo.newBuilder();
		votingResult.setSeatNum(Contant.seatNum);
		votingResult.setSerialNum(TVBVotingApp.getInstance().getVotingInfo().getActivityInfo().getSerialNum());
		votingResult.setVotingTypeId(type);
		votingResult.setDevId(Utils.getSerialNumber());
		votingResult.addAllRecords(checkRecordValue(type,TVBVotingApp.getInstance().getContestantRecordMap().values()));

		mThanks.setVisibility(View.VISIBLE);
		sendMessage(Contant.CATEGORY_VOTING_RESULT_ID,Contant.PROPERTY_VOTING_RESULT_SUBMIT_ID,votingResult.build().toByteArray());
		SocketService.isVoting = false;
		Utils.PrintLogE(TAG, "votingResult submit");
	}

	private Collection<CContestantRecord>  checkRecordValue(int votingTypeId, Collection<CContestantRecord> contestantRecord){
        Iterator iterator = contestantRecord.iterator();

        switch (votingTypeId){
            case Contant.VOTING_SCORE:
                break;
            case Contant.VOTING_CHECK:
            case Contant.VOTING_LIKE:
                while (iterator.hasNext()){
                    CContestantRecord record = (CContestantRecord) iterator.next();
                    if (record.getRecordValue(Contant.ONLYONE).getRecordData() != Contant.LIKEORCHECK){
                        iterator.remove();
                    }
                }
                break;
            case Contant.VOTING_LICKORUNLIKE:
                while (iterator.hasNext()){
                    CContestantRecord record = (CContestantRecord) iterator.next();
                    if (record.getRecordValue(Contant.ONLYONE).getRecordData() == Contant.NOTCHECK){
                        iterator.remove();
                    }
                }
                break;
        }
        return contestantRecord;
    }

	private void sendMessage(int category,int categorySub,byte[] result) {
		if (iService == null) {
			Utils.PrintLogE(TAG, "iService disconnect");
		} else {
			try {
				iService.sendByteMessage(category,categorySub,result);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setVotingInfoDisplay(String votingname, String votinglogo) {
		mThanks.setVisibility(View.INVISIBLE);
		mVotingName.setText(votingname);
		//Utils.setImage(votingname,votinglogo,"L",mIcon);
		Utils.setImage(votinglogo,mIcon);
	}

	@Override
	public void startVoting() {
		Intent intent = new Intent(this, TVBVotingActivity.class);
		startActivityForResult(intent, 0);
	}
	



	@Override
	public void submitVotingResult(String code) {
		if(code.equals(Contant.SUCESS)){
			Utils.ToastMessage(TVBLogoActivity.this,R.string.submit_sucss);
		}else{
			Utils.ToastMessage(TVBLogoActivity.this,R.string.submit_error);
		}
	}

	@Override
	public void checkPswResult(String code) {

	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void OnEventCmd(EventBusCMD cmd) {
		if(cmd.getCmd() == Contant.CHANGE_LANG){
			Resources res = this.getResources();
			mThanks.setText(res.getString(R.string.thanks));
			mTitle.setText(res.getString(R.string.voting_system));
		}else if(cmd.getCmd() == Contant.CHECKNET_OK){
			mNetTv.setVisibility(View.INVISIBLE);
		}else if(cmd.getCmd() == Contant.CHECKNET_NG){
			if(isNetWorkOK){
				mNetTv.setText(R.string.connect_server);
			}else{
				mNetTv.setText(R.string.network_ng);
			}
		}
	}


	private BroadcastReceiver mNetworkStateReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {

			Log.e(TAG, intent.getAction());
			if (intent.getAction().equals(
					ConnectivityManager.CONNECTIVITY_ACTION)
					|| intent.getAction().equals(
					"android.net.conn.CONNECTIVITY_CHANGE")) {


				switch (isNetworkAvailable(TVBLogoActivity.this)) {
					case 1:
						Log.e(TAG, "-----------networktest----------有线");
						isNetWorkOK = true;

						mNetTv.setText(R.string.connect_server);
						TVBVotingApp.getInstance().getSocketService().initSocket();
						break;
					case 0:
						Log.e(TAG, "-----------networktest----------无网络");
						isNetWorkOK = false;
						mNetTv.setText(R.string.network_ng);
						mNetTv.setVisibility(View.VISIBLE);
						break;
					default:
						break;
				}


			}


		}
	};

	private int isNetworkAvailable(Context context) {
		ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo ethNetInfo = connectMgr
				.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

		if (ethNetInfo != null && ethNetInfo.isConnected()) {
			return NET_ETHERNET;
		} else {
			return NET_NOCONNECT;
		}
	}

	private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
		@Override
		public void onPermissionGranted(int requestCode) {

		}
	};
}