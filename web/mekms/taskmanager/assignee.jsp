<%@include file="/common/header.jsp"%>
<x:config>
    <page name="taskformassignee">
        <com.tms.collab.taskmanager.ui.MAssigneeTable name="table"/>
    </page>
</x:config>
<c:if test="${!empty param.init}">
    <x:set name="taskformassignee.table" value="${param.init}" property="callingWidget"/>
</c:if>
<c:url var="redirect" value="/mekms/taskmanager/taskform.jsp"/>
<c:set var="origin" value="${widgets['taskformassignee.table'].callingWidget}"/>
<c:choose>
    <c:when test="${origin=='edittaskformpage.taskform'}">
        <c:url var="redirect" value="/mekms/calendar/edittodotaskform.jsp?reload=false"/>
    </c:when>
</c:choose>
<c:set var="headerCaption" scope="request"><fmt:message key="emeeting.label.assignees"/></c:set>
<jsp:include page="../includes/mheader.jsp"/>
<table width="100%" border="0" cellpadding="5" cellspacing="0" valign="top">
    <tr valign="top">
        <td align="left" valign="top">
            <table cellpadding="0" cellspacing="1" width="100%">
                <tr><td align="left" valign="top" class="data"><x:display name="taskformassignee"/></td></tr>
                <tr>
                    <td align="left" valign="top" class="data">
                        <input type="button" value="<fmt:message key="general.label.submit"/>" class="button" onClick="document.location='<c:out value="${redirect}"/>';">
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<jsp:include page="../includes/mfooter.jsp" />
