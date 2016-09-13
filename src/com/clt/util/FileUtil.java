package com.clt.util;

import java.io.File;

import android.os.Environment;

/**
 * 文件操作工具类
 *
 */
public class FileUtil
{
    /**
     * 判断上传的文件是否存在
     * 
     * @param fileName
     * @return
     */
    public boolean isFileExist(String fileName)
    {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
        {
            File uploadFile = new File(
                    Environment.getExternalStorageDirectory(), fileName);
            if (uploadFile.exists())
            {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 获取文件的大小
     * 
     * @param fileName
     * @return
     */
    public long getFileSize(String fileName)
    {
        if (isFileExist(fileName))
        {
            File uploadFile = new File(
                    Environment.getExternalStorageDirectory(), fileName);
            return uploadFile.length();
        }
        return -1;
    }
    
    /**
     * 删除本地文件
     * @param file
     * @return
     */
    public static boolean deleteLocalFile(File file){
        boolean flag=false;
        // 删除文件
        if (file.exists())
        {

            flag = file.delete();
        }
        return flag;
    }
    
    /**
     * 删除本地文件夹
     */
    public static boolean deleteLocalDirectory(String dirpath){
        boolean flag=false;
        File dir=new File(dirpath);
        if(!dir.isDirectory()){
            return false;
        }
        if (dir.exists())
        {
            File [] files = dir.listFiles();
            for (File file : files)
            {
                flag = file.delete();
                if (flag == false)
                {
                    break;
                }
            }
            flag = dir.delete();
        }
        return flag;
        
    }

}
