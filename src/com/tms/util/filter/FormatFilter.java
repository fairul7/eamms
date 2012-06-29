package com.tms.util.filter;

import kacang.util.Log;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.tms.ekms.security.filter.EkmsLoginFilter;
import com.tms.util.FormatUtil;

/**
 * Created by IntelliJ IDEA.
 * User: jui
 * Date: Aug 25, 2004
 * Time: 10:21:31 AM
 * To change this template use Options | File Templates.
 */
public class FormatFilter implements Filter {

    private FilterConfig filterConfig;
    private Log log;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        log = Log.getLog(EkmsLoginFilter.class);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        FormatUtil util = FormatUtil.getInstance();
        String[] properties = util.getProperties();
        for (int i = 0;i<properties.length;i++){
            request.setAttribute(properties[i],util.getProperty(properties[i]));
        }
        filterChain.doFilter(request, servletResponse);
    }

    public void destroy() {
    }

}
