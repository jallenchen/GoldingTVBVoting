package com.golding.tvbvotingsystem;

import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TVBSettingActivity extends TVBBaseActivity implements OnClickListener{
	private EditText seatNumET,ipET,portET;
	private Button confirmBtn;
	private SharedPreferences mShare;
	private String ip,port,seatNum;
	private TextView mVersionTV;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_tvbsetting);
		initView();
		initData();
	}
	
	private void initView(){
		seatNumET = (EditText) findViewById(R.id.edit_seat);
		ipET = (EditText) findViewById(R.id.edit_ip);
		portET = (EditText) findViewById(R.id.edit_port);
		confirmBtn = (Button) findViewById(R.id.set_confirm);
		mVersionTV = (TextView) findViewById(R.id.version);

		confirmBtn.setOnClickListener(this);
		mShare = getSharedPreferences(Contant.KEY_SHARE_NAME,Context.MODE_PRIVATE);
		seatNum = Contant.seatNum;
		ip = mShare.getString("ip", "");
		port = mShare.getString("port", "9002");
		String ver = "System:"+Utils.getSysOsVersion() +" App:"+Utils.getAppVersionName(this);
		mVersionTV.setText(ver);
	}
	
	private void initData(){
		seatNumET.setText(seatNum);
		ipET.setText(ip);
		portET.setText(port);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.set_confirm:
			seatNum = seatNumET.getEditableText().toString();
			seatNum = seatNum.replace(" ","");
		//	ip = ipET.getEditableText().toString();
			port = portET.getEditableText().toString();

			if(!Utils.isValidSeatNum(seatNum)){
				Utils.ToastMessage(TVBSettingActivity.this,R.string.seatnum_error);
				return;
			}
		//	if(!Utils.isValidIpAddress(ip)){
			//	Utils.ToastMessage(TVBSettingActivity.this,R.string.ipaddress_error);
				//return;
		//	}

			Utils.setSeatNum2File(seatNum);
			Contant.seatNum = seatNum;

		//	String server=String.format("ws://%s:%s/votingSysTVB", ip,port);
			Editor edit = mShare.edit();
		//	edit.putString("serverUrl", server);
		//	edit.putString("ip", ip);
			edit.putString("port", port);
			edit.putString("seatNum", Contant.seatNum);
			edit.apply();
			Intent intent = new Intent();
		//	intent.putExtra("serverUrl", server);
			setResult(RESULT_OK,intent);
			finish();
			break;
		}
		
	}
}
