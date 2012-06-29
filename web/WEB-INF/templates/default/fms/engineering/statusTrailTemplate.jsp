<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>
<br>
<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="100%">
	<tr>
		<td colspan="2" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.request.label.statusTrail"/></font></b></td>
	</tr>
    <tr>
        <td width="30%" nowrap class="profileRow"  valign="top" align="right">&nbsp;</td>
        <td width="70%" class="profileRow"><table>
        <c:forEach items="${widget.statusTrail}" var="status">
        <tr><td>
            <b><c:out value="${status.statusLabel}"/></b> , on <fmt:formatDate value="${status.createdDate}"  pattern="${globalDatetimeLong}"/> by <c:out value="${status.createdBy}"/>
            	<c:if test="${(not empty status.remarks) || (not empty status.additionalInfo)}">
            	, <b><c:out value="${status.remarks}"/> <c:out value="${status.additionalInfo}" escapeXml="false"/></b>
            	</c:if> 
        </td></tr>
        </c:forEach>
        </table>
        </td>
    </tr>
</table><br>
<jsp:include page="../../form_footer.jsp" flush="true"/>

