package me.duanbn.snailfish.test.util;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import me.duanbn.snailfish.util.HttpClient;
import me.duanbn.snailfish.util.HttpClient.HttpRequest;
import me.duanbn.snailfish.util.HttpClient.HttpRequestMethod;
import me.duanbn.snailfish.util.HttpClient.HttpResponse;

@Slf4j
public class HttpClientTest {

	@Test
	public void testDoRequest() throws Exception {
		String url = "https://www.aliyun.com";
		HttpRequest req = new HttpRequest();
		req.setUrl(url);
		req.setMethod(HttpRequestMethod.GET);
		HttpResponse response = HttpClient.doRequest(req);
		log.info("{}", response);
	}

	@Test
	public void testDoPost() throws Exception {
		String url = "https://www.aliyun.com";
		HttpResponse resp = HttpClient.doPost(url);
		log.info("{}", resp);
	}

	@Test
	public void testDoGet() throws Exception {
		String url = "https://www.aliyun.com";

		HttpResponse resp = HttpClient.doGet(url);
		log.info("{}", resp);
	}

}
