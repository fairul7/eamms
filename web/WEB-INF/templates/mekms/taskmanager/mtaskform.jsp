<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../mform_header.jsp" flush="true"/>
<table cellpadding="0" cellspacing="1" width="100%">
    <tr><td align="left" valign="top" class="title"><font color="#FF0000">*</font><fmt:message key="taskmanager.label.Title"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.title.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="taskmanager.label.StartDate"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.startDate.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="taskmanager.label.DueDate"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.dueDate.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="taskmanager.label.Category"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.categories.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><font color="#FF0000">*</font><fmt:message key="taskmanager.label.Description"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.description.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="taskmanager.label.Assignees"/> : </td></tr>
    <tr>
        <td align="left" valign="top" class="data">
            <c:choose>
                <c:when test="${empty form.assigneeMap}">
                    -<br>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${form.assigneeMap}" var="key">
                        <c:out value="${key.value.name}"/><br>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.assigneeButton.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="taskmanager.label.Allowassigneestoreassignthetask"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.reassignNo.absoluteName}" /> <x:display name="${form.reassignYes.absoluteName}" /></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="taskmanager.label.Classification"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.radioPublic.absoluteName}"/><fmt:message key='taskmanager.label.Public'/><x:display name="${form.radioPrivate.absoluteName}"/><fmt:message key='taskmanager.label.Private'/></td></tr>
    <tr>
        <td align="left" valign="top" class="data">
            <x:display name="${form.submitButton.absoluteName}"/>
            <x:display name="${form.cancelButton.absoluteName}"/>
            <x:display name="${form.resourcesHidden.absoluteName}"/>
            <x:display name="${form.userHidden.absoluteName}"/>
            <div style="display:none">
                <x:display name="${form.reminderTime.absoluteName}"/>
            </div>
        </td>
    </tr>
</table>
<jsp:include page="../mform_footer.jsp" flush="true"/>
