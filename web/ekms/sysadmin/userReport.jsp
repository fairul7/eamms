<%@ page import="com.tms.report.model.ReportModule"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="kacang.services.security.Group"%>
<%@ page import="kacang.model.DaoQuery"%>
<%@ include file="/common/header.jsp" %>

<%
    ReportModule mod = (ReportModule)Application.getInstance().getModule(ReportModule.class);
    SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
    Collection users = null;
    try {
        users = ss.getUsers(new DaoQuery(),0,-1,"username",false);
    }
    catch(Exception e) {

    }

%>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="com.tms.report"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

<table cellpadding="4" cellspacing="2" width="100%" class="tableBackground">
    <tr>
        <td>
            User - Groups Report
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" cellpadding="4" cellspacing="1">
                <tr>
                    <td class="tableHeader" windth="5%" rowspan="2">No</td>
                    <td class="tableHeader" width="60%" colspan="3">User</td>
                    <td class="tableHeader" width="35%" rowspan="2">Groups</td>
                </tr>
                <tr>
                    <td class="tableHeader">Username</td>
                    <td class="tableHeader">Name</td>
                    <td class="tableHeader">Active</td>
                </tr>
                <%
                    if (users!=null && users.size()>0) {
                        int counter=0;
                        for (Iterator i=users.iterator();i.hasNext();) {
                            counter++;
                            User user = (User)i.next();
                            String name =  (user.getProperty("firstName")==null?"":(String)user.getProperty("firstName")) +
                                    " "+ (user.getProperty("lastName")==null?"":(String)user.getProperty("lastName"));
                            String active = user.isActive()?"Active":"Inactive";
                            Collection col = mod.getGroupsByUser(user.getId());
                            if (user.getUsername()!=null && !user.getUsername().equals("anonymous")) {
                %>
                    <tr>
                        <td class="tableRow" valign="top"><%=""+counter%></td>
                        <td class="tableRow" valign="top"><%=user.getUsername()%></td>
                        <td class="tableRow" valign="top"><%=name%> </td>
                        <td class="tableRow" valign="top"><%=active%></td>
                        <td class="tableRow">
                            <%
                                for (Iterator gi=col.iterator();gi.hasNext();) {
                                    Group group = (Group)gi.next();
                            %>
                                <%=group.getName()%><br>
                            <%
                                }
                            %>
                        </td>
                    </tr>
                <%
                            }
                        }
                    }
                %>
            </table>
        </td>
    </tr>
</table>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>