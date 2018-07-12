package com.golding.tvbvotingsystem.fragment;

import com.golding.tvbcloud.VotingResultInfo.*;
import com.golding.tvbcloud.VotingInfo.CVotingPlayer;
import com.golding.tvbvotingsystem.R;
import com.golding.tvbvotingsystem.TVBVotingApp;
import com.golding.tvbvotingsystem.adapter.LikeVotingAdapter;
import com.golding.tvbvotingsystem.adapter.PromoteVotingAdapter;
import com.golding.tvbvotingsystem.bean.EventBusCMD;
import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.ui.MyImageButton;
import com.golding.tvbvotingsystem.utils.Utils;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import static com.golding.tvbvotingsystem.TVBVotingActivity.players;

/**
 * @author :Jallen
 * @date ：2017-6-16 6:51:11
 * @version :1.0
 * @desc :
 */
@SuppressLint("InflateParams")
public class LikeVotingOneFrag extends BaseFragment{
	private static final String TAG = "LikeVotingOneFrag";
	private ListView mList;
	private Button mConfirmBtn;
	private  LikeVotingAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.base_list, container,false );
		mList = (ListView) view.findViewById(R.id.list);
		mConfirmBtn = (Button) view.findViewById(R.id.confirm_btn);

		players.clear();
		players.addAll(TVBVotingApp.getInstance().getVotingInfo().getPlayersList());
		mAdapter = new LikeVotingAdapter(getActivity(), players,new MyImageButtonListener());

		mList.setAdapter(mAdapter);
		mConfirmBtn.setText(R.string.submit);
		mConfirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(TVBVotingApp.getInstance().getVotingInfo().getActivityInfo().getVotingLimit() != TVBVotingApp.getInstance().getContestantRecordMap().size()){
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
		LikeVotingTwoFrag frag = LikeVotingTwoFrag.newInstance(position,false);
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.container, frag, "like2");
		ft.hide(this);
		ft.addToBackStack("like2");
		ft.commit();
	}

	public class MyImageButtonListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			int position = ((MyImageButton)view).getIndex();
			switch(view.getId()){
			case R.id.allow:
				initListView(position);
				break;
			case R.id.like:
                CContestantRecord.Builder cContestantRecord =  CContestantRecord.newBuilder();
			    boolean isChecked = view.isSelected();
				CVotingPlayer mSinger = players.get(position);
                CRecordValue.Builder recordValue = CRecordValue.newBuilder();
				cContestantRecord.setUserId(mSinger.getUserId());
				if(!isChecked){
					if(TVBVotingApp.getInstance().getVotingInfo().getActivityInfo().getVotingLimit() <= TVBVotingApp.getInstance().getContestantRecordMap().size()){
						Utils.ToastMessage(getActivity(),R.string.voting_limit);
						return;
					}
					recordValue.setRecordData(Contant.LIKEORCHECK);
					cContestantRecord.addRecordValue(recordValue);
					TVBVotingApp.getInstance().getContestantRecordMap().put(mSinger.getUserId(), cContestantRecord.build());
				}else{
					if(TVBVotingApp.getInstance().getContestantRecordMap().containsKey(mSinger.getUserId())){
						TVBVotingApp.getInstance().getContestantRecordMap().remove(mSinger.getUserId());
					}
				}
				getCallData();
				break;
			}
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(!hidden){
			getCallData();
		}
	}
	public  void getCallData() {
		// TODO Auto-generated method stub
		if (mAdapter == null) {
			return;
		}
		mAdapter.refreshDatas(listSort());
	}

	private static List<CVotingPlayer> listSort(){

		Collections.sort(players, new Comparator<CVotingPlayer>() {

			@Override
			public int compare(CVotingPlayer o1, CVotingPlayer o2) {
				int value1 = Contant.NOTCHECK;
				int value2 = Contant.NOTCHECK;
				if(TVBVotingApp.getInstance().getContestantRecordMap().containsKey(o1.getUserId())){
					value1 = TVBVotingApp.getInstance().getContestantRecordMap().get(o1.getUserId()).getRecordValue(Contant.ONLYONE).getRecordData();
				}

				if(TVBVotingApp.getInstance().getContestantRecordMap().containsKey(o2.getUserId())){
					value2 = TVBVotingApp.getInstance().getContestantRecordMap().get(o2.getUserId()).getRecordValue(Contant.ONLYONE).getRecordData();
				}

				if(value1 == value2){
					int index1 = Integer.valueOf(o1.getIndexNum());
					int index2 = Integer.valueOf(o2.getIndexNum());
					if(index1 > index2){
						return 1;
					}else {
						return -1;
					}
				}else if(value1 > value2){
					return -1;
				}else if(value1 < value2){
					return 1;
				}else {
					return 0;
				}
			}
		});
		return players;
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void OnEventCmd(EventBusCMD cmd) {
		if(cmd.getCmd() == Contant.CHANGE_LANG) {
			mConfirmBtn.setText(R.string.submit);
		}
	}


}
