<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<link ref="stylesheet" href="images/style.css">
<jsp:include page="../form_header.jsp" flush="true"/>
<tr>
    <td align="right" class="profileRow"><fmt:message key="tmsPIMSync.label.devices"/> </td>
    <td class="profileRow">
        <x:display name="${widget.chkOutlook.absoluteName}"/>
        <br>
        <x:display name="${widget.chkSymbian.absoluteName}"/> <x:display name="${widget.symDeviceId.absoluteName}"/>
        <br>
        <x:display name="${widget.chkWindows.absoluteName}"/> <x:display name="${widget.winDeviceId.absoluteName}"/>       
        <br>
    </td>
</tr>
<jsp:include page="../form_footer.jsp" flush="true"/>
