package com.tms.ekms.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.*;
import java.io.IOException;

public class ProxyFilter implements Filter
{
    FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        ProxyHttpServletRequest wrappedRequest = null;
        try
        {
            wrappedRequest = new ProxyHttpServletRequest((HttpServletRequest)servletRequest);
            servletRequest = wrappedRequest;
            filterChain.doFilter(servletRequest, servletResponse);
        }
        finally
        {
            if (wrappedRequest != null)
                servletRequest = wrappedRequest.getRequest();
        }
    }

    public void destroy()
    {
    }
}
