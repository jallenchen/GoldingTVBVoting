package com.golding.tvbvotingsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.golding.tvbcloud.VotingInfo.*;
import com.golding.tvbvotingsystem.bean.EventBusCMD;
import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.fragment.LikeVotingOneFrag;
import com.golding.tvbvotingsystem.fragment.LikeVotingTwoFrag;
import com.golding.tvbvotingsystem.fragment.PromoteVotingOneFrag;
import com.golding.tvbvotingsystem.fragment.PromoteVotingTwoFrag;
import com.golding.tvbvotingsystem.fragment.ScoreVotingOneFrag;
import com.golding.tvbvotingsystem.fragment.ScoreVotingTwoFrag;
import com.golding.tvbvotingsystem.fragment.UnLikeOrLikeVotingOneFrag;
import com.golding.tvbvotingsystem.fragment.UnLikeOrLikeVotingTwoFrag;
import com.golding.tvbvotingsystem.interfaces.IStopVotingCmd;
import com.golding.tvbvotingsystem.socket.SocketService;
import com.golding.tvbvotingsystem.ui.MTextView;
import com.golding.tvbvotingsystem.utils.Utils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import de.greenrobot.event.EventBus;

/**
 * @author :Jallen
 * @date ��2017-6-16 ����8:59:33
 * @version :1.0
 * @desc :
 */
@SuppressLint("HandlerLeak")
public class TVBVotingActivity extends TVBBaseActivity implements IStopVotingCmd{
	private static final String TAG = "TVBVotingActivity";
	private ImageView mVotingLogo;
	private TextView mTime;
	private MTextView mVotingName;
	private MTextView mVotingCircle;
	private static final int COUNTTIME = 0;
	private List<CVotingPlayer> mSingers ;
	private CActivityInfo mActivityInfo ;
    public static List<CVotingPlayer> players = new ArrayList<CVotingPlayer>();
	private Timer mTimer = new Timer();
	private int mCountTime = 0;
	private TimerTask mTimerTask = new TimerTask() {

		@Override
		public void run() {
			mHandler.sendEmptyMessage(COUNTTIME);
		}
	};


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.activity_tvbvoting);
		initView();
		initData();
		initListView(mActivityInfo.getVotingTypeId());
		Utils.PrintLogD(TAG, "TVBVotingActivity onCreate");
		mTimer.schedule(mTimerTask, 0, 1000);
		SocketService.setVotingStopLisenter(this);
	}

	private void initData() {
		mSingers = TVBVotingApp.getInstance().getVotingInfo().getPlayersList();
		mActivityInfo = TVBVotingApp.getInstance().getVotingInfo().getActivityInfo();
		mCountTime = mActivityInfo.getVotingTimer();
		mVotingName.setText(mActivityInfo.getVotingName());
		mVotingCircle.setText(mActivityInfo.getVotingSession());
		Utils.setImage(mActivityInfo.getVotingLogoS(),mVotingLogo);
		//Utils.setImage(mActivityInfo.getVotingName(),mActivityInfo.getVotingLogoS(),"S",mVotingLogo);
	}

	private void initView() {
		mVotingLogo = (ImageView) findViewById(R.id.voting_logo);
		mTime = (TextView) findViewById(R.id.voting_time);
		mVotingName = (MTextView) findViewById(R.id.voting_name);
		mVotingCircle = (MTextView) findViewById(R.id.voting_circle);
	}
	
	private void initListView(int type) {
		FragmentManager fm = this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		switch (type) {
		case Contant.VOTING_SCORE:
			if (mSingers.size() == 1) {
				ft.replace(R.id.container, ScoreVotingTwoFrag.newInstance(Contant.ONLYONE,true));
			} else {
				ft.replace(R.id.container, new ScoreVotingOneFrag());
			}
			break;
		case Contant.VOTING_CHECK:
			if (mSingers.size() == 1) {
				ft.replace(R.id.container, PromoteVotingTwoFrag.newInstance(Contant.ONLYONE,true));
			} else {
				ft.replace(R.id.container, new PromoteVotingOneFrag());
			}
			break;
		case Contant.VOTING_LIKE:
			if (mSingers.size() == 1) {
				ft.replace(R.id.container, LikeVotingTwoFrag.newInstance(Contant.ONLYONE,true));
			} else {
				ft.replace(R.id.container, new LikeVotingOneFrag());
			}
			
			break;
		case Contant.VOTING_LICKORUNLIKE:
			if (mSingers.size() == 1) {
				ft.replace(R.id.container, UnLikeOrLikeVotingTwoFrag.newInstance(Contant.ONLYONE,true));
			} else {
				ft.replace(R.id.container, new UnLikeOrLikeVotingOneFrag());
			}

			break;
		}

		ft.commit();
	}

	
	public void onChangeLang(View view){
		RadioButton mView = (RadioButton) view;
		mView.setChecked(true);
		switch (mView.getId()) {
		case R.id.eng_btn:
			 Utils.changeLanguage(Locale.ENGLISH);
			break;
		case R.id.fan_btn:
			 Utils.changeLanguage(Locale.TRADITIONAL_CHINESE);
			break;
		case R.id.jian_btn:
			 Utils.changeLanguage(Locale.SIMPLIFIED_CHINESE);
			break;
		}
		EventBus.getDefault().post(new EventBusCMD(Contant.CHANGE_LANG));
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		clearTimer();
		Utils.PrintLogD(TAG, "TVBVotingActivity onDestroy");
	}

	private void clearTimer() {
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case COUNTTIME:
				mCountTime --;
				if(mCountTime < 59){
					mTime.setTextColor(getResources().getColor(R.color.color_red));
				}
				mTime.setText(Utils.countTimeToString(mCountTime));
				if(mCountTime == 0){
					 setResult(RESULT_OK);
					 finish();
					Utils.PrintLogD(TAG, "TVBVotingActivity finish");
				}
				break;
			}

			super.handleMessage(msg);
		}

	};

	@Override
	public void stopVoting() {
		 setResult(RESULT_OK);
		 finish();
	}
}
