package cn.richinfo.richadmin.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Component
public class UseOtherSystemInterface {

    @Autowired
    Environment environment;
    /**
     * 通过工号从hr系统中获取用户信息
     */
    public String sendPost(String userId) throws IOException {
        byte[] data = userId.getBytes();
        String path = environment.getProperty("hr_url") + "?userName=" + environment.getProperty("userName") + "&password="
                + environment.getProperty("password")+"&userId="+userId;
        java.net.URL url = new java.net.URL(path);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5 * 1000);// 设置连接超时时间为5秒
        conn.setReadTimeout(20 * 1000);// 设置读取超时时间为20秒
        // 使用 URL 连接进行输出，则将 DoOutput标志设置为 true
        conn.setDoOutput(true);

        conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        conn.setRequestProperty("Content-Encoding", "gzip");
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        OutputStream outStream = conn.getOutputStream();// 返回写入到此连接的输出流
        outStream.write(data);
        outStream.close();// 关闭流
        String msg = "";// 保存调用http服务后的响应信息
        // 如果请求响应码是200，则表示成功
        if (conn.getResponseCode() == 200) {
            // HTTP服务端返回的编码是UTF-8,故必须设置为UTF-8,保持编码统一,否则会出现中文乱码
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            msg = in.readLine();
            in.close();
        }
        conn.disconnect();// 断开连接
		/*
		 * 将获取从hr系统获取的数据转化成标准的json串
		 */
        String newMsg = msg.replace("\"{", "{");
        newMsg = newMsg.replace("}\"", "}");
        newMsg = newMsg.replace("\\\"", "\"");
        return 	newMsg.substring(18,newMsg.lastIndexOf(",\"flag\""));  //截取data中的员工信息；
    }
}
