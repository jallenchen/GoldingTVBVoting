package com.golding.tvbvotingsystem;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.golding.tvbvotingsystem.bean.EventBusCMD;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/** 
 * @author  :Jallen 
 * @date    ：2017-6-19 下午2:00:40 
 * @version :1.0 
 * @desc :
 */
public class TVBBaseActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		EventBus.getDefault().register(this);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void OnEventCmd(EventBusCMD cmd) {
	}
}
