<%@ include file="/common/header.jsp" %>
<%@ page import="kacang.Application,
                 com.tms.portlet.ui.PortalServer,
                 com.tms.portlet.theme.ThemeManager,
                 com.tms.workflow.JogetClientModule " %>

<%
    Application app = Application.getInstance();
    JogetClientModule client = (JogetClientModule) app.getModule(JogetClientModule.class);
    pageContext.setAttribute("auth", client.createJogetAuthentication(app.getCurrentUser().getId()));
    pageContext.setAttribute("hostPath", client.getWflowHost());
%>

<x:permission permission="com.tms.workflow.WorkflowUser" module="com.tms.workflow.JogetClientModule" var="hvJogetUser"/>
<x:permission permission="com.tms.workflow.WorkflowAdministrator" module="com.tms.workflow.JogetClientModule" var="hvJogetAdmin"/>

<c:if test="${!hvJogetUser && !hvJogetAdmin}">
    <script>
        document.location="/ekms/cmsadmin/noPermission.jsp";
    </script>
</c:if>

<c:if test="${!empty param.addr}">
    <script>
	    window.open('toPg.jsp?addr=' + '<c:out value="${param.addr}"/>' ,'mywin','scrollbars=no,menubar=no,height=600,width=800,resizable=no,toolbar=no,location=no,status=no');
	    document.location="index.jsp";
    </script>
</c:if>

<script type="text/javascript" src="${hostPath}/jw/js/json/util.js"></script>
<script language="JavaScript" type="text/javascript" src="/ekms/includes/jquery-1.4.2.min.js"></script>
<script type="text/javascript">

    $(function() {
        AssignmentManager.login('${hostPath}/jw',
        '${auth.userName}',
        '${auth.secureHash}',
        {        	 
             'failure': function() { $("div#message").textContent("Unable to Log in to Joget Workflow!"); }
        });
    });    
    
</script>
<%-- Render --%>
<%@ include file="../../includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="24%" valign="top">
            <%-- Engineering New & Notification Spot --%>
            <table cellpadding="0" cellspacing="0">
                <tr><td><img src="<%= request.getContextPath() %>/ekms/images/fms2008/clear.gif" height="3" width="1"></td></tr>
            </table>
            <table width="100%" border="0" cellspacing="3" cellpadding="0">
                <tr align="center" valign="top">
                    <td width="100%">
                        <div id="entity_news" style="position:relative;width:100%">
                            <table cellpadding=0 cellspacing=0 border=0 width=100%>
                                <tr><td valign=top align=left class="portletHeaderLine"><IMG src="<%= request.getContextPath() %>/ekms/images/fms2008/clear.gif" height=1 width=1 border=0></td></tr>
                                <tr>
                                    <td valign=top align=left class="portletHeaderTitle">
                                        <table border=0 cellspacing=2 cellpadding=1 width=100%>
                                            <tr>
                                                <td valign=middle align=left width=25></td>
                                                <td valign=middle align=left><span class="portletHeader"><fmt:message key="theme.fms2008.engineeringNews"/></span></td>
                                                <td valign=middle align=right width=52>&nbsp;</td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr><td valign=top align=left><IMG src="<%= request.getContextPath() %>/ekms/images/fms2008/clear.gif" height=1 width=1 border=0></td></tr>
                            </table>
                            <table cellpadding=6 cellspacing=0 border=0 width=100%>
                                <tr>
                                    <td valign=top align=left class="portletBorder">
                                        <table cellpadding=0 cellspacing=2 border=0 width=100% class="portletBody">
                                            <tr>
                                                <td valign=top align=left class="portletBody">
                                                    <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_EammsNews" />
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
            </table>
            <%-- END: Engineering New & Notification Spot --%>
     </td>
     <td width="1%">&nbsp;</td>
        <td width="24%" valign="top">
            <%-- Knowledge Base Spot --%>
            <table cellpadding="0" cellspacing="0">
                <tr><td><img src="<%= request.getContextPath() %>/ekms/images/fms2008/clear.gif" height="3" width="1"></td></tr>
            </table>
            <table width="100%" border="0" cellspacing="3" cellpadding="0">
                <tr align="center" valign="top">
                    <td width="100%">
                        <div id="entity_knowledge" style="position:relative;width:100%">
                            <table cellpadding=0 cellspacing=0 border=0 width=100%>
                                <tr><td valign=top align=left class="portletHeaderLine"><IMG src="<%= request.getContextPath() %>/ekms/images/fms2008/clear.gif" height=1 width=1 border=0></td></tr>
                                <tr>
                                    <td valign=top align=left class="portletHeaderTitle">
                                        <table border=0 cellspacing=2 cellpadding=1 width=100%>
                                            <tr>
                                                <td valign=middle align=left width=25></td>
                                                <td valign=middle align=left><span class="portletHeader"><fmt:message key="theme.fms2008.knowledge"/></span></td>
                                                <td valign=middle align=right width=52>&nbsp;</td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr><td valign=top align=left><IMG src="<%= request.getContextPath() %>/ekms/images/fms2008/clear.gif" height=1 width=1 border=0></td></tr>
                            </table>
                            <table cellpadding=6 cellspacing=0 border=0 width=100%>
                                <tr>
                                    <td valign=top align=left class="portletBorder">
                                        <table cellpadding=0 cellspacing=2 border=0 width=100% class="portletBody">
                                            <tr>
                                                <td valign=top align=left class="portletBody">
                                                    <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.section.Section_KnowledgeBase" />
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
            </table>
            <%-- END: Knowledge Base Spot --%>
     </td>
     </tr>
     </table>
     

<%@ include file="../../includes/footer.jsp" %>



