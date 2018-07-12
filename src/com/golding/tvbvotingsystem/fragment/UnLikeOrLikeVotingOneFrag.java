package com.golding.tvbvotingsystem.fragment;

import com.golding.tvbcloud.VotingResultInfo.*;
import com.golding.tvbcloud.VotingInfo.CVotingPlayer;
import com.golding.tvbvotingsystem.R;
import com.golding.tvbvotingsystem.TVBVotingApp;
import com.golding.tvbvotingsystem.adapter.UnlickOrLikeVotingAdapter;
import com.golding.tvbvotingsystem.bean.EventBusCMD;
import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.ui.MyImageButton;
import com.golding.tvbvotingsystem.utils.Utils;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * @author :Jallen
 * @date ��2017-6-16 ����6:53:27
 * @version :1.0
 * @desc :
 */
public class UnLikeOrLikeVotingOneFrag extends BaseFragment {
	private static final String TAG = "UnLikeOrLikeVotingOneFrag";
	private ListView mList;
	private Button mConfirmBtn;
	private  UnlickOrLikeVotingAdapter mUnlikeAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.base_list,container,false );
		mList = (ListView) view.findViewById(R.id.list);
		mConfirmBtn = (Button) view.findViewById(R.id.confirm_btn);
		mUnlikeAdapter = new UnlickOrLikeVotingAdapter(getActivity(),
				TVBVotingApp.getInstance().getVotingInfo().getPlayersList(),new MyImageButtonListener());
		mList.setAdapter(mUnlikeAdapter);
		mConfirmBtn.setText(R.string.submit);
		mConfirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(TVBVotingApp.getInstance().getVotingInfo().getPlayersList().size() != TVBVotingApp.getInstance().getContestantRecordMap().size()){
					Utils.ToastMessage(getActivity(),R.string.not_finish);
				}else{
					try {
						Utils.confirmDialog(getActivity());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		return view;
	}

	private void initListView(int position) {
		UnLikeOrLikeVotingTwoFrag frag = UnLikeOrLikeVotingTwoFrag
				.newInstance(position,false);
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.hide(this);
		ft.add(R.id.container, frag, "unlike2");
		ft.addToBackStack("unlike2");
		ft.commit();
	}

	public class MyImageButtonListener implements OnClickListener {
		
		@Override
		public void onClick(View view) {
			int position = ((MyImageButton)view).getIndex();
			CVotingPlayer mSinger = TVBVotingApp.getInstance().getVotingInfo().getPlayersList().get(position);
			CContestantRecord.Builder cContestantRecord =  CContestantRecord.newBuilder();
			CRecordValue.Builder recordValue = CRecordValue.newBuilder();
			cContestantRecord.setUserId(mSinger.getUserId());
			switch(view.getId()){
			case R.id.allow:
				initListView(position);
				break;
			case R.id.like:
				boolean isChecked = view.isSelected();

				if(!isChecked){
					recordValue.setRecordData(Contant.LIKEORCHECK);
					cContestantRecord.addRecordValue(recordValue);
                    TVBVotingApp.getInstance().getContestantRecordMap().put(mSinger.getUserId(), cContestantRecord.build());
				}else{
					return;
				}

				getCallData();
				break;
				
			case R.id.unlike_mib:
				if(!view.isSelected()){
					recordValue.setRecordData(Contant.UNLIKE);
					cContestantRecord.addRecordValue(recordValue);
                    TVBVotingApp.getInstance().getContestantRecordMap().put(mSinger.getUserId(), cContestantRecord.build());
				}else{
                    return;
				}

				getCallData();
				break;
		}
	  }
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden == false){
			getCallData();
		}
	}

	public  void getCallData() {
		// TODO Auto-generated method stub
		if (mUnlikeAdapter == null) {
			return;
		}
		mUnlikeAdapter.notifyDataSetChanged();
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void OnEventCmd(EventBusCMD cmd) {
		if(cmd.getCmd() == Contant.CHANGE_LANG) {
			mConfirmBtn.setText(R.string.submit);
		}
	}
}
