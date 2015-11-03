package com.gexin.artplatform.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import com.gexin.artplatform.dlog.DLog;

import android.util.Log;

public class NetUtil {

	private static final String TAG = "NetUtil";
	private static final int TIME_OUT = 100 * 1000; // 超时时间
	private static final String CHARSET = "utf-8"; // 设置编码

	public static final int GET = 0;
	public static final int POST = 1;
	public static final int PUT = 2;
	public static final int DELETE = 3;

	public static String connect(int method, String url,
			List<NameValuePair> data) {
		String result = "";
		HttpClient httpClient;
		httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 6000);
		try {
			HttpResponse response = null;
			switch (method) {
			case GET:
				response = httpClient.execute(new HttpGet(url));
				break;
			case POST:
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8));
				response = httpClient.execute(httpPost);
				break;
			case PUT:
				HttpPut httpPut = new HttpPut(url);
				httpPut.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8));
				response = httpClient.execute(httpPut);
				break;
			case DELETE:
				response = httpClient.execute(new HttpDelete(url));
				break;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line;
			while ((line = br.readLine()) != null)
				result += line;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param RequestURL
	 *            请求的rul
	 * @return 返回响应的内容
	 */
	public static String uploadFile(File file, String RequestURL) {
		int res = 0;
		String result = "";
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		DLog.v(TAG, "RequestURL:"+RequestURL+" File:"+file.getName());
		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			if (file != null) {
				/**
				 * 当文件不为空时执行上传
				 */
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * filename是文件的名字，包含后缀名
				 */

				sb.append("Content-Disposition: form-data; name=\"image\"; filename=\""
						+ file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
				res = conn.getResponseCode();
				DLog.v(TAG, "response code:" + res);
				if (res == 200) {
					DLog.v(TAG, "request success");
					InputStream input = conn.getInputStream();
					StringBuffer sb1 = new StringBuffer();
					int ss;
					while ((ss = input.read()) != -1) {
						sb1.append((char) ss);
					}
					result = sb1.toString();
					DLog.v(TAG, "result : " + result);
				} else {
					DLog.v(TAG, "request error");
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
