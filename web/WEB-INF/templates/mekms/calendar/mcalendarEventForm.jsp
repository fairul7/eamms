<%@ page import="kacang.services.security.SecurityService,
                 kacang.Application,
                 com.tms.collab.calendar.model.CalendarModule
                 ,
                 kacang.util.Log"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<table cellpadding="0" cellspacing="1" width="100%">
    <jsp:include page="../mform_header.jsp" flush="true"/>
    <c:if test="${!form.newResourceBooking}" >
        <tr><td align="left" valign="top" class="title"><font color="#FF0000">*</font><fmt:message key="calendar.label.Title"/> : </td></tr>
        <tr><td align="left" valign="top" class="data"><x:display name="${form.title.absoluteName}"/><x:display name="${form.validTitle.absoluteName}"/></td></tr>
    </c:if>
    <tr><td align="left" valign="top" class="title"><fmt:message key="calendar.label.StartDate"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.startDate.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="calendar.label.EndDate"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.endDate.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="calendar.label.Description"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.description.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="calendar.label.location"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.location.absoluteName}"/></td></tr>
    <tr><td align="left" valign="top" class="title"><fmt:message key="calendar.label.classification"/> : </td></tr>
    <tr><td align="left" valign="top" class="data"><x:display name="${form.radioPublic.absoluteName}"/><fmt:message key='calendar.label.public'/> <x:display name="${form.radioPrivate.absoluteName}"/><fmt:message key='calendar.label.private'/></td></tr>
    <tr>
        <td align="left" valign="top" class="data">
            <x:display name="${form.submitButton.absoluteName}"/>
            <x:display name="${form.cancelButton.absoluteName}"/>
            <x:display name="${form.compulsoryHidden.absoluteName}"/>
            <x:display name="${form.optionalHidden.absoluteName}"/>
            <x:display name="${form.resourcesHidden.absoluteName}"/>
        </td>
    </tr>
    <jsp:include page="../form_footer.jsp" flush="true"/>
</table>