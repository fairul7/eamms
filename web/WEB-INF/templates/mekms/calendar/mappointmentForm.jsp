<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../mform_header.jsp" flush="true"/>
<table cellpadding="0" cellspacing="1" width="100%">
    <c:if test="${!form.newResourceBooking}">
        <tr><td align="left" valign="top" class="title"><font color="#FF0000">*</font><fmt:message key="calendar.label.Title"/> : </td></tr>
        <tr><td align="left" valign="top" class="data"><x:display name="${form.title.absoluteName}"/><x:display name="${form.validTitle.absoluteName}"/></td></tr>
    </c:if>
    <tr><td align="left" valign="top" class="title"><fmt:message key="calendar.label.StartDate"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.startDate.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="calendar.label.StartTime"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.startTime.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="calendar.label.EndDate"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.endDate.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="calendar.label.EndTime"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.endTime.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="calendar.label.CompulsoryAttendees"/> : </td></tr>
    <tr>
        <td align="left" valign="top" class="data">
            <c:choose>
                <c:when test="${empty form.compulsoryMap}">
                    -<br>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${form.compulsoryMap}" var="key">
                        <c:out value="${key.value.name}"/><br>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="calendar.label.optionalAttendees"/> : </td></tr>
    <tr>
        <td align="left" valign="top" class="data">
            <c:choose>
                <c:when test="${empty form.attendeeMap}">
                    -<br>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${form.attendeeMap}" var="key">
                        <c:out value="${key.value.name}"/><br>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.attendeeButton.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="calendar.label.Description"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.description.absoluteName}"/></td></tr>
    <tr>
        <td align="left" valign="top" class="data">
            <x:display name="${form.submitButton.absoluteName}"/>
            <x:display name="${form.cancelButton.absoluteName}"/>
            <x:display name="${form.compulsoryHidden.absoluteName}"/>
            <x:display name="${form.optionalHidden.absoluteName}"/>
            <x:display name="${form.resourcesHidden.absoluteName}"/>
        </td>
    </tr>
</table>
<jsp:include page="../mform_footer.jsp" flush="true"/>



