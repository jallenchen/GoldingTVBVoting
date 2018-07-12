package com.golding.tvbvotingsystem.socket;

interface ISocketService{
	//boolean sendMessage(String message);
	boolean sendByteMessage(int category,int categorySub,in byte[] data);
}