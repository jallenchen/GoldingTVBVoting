/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Jallen\\TVB\\TVBSystemCode\\GoldingTVBVoting\\src\\com\\golding\\tvbvotingsystem\\socket\\ISocketService.aidl
 */
package com.golding.tvbvotingsystem.socket;
public interface ISocketService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.golding.tvbvotingsystem.socket.ISocketService
{
private static final java.lang.String DESCRIPTOR = "com.golding.tvbvotingsystem.socket.ISocketService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.golding.tvbvotingsystem.socket.ISocketService interface,
 * generating a proxy if needed.
 */
public static com.golding.tvbvotingsystem.socket.ISocketService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.golding.tvbvotingsystem.socket.ISocketService))) {
return ((com.golding.tvbvotingsystem.socket.ISocketService)iin);
}
return new com.golding.tvbvotingsystem.socket.ISocketService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_sendByteMessage:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
byte[] _arg2;
_arg2 = data.createByteArray();
boolean _result = this.sendByteMessage(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.golding.tvbvotingsystem.socket.ISocketService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
//boolean sendMessage(String message);

@Override public boolean sendByteMessage(int category, int categorySub, byte[] data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(category);
_data.writeInt(categorySub);
_data.writeByteArray(data);
mRemote.transact(Stub.TRANSACTION_sendByteMessage, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_sendByteMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
//boolean sendMessage(String message);

public boolean sendByteMessage(int category, int categorySub, byte[] data) throws android.os.RemoteException;
}
