<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget }" />
<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%" border="0" cellspacing="6" cellpadding="0" class="contentTitleFont">
<tr>
  <td><fmt:message key='com.tms.quotation.sortColumn'/></td>
</tr>
</table>

<table width="100%" border="0" cellspacing="6" cellpadding="0" class="contentBgColor">
<%--
<tr>
  <td colspan="2"><x:display name="${form.childMap.errorMsg.absoluteName}"/></td>
</tr>
--%>
<tr>
  <td align="right" valign="top" width="15%"><b><fmt:message key='com.tms.quotation.sortColumn.cols'/><!---marker---></b></td>
  <td><x:display name="${form.childMap.columnSort.absoluteName}" /></td>
</tr>
<tr>
  <td colspan="2" height="10"></td>
</tr>
<tr>
  <td align="right" valign="top"></td>
  <td>
    <x:display name="${form.childMap.submit.absoluteName}" />
    <x:display name="${form.childMap.reset.absoluteName}" />
    <x:display name="${form.childMap.cancel.absoluteName}" />
  </td>
</tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
