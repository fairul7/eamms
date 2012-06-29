<%@ page import="java.util.Set,
                 com.tms.collab.calendar.ui.ConflictForm,
                 java.util.Collection,
                 java.util.Iterator,
                 com.tms.collab.resourcemanager.model.ResourceBooking"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="view" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table  cellpadding="0" cellspacing="1" width="100%">
    <tr><td colspan="2" align="left" valign="top" class="title"><fmt:message key="calendar.label.Title"/> : </td></tr>
    <tr><td colspan="2" align="left" valign="top" class="data"><c:out value="${view.event.title}"/></td></tr>
    <tr><td colspan="2" align="left" valign="top" class="title"><fmt:message key="calendar.label.StartDate"/> : </td></tr>
    <tr><td colspan="2" align="left" valign="top" class="data"><fmt:formatDate value="${view.event.startDate}" pattern="${globalDatetimeShort}" /></td></tr>
    <tr><td colspan="2" align="left" valign="top" class="title"><fmt:message key="calendar.label.EndDate"/> : </td></tr>
    <tr><td colspan="2" align="left" valign="top" class="data"><fmt:formatDate value="${view.event.endDate}" pattern="${globalDatetimeShort}" /></td></tr>
    <tr><td colspan="2" align="left" valign="top" class="title"><fmt:message key="calendar.label.conflicts"/> : </td></tr>
    <c:set var="conflictAttendees" value="${view.conflictAttendees}"/>
     <%
         Set conflictAttendees = (Set)pageContext.getAttribute("conflictAttendees");
         String userId;
     %>
    <c:set var="attFound" value="0"/>
    <c:if test="${!widget.ownerExcluded}" >
        <c:set var="userId" value="${view.event.userId}"/>
        <%
            userId = (String)pageContext.getAttribute("userId");
            if(conflictAttendees.contains(userId))
                pageContext.setAttribute("hasConflict",Boolean.TRUE);
            else
                pageContext.setAttribute("hasConflict",Boolean.FALSE);
        %>
        <c:choose>
            <c:when test="${hasConflict}">
                <c:set var="attFound" value="1"/>
                <tr>
                    <td class="data" align="left" width="2" valign="top"><input type="checkbox" name="comAttendeesCB" value="<c:out value="${view.event.userId}"/>" checked></td>
                    <td class="data" align="left"><font color="FF0000"><c:out value="${view.event.userName}"/></font></td>
                </tr>
            </c:when>
            <c:otherwise>
                <input type="checkbox" name="comAttendeesCB" value="<c:out value="${view.event.userId}"/>" style="display:none; visibility:hidden" checked>
            </c:otherwise>
        </c:choose>
    </c:if>
    <c:forEach items="${view.event.attendees}" var="att" varStatus="status"  >
        <c:if test="${att.compulsory&&att.userId!=view.event.userId}" >
            <c:set var="userId" value="${att.userId}"/>
            <%
                userId = (String)pageContext.getAttribute("userId");
                if(conflictAttendees.contains(userId))
                    pageContext.setAttribute("hasConflict",Boolean.TRUE);
                else
                    pageContext.setAttribute("hasConflict",Boolean.FALSE);
            %>
            <c:choose>
                <c:when test="${hasConflict}">
                    <c:set var="attFound" value="1"/>
                    <tr>
                        <td class="calendarRow" align="left" width="10%" valign="top" valign="top"><input type="checkbox" name="comAttendeesCB" value="<c:out value="${att.userId}"/>" checked></td>
                        <td class="data" align="left" valign="top" valign="top"><font color="FF0000"><c:out value="${att.name}"/></font></td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <input type="checkbox" name="comAttendeesCB" value="<c:out value="${att.userId}"/>" style="display:none" checked>
                </c:otherwise>
            </c:choose>
        </c:if>
    </c:forEach>
    <c:forEach items="${view.event.attendees}" var="att" varStatus="status"  >
        <c:if test="${!att.compulsory}">
            <c:set var="userId" value="${att.userId}"/>
            <%
                userId = (String)pageContext.getAttribute("userId");
                if(conflictAttendees.contains(userId))
                    pageContext.setAttribute("hasConflict",Boolean.TRUE);
                else
                    pageContext.setAttribute("hasConflict",Boolean.FALSE);
            %>
            <c:choose>
                <c:when test="${hasConflict}">
                    <c:set var="attFound" value="1"/>
                    <tr>
                        <td class="data" align="left" width="10%" valign="top" valign="top"><input type="checkbox" name="opAttendeesCB" value="<c:out value="${att.userId}"/> " CHECKED></td>
                        <td class="data" align="left" valign="top" valign="top"><font color="FF0000"><c:out value="${att.name}"/></font></td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <input type="checkbox" name="opAttendeesCB" value="<c:out value="${att.userId}"/> " style="display:none" checked>
                </c:otherwise>
            </c:choose>
        </c:if>
    </c:forEach>
    <c:if test="${attFound == '0'}">
        <tr><td colspan="2" align="left" valign="top" class="data">-</td></tr>
    </c:if>
    <c:set var="resourceFound" value="0"/>
    <tr><td colspan="2" align="left" valign="top" class="title"><fmt:message key="calendar.label.Resources"/> : </td></tr>
    <c:forEach items="${view.event.resources}" var="resource" >
        <c:set var="resourceId" value="${resource.id}"/>
        <c:set var="resourceList" value="${view.resourceConflicts}"/>
        <%
            pageContext.setAttribute("hasConflict",Boolean.FALSE);
            String resourceId = (String)pageContext.getAttribute("resourceId");
            Collection conflicts = (Collection)pageContext.getAttribute("resourceList");
            for (Iterator iterator = conflicts.iterator(); iterator.hasNext();)
            {
                ResourceBooking resourceBooking = (ResourceBooking) iterator.next();
                if(resourceBooking.getResourceId().equals(resourceId)){
                    pageContext.setAttribute("hasConflict",Boolean.TRUE);
                    break;
                }else {
                    pageContext.setAttribute("hasConflict",Boolean.FALSE);
                }
            }
        %>
        <c:choose>
            <c:when test="${hasConflict}">
                <c:set var="resourceFound" value="1"/>
                <tr>
                    <td class="data" align="left"><input type="checkbox" name="resourcesCB" value="<c:out value="${resource.id}"/>" checked></td>
                    <td class="data" align="left"><font color="FF0000"><c:out value="${resource.name}"/></font></td>
                </tr>
            </c:when>
            <c:otherwise>
                <input type="checkbox" name="resourcesCB" value="<c:out value="${resource.id}"/>" style="display:none" checked>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <c:if test="${resourceFound == '0'}">
        <tr><td colspan="2" align="left" valign="top" class="data">-</td></tr>
    </c:if>
    <tr>
        <td class="data" colspan="2">
            <x:display name="${view.addButton.absoluteName}" /> <x:display name="${view.cancelButton.absoluteName}" ></x:display>
        </td>
    </tr>
    <jsp:include page="../form_footer.jsp" flush="true"/>
</table>
