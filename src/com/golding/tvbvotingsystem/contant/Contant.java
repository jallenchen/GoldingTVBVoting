package com.golding.tvbvotingsystem.contant;

import android.os.Environment;

/**
 * @author :Jallen
 * @date ：2017-6-16 4:47:30
 * @version :1.0
 * @desc :
 */
public class Contant {
	public static  String address = "ws://192.168.0.2:9002/votingSysTVB";
	public static final String KEY_SHARE_NAME= "tvb";
	public static final String KEY_INDEX = "index";
	public static final String KEY_ONLYONE = "onlyone";
	public static  String fILE_SEATNUM = "seatnum.txt";
	public static final String SUCESS = "200";

	public static final int VOTING_SCORE = 1;
	public static final int VOTING_CHECK = 2;
	public static final int VOTING_LICKORUNLIKE = 3;
	public static final int VOTING_LIKE = 4;

	public static String seatNum = "";
	public static final String PSW = "tvb888";
	public static final String CHECKNET_PSW = "147258369";

	/* CATEGORY class id */
	public final static int CATEGORY_CLIENT_INFO_ID  = 1;   // 客户端
	public final static int CATEGORY_VOTING_ID  = 2;         //活动
	public final static int CATEGORY_VOTING_RESULT_ID  = 3;  //投票结果 
	public final static int CATEGORY_VOTING_RESPOND_ID  = 4;  //反馈 

	//PROPERTY   id 
	//CATEGORY_CLIENT_INFO_ID
	public final static int PROPERTY_DIAGNOSE_DEVICE_ID   = 1;     // 客户端诊断
	public final static int PROPERTY_CLIENT_STATE_ID   = 2;       // 客户端状态 在线 离线  后台根据客户端状态变化通知屏端
	public final static int PROPERTY_CLIENT_LOGIN_ID   = 3;     // 客户端登录
	public final static int PROPERTY_CLIENT_UPGRADE_ID   = 4;     // 客户端升级

	//CATEGORY_VOTING_ID
	public final static int PROPERTY_VOTING_CMD_ID   = 1;   // 指令信息
	public final static int PROPERTY_VOTING_INFO_ID   = 2;   // 活动信息
	public final static int PROPERTY_VOTING_SEATINFO_ID   = 3;   // 保存座位信息

	//CATEGORY_VOTING_RESULT_ID
	public final static int PROPERTY_VOTING_RESULT_STATISC_ID   = 1;   // 投票 统计 结果
	public final static int PROPERTY_VOTING_RESULT_SUBMIT_ID   = 2;   // 保存投票结果

	//CATEGORY_VOTING_RESPOND_ID  
	public final static int PROPERTY_RESPOND_ID   = 1;   // 指令回复


	public static final int VOTING_START = 1;
	public static final int VOTING_STOP = 2;
	public static final int VOTING_CHECKPSW= 3;
	public static final int VOTING_FIXPSW= 4;
	public static final int VOTING_DIAGNOSE= 5;
	public static final int VOTING_UPGRADE= 6;

	public static final int ONLYONE = 0;
	public static final int NOTCHECK = 0;
	public static final int LIKEORCHECK = 1;
	public static final int UNLIKE = 2;

	public static final int CHANGE_LANG = 0x10;
	public static final int CHECKNET_OK = 0x101;
	public static final int CHECKNET_NG = 0x102;

	public static String SystenTime = "";

	
    public static String getSeatNumFolderTxt() {
        return Environment.getExternalStorageDirectory() + "/TVBSeatNum/";
    }
}
