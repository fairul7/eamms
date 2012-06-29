<%@ page import="java.util.Date,
                 kacang.Application,
				 kacang.ui.menu.MenuGenerator,
				 kacang.services.security.SecurityService,
                 com.tms.portlet.taglibs.PortalServerUtil,
				 java.net.InetAddress,
				 java.util.ArrayList,
				 java.util.Iterator,
				 kacang.ui.menu.MenuItem,
                 com.tms.collab.messenger.MessageModule"%>
<%@ include file="/common/header.jsp"%>


<%!
	public static final String URI_PREFIX = "/ekms/fms/";
	public String getMainFolder(String uri) {
		if (uri != null && uri.startsWith(URI_PREFIX)) {
			String part = uri.substring(URI_PREFIX.length());
			int posi = part.lastIndexOf("/");
			if (posi != -1) {
				part = part.substring(0, posi);
				return part;
			}
		}
		return "";
	}
%>

<x:config>
	<page name="header">
		<com.tms.collab.calendar.ui.WeeklyView name="calendar"/>
	</page>
</x:config>
<c:set var="showServerDetail"><%= Application.getInstance().getProperty("cluster.showServerDetail") %></c:set>

<html>
<head>
<title><fmt:message key="general.label.ekp"/></title>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="/ekms/images/fms2008/default.css" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">    
<script>
<!--
	var isIE = (navigator.appName == "Microsoft Internet Explorer") ? true : false;
	var portletList = [];
	var placeholderList = [];
	var currentlyDragging = false;
//-->
</script>
<!--[if lt IE 7.]>
<script defer type="text/javascript" src="images/pngfix.js"></script>
<![endif]-->
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}
//-->
</script>

    <script language="javascript" src="<c:url value="/ekms/images/fms2008/fms2008.js"/>"></script>
	<script language="javascript" src="<c:url value="/common/tree/tree.js"/>"></script>
	<script language="javascript" src="<c:url value="/common/WCH.js"/>"></script>
</head>


<body leftmargin="0" topmargin="0" rightmargin="0" marginwidth="0" marginheight="0" class="bg">
<iframe src="<%= request.getContextPath() %>/ekms/poll.jsp" width="0" height="0" frameborder="0" marginheight="0" marginwidth="0"></iframe>

<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr><td>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td height="135" class="header_bg">
	<!-- Header -->
	<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
  	<tr>
    <td width="40" height="104"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" alt="" width="40" height="1"></td>
    <td width="140"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/logo.png" width="135" height="62"></td>
    <td class="header_title">Facility Management System</td>
    <td align="right" class="header-login">Welcome, <c:out value="${sessionScope.currentUser.propertyMap.firstName}"/> <c:out value="${sessionScope.currentUser.propertyMap.lastName}"/>&nbsp;&nbsp;   <a href="/ekms/profile/profile.jsp"><fmt:message key="theme.fms2008.profile"/></a>&nbsp; | &nbsp;<a href="/ekms/logout.jsp"><fmt:message key="theme.fms2008.logout"/></a> 	</td>
    <td width="20"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" alt="" width="20" height="1"></td>
  	</tr>
	</table>
  	<!-- Header End -->
    <!-- Menu -->
    <table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
    <td width="20" height="31"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="20" height="1"></td>
    <td width="135" align="center" class="menubg-current menu-dashboard">
	    <table border="0" align="center" cellpadding="0" cellspacing="0">
	    <tr>
        <td align="center"><a href="/ekms/">Dashboard</a> &nbsp;&nbsp;|&nbsp;&nbsp;</td>
        <td width="18" align="center">
        <c:if test="${pageContext.request.requestURI == '/ekms/home.jsp'}">
        <c:set var="ekpDashboardCookieValue" scope="request" value="${cookie['ekpLockDashboard'].value}"/>
        <c:choose>
            <c:when test="${ekpDashboardCookieValue == false}">
                <a href="#" onClick="toggleDashboard(); return false;"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/icn_unlock.gif" width="18" height="16" border="0"></a>
            </c:when>
            <c:otherwise>
                <a href="#" onClick="toggleDashboard(); return false;"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/icn_lock.gif" width="18" height="16" border="0"></a>
            </c:otherwise>
        </c:choose>
        </c:if>
        </td>
        </tr>
      	</table>
    </td>
	<td>&nbsp;</td>
	
		
	<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
	<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	
	boolean transRequest = service.hasPermission(userId, "com.tms.fms.transport.transportRequest", null, null);
	boolean transAdmin = service.hasPermission(userId, "com.tms.fms.transport.admin", null, null);
	boolean transManager = service.hasPermission(userId, "com.tms.fms.transport.manager", null, null);    
	boolean facilityRequest = service.hasPermission(userId, "com.tms.fms.facility.facilityRequest", null, null);	
	boolean facilityAdmin = service.hasPermission(userId, "com.tms.fms.facility.admin", null, null);
	boolean facilityManager = service.hasPermission(userId, "com.tms.fms.facility.manager", null, null);	
	boolean userView = service.hasPermission(userId, SecurityService.PERMISSION_USER_VIEW, null, null);
	boolean groupView = service.hasPermission(userId, SecurityService.PERMISSION_GROUP_VIEW, null, null);	
	boolean designView = service.hasPermission(userId, "com.tms.cms.SiteDesign", null, null);
	boolean wfuser = service.hasPermission(userId, "com.tms.workflow.WorkflowUser", null, null);
	boolean wfadmin = service.hasPermission(userId, "com.tms.workflow.WorkflowAdministrator", null, null);
	
	String mainFolder = getMainFolder(request.getRequestURI());
%>
	<% if (mainFolder.equals("transport")) { %>
		<%if (facilityRequest){ %>
		<td width="160" align="center" class="menu-bg menu"><a href="/ekms/fms/engineering/"><fmt:message key="theme.fms2008.facilitiesRequest"/></a></td>
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if (transRequest){%>
		<td width="160" align="center" class="menu-bg menu"><a href="/ekms/fms/transport/request"><fmt:message key="theme.fms2008.transportRequest"/></a></td>	
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if(transAdmin || transManager){%>
		<td width="190" align="center" class="menubg-current menu-current"><a href="/ekms/fms/transport"><fmt:message key="theme.fms2008.transportManagement"/></a></td>
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if(facilityAdmin || facilityManager){%>
		<td width="190" align="center" class="menu-bg menu"><a href="/ekms/fms/facility"><fmt:message key="theme.fms2008.facilitiesManagement"/></a></td>		
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>    
		<%} %>
		
	<% } else if (mainFolder.equals("facility")) { %>     
    
		<%if (facilityRequest){ %>
		<td width="160" align="center" class="menu-bg menu"><a href="/ekms/fms/engineering/"><fmt:message key="theme.fms2008.facilitiesRequest"/></a></td>
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if (transRequest){%>
		<td width="160" align="center" class="menu-bg menu"><a href="/ekms/fms/transport/request"><fmt:message key="theme.fms2008.transportRequest"/></a></td>	
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if(transAdmin || transManager){%>
		<td width="190" align="center" class="menu-bg menu"><a href="/ekms/fms/transport"><fmt:message key="theme.fms2008.transportManagement"/></a></td>
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if(facilityAdmin || facilityManager){%>
		<td width="190" align="center" class="menubg-current menu-current"><a href="/ekms/fms/facility"><fmt:message key="theme.fms2008.facilitiesManagement"/></a></td>		
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>    
		<%} %>
		
	<% } else if (mainFolder.equals("transport/request")) { %> 
    
		<%if (facilityRequest){ %>
		<td width="160" align="center" class="menu-bg menu"><a href="/ekms/fms/engineering/"><fmt:message key="theme.fms2008.facilitiesRequest"/></a></td>
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if (transRequest){%>
		<td width="160" align="center" class="menubg-current menu-current"><a href="/ekms/fms/transport/request"><fmt:message key="theme.fms2008.transportRequest"/></a></td>	
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if(transAdmin || transManager){%>
		<td width="190" align="center" class="menu-bg menu"><a href="/ekms/fms/transport"><fmt:message key="theme.fms2008.transportManagement"/></a></td>
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if(facilityAdmin || facilityManager){%>
		<td width="190" align="center" class="menu-bg menu"><a href="/ekms/fms/facility"><fmt:message key="theme.fms2008.facilitiesManagement"/></a></td>		
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>    
		<%} %>
		
	<% } else if (mainFolder.equals("engineering")) { %>           
    
		<%if (facilityRequest){ %>
		<td width="160" align="center" class="menubg-current menu-current"><a href="/ekms/fms/engineering/"><fmt:message key="theme.fms2008.facilitiesRequest"/></a></td>
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if (transRequest){%>
		<td width="160" align="center" class="menu-bg menu"><a href="/ekms/fms/transport/request"><fmt:message key="theme.fms2008.transportRequest"/></a></td>	
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if(transAdmin || transManager){%>
		<td width="190" align="center" class="menu-bg menu"><a href="/ekms/fms/transport"><fmt:message key="theme.fms2008.transportManagement"/></a></td>
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if(facilityAdmin || facilityManager){%>
		<td width="190" align="center" class="menu-bg menu"><a href="/ekms/fms/facility"><fmt:message key="theme.fms2008.facilitiesManagement"/></a></td>		
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>    
		<%} %>             
	
	<% } else { %>
	
    	<%if (facilityRequest){ %>
		<td width="160" align="center" class="menu-bg menu"><a href="/ekms/fms/engineering/"><fmt:message key="theme.fms2008.facilitiesRequest"/></a></td>
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if (transRequest){%>
		<td width="160" align="center" class="menu-bg menu"><a href="/ekms/fms/transport/request"><fmt:message key="theme.fms2008.transportRequest"/></a></td>	
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if(transAdmin || transManager){%>
		<td width="190" align="center" class="menu-bg menu"><a href="/ekms/fms/transport"><fmt:message key="theme.fms2008.transportManagement"/></a></td>
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>
		
		<% if(facilityAdmin || facilityManager){%>
		<td width="190" align="center" class="menu-bg menu"><a href="/ekms/fms/facility"><fmt:message key="theme.fms2008.facilitiesManagement"/></a></td>		
		<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>    
		<%} %>
	
	<% } %>
		
		<% if(wfuser || wfadmin){%>
			<td width="150" align="center" class="menu-bg menu"><a href="/ekms/eamms/index.jsp">EAMMS</a></td>							
			<td width="2"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/spacer.gif" width="2" height="1"></td>
		<%} %>    
	
	<td width="75" align="center" class="menu-bg menu">
		<table border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
		<td><a href="#" onClick="toggleHeaderTopMenu(); return false;"><fmt:message key="theme.fms2008.more"/> </a></td>
		<td width="12" align="right"><a href="#" onClick="toggleHeaderTopMenu(); return false;"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/arrow_more_white.gif" width="8" height="4" border="0"></a></td>
		</tr>
		</table>
	</td>
	</tr>
	</table>
    <!-- Menu End -->
</td>
</tr>
</table>

<div id="ekmsTopMenu" style="position:absolute; width:235px; height:10px; z-index:100; top: 102px; visibility:hidden">
	<table border="0" cellspacing="0" cellpadding="2">
	<tr>
	<td>
		<table width="75" border="0" align="right" cellpadding="0" cellspacing="0">
    	<tr>
      	<td height="31" align="center" class="menu-bg menu-current">
        	<table border="0" align="center" cellpadding="0" cellspacing="0">
        	<tr>
          	<td><a href="#" onClick="toggleHeaderTopMenu(); return false;"><fmt:message key="theme.fms2008.more"/> </a></td>
          	<td width="12" align="right"><a href="#" onClick="toggleHeaderTopMenu(); return false;"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/arrow_more_black.gif" alt="" width="8" height="4" border="0"></a></td>
        	</tr>
      		</table> 
		</td>
    	</tr>
    	</table>    
  	</td>
	</tr>
	<tr>
	<td bgcolor="#aecc2e">
		<table border="0" cellspacing="0" cellpadding="3">
		<tr>
		<%-- 
      	<td width="24" align="center" bgcolor="#aecc2e"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/icn_mssg_new.gif" width="19" height="18"></td>
      	
      	<td width="140" bgcolor="#f0f6dd">&nbsp;<a href="/ekms/messaging" class="dropmenu_text"><fmt:message key="theme.fms2008.messaging"/></a>
      	 </td>
      	 --%>
    	</tr>
    	<tr>
      	<td width="24" align="center" bgcolor="#aecc2e"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/ic_scheduler.gif" width="19" height="18"></td>
      	<td width="140" bgcolor="#f0f6dd">&nbsp;<a href="/ekms/calendar" class="dropmenu_text"><fmt:message key="theme.fms2008.scheduler"/></a> </td>
    	</tr>
    	<tr>
      	<td width="24" align="center" bgcolor="#aecc2e"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/ic_forums.gif" width="19" height="18"></td>
      	<td width="140" bgcolor="#f0f6dd">&nbsp;<a href="/ekms/forums" class="dropmenu_text"><fmt:message key="theme.fms2008.forum"/></a> </td>
    	</tr>
    	<tr>
      	<td width="24" align="center" bgcolor="#aecc2e"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/icn_content.gif" width="19" height="18"></td>
      	<td width="140" bgcolor="#f0f6dd">&nbsp;<a href="/ekms/cmsadmin" class="dropmenu_text"><fmt:message key="theme.fms2008.manageContent"/></a> </td>
    	</tr>
    	
        
        <tr>
        <td height="2" align="center" bgcolor="#aecc2e"></td>
        <td height="2" align="left" bgcolor="#f0f6dd" class="dropmenu_text"><hr size="1" color="#aecc2e"></td>
        </tr>
        <tr>
        <td width="24" align="center" bgcolor="#aecc2e"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/icn_reporting.gif" width="19" height="18"></td>
        <td width="140" bgcolor="#f0f6dd">&nbsp;<a href="/ekms/fms/dutyroster" class="dropmenu_text"><fmt:message key="fms.label.engineering.dutyRoster"/></a> </td>
        </tr>
        <tr>
        <td width="24" align="center" bgcolor="#aecc2e"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/icn_form.gif" width="19" height="18"></td>
        <td width="140" bgcolor="#f0f6dd">&nbsp;<a href="/ekms/formwizard" class="dropmenu_text"><fmt:message key="theme.fms2008.formWizard"/></a> </td>
        </tr>
        <tr>
        <td width="24" align="center" bgcolor="#aecc2e"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/icn_resource.gif" width="19" height="18"></td>
        <td width="140" bgcolor="#f0f6dd">&nbsp;<a href="/ekms/resourcemanager" class="dropmenu_text"><fmt:message key="theme.fms2008.resourceManager"/></a> </td>
        </tr>
        <tr>
        <td height="2" align="center" bgcolor="#aecc2e"></td>
        <td height="2" bgcolor="#f0f6dd" class="dropmenu_text"><hr size="1" color="#aecc2e"></td>
        </tr>
        <tr>
        <%if(designView){ %>
        <td width="24" align="center" bgcolor="#aecc2e"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/icn_web.gif" width="19" height="18"></td>
        <td width="140" bgcolor="#f0f6dd">&nbsp;<a href="/ekms/siteadmin" class="dropmenu_text"><fmt:message key="theme.fms2008.webAdministration"/></a> </td>
        <%} %>
        </tr>
        <tr>
        <%if (userView || groupView) { %>
        <td width="24" align="center" bgcolor="#aecc2e"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/icn_system.gif" width="19" height="18"></td>
        <td width="140" bgcolor="#f0f6dd">&nbsp;<a href="/ekms/sysadmin" class="dropmenu_text"><fmt:message key="theme.fms2008.systemAdministration"/></a> </td>
        <%} %>
        </tr>
        
        <tr>
        	<td width="24" align="center" bgcolor="#aecc2e"><img src="<%= request.getContextPath() %>/ekms/images/fms2008/icn_reporting.gif" width="19" height="18"></td>
        	<td width="140" bgcolor="#f0f6dd">&nbsp;<a href="/ekms/fms/reports" class="dropmenu_text">Reports</a> </td>
        </tr>
  		</table>  
  	</td>
  	</tr>
  	</table>
</div>



<!-- Content -->
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>&nbsp;</td>
</tr>
<tr colspan="3">
<td>
	<!-- Scrolling notice/date -->
	<table width="100%" border="0" cellspacing="5" cellpadding="0">
		<tr>
			<td width="60" valign="top"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td>
			<td width="80" valign="top"><fmt:message key="theme.fms2008.usersOnline"/>: </td>
			<td width="20" valign="top"><span class="textsmallRed"><div id="HSTOnline">-</div></span></td>	
			<td width="20" valign="top"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td>
			<td valign="top">&nbsp;<!-- Notice: Scrolling Messenge Here. Check out the Release Notes for 1.5.1. --></td>
			<td width="250" align="right" valign="top">
				<%  pageContext.setAttribute("date", new Date()); %><fmt:formatDate value="${date}" pattern="EEEE, MMM dd, yyyy"/>
				&nbsp;<b><fmt:message key='com.tms.fms.version' /></b>
			</td>
			<td width="60" valign="top"><IMG SRC="<%= request.getContextPath() %>/ekms/images/ekp2005/clear.gif" height=1 width=1 border=0></td>				
		</tr>
		 
		<iframe src="<%= request.getContextPath() %>/ekms/status.jsp" width="0" height="0" frameborder="0" marginheight="0" marginwidth="0"></iframe>
		
	</table>
	
	<!-- Scrolling notice/date End -->
</td>
</tr>
<tr>
</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr valign="top">
<tr> 
    <td bgcolor="#cac9c3" height="1"><spacer type="block" height="1"></td>
  </tr>
  

<td width="1"><IMG SRC="<%= request.getContextPath() %>/ekms/images/fms2008/clear.gif" height=1 width=1 border=0></td>
<td>
	<table cellpadding="0" cellspacing="0" width="100%" border="0"><tr><td bgcolor="#cac9c3" height="1"><spacer type="block" height="1"></td></tr>
					<tr>
						<td colspan="2">
                            <table cellpadding="0" cellspacing="0" width="100%">
                                <tr><td><x:display name="header.calendar"/></td></tr>
                            </table>
							<table cellpadding=0 cellspacing=0 border=0 width=100%>
								<tr><td valign=middle align=center height=1><img src="<%= request.getContextPath() %>/ekms/images/fms2008/clear.gif" height="1" width="1"></td></tr>
								<tr><td valign=middle align=center height=5 background="<%= request.getContextPath() %>/ekms/images/fms2008/bg_calminbar.gif"><a href="" onClick="toggleCalendarMenu(); return false;"><img id="topCalendarButton" src="<%= request.getContextPath() %>/ekms/images/fms2008/but_miup.gif" height=5 width=26 border=0></a></td></tr>
							</table>
						</td>
					</tr>
                    <script>
                    <!--
                        function toggleCalendarMenu()
                        {
                            if(document.getElementById("ekmsCalendarWidget").style.display == "none" || document.getElementById("ekmsCalendarWidget").style.display == "" || document.getElementById("ekmsCalendarWidget").style.display == null)
                            {
                                document.getElementById("ekmsCalendarWidget").style.display="block";
                            }
                            else
                            {
                                document.getElementById("ekmsCalendarWidget").style.display="none";
                            }
                            updateCalendarImage();
                            treeSave("ekmsCalendarWidget");
                        }
                        function updateCalendarImage() {
                            if (document.getElementById("ekmsCalendarWidget").style.display == "none" || document.getElementById("ekmsCalendarWidget").style.display == "" || document.getElementById("ekmsCalendarWidget").style.display == null) {
                                document.getElementById("topCalendarButton").src = "<%= request.getContextPath() %>/ekms/images/fms2008/but_midwn.gif";
                            }
                            else {
                                document.getElementById("topCalendarButton").src = "<%= request.getContextPath() %>/ekms/images/fms2008/but_miup.gif";
                            }
                        }
                        updateCalendarImage();
                    //-->
                    </script>
					<tr>
						<x:display name="portal.menu" body="custom">
						<%
							ArrayList items = (ArrayList) request.getAttribute(MenuGenerator.MENU_FILE);
							if(items != null)
							{
                                items = new ArrayList(items);
							%>
								<td valign="top" align="left" width="170" class="dashboardModuleMenu">
									<table border=0 cellspacing=0 cellpadding=0 width=170>
									<%
										for (Iterator i = items.iterator(); i.hasNext();)
										{
											MenuItem item =  (MenuItem) i.next();
											if(item.isHeader())
											{
											%>
												<tr><td valign=middle align=left height=25 colspan=2 background="<%= request.getContextPath() %>/ekms/images/fms2008/menu_bg.gif">&nbsp;&nbsp;&nbsp;<span class="contentTitleFont"><%= item.getLabel() %></span></td></tr>
											<%
											}
											else
											{
												if(item.isLinked())
												{
												%>
													<tr class="dashboardModuleMenu">
														<td valign=middle width=15% align=right height=28><IMG src="<%= request.getContextPath() %>/ekms/images/fms2008//bullet_icn.gif" height=14 width=15 border=0></td>
														<td valign=middle width=85% align=left height=28>&nbsp;<a href="<%= item.getLink() %>" class="folderlink" <% if(!(item.getTarget()==null || "".equals(item.getTarget()))) {%>target="<%= item.getTarget() %>"<% } %>><%= item.getLabel() %></a></td>
													</tr>
												<%
												}
												if (item.getInclude() != null)
												{
												%>
                                                    <c:set var="includedContent"><jsp:include page="<%= item.getInclude() %>" /></c:set>
                                                    <c:if test="${!empty includedContent}">
													<tr class="dashboardModuleMenu">
														<td valign=middle align=left height=28 colspan="2"><c:out value="${includedContent}" escapeXml="false" /></td>
													</tr>
                                                    </c:if>
												<%
												}
												%>
                                                <tr class="dashboardModuleShadow"><td valign=middle width=100% colspan=2 align=right height=1><IMG src="<%= request.getContextPath() %>/ekms/images/fms2008/clear.gif" height=1 width=1 border=0></td></tr>
                                                <tr class="dashboardModuleLine"><td valign=middle width=100% colspan=2 align=right height=1><IMG src="<%= request.getContextPath() %>/ekms/images/fms2008/clear.gif" height=1 width=1 border=0></td></tr>
											<%
											}
										}
										%>

                                        </table>
									</td>
								<%
							}
						%>
						</x:display>
                
						<% ArrayList items = (ArrayList) request.getAttribute(MenuGenerator.MENU_FILE); %>
						<td align="center" valign="top" width="90%" <% if(items == null) { %>colspan="2"<% } %>>
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td valign="top">
										<table width="100%" border="0" cellpadding="5" cellspacing="0">
											<tr>
												<td valign="top">
	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
