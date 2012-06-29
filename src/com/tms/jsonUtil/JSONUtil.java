package com.tms.jsonUtil;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.metaparadigm.jsonrpc.JSONRPCBridge;


public class JSONUtil implements Serializable{
		
	public static JSONRPCBridge  getJsonBridge(HttpServletRequest req){
		HttpSession session = req.getSession();
		JSONRPCBridge json_bridge = null;
		json_bridge = (JSONRPCBridge) session.getAttribute("JSONRPCBridge");
		if(json_bridge == null) {
		    json_bridge = new JSONRPCBridge();
		    session.setAttribute("JSONRPCBridge", json_bridge);
		}
		return json_bridge;
	}
		
}
