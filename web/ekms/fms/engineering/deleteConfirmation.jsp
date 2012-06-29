<%@ include file="/common/header.jsp"%>
<%@page import="kacang.Application, 
				com.tms.fms.engineering.model.EngineeringModule"
%>
<%
	Application app = Application.getInstance();
	EngineeringModule module = (EngineeringModule) app.getModule(EngineeringModule.class);
	
	String assignmentId = request.getParameter("assignmentId");
	String serviceType = request.getParameter("serviceType");
	String status = request.getParameter("status");
	
	module.cancelAssignment(assignmentId, serviceType, status);
%>