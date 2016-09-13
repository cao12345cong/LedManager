package com.clt.upload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class Ftp
{
    private static String USERNAME = "log";

    private static String PASSWORD = "log";

    private static int PORT = 21;

    private static String REMOTE_DIR = "/program/";

    private FTPClient ftpClient;

    private String strIp;

    /* *
     * Ftp构造函数
     */
    public Ftp(String strIp)
    {
        this.strIp = strIp;
        this.ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
    }

    /**
     * @return 判断是否登入成功
     * */
    public boolean login()
    {
        try
        {
            boolean success = false;
            int reply;
            ftpClient.connect(strIp);// 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            success = ftpClient.login(USERNAME, PASSWORD);// 登录
            if (!success)
            {
                return success;
            }
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftpClient.disconnect();
                return success;
            }
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            return success;
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @退出关闭服务器链接
     * */
    public void close()
    {
        if (null != this.ftpClient && this.ftpClient.isConnected())
        {
            try
            {
                boolean reuslt = this.ftpClient.logout();// 退出FTP服务器
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    this.ftpClient.disconnect();// 关闭FTP服务器的连接
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 在ftp服务端新建文件夹
     * @param remoteDirectoryPath 应该以/结束
     * @return
     */
    public boolean createDirctory(String remoteDirectoryPath)
    {
        try
        {
            return this.ftpClient.makeDirectory(remoteDirectoryPath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /***
     * 上传Ftp文件
     * @param localFile 当地文件
     * @param romotUpLoadePath上传服务器路径 - 应该以/结束
     * */
    public boolean uploadFile(File localFile, String romotUpLoadePath)
    {
        BufferedInputStream inStream = null;
        boolean success = false;
        try
        {
            this.ftpClient.makeDirectory(romotUpLoadePath);
            this.ftpClient.changeWorkingDirectory(romotUpLoadePath);// 改变工作路径
            inStream = new BufferedInputStream(new FileInputStream(localFile));
            success = this.ftpClient.storeFile(localFile.getName(), inStream);
            if (success == true)
            {
                return success;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (inStream != null)
            {
                try
                {
                    inStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    /***
     * 下载文件
     * @param remoteFileName   待下载文件名称
     * @param localDires 下载到当地那个路径下
     * @param remoteDownLoadPath remoteFileName所在的路径
     * */

    public boolean downloadFile(String remoteFileName, String localDires,
            String remoteDownLoadPath)
    {
        String strFilePath = localDires + remoteFileName;
        BufferedOutputStream outStream = null;
        boolean success = false;
        try
        {
            this.ftpClient.changeWorkingDirectory(remoteDownLoadPath);
            outStream = new BufferedOutputStream(new FileOutputStream(
                    strFilePath));
            success = this.ftpClient.retrieveFile(remoteFileName, outStream);
            if (success == true)
            {
                return success;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != outStream)
            {
                try
                {
                    outStream.flush();
                    outStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (success == false)
        {
        }
        return success;
    }

    /***
     * @上传文件夹
     * @param localDirectory
     *            当地文件夹
     * @param remoteDirectoryPath
     *            Ftp 服务器路径 以目录"/"结束
     * */
    public boolean uploadDirectory(String localDirectory,
            String remoteDirectoryPath)
    {
        File src = new File(localDirectory);
        try
        {
            remoteDirectoryPath = remoteDirectoryPath + src.getName() + "/";
            this.ftpClient.makeDirectory(remoteDirectoryPath);
            FTPFile [] files = ftpClient.listDirectories();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        File [] allFile = src.listFiles();
        for (int currentFile = 0; currentFile < allFile.length; currentFile++)
        {
            if (!allFile[currentFile].isDirectory())
            {
                String srcName = allFile[currentFile].getPath().toString();

                uploadFile(new File(srcName), remoteDirectoryPath);
            }
        }
        // for (int currentFile = 0; currentFile < allFile.length;
        // currentFile++)
        // {
        // if (allFile[currentFile].isDirectory())
        // {
        // // 递归
        // uploadDirectory(allFile[currentFile].getPath().toString(),
        // remoteDirectoryPath);
        // }
        // }
        return true;
    }

    /***
     * @下载文件夹
     * @param localDirectoryPath本地地址
     * @param remoteDirectory 远程文件夹
     * */
    public boolean downLoadDirectory(String localDirectoryPath,
            String remoteDirectory)
    {
        try
        {
            String fileName = new File(remoteDirectory).getName();
            localDirectoryPath = localDirectoryPath + fileName + "//";
            new File(localDirectoryPath).mkdirs();
            FTPFile [] allFile = this.ftpClient.listFiles(remoteDirectory);
            for (int currentFile = 0; currentFile < allFile.length; currentFile++)
            {
                if (!allFile[currentFile].isDirectory())
                {
                    downloadFile(allFile[currentFile].getName(),
                            localDirectoryPath, remoteDirectory);
                }
            }
            for (int currentFile = 0; currentFile < allFile.length; currentFile++)
            {
                if (allFile[currentFile].isDirectory())
                {
                    String strremoteDirectoryPath = remoteDirectory + "/"
                            + allFile[currentFile].getName();
                    downLoadDirectory(localDirectoryPath,
                            strremoteDirectoryPath);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // FtpClient的Set 和 Get 函数
    public FTPClient getFtpClient()
    {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient)
    {
        this.ftpClient = ftpClient;
    }

    // public static void main(String [] args) throws IOException
    // {
    // Ftp ftp = new Ftp("192.168.1.110", 21, "log", "log");
    // ftp.ftpLogin();
    // // 上传文件夹
    // ftp.uploadDirectory("E:/caocong.files", "/program/");
    // // 下载文件夹
    // //ftp.downLoadDirectory("d://tmp//", "/home/data/DataProtemp");
    // ftp.ftpLogOut();
    // }

    /** */
    /** 
    * 上传文件到服务器,新上传和断点续传 
    * @param remoteFile 远程文件名，在上传之前已经将服务器工作目录做了改变 
    * @param localFile 本地文件File句柄，绝对路径 
    * @param processStep 需要显示的处理进度步进值 
    * @param ftpClient FTPClient引用 
    * @return 
    * @throws IOException 
    */
    public boolean uploadFile(File localFile, String remotePath,long remoteSize,OnUploadListener listener)
    {
        // 显示进度的上传
        try
        {
            boolean flag = this.ftpClient.makeDirectory(new File(remotePath).getParent());
            ftpClient.setControlEncoding("UTF-8");//这里设置编码
            long totalSize = localFile.length();
            //long process = 0;
            long localreadbytes = 0L;
            RandomAccessFile raf = new RandomAccessFile(localFile, "r");
            //String path = remoteFile.getPath();
            OutputStream out = ftpClient.storeFileStream(new String(remotePath.getBytes("UTF-8"),"UTF-8"));
            int code = ftpClient.getReplyCode();
            // 断点续传
            if (remoteSize > 0)
            {
                ftpClient.setRestartOffset(remoteSize);
                //process = remoteSize / step;
                raf.seek(remoteSize);
                localreadbytes = remoteSize;
            }
            byte [] bytes = new byte [1024*2];
            int c;
            while ((c = raf.read(bytes)) != -1)
            {
                out.write(bytes, 0, c);
                localreadbytes += c;
                if(listener!=null){
                    listener.onUploadprogress(localreadbytes, totalSize);
                }
            }
            out.flush();
            raf.close();
            out.close();
            return ftpClient.completePendingCommand();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 在服务器上创建一个文件夹
     *
     * @param dir 文件夹名称，不能含有特殊字符，如 \ 、/ 、: 、* 、?、 "、 <、>...
     */
    public boolean makeDirectory(String dir)
    {
        boolean flag = true;
        try
        {
            flag = ftpClient.makeDirectory(dir);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return flag;
    }
}
