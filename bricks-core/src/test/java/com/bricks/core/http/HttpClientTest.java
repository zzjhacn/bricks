package com.bricks.core.http;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.junit.Test;

import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public class HttpClientTest implements LogAble {

	@Test
	public void test() throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		HttpHost host = new HttpHost("localhost", 8001);
		HttpPost req = new HttpPost("/com.bricks.kvs.KVStore");
		req.addHeader("method", "get");
		req.addHeader("scope", "get");
		HttpContext ctx = new HttpCoreContext();
		ctx.setAttribute("scope", "aaaa");
		ctx.setAttribute("method", "get");
		HttpResponse resp = client.execute(host, req, ctx);
		log().info("resp : [{}]", resp.getStatusLine().getStatusCode());
	}
}
