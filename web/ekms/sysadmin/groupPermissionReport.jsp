<%@ page import="com.tms.report.model.ReportModule"%>
<%@ page import="kacang.Application"%>
<%@ page import="java.util.Collection"%>
<%@ page import="kacang.services.security.SecurityService"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="kacang.services.security.Group"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="kacang.model.Module"%>
<%@ page import="kacang.model.DaoQuery"%>
<%@ include file="/common/header.jsp" %>

<%
    ReportModule mod = (ReportModule) Application.getInstance().getModule(ReportModule.class);
    SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
    Collection groups = null;
    try {
        groups = ss.getGroups(new DaoQuery(),0,-1,"groupName",false);
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
            Groups - Permission Report
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" cellpadding="4" cellspacing="2">
                <tr>
                    <td class="tableHeader" width="5%">No</td>
                    <td class="tableHeader" width="40%">Group</td>
                    <td class="tableHeader" width="5%">Active</td>
                    <td class="tableHeader" width="50%">Permission(s)</td>
                </tr>
                <%
                    if(groups!=null && groups.size()>0) {
                        int counter=0;
                        for (Iterator i=groups.iterator();i.hasNext();) {
                            counter++;
                            Group group = (Group) i.next();
                            String active = group.isActive()?"Active":"Inactive";
                            Collection modules = mod.getModulesByGroup(group.getId());

                %>
                <tr>
                    <td class="tableRow" valign="top"><%=""+counter%></td>
                    <td class="tableRow" valign="top"><%=group.getName()%></td>
                    <td class="tableRow" valign="top"><%=active%></td>
                    <td class="tableRow">
                        <%
                            for (Iterator modIterator=modules.iterator();modIterator.hasNext();) {
                                HashMap map = (HashMap)modIterator.next();
                                String sModuleId = (String)map.get("moduleId");
                                String sModuleName = Application.getInstance().getMessage(sModuleId);
                        %>
                                <b><%=sModuleName%></b><br>
                        <%
                                Collection permissionCol = mod.getPermissionByGroupAndModule(group.getId(),sModuleId);
                                if (permissionCol!=null && permissionCol.size()>0) {
                                    for (Iterator permissionIterator=permissionCol.iterator();permissionIterator.hasNext();) {
                                        HashMap permission = (HashMap)permissionIterator.next();
                                        String sPermissionId = (String)permission.get("permissionId");
                                        String sPermissionName = Application.getInstance().getMessage(sPermissionId);

                        %>
                                        - <%=sPermissionName%> <br>
                        <%
                                    }
                                }
                            }
                        %>
                    </td>
                </tr>
                <%

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