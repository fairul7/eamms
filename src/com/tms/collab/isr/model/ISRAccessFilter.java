package com.tms.collab.isr.model;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kacang.Application;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import com.tms.collab.isr.permission.model.PermissionModel;

public class ISRAccessFilter implements Filter {
	public static final String ISR_PREFIX = "/ekms/isr";
	private static final String[] ISR_REQUEST_PAGES= new String[] {"attendantProcessRequest.jsp", "attendantViewRequest.jsp", 
		"requestorEditRequest.jsp", "requestorResolveRequest.jsp", "requestorViewRequest.jsp"};
	
	private FilterConfig filterConfig;
	
	public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		if(isISRPage(request)) {
			Application application = Application.getInstance();
			String userId = application.getCurrentUser().getId();
			PermissionModel permissionModel = (PermissionModel) application.getModule(PermissionModel.class);
			RequestModel requestModel = (RequestModel) application.getModule(RequestModel.class);
			SecurityService security = (SecurityService) application.getService(SecurityService.class);
			boolean isUserInActiveGroup = permissionModel.isUserInActiveGroup(userId);
			boolean systemLevelPermissionAccessibility = false;
			try {
				systemLevelPermissionAccessibility = security.hasPermission(userId, "isr.permission.managePermission", null, null);
			}
			catch(SecurityException error) {
			}
			
			if(isUserInActiveGroup || systemLevelPermissionAccessibility) {
				if(isISRRequestPage(request)) {
					if(request.getParameter("requestId") != null) {
						if(!"".equals(request.getParameter("requestId"))) {
							if(! requestModel.isAccessibleRequest(request.getParameter("requestId"), request)) {
								response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
							}
						}
					}
				}
			}
			else {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	private boolean isISRPage(HttpServletRequest request) {
        if (request.getServletPath().startsWith(ISR_PREFIX)) {
        	if(!request.getServletPath().endsWith("noOrgChartSetup.jsp"))
        		return true;
        	else
        		return false;
        }
        else {
            return false;
        }
    }
	
	private boolean isISRRequestPage(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		
		for(int i=0; i<ISR_REQUEST_PAGES.length; i++) {
			if(servletPath.indexOf(ISR_REQUEST_PAGES[i]) != -1) {
				return true;
			}
		}
		
		return false;
	}
	
	public void destroy() {
    }
}
