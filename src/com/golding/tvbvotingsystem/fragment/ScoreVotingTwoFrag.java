package com.golding.tvbvotingsystem.fragment;

import com.golding.tvbcloud.VotingInfo;
import com.golding.tvbcloud.VotingResultInfo.*;
import com.golding.tvbcloud.VotingInfo.CVotingPlayer;
import com.golding.tvbvotingsystem.R;
import com.golding.tvbvotingsystem.TVBVotingActivity;
import com.golding.tvbvotingsystem.TVBVotingApp;
import com.golding.tvbvotingsystem.bean.EventBusCMD;
import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.ui.MTextView;
import com.golding.tvbvotingsystem.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * @author :Jallen
 * @date ��2017-6-16 ����9:09:15
 * @version :1.0
 * @desc :
 */
public class ScoreVotingTwoFrag extends BaseFragment{
	private TextView mIndex;
	private ImageView mHead;
	private TextView[] mType = new TextView[4];
	private EditText[] mScoreETs = new EditText[4];
	private CheckBox[] mCheck = new CheckBox[4];
	private MTextView mSingerName;
	private Button mConfirmBtn;
    private int mScoreCnt = 4;
	private boolean mOnlyOne = false;
	private int[] scoring = new int[4];
    private LinearLayout[] linearLayouts = new LinearLayout[4];
	private CVotingPlayer mSinger ;
	private CContestantRecord.Builder cContestantRecord =  CContestantRecord.newBuilder();
    private List<VotingInfo.CContestantScore> cContestantScores = new ArrayList<VotingInfo.CContestantScore>();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof TVBVotingActivity) {
			TVBVotingActivity votingActivity = (TVBVotingActivity) activity;
			mSingerName = (MTextView) votingActivity
					.findViewById(R.id.voting_circle);
		}
        mScoreCnt = TVBVotingApp.getInstance().getVotingInfo().getActivityInfo().getScoreTypeCount();
		cContestantScores = TVBVotingApp.getInstance().getVotingInfo().getActivityInfo().getScoreTypeList();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle mBundle = getArguments();
		Integer	index = mBundle.getInt(Contant.KEY_INDEX);
		mOnlyOne = mBundle.getBoolean(Contant.KEY_ONLYONE);
		mSinger = TVBVotingApp.getInstance().getVotingInfo().getPlayersList().get(index);
	
		if (TVBVotingApp.getInstance().getContestantRecordMap().containsKey(mSinger.getUserId())) {
			cContestantRecord = TVBVotingApp.getInstance().getContestantRecordMap()
					.get(mSinger.getUserId()).toBuilder();
            for (int i=0;i<mScoreCnt;i++)
				scoring[i] = cContestantRecord.getRecordValue(i).getRecordData();
		}

	}

	public static ScoreVotingTwoFrag newInstance(int position,boolean onlyOne) {
		ScoreVotingTwoFrag frag = new ScoreVotingTwoFrag();
		Bundle bundle = new Bundle();
		frag.setArguments(bundle);
		bundle.putInt(Contant.KEY_INDEX, position);
		bundle.putBoolean(Contant.KEY_ONLYONE, onlyOne);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.voting_score2, container,false );
		mIndex = (TextView) view.findViewById(R.id.index);
		mHead = (ImageView) view.findViewById(R.id.head);
        linearLayouts[0] = (LinearLayout) view.findViewById(R.id.ll1);
        linearLayouts[1] = (LinearLayout) view.findViewById(R.id.ll2);
        linearLayouts[2] = (LinearLayout) view.findViewById(R.id.ll3);
        linearLayouts[3] = (LinearLayout) view.findViewById(R.id.ll4);
		mType[0] = (TextView) view.findViewById(R.id.type1);
        mType[1] = (TextView) view.findViewById(R.id.type2);
        mType[2] = (TextView) view.findViewById(R.id.type3);
        mType[3] = (TextView) view.findViewById(R.id.type4);
        mScoreETs[0] = (EditText) view.findViewById(R.id.scoring1);
        mScoreETs[1] = (EditText) view.findViewById(R.id.scoring2);
        mScoreETs[2] = (EditText) view.findViewById(R.id.scoring3);
        mScoreETs[3] = (EditText) view.findViewById(R.id.scoring4);
        mCheck[0] = (CheckBox) view.findViewById(R.id.check1);
        mCheck[1] = (CheckBox) view.findViewById(R.id.check2);
        mCheck[2] = (CheckBox) view.findViewById(R.id.check3);
        mCheck[3] = (CheckBox) view.findViewById(R.id.check4);
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

		msg.append("   "+getString(R.string.score_40_100));

		mSingerName.setText(msg.toString());

        for(int i = mScoreCnt;i<linearLayouts.length;i++){
            linearLayouts[i].setVisibility(View.GONE);
        }

		setScoreTypeTxt(mScoreCnt);
		setConfirmBtnTxt();

		mConfirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				checkScoringError();
				
			}
		});
	}

	private void setScoreTypeTxt(int cnt){
        for(int i =0 ;i < cnt; i++){
			mScoreETs[i].addTextChangedListener(new CustomTextWatcher(mScoreETs[i]));
            mType[i].setText(cContestantScores.get(i).getScoreAlias());
            if(scoring[i] !=  0){
                mScoreETs[i].setText(scoring[i]+"");
            }
            if(cnt < 4){
				mCheck[i].setBackgroundResource(R.drawable.check_bg2);
			}
			boolean isOk = Utils.checkScoring(String.valueOf(scoring[i]));
			mCheck[i].setChecked(isOk);
        }
	}


	private void saveScoringValue() {
		cContestantRecord.clear();
        cContestantRecord.setUserId(mSinger.getUserId());
        for(int i =0 ;i < mScoreCnt; i++){
            scoring[i] = Integer.valueOf(mScoreETs[i].getEditableText().toString());
            CRecordValue.Builder cRecordValues = CRecordValue.newBuilder();
            cRecordValues.setRecordData(scoring[i]);
            cRecordValues.setRecordName(cContestantScores.get(i).getScoreName());
            cContestantRecord.addRecordValue(cRecordValues);
        }
		TVBVotingApp.getInstance().getContestantRecordMap().put(mSinger.getUserId(), cContestantRecord.build());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
        mSingerName.setText(TVBVotingApp.getInstance().getVotingInfo().getActivityInfo().getVotingSession());
	}

	public void onScoringChange(View view,Editable s) {
		
		boolean isOk ;
		switch(view.getId()){
		case R.id.scoring1:
			isOk = Utils.checkScoring(s.toString());
            mCheck[0].setChecked(isOk);
			if(isOk){
				mScoreETs[1].requestFocus();
				mScoreETs[1].setSelection(mScoreETs[1].length());
			}
			break;
		case R.id.scoring2:
			isOk =Utils.checkScoring(s.toString());
            mCheck[1].setChecked(isOk);
			if(isOk){
				mScoreETs[2].requestFocus();
				mScoreETs[2].setSelection(mScoreETs[2].length());
			}
			break;
		case R.id.scoring3:
			isOk= Utils.checkScoring(s.toString());
            mCheck[2].setChecked(isOk);
			if(isOk){
				mScoreETs[3].requestFocus();
				mScoreETs[3].setSelection(mScoreETs[3].length());
			}
			break;
		case R.id.scoring4:
			isOk =Utils.checkScoring(s.toString());
            mCheck[3].setChecked(isOk);
			if(isOk){
				mScoreETs[0].requestFocus();
				mScoreETs[0].setSelection(mScoreETs[0].length());
			}
			break;
		}
		setConfirmBtnTxt();
	}

	private void setConfirmBtnTxt(){
		mConfirmBtn.setText(R.string.confirm);
//		if(isCheckedError() == -1){
//			mConfirmBtn.setText(R.string.confirm);
//		}else{
//			mConfirmBtn.setText("next");
//		}
	}

	private int isCheckedError(){
        int errorIndex = -1;
        for (int i= 0;i <mScoreCnt;i++ ){
            if(!mCheck[i].isChecked()){
				errorIndex = i;
                break;
            }else{
				errorIndex = -1;
            }
        }
        return errorIndex;
    }

	public boolean checkScoringError(){
		int errorIndex = isCheckedError();
		
		if(errorIndex == -1){
			goneKeyboard();
			saveScoringValue();

			if(mOnlyOne){
				 getActivity().setResult(-1);
				 getActivity().finish();
			}else{
				getActivity().getSupportFragmentManager().popBackStack();
			}
			
			return true;
		}else{
			mScoreETs[errorIndex].requestFocus();
			Utils.ToastMessage(getActivity(), R.string.not_finish);
			return false;
		}
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class CustomTextWatcher implements TextWatcher {
	    private EditText mEditText; 
	  
	    public CustomTextWatcher(EditText e) {  
	        mEditText = e;  
	    }  
	  
	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {  
	    }
	  
	    public void onTextChanged(CharSequence s, int start, int before, int count) {  
	      
	    }  
	  
	    public void afterTextChanged(Editable s) {
	    	if(s.length() == 3 && !s.toString().equals("100")){
				s.delete(s.length()-1,s.length());
			}
	    	onScoringChange(mEditText,s);
	    }  
	}
	@Subscribe(threadMode = ThreadMode.MainThread)
	public void OnEventCmd(EventBusCMD cmd) {
		if(cmd.getCmd() == Contant.CHANGE_LANG) {
			setConfirmBtnTxt();
		}
	}
}
