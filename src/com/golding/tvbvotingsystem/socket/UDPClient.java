package com.golding.tvbvotingsystem.socket;

/**
 * Created by Jallen on 2018/5/16 0016 10:19.
 */
import android.content.Context;
import android.content.SharedPreferences;

import com.golding.tvbvotingsystem.TVBVotingApp;
import com.golding.tvbvotingsystem.contant.Contant;
import com.golding.tvbvotingsystem.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
/*
   //建立线程池  调用
                    ExecutorService exec = Executors.newCachedThreadPool();
                    client = new UDPClient();
                    exec.execute(client);
 */

public class UDPClient implements Runnable{
    final static int udpPort = 9004;
    final static String hostIp = "255.255.0.0";
    private static MulticastSocket socket = null;
    private static DatagramPacket packetSend,packetRcv;
    private boolean udpLife = true; //udp生命线程
    private byte[] msgRcv = new byte[1024]; //接收消息
    private String RcvIp = "";

    public UDPClient(){
        super();
    }

    //返回udp生命线程因子是否存活
    public boolean isUdpLife(){
        if (udpLife){
            return true;
        }

        return false;
    }

    //更改UDP生命线程因子
    public void setUdpLife(boolean b){
        udpLife = b;
    }

    //发送消息
    public String send(String msgSend){
        InetAddress hostAddress = null;

        try {
            hostAddress = InetAddress.getByName(hostIp);
        } catch (UnknownHostException e) {
            Utils.PrintLogE("udpClient","未找到服务器");
            e.printStackTrace();
        }

        packetSend = new DatagramPacket(msgSend.getBytes() , msgSend.getBytes().length,hostAddress,udpPort);

        try {
            socket.send(packetSend);
        } catch (IOException e) {
            e.printStackTrace();
            Utils.PrintLogE("udpClient","发送失败");
        }
        //   socket.close();
        return msgSend;
    }

    @Override
    public void run() {

        try {
            socket = new MulticastSocket(udpPort);
            //socket.setSoTimeout(3000);//设置超时为3s
        } catch (IOException e) {
            Utils.PrintLogE("udpClient","建立接收数据报失败");
            e.printStackTrace();
        }
        packetRcv = new DatagramPacket(msgRcv,msgRcv.length);
        while (udpLife){
            try {
                Utils.PrintLogD("udpClient", "UDP监听");
                socket.receive(packetRcv);
                String RcvMsg = new String(packetRcv.getData(),packetRcv.getOffset(),packetRcv.getLength());
                Utils.PrintLogD("Rcv",RcvMsg);
                Contant.address = "ws://"+RcvMsg+":9002/votingSysTVB";
                if(RcvMsg.equals(RcvIp)){
                    return;
                }
                RcvIp = RcvMsg;
                //TODO 将收到的消息处理
                SharedPreferences mShare = TVBVotingApp.getInstance().getSharedPreferences(Contant.KEY_SHARE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = mShare.edit();
                edit.putString("ip", RcvIp);
                edit.putString("serverUrl", Contant.address);
                edit.apply();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        Utils.PrintLogD("udpClient","UDP监听关闭");
        socket.close();
    }
}
