package me.duanbn.snailfish.util;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import me.duanbn.snailfish.util.collection.Lists;
import me.duanbn.snailfish.util.collection.Maps;
import me.duanbn.snailfish.util.lang.StringUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpClient {

	public static HttpResponse doRequest(HttpRequest request) {
		String body = request.getBody();
		Map<String, String> header = request.getHeader();
		HttpRequestMethod method = request.getMethod();
		String url = request.getUrl();

		Validation.assertNotEmpty(url, "url is empty");
		Validation.assertNotNull(method, "method is null");

		switch (method) {
			case PATCH:
				return doPatch(url, header, body);
			case PUT:
				return doPut(url, header, body);
			case POST:
				return doPost(url, header, body);
			case GET:
				return doGet(url, header);
			case DELETE:
				return doDelete(url, header);
			default:
				throw new IllegalArgumentException("Unknown Request Method: " + method);
		}
	}

	public static HttpResponse doPatch(String url) {
		return doPatch(url, null, null);
	}

	public static HttpResponse doPatch(String url, String body) {
		return doPatch(url, null, body);
	}

	public static HttpResponse doPatch(String url, Map<String, String> headers) {
		return doPatch(url, headers, null);
	}

	public static HttpResponse doPatch(String url, Map<String, String> headers, String body) {
		HttpPatch httpPatch = new HttpPatch(url);
		if (!CollectionUtils.isEmpty(headers)) {
			headers.forEach((k, v) -> {
				httpPatch.addHeader(k, v);
			});
		}
		return execute0(httpPatch, body);
	}

	public static HttpResponse doDelete(String url) {
		return doDelete(url, null);
	}

	public static HttpResponse doDelete(String url, Map<String, String> headers) {
		HttpDelete httpDelete = new HttpDelete(url);

		if (!CollectionUtils.isEmpty(headers)) {
			headers.forEach((k, v) -> {
				httpDelete.addHeader(k, v);
			});
		}

		return execute0(httpDelete);
	}

	public static HttpResponse doPut(String url) {
		return doPut(url, null, null);
	}

	public static HttpResponse doPut(String url, String body) {
		return doPut(url, null, body);
	}

	public static HttpResponse doPut(String url, Map<String, String> headers) {
		return doPut(url, headers, null);
	}

	public static HttpResponse doPut(String url, Map<String, String> headers, String body) {
		HttpPut httpPut = new HttpPut(url);
		if (!CollectionUtils.isEmpty(headers)) {
			headers.forEach((k, v) -> {
				httpPut.addHeader(k, v);
			});
		}
		return execute0(httpPut, body);
	}

	public static HttpResponse doPost(String url) {
		return doPost(url, Maps.newHashMap(), "");
	}

	public static HttpResponse doPost(String url, String body) {
		return doPost(url, null, body);
	}

	public static HttpResponse doPost(String url, Map<String, String> formData) {
		return doPost(url, null, formData);
	}

	public static HttpResponse doPost(String url, Map<String, String> headers, Map<String, String> formData) {
		HttpPost httpPost = new HttpPost(url);
		if (!CollectionUtils.isEmpty(headers)) {
			headers.forEach((k, v) -> {
				httpPost.addHeader(k, v);
			});
		}
		return execute0(httpPost, formData);
	}

	public static HttpResponse doPost(String url, Map<String, String> headers, String body) {
		HttpPost httpPost = new HttpPost(url);
		if (!CollectionUtils.isEmpty(headers)) {
			headers.forEach((k, v) -> {
				httpPost.addHeader(k, v);
			});
		}
		return execute0(httpPost, body);
	}

	public static HttpResponse doGet(String url) {
		return doGet(url, null);
	}

	public static HttpResponse doGet(String url, Map<String, String> headers) {
		log.debug("headers: {}, url: {}", headers, url);

		HttpGet httpGet = new HttpGet(url);

		if (!CollectionUtils.isEmpty(headers)) {
			headers.forEach((k, v) -> {
				httpGet.addHeader(k, v);
			});
		}

		return execute0(httpGet);
	}

	private static HttpResponse execute0(HttpRequestBase request) {
		CloseableHttpClient client = null;
		try {
			client = buildClient();
			CloseableHttpResponse httpResponse = client.execute(request);
			return new HttpResponse(httpResponse);
		} catch (Exception e) {
			throw new UtilException(e);
		} finally {
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					throw new UtilException(e);
				}
			}
		}
	}

	private static HttpResponse execute0(HttpEntityEnclosingRequestBase request, Map<String, String> formData) {
		CloseableHttpClient client = null;
		try {
			client = buildClient();

			if (formData != null) {
				List<NameValuePair> nameValuePairList = Lists.newArrayList();
				formData.forEach((k, v) -> {
					nameValuePairList.add(new BasicNameValuePair(k, v));
				});
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
				request.setEntity(urlEncodedFormEntity);
			}

			CloseableHttpResponse httpResponse = client.execute(request);
			return new HttpResponse(httpResponse);
		} catch (Exception e) {
			throw new UtilException(e);
		} finally {
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					throw new UtilException(e);
				}
			}
		}
	}

	private static HttpResponse execute0(HttpEntityEnclosingRequestBase request, String body) {
		CloseableHttpClient client = null;
		try {
			client = buildClient();

			if (StringUtil.isNotBlank(body)) {
				StringEntity stringEntity = new StringEntity(body, "UTF-8");
				stringEntity.setContentEncoding("UTF-8");
				stringEntity.setContentType("application/json");
				request.setEntity(stringEntity);
			}

			CloseableHttpResponse httpResponse = client.execute(request);
			return new HttpResponse(httpResponse);
		} catch (Exception e) {
			throw new UtilException(e);
		} finally {
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					throw new UtilException(e);
				}
			}
		}
	}

	private static CloseableHttpClient buildClient() throws Exception {
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new X509TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		} }, null);
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000)
				.setConnectionRequestTimeout(10000).build();

		return HttpClients.custom().setSSLContext(sslContext).setDefaultRequestConfig(defaultRequestConfig).build();
	}

	public static enum HttpRequestMethod {
		PATCH,
		PUT,
		POST,
		GET,
		DELETE;
	}

	@Data
	public static class HttpRequest {
		private String url;
		private HttpRequestMethod method;
		private Map<String, String> header;
		private String body;
	}

	@Data
	public static class HttpResponse {
		private int statusCode;
		private Map<String, String> header;
		private String content;

		public HttpResponse(CloseableHttpResponse resp) throws Exception {
			this.statusCode = resp.getStatusLine().getStatusCode();

			if (resp.getEntity() != null) {
				this.content = EntityUtils.toString(resp.getEntity(), "UTF-8");
			}

			this.header = Maps.newHashMap();
			Header[] headers = resp.getAllHeaders();
			if (headers != null) {
				for (Header header : headers) {
					this.header.put(header.getName(), header.getValue());
				}
			}
		}
	}

}
