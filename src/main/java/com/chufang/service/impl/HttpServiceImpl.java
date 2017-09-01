package com.chufang.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chufang.service.HttpService;


@Service
public class HttpServiceImpl implements HttpService {

	private static Logger logger = LoggerFactory
			.getLogger(HttpServiceImpl.class);
	private final static int timeOut = 5000;
	
	private static HttpClientBuilder httpClientbuilder = HttpClients.custom();
	private RequestConfig defaultRequestConfig = RequestConfig.custom()
			  .setSocketTimeout(timeOut)
			  .setConnectTimeout(timeOut)
			  .setConnectionRequestTimeout(timeOut)
			  .build();
	@PostConstruct
	public void initHttp() {
		System.setProperty("jsse.enableSNIExtension", "false");
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
					return true;
				}

			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			httpClientbuilder.setSSLSocketFactory(sslsf);
			httpClientbuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
		} catch (Exception e) {
			logger.error("fail to inis trust", e.toString());
		}
	}
	
	public JSONObject processRequest(String url, JSONObject data) {
//		CloseableHttpClient client = HttpClients.custom().build();
		CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		
		HttpPost post = new HttpPost(url);
		if (data != null) {
			StringEntity se = new StringEntity(data.toString(), "UTF-8");
			se.setContentType("application/json");
			post.setEntity(se);
		}
		CloseableHttpResponse response = null;
		try {
			response = client.execute(post);
			JSONObject result = JSONObject.fromObject((EntityUtils
					.toString(response.getEntity(),"utf-8")));
			return result;
		} catch (Exception e) {
			logger.error("error", e.toString());
			return null;
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {

				}
			}
		}
	}
	
	public String processRequestStr(String url, String data) {
		CloseableHttpClient client = HttpClients.custom().build();
		HttpPost post = new HttpPost(url);
		if (data != null && StringUtils.isNotBlank(data)) {
			StringEntity se = new StringEntity(data.toString(), "UTF-8");
			se.setContentType("application/x-www-form-urlencoded");
			post.setEntity(se);
		}
		CloseableHttpResponse response = null;
		try {
			response = client.execute(post);
			return EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			logger.error("error", e);
			return null;
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {

				}
			}
		}
	}
	
	public JSONObject processRequestStr(String url, Map<String, Object> params) {
		String result = processRequestStrV2(url, params);
		if (result != null) {
			return JSONObject.fromObject(result);
		} else {
			return null;
		}
	}

	public String processRequestStrV2(String url, Map<String, Object> params) {
		List<NameValuePair> pramas = new ArrayList<NameValuePair>();
		Iterator<String> keys = params.keySet().iterator();
		String key = null;
		while (keys.hasNext()) {
			key = keys.next();
			pramas.add(new BasicNameValuePair(key, params.get(key) == null ? "" : params.get(key).toString()));
		}
		CloseableHttpResponse response = null;

		CloseableHttpClient client = HttpClients.custom().build();
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(pramas, "utf-8"));
			CloseableHttpResponse resp = client.execute(post);
			return EntityUtils.toString(resp.getEntity());
		} catch (Exception e) {
			logger.error("fail to process", e);
			return null;
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {

				}
			}
		}

	}
	
	public JSONObject processRequestStr(String url, Map<String, Object> params, int timeOut) {
		List<NameValuePair> pramas = new ArrayList<NameValuePair>();
		Iterator<String> keys = params.keySet().iterator();
		String key = null;
		while (keys.hasNext()) {
			key = keys.next();
			pramas.add(new BasicNameValuePair(key, params.get(key) == null ? "" : params.get(key).toString()));
		}
		CloseableHttpResponse response = null;
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				  .setSocketTimeout(timeOut)
				  .setConnectTimeout(timeOut)
				  .setConnectionRequestTimeout(timeOut)
				  .build();
		CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(pramas, "utf-8"));
			CloseableHttpResponse resp = client.execute(post);
			String result = EntityUtils.toString(resp.getEntity());
			return JSONObject
					.fromObject(result);
		} catch (Exception e) {
			logger.error("fail to process", e);
			return null;
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {

				}
			}
		}

	}

   //将map型转为请求参数型
   public String urlencode(Map<String, ?> data) {
       StringBuilder sb = new StringBuilder();
       for (Map.Entry<String, ?> i : data.entrySet()) {
           try {
               sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
           } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
           }
       }
       return sb.toString();
   }

	@Override
	public JSONObject processGetRequestStr(String url, Map<String, Object> params) {
		org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
		GetMethod method = new GetMethod(url + "?" + urlencode(params));
		try {
			client.executeMethod(method);
			String result = inputStream2String(method.getResponseBodyAsStream());
			//result=URLDecoder.decode(result,"unicode");
			//System.err.println("原始:"+result);
			JSONObject resJson = JSONObject.fromObject(result);
			return resJson;
		} catch (Exception e) {
			logger.error("error", e);
			return null;
		}
	}
	
	private String inputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}
	
	@Override
	public String processGetRequestStr(String url) {
		CloseableHttpClient client = HttpClients.custom().build();
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(get);
			return EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			logger.error("error", e);
			return null;
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {

				}
			}
		}
	}

	@Override
	public String processGetRequestStr(String url, Map<String, String> header, Map<String, String> data) {
		CloseableHttpClient client = HttpClients.custom().build();
		CloseableHttpResponse response = null;
		try {
			HttpGet get = new HttpGet(buildGetUrl(url,data));
			for (Map.Entry<String, String> e : header.entrySet()) {
				get.addHeader(e.getKey(), e.getValue());
	        }
			
			response = client.execute(get);
			return EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			logger.error("error", e);
			return null;
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {

				}
			}
		}
	}
	
	private String buildGetUrl(String url, Map<String, String> querys) throws UnsupportedEncodingException {
		StringBuilder sbUrl = new StringBuilder();
    	sbUrl.append(url);
    	if (null != querys) {
    		StringBuilder sbQuery = new StringBuilder();
        	for (Map.Entry<String, String> query : querys.entrySet()) {
        		if (0 < sbQuery.length()) {
        			sbQuery.append("&");
        		}
        		if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
        			sbQuery.append(query.getValue());
                }
        		if (!StringUtils.isBlank(query.getKey())) {
        			sbQuery.append(query.getKey());
        			if (!StringUtils.isBlank(query.getValue())) {
        				sbQuery.append("=");
        				sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
        			}        			
                }
        	}
        	if (0 < sbQuery.length()) {
        		sbUrl.append("?").append(sbQuery);
        	}
        }
    	return sbUrl.toString();
	}
}
