package com.bluefatty.utils;

import com.bluefatty.common.URLType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-11-28
 */
public class HttpUtils {
    /**
     * 代理 请求
     *
     * @param urlStr
     * @param proxy
     * @param requestMessage
     * @return
     * @throws Exception
     */
    public static String request(String urlStr, Proxy proxy, String requestMessage) throws Exception {
        URLType urlType = URLType.HTTP;
        if(urlStr.startsWith("https")){
            urlType = URLType.HTTPS;
        }
        return request(urlStr, proxy, requestMessage, urlType, "UTF-8", null);
    }

    /**
     * @Description: http请求工具类，完成HTTP请求
     * @LastEditTime:
     * @param urlStr
     *            请求URL
     * @param requestMessage
     *            请求报文
     *            请求URL的格式，
     * @return
     * @throws Exception
     */
    public static String request(String urlStr, String requestMessage) throws Exception {
        URLType urlType = URLType.HTTP;
        if(urlStr.startsWith("https")){
            urlType = URLType.HTTPS;
        }
        return request(urlStr, null, requestMessage, urlType, "UTF-8", null);
    }

    /**
     * 请求处理
     *
     * @param urlStr
     * @param proxy
     * @param requestMessage
     * @param urlType
     * @param chartSet
     * @param headers
     * @return
     * @throws Exception
     */
    public static String request(String urlStr, Proxy proxy, String requestMessage, URLType urlType, String chartSet, Map<String, String> headers) throws Exception {
        if (URLType.HTTPS.equals(urlType)) {
            return requestHttps(requestMessage, urlStr, proxy, chartSet,headers);
        }
        String result = "";
        URL url = new URL(urlStr);
        HttpURLConnection conn = null;
        OutputStream writer = null;
        InputStream inputStream = null;
        try {
            if (proxy != null) {
                conn = (HttpURLConnection) url.openConnection(proxy);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.setConnectTimeout(10000);
            //edit by caish 响应时间过长 占用资源
            conn.setReadTimeout(30000);//modify by crd 20190329 reason HIS接口时间没那么快，由原来的10000改成30000
            conn.setUseCaches(false);

            // 消息头
            if (headers == null) {
                conn.setRequestProperty("Content-Type", "text/xml");
            } else {
                for (Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            writer = conn.getOutputStream();

            if (chartSet == null || chartSet.trim().length() == 0) {
                chartSet = "UTF-8";
            }
            writer.write(requestMessage.getBytes(chartSet));
            System.out.println("------------------向外发送" + "请求------------------");
            System.out.println("请求URL:" + url);

            System.out.println("请求报文体：" + requestMessage);
            System.out.println("-------------------------------------------------------------");
            String responseStr = "", sCurrentLine = "";

            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, chartSet));

                while ((sCurrentLine = reader.readLine()) != null) {
                    responseStr = responseStr + sCurrentLine;
                }
            } else {
                System.out.println("响应错误：" + conn.getResponseCode() + ", " + conn.getResponseMessage());
                throw new Exception("响应错误:HTTP响应码:"+conn.getResponseCode()+","+ conn.getResponseMessage());
            }

            if (!"".equals(responseStr)) {
                result = responseStr;
            }

            System.out.println("响应内容：" + responseStr);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String urlStr = "https://127.0.0.1:8080/onepay-web/bankCallback/k4U9dR";
        urlStr = "https://api.weixin.qq.com/payinsurance/unifiedorder";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/x-www-form-urlencoded; charset=GBK");
        request(urlStr, null, "user=李刚&pass=abc", URLType.HTTPS, "GBK", headers);
    }

    private static String requestHttps(String message, String urlStr, Proxy proxy, String charSet) {
        String result = "";
        try {
            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return urlHostName.equals(session.getPeerHost());
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { tm }, null);

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            HttpsURLConnection conn = null;

            if (proxy != null) {
                conn = (HttpsURLConnection) (new URL(urlStr)).openConnection(proxy);
            } else {
                conn = (HttpsURLConnection) (new URL(urlStr)).openConnection();
            }
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            conn.setRequestProperty("Charset", charSet);
            conn.setRequestProperty("Content-Type", "text/xml");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream urlOutputStream = conn.getOutputStream();
            // String params = "params="+message;
            urlOutputStream.write(message.getBytes(charSet));
            urlOutputStream.close();
            System.out.println("请求报文体：" + message);
            System.out.println("-------------------------------------------------------------");
            String responseStr = "", sCurrentLine = "";
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charSet));

                while ((sCurrentLine = reader.readLine()) != null) {
                    responseStr = responseStr + sCurrentLine;
                }
                result = responseStr;
            }
            System.out.println("响应内容：" + responseStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String requestHttps(String message, String urlStr, Proxy proxy, String charSet, Map<String, String> headers) throws Exception {
        String result = "";
        HttpsURLConnection conn = null;
        InputStream inputStream = null;
        try {
            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return urlHostName.equals(session.getPeerHost());
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { tm }, null);

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);


            if (proxy != null) {
                conn = (HttpsURLConnection) (new URL(urlStr)).openConnection(proxy);
            } else {
                conn = (HttpsURLConnection) (new URL(urlStr)).openConnection();
            }
            // 消息头
            if (headers == null) {
                conn.setRequestProperty("Content-Type", "text/xml");
            } else {
                for (Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Charset", charSet);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream urlOutputStream = conn.getOutputStream();
            // String params = "params="+message;
            urlOutputStream.write(message.getBytes(charSet));
            urlOutputStream.close();
            System.out.println("请求报文体：" + message);
            System.out.println("-------------------------------------------------------------");
            String responseStr = "", sCurrentLine = "";
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charSet));

                while ((sCurrentLine = reader.readLine()) != null) {
                    responseStr = responseStr + sCurrentLine + System.getProperty("line.separator");
                }
                result = responseStr;
            }else{
                throw new Exception("响应错误:HTTP响应码:"+conn.getResponseCode()+",响应内容:"+ conn.getResponseMessage());
            }
            System.out.println("响应内容：" + responseStr);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if(inputStream != null) {
                inputStream.close();
            }
        }
        return result;
    }
}
