package com.tm.servlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.AsyncContext;

/**
 * 异步会话上下文容器
 * @author fyz
 *
 */
public class ContextMap {
	public static Map<String,AsyncContext> contextMap=(Map<String, AsyncContext>) Collections.synchronizedMap(new HashMap<String,AsyncContext>());
	public static void main(String[] args)
	{
		contextMap.put("1",null);
	}
	public static AsyncContext getContext(String sessionId)
	{
		return contextMap.get(sessionId);
	}
	public static void setContext(String sessionId,AsyncContext asyncContext)
	{
		contextMap.put(sessionId, asyncContext);
	}
	public static void deleteContext(String sessionId)
	{
		contextMap.remove(sessionId);
	}
}
