package com.golding.tvbvotingsystem.fragment;

import com.golding.tvbcloud.VotingResultInfo.*;
import com.golding.tvbcloud.VotingInfo.CVotingPlayer;
import com.golding.tvbvotingsystem.R;
import com.golding.tvbvotingsystem.TVBVotingActivity;
import com.golding.tvbvotingsystem.TVBVotingApp;
import com.golding.tvbvotingsystem.bean.EventBusCMD;
import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.ui.MTextView;
import com.golding.tvbvotingsystem.utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import static com.golding.tvbvotingsystem.TVBVotingActivity.players;

/**
 * @author :Jallen
 * @date ：2017-6-16 6:52:03
 * @version :1.0
 * @desc :
 */
@SuppressLint("InflateParams")
public class LikeVotingTwoFrag extends BaseFragment implements
		OnCheckedChangeListener {
	private TextView mIndex;
	private ImageView mHead;
	private CheckBox mLikeBtn;
	private MTextView mSingerName;
	private Button mConfirmBtn;
	private boolean mOnlyOne = false;
	private CVotingPlayer mSinger ;
	private CContestantRecord.Builder cContestantRecord =  CContestantRecord.newBuilder();
	private CRecordValue.Builder cRecordValues = CRecordValue.newBuilder();
	private String songs="";
	private  int isLike = 0;

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
		mSinger = players.get(index);
		if (TVBVotingApp.getInstance().getContestantRecordMap().containsKey(mSinger.getUserId())) {
			isLike = TVBVotingApp.getInstance().getContestantRecordMap().get(mSinger.getUserId()).getRecordValue(Contant.ONLYONE).getRecordData();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.voting_like2,container,false );
		mIndex = (TextView) view.findViewById(R.id.index);
		mHead = (ImageView) view.findViewById(R.id.head);
		mLikeBtn = (CheckBox) view.findViewById(R.id.likecheck);
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

		if(isLike == 1){
			mLikeBtn.setChecked(true);
		}else{
			mLikeBtn.setChecked(false);
		}

		mLikeBtn.setOnCheckedChangeListener(this);
		mConfirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isLike == Contant.LIKEORCHECK){
					if(TVBVotingApp.getInstance().getVotingInfo().getActivityInfo().getVotingLimit() <= TVBVotingApp.getInstance().getContestantRecordMap().size()){
						if(!TVBVotingApp.getInstance().getContestantRecordMap().containsKey(mSinger.getUserId())){
							Utils.ToastMessage(getActivity(),R.string.voting_limit);
							return;
						}
					}

					cRecordValues.setRecordData(isLike);
					cContestantRecord.setUserId(mSinger.getUserId());
					cContestantRecord.addRecordValue(cRecordValues);
					TVBVotingApp.getInstance().getContestantRecordMap().put(mSinger.getUserId(),cContestantRecord.build());
				}else{
					if(TVBVotingApp.getInstance().getContestantRecordMap().containsKey(mSinger.getUserId())){
						TVBVotingApp.getInstance().getContestantRecordMap().remove(mSinger.getUserId());
					}
				}

				if(mOnlyOne){
					 getActivity().setResult(-1);
					 getActivity().finish();
				}else{
					getActivity().getSupportFragmentManager().popBackStack();
				}
				
			}
		});
	}

	public static LikeVotingTwoFrag newInstance(int position,boolean onlyOne) {
		LikeVotingTwoFrag frag = new LikeVotingTwoFrag();
		Bundle bundle = new Bundle();
		frag.setArguments(bundle);
		bundle.putInt(Contant.KEY_INDEX, position);
		bundle.putBoolean(Contant.KEY_ONLYONE, onlyOne);
		return frag;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
        mSingerName.setText(TVBVotingApp.getInstance().getVotingInfo().getActivityInfo().getVotingSession());
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean checked) {

		if(checked){
			isLike = Contant.LIKEORCHECK;
		}else{
			isLike = Contant.NOTCHECK;
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void OnEventCmd(EventBusCMD cmd) {
		if(cmd.getCmd() == Contant.CHANGE_LANG) {
			mConfirmBtn.setText(R.string.confirm);
			mSingerName.setText(mSinger.getName() + songs);
		}
	}
}
