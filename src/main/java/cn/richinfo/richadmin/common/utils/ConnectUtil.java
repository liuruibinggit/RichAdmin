package cn.richinfo.richadmin.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by LiuRuibing on 2019/4/8 0008.
 */
public class ConnectUtil {

    private static Logger logger = (Logger) LoggerFactory.getLogger(ConnectUtil.class);

    private final static String a = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiIzaWNvLmNvbSIsImlhdCI6MTUwNDYxNjA3NCwiZXhwIjoxNTA0NjI2ODc0LCJ1aWQiOjY2OTksInVjb2RlIjoiMTU1ODUiLCJtb2JpbGUiOiIxODU2NTcwMjk2OSIsInJvbGUiOjN9.YYcxuwLvYwHMnlWPHhNpww3ppGD3PtwmdSeMEF73Hkh5b68mDAi5BzFZafj8sC5X0VUwulJbcb2XiTqKaR7Kgw";


    public static String sendPost(String path) throws IOException{
        String msg = "";// 保存调用http服务后的响应信息
        java.net.URL url = new java.net.URL(path);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        try{
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(10 * 1000);// 设置连接超时时间为5秒
            conn.setReadTimeout(20 * 1000);// 设置读取超时时间为20秒
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Content-Encoding", "gzip");
            conn.setRequestProperty("Authorization", a);
            OutputStream outStream = conn.getOutputStream();// 返回写入到此连接的输出流
            outStream.close();// 关闭流
            // 如果请求响应码是200，则表示成功
            if (conn.getResponseCode() == 200) {
                // HTTP服务端返回的编码是UTF-8,故必须设置为UTF-8,保持编码统一,否则会出现中文乱码
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                msg = in.readLine();
                in.close();
            }
        }catch(Exception e){
            logger.error("tid={} | msg=连接Q5系统失败", e);
        }
        conn.disconnect();// 断开连接
        return msg;
    }

    public static void main(String[] args) throws IOException{
        String url= "http://120.55.160.250:8080/api/batch/batchtransfer?address=0x09cD4ddB021CaeD6f5Bb31e943558b287C4854A1&amount=1187500&tokenAddress=0x97b846f94368105266c332c70bc17e921058e87e";
        String s = sendPost(url);
        System.out.println(s);
    }
}
