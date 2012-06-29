<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
    <jsp:include page="../../form_header.jsp" flush="true"/>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.requestTitle"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.request.title}"/></td>
    </tr>
	<tr>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.facility.label.requiredDate"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><fmt:formatDate value="${widget.request.requiredFrom}"  pattern="${globalDateLong}"/> - <fmt:formatDate value="${widget.request.requiredTo}"  pattern="${globalDateLong}"/></td>
    </tr>
	<c:if test="${widget.request.requestType eq 'I'}">
	    <tr>
	        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.program"/></b>&nbsp;</td>
	        <td width="70%" class="profileRow"><c:out value="${widget.request.programName}"/></td>
	    </tr>
	</c:if>
	<c:if test="${widget.request.requestType eq 'E'}">
	    <tr>
	        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.clientName"/></b>&nbsp;</td>
	        <td width="70%" class="profileRow"><c:out value="${widget.request.clientName}"/></td>
	    </tr>
	</c:if>
    <tr>
        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.request.label.remarks"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.request.description}" escapeXml="false"/></td>
    </tr>
    
    <tr><td class="profileFooter" colspan="2">
    	<c:forEach items="${widget.serviceForms}" var="profiler">
            <x:display name="${profiler.absoluteName}"/>
        </c:forEach>
    	</td>
    </tr>
    <tr>
    	<td class="contentTitleFont" colspan="2" align="center"></td>
    </tr>
    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
