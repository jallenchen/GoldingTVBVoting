package com.golding.tvbvotingsystem.adapter;

import java.util.List;

import com.golding.tvbcloud.VotingResultInfo.*;
import com.golding.tvbcloud.VotingInfo.*;
import com.golding.tvbvotingsystem.R;
import com.golding.tvbvotingsystem.TVBVotingApp;
import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.utils.Utils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author :Jallen
 * @date ：2017-6-16 11:56:38
 * @version :1.0
 * @desc :
 */
public class ScoreVotingAdapter extends BaseAdapter {
	private final LayoutInflater inflater;
	private Context mContext;
	private List<CVotingPlayer>mSingers ;
	private int mScoreCnt = 4;

	public ScoreVotingAdapter(Context context,
							  List<CVotingPlayer> list) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		mSingers = list;
		mScoreCnt = TVBVotingApp.getInstance().getVotingInfo().getActivityInfo().getScoreTypeCount();
	}

	@Override
	public int getCount() {
		return mSingers.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder ;
		CVotingPlayer mSinger =  mSingers.get(position);
		CContestantRecord  contestantRecord = TVBVotingApp.getInstance().getContestantRecordMap().get(mSinger.getUserId());
		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_voting_score, null);
			viewHolder.mIndex = (TextView) convertView.findViewById(R.id.index);
			viewHolder.mHead = (ImageView) convertView.findViewById(R.id.head);

			viewHolder.mScore[0] = (TextView) convertView
					.findViewById(R.id.score1);
			viewHolder.mScore[1] = (TextView) convertView
					.findViewById(R.id.score2);
			viewHolder.mScore[2] = (TextView) convertView
					.findViewById(R.id.score3);
			viewHolder.mScore[3] = (TextView) convertView
					.findViewById(R.id.score4);
			viewHolder.mAllow = (ImageView) convertView
					.findViewById(R.id.allow);

			for (int i = mScoreCnt ;i<viewHolder.mScore.length;i++){
				viewHolder.mScore[i].setVisibility(View.GONE);
			}

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.mIndex.setText(mSinger.getIndexNum());
		if (TVBVotingApp.getInstance().getContestantRecordMap()
				.containsKey(mSinger.getUserId())) {
			for (int i = 0 ;i < mScoreCnt;i++){
				viewHolder.mScore[i].setText(contestantRecord.getRecordValue(i).getRecordData()+"");
			}
		}

		if (contestantRecord!= null && (contestantRecord.getRecordValue(Contant.ONLYONE).getRecordData() != Contant.NOTCHECK)) {
			
			viewHolder.mAllow.setBackgroundResource(R.drawable.arrow_left_blue);
		} else {
			viewHolder.mAllow.setBackgroundResource(R.drawable.arrow_right_gray);
		}
		//Utils.setImage(mSinger.getName(),mSinger.getImageS(),"S",viewHolder.mHead);
		Utils.setImage(mSinger.getImageS(),viewHolder.mHead);
		return convertView;
	}

	private class ViewHolder {
		private TextView mIndex;
		private ImageView mHead;
		private TextView[] mScore = new TextView[4] ;
		private ImageView mAllow;
	}

}
