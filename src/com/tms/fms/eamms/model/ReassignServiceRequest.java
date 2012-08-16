package com.tms.fms.eamms.model;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kacang.Application;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class ReassignServiceRequest extends HttpServlet {
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		
		HttpClient httpclient = new DefaultHttpClient();
		EammsModule mod = (EammsModule) Application.getInstance().getModule(EammsModule.class);
		//HashMap engineer = mod.getEngineersAssignedMSR(request.getParameter("requestId"));
		
		//String username= (String) engineer.get("c_engineer1UserId");	
	    String replacedBy=request.getParameter("replacedBy");
	    
	    
		String activityId = mod.getActivityId(request.getParameter("requestId"));	
				
		//String urlReassign = request.getParameter("jogetsiteurl")+ "/jw/web/monitoring/running/activity/reassign?username="+replacedBy+"&state=&processDefId=&activityId="+activityId+"&processId=&replaceUser="+username+"&j_username=master&hash=E505CF727D214A68CB03DA25DA978500";
		String urlVariable = request.getParameter("jogetsiteurl")+ "/jw/web/json/monitoring/activity/variable/"+ activityId +"/assignedEngineersUsername?value="+replacedBy+"&activityId="+activityId+"&j_username=master&hash=E505CF727D214A68CB03DA25DA978500";
		String urlReevaluate = request.getParameter("jogetsiteurl")+ "/jw/web/json/monitoring/activity/reevaluate?activityId="+activityId+"&j_username=master&hash=E505CF727D214A68CB03DA25DA978500";

        HttpGet httpVariable = new HttpGet(urlVariable);
		HttpPost httpReevaluate = new HttpPost(urlReevaluate);
        
        

        ResponseHandler<String> responseHandlerActivity = new BasicResponseHandler();
        try {
            httpclient.execute(httpVariable, responseHandlerActivity);
            httpclient.execute(httpReevaluate, responseHandlerActivity);
			
		} catch (Exception e) {
            e.printStackTrace();
        } 
			  
	}
}
