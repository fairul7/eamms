<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>
<x:display name="${widget.panel.absoluteName}" body="custom">
<br>
<c:set var="colSpanValue" value="11"/>
<c:if test="${widget.viewMode eq 'false'}">
    <c:set var="colSpanValue" value="12"/>
</c:if>

<fmt:message key="eamms.feed.list.msg.assignDetail" var="assignemntDetails"/>
<table width="30%">
    <tr>
        <td><b>B. ${fn:toUpperCase(assignemntDetails)}</b></td>
    </tr>
</table>
<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="95%">
    <tr>
        <th><fmt:message key="fms.facility.label.sNo"/></th>
        <th><fmt:message key="eamms.feed.list.msg.assignId"/></th>
        <th><fmt:message key="eamms.feed.list.msg.tvroTitle"/></th>
        <th><fmt:message key="eamms.feed.list.msg.requiredDate"/></th>
        <th><fmt:message key="eamms.feed.list.msg.requiredTime"/></th>
        <th><fmt:message key="eamms.feed.list.msg.totalTimeReq"/></th>
        <th><fmt:message key="eamms.feed.list.msg.remarks"/></th>
        <c:if test="${widget.showsNetworkColumns}">
	        <th><b><fmt:message key="eamms.feed.list.msg.bookingStat"/></b></th>
	        <th><b><fmt:message key="eamms.feed.list.msg.networkRemarks"/></b></th>
	        <th><b><fmt:message key="eamms.feed.list.msg.attc"/></b></th>
	        <th><b><fmt:message key="eamms.feed.list.msg.status"/></b></th>
        </c:if>
        <c:if test="${widget.viewMode eq 'false'}">
            <th><x:display name="${widget.childMap.delete.absoluteName}"/></th>
        </c:if>
    </tr>
        <c:forEach items="${widget.assignments}" var="obj" varStatus="sNo">
        <tr>
            <td class="profileRow" align="right"><c:out value="${(sNo.index+1)}"/></td>
            <td class="profileRow" align="center"><c:out value="${obj.title}" escapeXml="false"/></td>
            <td class="profileRow" align="center"><c:out value="${obj.propertyMap.feedTitle}"/></td>
            <%--
            <c:if test="${obj.blockbooking}">
                <td class="profileRow" align="center"><c:out value="${obj.propertyMap.requiredDateRangeStr}"/></td>
            </c:if>
            <c:if test="${!obj.blockbooking}">
                <td class="profileRow" align="center"><c:out value="${obj.propertyMap.requiredDateStr}"/></td>
            </c:if>
            --%>
            <td class="profileRow" align="center"><c:out value="${obj.propertyMap.requiredDateRangeStr}"/></td>
            <td class="profileRow" align="center">
                <c:out value="${obj.propertyMap.hourFrStr}"/>:<c:out value="${obj.propertyMap.minFrStr}"/> - 
                <c:out value="${obj.propertyMap.hourToStr}"/>:<c:out value="${obj.propertyMap.minToStr}"/>
            </td>
            <td class="profileRow" align="center"><c:out value="${obj.propertyMap.totalReqTime_measure}"/></td>
            <td class="profileRow" align="center"><c:out value="${obj.remarks}"/></td>
            <c:if test="${widget.showsNetworkColumns}">
                <td class="profileRow" align="center"><c:out value="${obj.bookingStatus}"/></td>
                <td class="profileRow" align="center"><c:out value="${obj.networkRemarks}"/></td>
                <td class="profileRow" align="center"><c:out value="${obj.propertyMap.attchLink}" escapeXml="false"/></td>
                <td class="profileRow" align="center"><c:out value="${obj.status}"/></td>
            </c:if>
            <c:if test="${widget.viewMode eq 'false'}">
                <td class="profileRow" align="center">&nbsp;
                    <c:forEach items="${widget.checkBoxes}" var="check" varStatus="checkStatus">
                        <c:if test="${checkStatus.index eq sNo.index}">
                            <x:display name="${check.absoluteName}"/>
                        </c:if>
                    </c:forEach>
                </td>
            </c:if>
        </tr>
        </c:forEach>
    
    <tr><td class="profileFooter" colspan='<c:out value="${colSpanValue}"/>'><x:display name="${widget.childMap.add.absoluteName}"/></td></tr>
</table><br>
</x:display>
<jsp:include page="../../form_footer.jsp" flush="true"/>

