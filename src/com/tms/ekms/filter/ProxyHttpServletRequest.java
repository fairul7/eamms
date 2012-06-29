package com.tms.ekms.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ProxyHttpServletRequest extends HttpServletRequestWrapper
{
    public ProxyHttpServletRequest(HttpServletRequest request)
    {
        super(request);
    }

    public String getRemoteAddr()
    {
        String ipAddress;
        if (getHeader("x-forwarded-for") == null)
            ipAddress = super.getRemoteAddr();
        else 
        {
            ipAddress = getHeader("x-forwarded-for");
            if (ipAddress.indexOf(",") > 0)
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }
        return ipAddress;
    }
}