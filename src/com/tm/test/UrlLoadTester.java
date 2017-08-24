package com.tm.test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.ccservice.Util.httpclient.CCSGetMethod;
import com.ccservice.Util.httpclient.CCSHttpClient;
import com.ccservice.Util.httpclient.CCSPostMethod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tm.servlet.MessageEntity;

public class UrlLoadTester {
	public static final AtomicInteger counter = new AtomicInteger(0);
	public static final int maxThreadCount = 300;
	public static final Map<Integer, Long> timeAccount = new HashMap<Integer, Long>();
	static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	public static void main(String[] args) throws InterruptedException {
//		Date startDate = new Date();
//		CCSHttpClient httpClient = new CCSHttpClient(false, 60000L, "utf-8");
//		CCSPostMethod post = new CCSPostMethod("http://192.168.0.168:9528");
//		post.setFollowRedirects(false);
//		post.addRequestHeader("If-Modified-Since", "0");
//		MessageEntity message = new MessageEntity();
//		message.setId("651b7d39d73f4a4f8935fcf462929bb5");
//		message.setEnType(1);
//		message.setValue(
//				"FJEOwzavqQHBOFaKkDtumUQRt2SGqQoFua/j4lH62isKXibYwfxv9/qt4Cv5tY4zjCV3ohX/H16VEu1RRBejJIXfFurYU7HPjERRjoGgU7gI1l2zIvZqEvs/fA2c8FtUhOQ2fw9F+R+OntUi9tEtcXDO5eKAS4Z298kd8g147Rrr0InGVTLjAlvxK0n3jFlSOk8Tn1WZxPnrUJsQohFzBS6MZXRV1Ij71dz6pFur5jTI7RdMNdVeb8b81nVdl7WiExJK+P39tgGNg0TK7LxAY3w7ikhANtLQmuyPUaNCphJM9fy4xt9QeXNqZ4DNyO8p9ErM+s3l8rCLZmJrYWgo2mDGFe9hbGa/x2eoxzTDI6SOFG3WeF2TNWoVraktmmXlpIDUOQsHo3OyjWSG3fdh8NHtFX6i8dkuWMWYg+xtJsTmY6oT0b+R7b3VYLCADr98fSARWLPetDymJ1q/xwpeZBYDTzhGo8mlCYFLD69xmUetBxEM1fQfwiXcqdfwH2HPW1mwDZapL8/qPu0OXtJnWS2/avYqHZhxxxtpWTbKUzhQTHgQQgx4iBzQJNT8FB3Kg8vxUdiLjte6fvpldU7mXFudG1tjFFLogDmLb3CflBOQdyzdkKe3B8yDunbziTnrn5Un53TuevdY10FRd9jOWYYAUaBnsq8SFYlbV2WM4/pcPmlZ9mrs+gw6DZQNmKZzHJDvOUr90HJx1UXRlQmfj14d29bNxB0wkRiVikhTHgRlQwJm0kqqTvPdO8O30VfD8B81hJSAxbwT9JjYzsfmVHQcY+muoDhL2cqDAZ/U2VQI=");
//		String messageContent = gson.toJson(message);
//		post.setRequestBody(messageContent);
//		try {
//			int statusCode = httpClient.executeMethod(post);
//			String s_ = post.getResponseBodyAsString();
//			Date endDate = new Date();
//			System.out.println(s_ + "，time:" + (endDate.getTime() - startDate.getTime()));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		// new UrlLoadTester();

	}

	public UrlLoadTester() throws InterruptedException {

		// call simple servlet

		ExecutorService exec1 = Executors.newCachedThreadPool();

		for (int i = 0; i < maxThreadCount; i++) {

			exec1.submit(new UrlReaderTask((i + 1),
					"http://v2.app.12306.lvvto.com/web-ticket/asyncServlet?enType=0&content=abc"));
			try {
				// Thread.sleep(10);
			} catch (Exception e) {

			}
		}
		exec1.shutdown();
		while (!exec1.isTerminated()) {

		}
		for (Map.Entry<Integer, Long> entry : timeAccount.entrySet()) {
			System.out.println("entry:" + entry.getKey() + ",value:" + entry.getValue());
		}
		Thread.currentThread().sleep(30000);

	}

	public class UrlReaderTask implements Runnable {

		private String endpoint;
		private int num = 0;

		public UrlReaderTask(int i, String s) {
			endpoint = s;
			num = i;
		}

		public void run() {

			try {
				actuallyrun(num);
			} catch (Exception e) {
				System.err.println(e.toString());
			}

		}

		public void actuallyrun(int num) throws Exception {
			Date startDate = new Date();
			CCSHttpClient httpClient = new CCSHttpClient(false, 60000L, "utf-8");
			CCSGetMethod get = new CCSGetMethod(endpoint);
			get.setFollowRedirects(false);
			get.addRequestHeader("If-Modified-Since", "0");
			try {
				int statusCode = httpClient.executeMethod(get);
				String s_ = get.getResponseBodyAsString();
				Date endDate = new Date();
				timeAccount.put(Integer.valueOf(num), Long.valueOf((endDate.getTime() - startDate.getTime())));
				System.out.println(num);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}// end class ComplexLoadTester
