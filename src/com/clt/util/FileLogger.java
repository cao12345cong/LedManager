package com.clt.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/**
 * 错误日志
 * @author Administrator
 */
public class FileLogger
{
    private static String filePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
    private static String fileName="filelog.txt";
    private boolean DEBUG=false;
    private static FileLogger instance;
    private File file;
    Context context;
    private FileLogger(Context context)
    {
        if(!DEBUG){
            return;
        }
        if(context!=null){
            context=context;
        }
        if(!hasSDCard()){
            return;
        }
        file=new File(filePath,fileName);
        // 如果父目录不存，则创建该目录
        if (!file.getParentFile().exists())
        {
            file.getParentFile().mkdirs();
        }
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public static FileLogger getInstance(Context context){
        
        if(instance==null){
            instance=new FileLogger(context);
        }
        return instance;
    }
    
    
    public void writeMessageToFile(String message){
        if(!DEBUG){
            return;
        }
        if(!hasSDCard()){
            return;
        }
        try
        {
            FileOutputStream fos=new FileOutputStream(file,true);
            PrintWriter pw=new PrintWriter(fos);
            pw.println("LedManager "+getTime());
            pw.println(message);
            pw.flush();
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void toastMessage(String message){
        if(!DEBUG){
            return;
        }
        Toast.makeText(context, message, 1000).show();
    }
    
    public String getTime(){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
        return dateFormat.format(now);
    }
   
    /**
     * 清空txt的内容
     * @throws FileNotFoundException 
     */
    public void clearTxtContent() throws FileNotFoundException{
        if(!DEBUG){
            return;
        }
        if(!hasSDCard()){
            return;
        }
        FileOutputStream fos=new FileOutputStream(file,false);
        PrintWriter pw=new PrintWriter(fos);
        pw.println("");
    }
//    public static void main(String [] args) throws FileNotFoundException
//    {
//        FileLogger.getInstance().clearTxtContent();
//        FileLogger.getInstance().writeMessage("我的");
//        FileLogger.getInstance().writeMessage("error2");
//    }
    /**
     * 判断手机是否存在SD卡
     * @return
     */
    public boolean hasSDCard(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
