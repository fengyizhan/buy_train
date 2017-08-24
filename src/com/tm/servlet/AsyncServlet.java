package com.tm.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import com.google.gson.Gson;

@javax.servlet.annotation.WebServlet(
		// servlet name
		name = "asyncServlet",
		// servlet url pattern
		value = { "/asyncServlet" },
		// async support needed
		asyncSupported = true)
public class AsyncServlet extends HttpServlet {

	static Gson gson=new Gson();
	public void write(AsyncContext ctx, String responseText) {
		try {
			ctx.getResponse().getWriter().write(responseText);
		} catch (Exception e) {
			log("AsyncServlet应答异常", e);
		}
	}

	/**
	 * Simply spawn a new thread (from the app server's pool) for every new
	 * async request. Will consume a lot more threads for many concurrent
	 * requests.
	 */
	public void service(ServletRequest req, final ServletResponse res) throws ServletException, IOException {

		// create the async context, otherwise getAsyncContext() will be null
		final String messageID=UUID.randomUUID().toString()+"_"+new Date().getTime();
		System.out.println("messageID:"+messageID);
		final AsyncContext ctx = req.startAsync();
		ContextMap.setContext(messageID, ctx);
		// set the timeout
		ctx.setTimeout(60000);
		final String action=req.getParameter("enType");
		final String encodestr=req.getParameter("content");
		// attach listener to respond to lifecycle events of this AsyncContext
		ctx.addListener(new AsyncListener() {
			public void onComplete(AsyncEvent event) throws IOException {
				log(action+" AsyncServlet onComplete called");
			}

			public void onTimeout(AsyncEvent event) throws IOException {
				log(action+" AsyncServlet onTimeout called");
				ContextMap.deleteContext(messageID);
			}

			public void onError(AsyncEvent event) throws IOException {
				log(action+" AsyncServlet onError called");
			}

			public void onStartAsync(AsyncEvent event) throws IOException {
				log(action+" AsyncServlet onStartAsync called");
			}
		});
		if(action!=null && !"".equals(action.trim()))
		{
			if(!("0".equals(action) || "1".equals(action) ))
			{//0：加密；1：解密
				write(ctx,"非法的访问");
				ctx.complete();
			}
			// spawn some task in a background thread
			ctx.start(new Runnable() {
				public void run() {
					MessageEntity sendMessage=new MessageEntity();
					sendMessage.setId(messageID);
					sendMessage.setValue(encodestr);
					sendMessage.setEnType(Integer.parseInt(action));
					String sendStr=gson.toJson(sendMessage);
					System.out.println("action:"+action+"，messageId:"+messageID+"，content:"+encodestr);
					try
					{
						RabbitMQProcess.SendMessage(sendStr);
					} catch (Exception e) {
						e.printStackTrace();
						log("Problem processing task", e);
					}
				}
			});
		}
		

	}

}
