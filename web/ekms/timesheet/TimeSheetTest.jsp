<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.timesheet.ui.TimeSheetVPForm,
                 com.tms.collab.timesheet.ui.TimeSheetViewForm,
                 com.tms.collab.timesheet.model.TimeSheetModule,
                 com.tms.collab.timesheet.TimeSheetUtil,
                 java.util.Collection,
                 com.tms.collab.project.WormsHandler,
                 java.util.Iterator,
                 com.tms.collab.project.Project"%>
 <%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: May 5, 2005
  Time: 11:02:07 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>


<script>
function changeTask(sb) {
    var pid="";
    for (var i=0; i<sb.length; i++) {
        if (sb.options[i].selected) {
            pid=sb.options[i].value;
        }
    }
    alert('projectid='+pid);
    window.frames['iframeworkingarea'].location.href = 'TimeSheetTestT.jsp?projectid='+pid;

}
</script>


<%
    Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
    Collection col=null;
    WormsHandler handler = (WormsHandler)Application.getInstance().getModule(WormsHandler.class);

    try {
                boolean hasPermission = service.hasPermission(userId, TimeSheetUtil.TIMESHEET_PERMISSION, TimeSheetModule.class.getName(), null);
                if (hasPermission) {
                    col = handler.getProjects();
                }
                else {
                    col = handler.getProjectsByOwner(userId);
                }

    }
    catch(Exception e) {
    }

%>

<%@include file="/ekms/includes/header.jsp" %>

<% if (col!=null && col.size()>0) { %>
<table width="100%" cellpadding="2" cellspacing="1">
<tr>
    <td>
    <form name="f1" method="post" action="">
    Select Project:
    <select name="sbproject" onchange="javascript:changeTask(this)">
    <% for (Iterator i=col.iterator();i.hasNext();) {
        Project p = (Project)i.next();
    %>
       <option value="<%=p.getProjectId()%>"><%=p.getProjectName()%></option>
    <% } %>
    </select>
    </form>
    </td>
</tr>
<tr>
    <td>
<iframe id="iframeworkingarea" name='iframeworkingarea' src="TimeSheetTestT.jsp" noresize scrolling="no" frameborder="0" name="hidframeworkingarea"></iframe>
    </td>
</tr>
</table>
<% } else { %>
<table width="100%" cellpadding="2" cellspacing="1">
<tr>
    <td>
        No task found!
    </td>
</tr>
</table>
<% } %>
<%@include file="/ekms/includes/footer.jsp" %>


