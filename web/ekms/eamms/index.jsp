<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.portlet.ui.PortalServer,
                 com.tms.portlet.theme.ThemeManager,
                 com.tms.workflow.JogetClientModule " %>

<%
    Application app = Application.getInstance();
    JogetClientModule client = (JogetClientModule) app.getModule(JogetClientModule.class);
    pageContext.setAttribute("auth", client.createJogetAuthentication(app.getCurrentUser().getId()));
    pageContext.setAttribute("hostPath", client.getWflowHost());
%>

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
<%@ include file="../includes/header.jsp" %>

<%@ include file="../includes/footer.jsp" %>



