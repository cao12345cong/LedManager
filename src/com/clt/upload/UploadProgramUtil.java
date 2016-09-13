package com.clt.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import android.util.Log;

public class UploadProgramUtil
{

    private static int PORT = 80;

    // private static String
    // s="/transmission/ftp/terminals/{terminalName}/programs/{programName}";
    /**
     * 拷贝文件
     * @param from  全路径    D:/1.jpg
     * @param to    全路径    D:/1.jpg
     */
    public static File createFile(String from, String to)
    {
        try
        {
            File toFile = new File(to);
            if (!toFile.getParentFile().exists())
            {
                toFile.getParentFile().mkdirs();
            }
            if (!toFile.exists())
            {
                toFile.createNewFile();
            }

            int bytesum = 0;
            int byteread = 0;
            FileInputStream inStream = new FileInputStream(from); // 读入原文件
            FileOutputStream fs = new FileOutputStream(to);
            byte [] buffer = new byte [1024];
            int length;
            while ((byteread = inStream.read(buffer)) != -1)
            {
                bytesum += byteread; // 字节数 文件大小
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
            return toFile;
        }
        catch (Exception e)
        {
            return null;
        }

    }

    /**
     * 发送HTTP消息
     * @param ip
     * @param terminalName
     * @param programName
     * @return
     */
    public static boolean httpPost(String ip, String terminalName,
            String programName)
    {
        try
        {
            StringBuilder data = new StringBuilder();
            byte [] entity = data.toString().getBytes();
            String mUrl = "http://" + ip + ":80"
                    + "/transmission/ftp/terminals/" + terminalName
                    + "/programs/" + programName;
            URL url = new URL(mUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json;charset=utf-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(entity);
            int code = connection.getResponseCode();
            if (code == 200)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }

    }

    public String doPost(String url, List<NameValuePair> params)
    {
        /* 建立HTTPPost对象 */
        HttpPost httpRequest = new HttpPost(url);
        String strResult = "doPostError";

        // 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
        HttpParams httpParams = new BasicHttpParams();
        // 设置连接超时和 Socket 超时，以及 Socket 缓存大小
        HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        // 设置重定向，缺省为 true
        HttpClientParams.setRedirecting(httpParams, true);
        // 设置 user agent
        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
        HttpProtocolParams.setUserAgent(httpParams, userAgent);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        ;
        // 创建一个 HttpClient 实例

        try
        {
            /* 添加请求参数到请求对象 */
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            /* 发送请求并等待响应 */
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            /* 若状态码为200 ok */
            if (httpResponse.getStatusLine().getStatusCode() == 200)
            {
                /* 读返回数据 */
                strResult = EntityUtils.toString(httpResponse.getEntity());
            }
            else
            {
                strResult = "Error Response: "
                        + httpResponse.getStatusLine().toString();
            }
        }
        catch (ClientProtocolException e)
        {
            strResult = e.getMessage().toString();
            e.printStackTrace();
        }
        catch (IOException e)
        {
            strResult = e.getMessage().toString();
            e.printStackTrace();
        }
        catch (Exception e)
        {
            strResult = e.getMessage().toString();
            e.printStackTrace();
        }
        Log.v("strResult", strResult);
        return strResult;
    }

    /**
     * 判断一个文件是不是图片
     * @param path
     * @return
     */
    public static boolean isPicture(String path)
    {
    	String[] str={".jpg",".JPG",".png",".PNG",".gif",".GIF",".jpeg",".JPEG"};
    	boolean flag=false;
    	for (int i = 0; i < str.length; i++) {
			flag=flag||path.endsWith(str[i]);
		}
        return flag;
    }

    public static boolean isVedio(String path)
    {
    	String[] str={".rm",".RM",".rmvb",".RMVB",".mpeg",".MPEG",".avi",".AVI",".flv",".FLV"
    			,".wmv",".WMV",".mov",".MOV"};
    	boolean flag=false;
    	for (int i = 0; i < str.length; i++) {
			flag=flag||path.endsWith(str[i]);
		}
        return flag;
    }

    /***************************生成VSN*********************************************/


    /**
     * 用发射创建xml节点
     * @param root
     * @param obj
     * @throws Exception
     */
    public static void setElements(Element root, Object obj) throws Exception
    {
        Class<?> clz = obj.getClass();
        Field [] fields = clz.getDeclaredFields();
        Element e = null;
        for (Field field : fields)
        {// --for() begin
            // System.out.println(field.getName()+":"+field.getType());//打印该类的所有属性类型
            field.setAccessible(true);
            Object object = field.get(obj);
            // PropertyDescriptor pd = new
            // PropertyDescriptor(field.getName(),clz);

            String type = field.getType().toString();
            // System.out.println(type);
            e = root.addElement(field.getName());
            // Method getMethod= pd.getReadMethod();//获得get方法
            if (type.equals("int") || type.equals("float")
                    || type.equals("double") || type.equals("boolean")
                    || type.equals("long"))
            {

                String value = String.valueOf(object);
                e.setText(value);
            }
            else if (type.equals("class java.lang.String"))
            {

                String value = String.valueOf(object);
                e.setText(value);
            }
            else if (type.equals("interface java.util.List"))
            {
                List list = (List) object;
                int size = list.size();

                for (int i = 0; i < size; i++)
                {
                    String name = field.getName();
                    Element el = e.addElement(name.substring(0,
                            name.length() - 1));
                    ;
                    setElements(el, list.get(i));
                }

            }
            else
            {
                Object o = object;
                if (o != null)
                {
                    setElements(e, o);
                }

            }
        }
    }

}
