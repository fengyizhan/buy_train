package com.tm.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
public class PermissionFilter implements Filter { 
    private FilterConfig filterConfig; 
 
    public void doFilter(ServletRequest request, ServletResponse response, 
            FilterChain chain) throws IOException, ServletException { 
        HttpServletRequest hsr=(HttpServletRequest)request;
        Object currentUser=hsr.getSession(true).getAttribute("currentUser");
        if(currentUser!=null)
        {
        	chain.doFilter(request, response);//放行。让其走到下个链或目标资源中 
        }else
        {
        	try {
				request.getRequestDispatcher("/login").forward(request,response);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    } 
 
    public void init(FilterConfig filterConfig) throws ServletException { 
        this.filterConfig = filterConfig; 
    } 
 
    public void destroy() { 
    } 
}
