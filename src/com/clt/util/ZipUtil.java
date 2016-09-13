package com.clt.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * zip压缩文件工具类
 * @author caocong 2014.5.8
 *
 */
public class ZipUtil
{
    /** 
     * 解压文件到当前目录 功能相当于右键 选择解压 
     * @param zipFile 
     * @param 
     * @author gabriel 
     */ 
    @SuppressWarnings("rawtypes") 
    public static void unZipFiles(File zipFile)throws IOException{ 
        //得到压缩文件所在目录 
        String path=zipFile.getAbsolutePath(); 
        path=path.substring(0,path.lastIndexOf("\\")); 
       // System.out.println("path "+path); 
        ZipFile zip = new ZipFile(zipFile); 
        for(Enumeration entries =zip.entries(); 
                entries.hasMoreElements();){ 
            ZipEntry entry = (ZipEntry)entries.nextElement(); 
            String zipEntryName = entry.getName();
           
            InputStream in = zip.getInputStream(entry); 
            String outPath = path+"\\"+zipEntryName;
            if(zipEntryName.endsWith("/")){//是目录
                File dir=new File(outPath);
                if(!dir.exists()){
                    dir.mkdirs(); 
                }
                continue;
            }
            
            //判断路径是否存在,不存在则创建文件路径 
            File file = new File(outPath);
            if(!file.getParentFile().exists()){ 
                file.mkdirs(); 
            }
            if(!file.exists()){
                file.createNewFile();
            }

            OutputStream out = new FileOutputStream(file); 
            byte[] buf1 = new byte[1024]; 
            int len; 
            while((len=in.read(buf1))>0){ 
                out.write(buf1,0,len); 
            } 
            in.close(); 
            out.close(); 
            } 
    } 

}
