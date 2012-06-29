/*
 * MediaAccessFilter
 * Date Created: Jul 12, 2005
 * Author: Tien Soon, Law
 * Description: TODO Change the class descriptions
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.model;

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
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.Log;
import kacang.util.UuidGenerator;


public class MediaAccessFilter implements Filter {
    private Log log = Log.getLog(getClass());
    private FilterConfig filterConfig;
    
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) 
    throws IOException, ServletException {
        
    	String path;
    	
        try{
        	HttpServletRequest req = (HttpServletRequest)request;
            HttpServletResponse resp = (HttpServletResponse)response;
            
            MediaModule mediaModule = (MediaModule)Application.getInstance().getModule(MediaModule.class);
    		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
    		User loginUser = service.getCurrentUser(req);
    		
            //check path
            path = req.getPathInfo();
            if (path.startsWith("/medialib/")) {
            	
                int dotPos = path.lastIndexOf(".");
                int spacePost = path.lastIndexOf(" ");
                
                String id = path.substring(spacePost + 1, dotPos);
                
                if(! "".equals(id.trim())){
    				if(mediaModule.isAccessible(id, req)){
    					
    		            // Update media statistics
    		            MediaStatObject mediaStat = new MediaStatObject();
    		            mediaStat.setId(UuidGenerator.getInstance().getUuid());
    		            mediaStat.setActionType("directAccess");
    		            mediaStat.setCreatedBy(loginUser.getId());
    		            mediaStat.setIp(request.getRemoteAddr());
    		            mediaStat.setMediaId(id);
    		            mediaModule.insertMediaStat(mediaStat);
    		            
    		            filterChain.doFilter(request, response);
    				}
    				else {
    				    resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    				    return;
    				}
    			}
            }
        }
        catch(Exception error){
            log.error("error accessing media file", error);
        }
    }
    
    public void destroy() {
    }
}