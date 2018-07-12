package com.golding.tvbvotingsystem.fragment;

import com.golding.tvbcloud.VotingResultInfo.*;
import com.golding.tvbcloud.VotingInfo.CVotingPlayer;
import com.golding.tvbvotingsystem.R;
import com.golding.tvbvotingsystem.TVBVotingActivity;
import com.golding.tvbvotingsystem.TVBVotingApp;
import com.golding.tvbvotingsystem.bean.EventBusCMD;
import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.ui.MTextView;
import com.golding.tvbvotingsystem.ui.MyImageButton;
import com.golding.tvbvotingsystem.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * @author :Jallen
 * @date ��2017-6-16 ����6:53:59
 * @version :1.0
 * @desc :
 */
public class UnLikeOrLikeVotingTwoFrag extends BaseFragment implements
		OnClickListener {
	private TextView mIndex;
	private TextView mChooseNote;
	private ImageView mHead;
	private Button mConfirmBtn;
	private MyImageButton mUnlikeBtn, mLikeBtn;
	private MTextView mSingerName;
	private boolean mOnlyOne = false;
	private CVotingPlayer mSinger ;
	private int isChecked = 0;
	private CContestantRecord.Builder cContestantRecord =  CContestantRecord.newBuilder();
	private CRecordValue.Builder cRecordValues = CRecordValue.newBuilder();
	private String songs ="";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof TVBVotingActivity) {
			TVBVotingActivity votingActivity = (TVBVotingActivity) activity;
			mSingerName = (MTextView) votingActivity
					.findViewById(R.id.voting_circle);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle mBundle = getArguments();
		Integer index = mBundle.getInt(Contant.KEY_INDEX);
		mOnlyOne = mBundle.getBoolean(Contant.KEY_ONLYONE);
		mSinger = TVBVotingApp.getInstance().getVotingInfo().getPlayersList().get(index);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.voting_unlike2,container,false );
		mIndex = (TextView) view.findViewById(R.id.index);
		mChooseNote = (TextView) view.findViewById(R.id.chooseNote);
		mHead = (ImageView) view.findViewById(R.id.head);
		mUnlikeBtn = (MyImageButton) view.findViewById(R.id.unlike_btn);
		mLikeBtn = (MyImageButton) view.findViewById(R.id.like_btn);
		mConfirmBtn = (Button) view.findViewById(R.id.confirm_btn);
		initView();
		return view;
	}

	private void initView() {
		mIndex.setText(mSinger.getIndexNum());
		//Utils.setImage(mSinger.getName(),mSinger.getImageL(),"L",mHead);
		Utils.setImage(mSinger.getImageL(),mHead);
		StringBuffer msg = new StringBuffer(mSinger.getName());
		if(mSinger.getAge() != 0){
			msg.append("   "+mSinger.getAge());
		}
		if(!TextUtils.isEmpty(mSinger.getArea())){
			msg.append("   "+mSinger.getArea());
		}
		if(!TextUtils.isEmpty(mSinger.getPerform())){
			msg.append("   "+mSinger.getPerform());
		}

		mSingerName.setText(msg.toString());

		if (TVBVotingApp.getInstance().getContestantRecordMap().containsKey(mSinger.getUserId())) {
			isChecked = TVBVotingApp.getInstance().getContestantRecordMap().get(mSinger.getUserId()).getRecordValue(Contant.ONLYONE).getRecordData();
		}

		if (isChecked ==Contant.LIKEORCHECK) {
			mUnlikeBtn.setSelected(false);
			mLikeBtn.setSelected(true);
		}else if (isChecked ==Contant.UNLIKE) {
			mUnlikeBtn.setSelected(true);
			mLikeBtn.setSelected(false);
		}else{
			mUnlikeBtn.setSelected(false);
			mLikeBtn.setSelected(false);
		}

		mUnlikeBtn.setOnClickListener(this);
		mLikeBtn.setOnClickListener(this);
		mConfirmBtn.setOnClickListener(this);
	}

	public static UnLikeOrLikeVotingTwoFrag newInstance(int position,boolean onlyOne) {
		UnLikeOrLikeVotingTwoFrag frag = new UnLikeOrLikeVotingTwoFrag();
		Bundle bundle = new Bundle();
		frag.setArguments(bundle);
		bundle.putInt(Contant.KEY_INDEX, position);
		bundle.putBoolean(Contant.KEY_ONLYONE, onlyOne);
		return frag;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.like_btn:
			if(!mLikeBtn.isSelected()){
				isChecked = Contant.LIKEORCHECK;
				mLikeBtn.setSelected(true);
				mUnlikeBtn.setSelected(false);
			}
			break;
		case R.id.unlike_btn:
			if(!mUnlikeBtn.isSelected()){
				isChecked = Contant.UNLIKE;
				mUnlikeBtn.setSelected(true);
				mLikeBtn.setSelected(false);
			}
			break;
		case R.id.confirm_btn:
			cContestantRecord.setUserId(mSinger.getUserId());
			cRecordValues.setRecordData(isChecked);
			cContestantRecord.addRecordValue(cRecordValues);
			TVBVotingApp.getInstance().getContestantRecordMap().put(mSinger.getUserId(), cContestantRecord.build());

			if(mOnlyOne){
				 getActivity().setResult(-1);
				 getActivity().finish();
			}else{
				getActivity().getSupportFragmentManager().popBackStack();
			}
			
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
        mSingerName.setText(TVBVotingApp.getInstance().getVotingInfo().getActivityInfo().getVotingSession());
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void OnEventCmd(EventBusCMD cmd) {
		if(cmd.getCmd() == Contant.CHANGE_LANG) {
			mChooseNote.setText(R.string.choose_note);
			mConfirmBtn.setText(R.string.confirm);
			mSingerName.setText(mSinger.getName() + songs);
		}
	}

}
