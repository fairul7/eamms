<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>
<%@ page import="kacang.Application, kacang.services.security.SecurityService ,java.util.*, java.sql.*, com.tms.fms.transport.model.*, com.tms.fms.setup.model.*"%>


<%	
		String assgId = null;
		String userId = null;
		String info = "Unable delete this User";
		SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
		assgId = request.getParameter("id");
		userId = request.getParameter("userId");
			
		TransportModule TM = (TransportModule) Application.getInstance().getModule(TransportModule.class);
				
		if(assgId != null){
		try{			
			TM.deleteAssignedDriver(assgId, userId);
			System.out.println("This user is deleted:"+ss.getUser(userId).getName());
			info = Application.getInstance().getMessage("fms.tran.youHaveDeletedUserSuccessful") + ss.getUser(userId).getName();
		}catch(Exception e){System.out.println("Unable to Delete vehicle inside assignment:"+e);}
			
		}	
%>	 

    <script>
    	
    </script>


<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <title><fmt:message key='fms.tran.deletingRecord'/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body class="calendarRow">
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">

<tr>
  <td class="calendarRow"><font color="red">Information :<br></font>
   	<%=info %>
  </td>
</tr>

<tr>
  <td class="calendarRow" align="left">
    <input value=" OK " type="button" class="button" onClick="window.opener.location.reload(); window.close();"/>
  </td>
</tr>

</table>
</body>
</html>
