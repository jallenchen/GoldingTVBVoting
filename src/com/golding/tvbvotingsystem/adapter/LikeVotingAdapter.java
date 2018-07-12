package com.golding.tvbvotingsystem.adapter;

import java.util.List;

import com.golding.tvbcloud.VotingInfo.*;
import com.golding.tvbcloud.VotingResultInfo;
import com.golding.tvbvotingsystem.R;
import com.golding.tvbvotingsystem.TVBVotingApp;
import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.fragment.LikeVotingOneFrag.MyImageButtonListener;
import com.golding.tvbvotingsystem.ui.MyImageButton;
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
 * @date ：2017-6-19 下午7:17:43
 * @version :1.0
 * @desc :
 */
public class LikeVotingAdapter extends BaseAdapter {
	private final LayoutInflater inflater;
	private Context mContext;
	private List<CVotingPlayer> mSingers ;
	private MyImageButtonListener mLisenter;
	public LikeVotingAdapter(Context context, List<CVotingPlayer> list,MyImageButtonListener listener) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		mSingers = list;
		mLisenter = listener;
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

	public void refreshDatas(List<CVotingPlayer> list){
		mSingers = list;
		notifyDataSetChanged();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder ;
		CVotingPlayer singer = mSingers.get(position);
		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_voting_like, null);
			viewHolder.mIndex = (TextView) convertView.findViewById(R.id.index);
			viewHolder.mHead = (ImageView) convertView.findViewById(R.id.head);
			viewHolder.mLike = (MyImageButton) convertView.findViewById(R.id.like);
			viewHolder.mAllow = (MyImageButton) convertView.findViewById(R.id.allow);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mLike.setIndex(position);
		viewHolder.mAllow.setIndex(position);
		viewHolder.mLike.setOnClickListener(mLisenter);
		viewHolder.mAllow.setOnClickListener(mLisenter);
		viewHolder.mIndex.setText(singer.getIndexNum());

		VotingResultInfo.CContestantRecord contestantRecord = TVBVotingApp.getInstance().getContestantRecordMap().get(singer.getUserId());
		if (TVBVotingApp.getInstance().getContestantRecordMap().containsKey(singer.getUserId())
				&& contestantRecord.getRecordValue(Contant.ONLYONE).getRecordData() == Contant.LIKEORCHECK) {
			viewHolder.mLike.setSelected(true);
			viewHolder.mAllow.setSelected(true);
	} else {
			viewHolder.mLike.setSelected(false);
			viewHolder.mAllow.setSelected(false);
		}
		
		//Utils.setImage(singer.getName(),singer.getImageS(),"S",viewHolder.mHead);
		Utils.setImage(singer.getImageS(),viewHolder.mHead);
		return convertView;
	}

	private class ViewHolder {
		private TextView mIndex;
		private ImageView mHead;
		private MyImageButton mLike;
		private MyImageButton mAllow;
	}

}
