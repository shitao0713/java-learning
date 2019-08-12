package com.example.learn.common.util.http;

import com.example.learn.common.constant.HttpConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * httpClient工具类
 *
 * @author user
 * @date 2017年11月12日 下午4:05:20
 * @version 0.1.0
 * @copyright wonhigh.cn
 */

@Slf4j
public class HttpClientUtil {
	
	private static PoolingHttpClientConnectionManager connMgr;
	private static RequestConfig requestConfig;
	private static final int MAX_TIMEOUT = 70000;
	private static CloseableHttpClient httpClient;
	private static CloseableHttpClient httpSSLClient;
	
	/**
	 * 私有构造器
	 */
	private HttpClientUtil() {
	}
	
	static {
		// 设置连接池
		connMgr = new PoolingHttpClientConnectionManager();
		// 设置连接池大小
		connMgr.setMaxTotal(200);
		
		connMgr.setDefaultMaxPerRoute(40);
		
		//validateAfterInactivity 空闲永久连接检查间隔，这个牵扯的还比较多
		//官方推荐使用这个来检查永久链接的可用性，而不推荐每次请求的时候才去检查
		connMgr.setValidateAfterInactivity(2000);
		
		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(MAX_TIMEOUT);
		// 设置读取超时
		configBuilder.setSocketTimeout(MAX_TIMEOUT);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
		
		requestConfig = configBuilder.build();
		
		httpClient = HttpClients.custom().setConnectionManager(connMgr).setConnectionManagerShared(true).
				setDefaultRequestConfig(requestConfig).build();
		
		httpSSLClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
				.setConnectionManager(connMgr).setConnectionManagerShared(true).setDefaultRequestConfig(requestConfig)
				.build();
	}
	
	/**
	 * 发送 GET 请求（HTTP/HTTPS），K-V形式
	 *
	 * @param url api地址
	 * @param params 参数
	 * @return map
	 */
	public static Map<String, Object> doGetByMapParas(String url, Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>(4);
		String apiUrl = url;
		StringBuilder param = new StringBuilder();
		int i = 0;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (i == 0) {
				param.append(HttpConstant.QUESTION_MARK);
			} else {
				param.append(HttpConstant.AND_MARK);
			}
			String key = entry.getKey();
			Object value = entry.getValue();
			param.append(key).append(HttpConstant.EQUAL_MARK).append(value);
			i++;
		}
		apiUrl += param;
		HttpGet httpGet = null;
		CloseableHttpResponse response = null;
		try {
			httpGet = new HttpGet(apiUrl);
			String httpMethod = getHttpProtocol(apiUrl);
			
			// 如果线程池创建失败, 则使用默认创建httpClient连接
			ifPollingClientNullCreateDefaultConnection();
			
			if (HttpConstant.HTTPS.equalsIgnoreCase(httpMethod)) {
				response = httpSSLClient.execute(httpGet);
			} else {
				response = httpClient.execute(httpGet);
			}
			
			int successStateCode = 200;
			if (response.getStatusLine().getStatusCode() == successStateCode) {
				String httpStr = EntityUtils.toString(response.getEntity(), HttpConstant.HTTP_UTF_8);
				result.put(HttpConstant.HTTP_ERROR_CODE, HttpConstant.HTTP_SUCCESS);
				result.put(HttpConstant.HTTP_RESULT, httpStr);
				return result;
			}
			result.put(HttpConstant.HTTP_ERROR_CODE, HttpConstant.RESULT_ERROR);
			result.put(HttpConstant.HTTP_RESULT, "Error Response: " + response.getStatusLine().toString());
			
		} catch (IOException e) {
			log.error("httpGet error:", e);
			result.put(HttpConstant.HTTP_ERROR_CODE, HttpConstant.RESULT_ERROR);
			result.put(HttpConstant.HTTP_RESULT, "httpGet failure :caused by-->" + e.getMessage());
		} finally {
			try {
				closeHttpGetConnection(httpGet, response);
			} catch (IOException e) {
				log.error("httpGet failure :caused by-->", e);
			}
		}
		return result;
	}
	
	private static String getHttpProtocol(String apiUrl) {
		String httpMethod = "";
		if (apiUrl.length() >= HttpConstant.HTTPS.length()) {
			httpMethod = apiUrl.substring(0, HttpConstant.HTTPS.length());
		}
		return httpMethod;
	}
	
	/**
	 * 发送 POST 请求（HTTP/HTTPS），K-V形式
	 *
	 * @param apiUrl API接口URL
	 * @param params 参数map
	 * @return map
	 */
	public static Map<String, Object> doPostByMapParas(String apiUrl, Map<String, String> params) {
		Map<String, Object> result = new HashMap<>(4);
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		try {
			httpPost = new HttpPost(apiUrl);
			httpPost.setConfig(requestConfig);
			List<NameValuePair> pairList = new ArrayList<>(params.size());
			for (Map.Entry<String, String> entry : params.entrySet()) {
				NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
				pairList.add(pair);
			}
			httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(HttpConstant.HTTP_UTF_8)));
			String httpMethod = getHttpProtocol(apiUrl);
			
			// 如果线程池创建失败, 则使用默认创建httpClient连接
			ifPollingClientNullCreateDefaultConnection();
			
			if (HttpConstant.HTTPS.equalsIgnoreCase(httpMethod)) {
				response = httpSSLClient.execute(httpPost);
			} else {
				response = httpClient.execute(httpPost);
			}
			
			int statusCode = response.getStatusLine().getStatusCode();
			int successStateCode = 200;
			if (statusCode == successStateCode) {
				String srtResult = EntityUtils.toString(response.getEntity(), HttpConstant.HTTP_UTF_8);
				result.put(HttpConstant.HTTP_ERROR_CODE, HttpConstant.HTTP_SUCCESS);
				result.put(HttpConstant.HTTP_RESULT, srtResult);
				return result;
			}
			result.put(HttpConstant.HTTP_ERROR_CODE, HttpConstant.RESULT_ERROR);
			result.put(HttpConstant.HTTP_RESULT, "Error Response: " + response.getStatusLine().toString());
		} catch (IOException e) {
			log.error("doPostSSLByMapParams error:", e);
			result.put(HttpConstant.HTTP_ERROR_CODE, HttpConstant.RESULT_ERROR);
			result.put(HttpConstant.HTTP_RESULT, "post failure :caused by-->" + e.getMessage());
			return result;
		} finally {
			try {
				closeHttpPostConnection(httpPost, response);
			} catch (IOException e) {
				log.error("httpPost failure :caused by-->", e);
			}
		}
		return result;
	}
	
	/**
	 * 发送 POST 请求（HTTP/HTTPS），JSON形式
	 *
	 * @param apiUrl api地址
	 * @param json   json对象
	 * @return map
	 */
	public static Map<String, Object> doPostByJsonParas(String apiUrl, Object json) {
		Map<String, Object> result = new HashMap<>(3);
		String httpStr = null;
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		try {
			httpPost = new HttpPost(apiUrl);
			httpPost.setConfig(requestConfig);
			// 解决中文乱码问题
			StringEntity stringEntity = new StringEntity(json.toString(), HttpConstant.HTTP_UTF_8);
			stringEntity.setContentEncoding(HttpConstant.HTTP_UTF_8);
			stringEntity.setContentType(HttpConstant.HTTP_APPLICATION_JSON);
			httpPost.setEntity(stringEntity);
			
			String httpMethod = getHttpProtocol(apiUrl);
			
			// 如果线程池创建失败, 则使用默认创建httpClient连接
			ifPollingClientNullCreateDefaultConnection();
			
			if (HttpConstant.HTTPS.equalsIgnoreCase(httpMethod)) {
				response = httpSSLClient.execute(httpPost);
			} else {
				response = httpClient.execute(httpPost);
			}
			
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == HttpConstant.HTTP_SUCCESS) {
				httpStr = EntityUtils.toString(entity, HttpConstant.HTTP_UTF_8);
				result.put(HttpConstant.HTTP_ERROR_CODE, HttpConstant.HTTP_SUCCESS);
				result.put(HttpConstant.HTTP_RESULT, httpStr);
				return result;
			} else {
				result.put(HttpConstant.HTTP_ERROR_CODE, 0);
				result.put(HttpConstant.HTTP_RESULT, "Error Response: " + response.getStatusLine().toString());
				return result;
			}
		} catch (IOException e) {
			log.error("doPostJson error:", e);
			result.put(HttpConstant.HTTP_ERROR_CODE, 0);
			result.put(HttpConstant.HTTP_RESULT, "post failure :caused by-->" + e.getMessage());
			return result;
		} finally {
			try {
				closeHttpPostConnection(httpPost, response);
			} catch (IOException e) {
				log.error("httpPost failure :caused by-->", e);
			}
		}
	}
	
	/**
	 * 发送 POST 请求（HTTP/HTTPS），file文件
	 *
	 * @param apiUrl API接口URL
	 * @param file 参数file
	 * @return map
	 */
	public static Map<String, Object> doPost4MultipartByParams(String apiUrl, File file) {
		Map<String, Object> result = new HashMap<>(4);
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		try {
			httpPost = new HttpPost(apiUrl);
			httpPost.addHeader(HttpConstant.HTTP_CONNECTION, HttpConstant.HTTP_KEEP_ALIVE);
			httpPost.addHeader(HttpConstant.HTTP_ACCEPT, HttpConstant.HTTP_ACCEPT_MATCH);
			
			String value = HttpConstant.HTTP_MULTIPART_FORM_DATA + String
					.format(";boundary=%s", getBoundaryStr(HttpConstant.HTTP_BOUNDARY));
			httpPost.addHeader(HttpConstant.HTTP_CONTENT_TYPE, value);
			httpPost.addHeader(HttpConstant.HTTP_USER_AGENT, HttpConstant.HTTP_USER_AGENT_VALUE);
			httpPost.setConfig(requestConfig);
			
			MultipartEntityBuilder meb = MultipartEntityBuilder.create();
			meb.setBoundary(getBoundaryStr(HttpConstant.HTTP_BOUNDARY))
					.setCharset(Charset.forName(HttpConstant.HTTP_UTF_8)).setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			meb.addBinaryBody(HttpConstant.HTTP_MEDIA, file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
			HttpEntity entity = meb.build();
			httpPost.setEntity(entity);
			
			String httpMethod = getHttpProtocol(apiUrl);
			
			// 如果线程池创建失败, 则使用默认创建httpClient连接
			ifPollingClientNullCreateDefaultConnection();
			
			if (HttpConstant.HTTPS.equalsIgnoreCase(httpMethod)) {
				response = httpSSLClient.execute(httpPost);
			} else {
				response = httpClient.execute(httpPost);
			}
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpConstant.HTTP_SUCCESS) {
				String srtResult = EntityUtils.toString(response.getEntity(), HttpConstant.HTTP_UTF_8);
				result.put(HttpConstant.HTTP_ERROR_CODE, HttpConstant.HTTP_SUCCESS);
				result.put(HttpConstant.HTTP_RESULT, srtResult);
				return result;
			}
			result.put(HttpConstant.HTTP_ERROR_CODE, 0);
			result.put(HttpConstant.HTTP_RESULT, "Error Response: " + response.getStatusLine().toString());
		} catch (IOException e) {
			log.error("doPostSSLByMapParams error:", e);
			result.put(HttpConstant.HTTP_ERROR_CODE, HttpConstant.RESULT_ERROR);
			result.put(HttpConstant.HTTP_RESULT, "post failure :caused by-->" + e.getMessage());
			return result;
		} finally {
			try {
				closeHttpPostConnection(httpPost, response);
			} catch (IOException e) {
				log.error("httpPost failure :caused by-->", e);
			}
		}
		return result;
	}
	
	/**
	 * 创建SSL安全连接
	 *
	 * @return SSLConnectionSocketFactory
	 */
	private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
		SSLConnectionSocketFactory sslSocketFactory = null;
		try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			
			sslSocketFactory = new SSLConnectionSocketFactory(sslContext);
		} catch (GeneralSecurityException e) {
			log.error("createSSLConnSocketFactory error:", e);
		}
		return sslSocketFactory;
	}
	
	/**
	 * 如果线程池创建失败, 则使用默认创建HttpClient连接
	 */
	private static void ifPollingClientNullCreateDefaultConnection() {
		if (httpClient == null) {
			httpClient = HttpClients.createDefault();
		}
		
		if (httpSSLClient == null) {
			httpSSLClient = HttpClients.createDefault();
		}
	}
	
	/**
	 * 关闭httpGet连接
	 *
	 * @param httpGet get
	 * @param response response
	 * @throws IOException 异常
	 */
	private static void closeHttpGetConnection(HttpGet httpGet, CloseableHttpResponse response)
			throws IOException {
		
		if (httpGet != null) {
			httpGet.releaseConnection();
		}
		
		if (response != null) {
			EntityUtils.consume(response.getEntity());
			response.close();
		}
	}
	
	/**
	 * 关闭httpPost连接
	 *
	 * @param httpPost post
	 * @param response  response
	 * @throws IOException 异常
	 */
	private static void closeHttpPostConnection(HttpPost httpPost, CloseableHttpResponse response)
			throws IOException {
		
		if (httpPost != null) {
			httpPost.releaseConnection();
		}
		
		if (response != null) {
			EntityUtils.consume(response.getEntity());
			response.close();
		}
	}
	
	private static String getBoundaryStr(String str) {
		return "------------" + str;
	}
	
}
