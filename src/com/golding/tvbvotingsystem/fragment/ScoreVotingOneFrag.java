package com.golding.tvbvotingsystem.fragment;

import com.golding.tvbvotingsystem.R;
import com.golding.tvbvotingsystem.TVBVotingApp;
import com.golding.tvbvotingsystem.adapter.ScoreVotingAdapter;
import com.golding.tvbvotingsystem.bean.EventBusCMD;
import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.utils.Utils;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * @author :Jallen
 * @date :2017-6-16 9:08:31
 * @version :1.0
 * @desc :
 */
public class ScoreVotingOneFrag extends BaseFragment implements
		OnItemClickListener {
	private static final String TAG = "TVBVotingStepOneFrag";
	private ListView mList;
	private Button mConfirmBtn;
	private ScoreVotingAdapter mSroceAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.base_list,container,false );
		mList = (ListView) view.findViewById(R.id.list);
		mConfirmBtn = (Button) view.findViewById(R.id.confirm_btn);
		mSroceAdapter = new ScoreVotingAdapter(getActivity(),
				TVBVotingApp.getInstance().getVotingInfo().getPlayersList());
		mList.setAdapter(mSroceAdapter);
		mList.setOnItemClickListener(this);
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
		ScoreVotingTwoFrag frag = ScoreVotingTwoFrag.newInstance(position,false);
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.container, frag, "voting2");
		ft.hide(this);
		ft.addToBackStack("voting2");
		ft.commit();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		Utils.PrintLogE(TAG, "pos:" + position);
		initListView(position);

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
		if (mSroceAdapter == null) {
			return;
		}
		mSroceAdapter.notifyDataSetChanged();
	}
	@Subscribe(threadMode = ThreadMode.MainThread)
	public void OnEventCmd(EventBusCMD cmd) {
		if(cmd.getCmd() == Contant.CHANGE_LANG) {
			mConfirmBtn.setText(R.string.submit);
		}
	}


}
