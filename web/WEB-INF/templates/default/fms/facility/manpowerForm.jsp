<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
    <jsp:include page="../../form_header.jsp" flush="true"/>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.facility.label.selectManpower"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><x:display name="${widget.userSelectBox.absoluteName}"/></td>
    </tr>
    <c:if test="${! empty widget.profilers}">
        <c:forEach items="${widget.profilers}" var="profiler">
            <tr><td colspan="2" class="profileHeader"><b><c:out value="${profiler.profileableLabel}"/></b></td></tr>
            <x:display name="${profiler.widget.absoluteName}"/>
        </c:forEach>
    </c:if>
    
    <tr>
        <td width="30%" nowrap class="profileRow">&nbsp;</td>
        <td width="70%" class="profileRow">
            <x:display name="${widget.panel.absoluteName}"/>
        </td>
    </tr>
    <tr><td class="profileFooter" colspan="2">&nbsp;</td></tr>
    <jsp:include page="../../form_footer.jsp" flush="true"/>
</table>
