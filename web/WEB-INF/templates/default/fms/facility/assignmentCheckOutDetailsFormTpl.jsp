<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
    <jsp:include page="../../form_header.jsp" flush="true"/>
	<c:if test="${not empty widget.asg}">
    <tr>
        <td width="25%" nowrap class="profileRow" align="right" height="25"><b><fmt:message key="fms.request.label.requestTitle"/></b>&nbsp;</td>
        <td width="75%" class="profileRow"><c:out value="${widget.asg.title}"/></td>
    </tr>
    <tr>
        <td nowrap class="profileRow" align="right" height="25"><b><fmt:message key="fms.facility.form.requestor"/></b>&nbsp;</td>
        <td class="profileRow"><c:out value="${widget.asg.createdByFullName}"/></td>
    </tr>
	<!-- <tr>
        <td nowrap class="profileRow" align="right" height="25"><b><fmt:message key="fms.label.groupAssignmentCode"/></b>&nbsp;</td>
        <td class="profileRow"><c:out value="${widget.asg.groupId}"/></td>
    </tr> -->
    <%--
    <tr>
        <td nowrap class="profileRow" align="right" height="25"><b><fmt:message key="fms.facility.label.requiredFrom"/></b>&nbsp;</td>
        <td class="profileRow"><x:display name="${widget.reportDateFrom.absoluteName}"/></td>
    </tr>
    <tr>
        <td nowrap class="profileRow" align="right" height="25"><b><fmt:message key="fms.facility.label.requiredTo"/></b>&nbsp;</td>
        <td class="profileRow"><x:display name="${widget.reportDateTo.absoluteName}"/></td>
    </tr>
    --%>
				
    <tr>
    	<td colspan="2" height="12">
    	&nbsp;
    	</td>
    </tr>
    <%--
    <tr>
        <td nowrap class="profileRow" align="right" height="25">&nbsp;</td>
        <td class="profileRow"><x:display name="${widget.checkOut.absoluteName}"/></td>
    </tr>
    --%>

<tr>
    	<td colspan="2" height="22">
    	&nbsp;
    	</td>
    </tr>
    <tr>
    	<td colspan="2" height="22" bgcolor="#003366" class="contentTitleFont">
    	<b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.title.itemListing"/></font></b>
    	</td>
    </tr>

	</c:if>
    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
