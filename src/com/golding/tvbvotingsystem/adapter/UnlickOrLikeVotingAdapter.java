package com.golding.tvbvotingsystem.adapter;

import java.util.List;

import com.golding.tvbcloud.VotingInfo.CVotingPlayer;
import com.golding.tvbvotingsystem.R;
import com.golding.tvbvotingsystem.TVBVotingApp;
import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.fragment.UnLikeOrLikeVotingOneFrag.MyImageButtonListener;
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
 * @date ：2017-6-19 下午2:10:35
 * @version :1.0
 * @desc :
 */
public class UnlickOrLikeVotingAdapter extends BaseAdapter {
	private final LayoutInflater inflater;
	private Context mContext;
	private List<CVotingPlayer> mSingers ;
	private MyImageButtonListener mLisenter;
	public UnlickOrLikeVotingAdapter(Context context,
			List<CVotingPlayer> list,MyImageButtonListener listener) {
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder ;
		CVotingPlayer singer = mSingers.get(position);
		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_voting_unlike, null);
			viewHolder.mIndex = (TextView) convertView.findViewById(R.id.index);
			viewHolder.mHead = (ImageView) convertView.findViewById(R.id.head);
			viewHolder.mUnLike = (MyImageButton) convertView
					.findViewById(R.id.unlike_mib);
			viewHolder.mLike = (MyImageButton) convertView.findViewById(R.id.like);
			viewHolder.mAllow = (MyImageButton) convertView.findViewById(R.id.allow);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mLike.setIndex(position);
		viewHolder.mUnLike.setIndex(position);
		viewHolder.mAllow.setIndex(position);
		viewHolder.mLike.setOnClickListener(mLisenter);
		viewHolder.mUnLike.setOnClickListener(mLisenter);
		viewHolder.mAllow.setOnClickListener(mLisenter);
		viewHolder.mIndex.setText(singer.getIndexNum());
		
		if (TVBVotingApp.getInstance().getContestantRecordMap().containsKey(singer.getUserId())) {
			int data = TVBVotingApp.getInstance().getContestantRecordMap()
					.get(singer.getUserId()).getRecordValue(Contant.ONLYONE).getRecordData();
			if(data == Contant.LIKEORCHECK){
                viewHolder.mLike.setSelected(true);
				viewHolder.mUnLike.setSelected(false);
				viewHolder.mAllow.setSelected(true);
            }else if(data == Contant.UNLIKE){
                viewHolder.mUnLike.setSelected(true);
				viewHolder.mLike.setSelected(false);
				viewHolder.mAllow.setSelected(true);
            }else{
				viewHolder.mLike.setSelected(false);
				viewHolder.mUnLike.setSelected(false);
				viewHolder.mAllow.setSelected(false);
			}
		} else {
			viewHolder.mLike.setSelected(false);
			viewHolder.mUnLike.setSelected(false);
			viewHolder.mAllow.setSelected(false);
		}
		
		//Utils.setImage(singer.getName(),singer.getImageS(),"S",viewHolder.mHead);
		Utils.setImage(singer.getImageS(),viewHolder.mHead);
		return convertView;
	}

	private class ViewHolder {
		private TextView mIndex;
		private ImageView mHead;
		private MyImageButton mUnLike;
		private MyImageButton mLike;
		private MyImageButton mAllow;
	}

}
