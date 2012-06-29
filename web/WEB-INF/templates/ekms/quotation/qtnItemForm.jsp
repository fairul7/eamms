<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%" border="0" cellspacing="6" cellpadding="0" class="contentTitleFont">
<tr>
  <td>
    <c:choose>
      <c:when test="${form.type eq 'Edit'}">
        <fmt:message key='com.tms.quotation.item.edit'/>
      </c:when>
      <c:when test="${form.type eq 'Add' }">
        <fmt:message key='com.tms.quotation.item.add'/>
      </c:when>
    </c:choose>
  </td>
</tr>
</table>

<table width="100%" border="0" cellspacing="6" cellpadding="0" class="contentBgColor">
<c:forEach items="${form.fieldList}" var="field" >
	<tr>
	  <td align="right" valign="top" width="20%">
	    <b>
	      <x:display name="${field.label.absoluteName}"/>
<%--      <c:if test="${field.compulsory eq '1'}">&nbsp;*</c:if> --%>
	    </b>
      </td>
	  <td><x:display name="${field.textField.absoluteName}"/></td>
<%--  <td><c:out value="${field.columnId}" /></td>	  --%>

	</tr>
</c:forEach>
	<tr>
	  <td></td>
	  <td colspan="2">
	    <x:display name="${form.childMap.submit.absoluteName }" />
	    <x:display name="${form.childMap.exit.absoluteName }" />
	    <x:display name="${form.childMap.reset.absoluteName }" />
	    <x:display name="${form.childMap.cancel.absoluteName }" />
<%--    <x:display name="${form.childMap.preview.absoluteName }" /> --%>
	  </td>
	</tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>