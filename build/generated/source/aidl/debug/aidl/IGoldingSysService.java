/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Jallen\\TVB\\TVBSystemCode\\GoldingTVBVoting\\src\\aidl\\IGoldingSysService.aidl
 */
package aidl;
// Declare any non-default types here with import statements

public interface IGoldingSysService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements aidl.IGoldingSysService
{
private static final java.lang.String DESCRIPTOR = "aidl.IGoldingSysService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an aidl.IGoldingSysService interface,
 * generating a proxy if needed.
 */
public static aidl.IGoldingSysService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof aidl.IGoldingSysService))) {
return ((aidl.IGoldingSysService)iin);
}
return new aidl.IGoldingSysService.Stub.Proxy(obj);
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
case TRANSACTION_exeCmd:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _result = this.exeCmd(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_installApk:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _result = this.installApk(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_updateSystem:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
int _result = this.updateSystem(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements aidl.IGoldingSysService
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
@Override public int exeCmd(java.lang.String cmd) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(cmd);
mRemote.transact(Stub.TRANSACTION_exeCmd, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int installApk(java.lang.String apkPath) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(apkPath);
mRemote.transact(Stub.TRANSACTION_installApk, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int updateSystem(java.lang.String filePath, java.lang.String md5) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(filePath);
_data.writeString(md5);
mRemote.transact(Stub.TRANSACTION_updateSystem, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_exeCmd = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_installApk = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_updateSystem = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public int exeCmd(java.lang.String cmd) throws android.os.RemoteException;
public int installApk(java.lang.String apkPath) throws android.os.RemoteException;
public int updateSystem(java.lang.String filePath, java.lang.String md5) throws android.os.RemoteException;
}
