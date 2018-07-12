// IGoldingSysService.aidl
package aidl;

// Declare any non-default types here with import statements

interface IGoldingSysService {
   int exeCmd(String cmd);
   int installApk(String apkPath);
   int updateSystem(String filePath,String md5);
}
