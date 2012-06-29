<!--- template directory = [root]\WEB-INF\templates\default --->
<!--- addCustomer.jsp --->
<!--- editCustomer.jsp --->

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<!--- Uncomment if template directory = [root]\WEB-INF\templates\default\[directory] --->
<jsp:include page="../form_header.jsp" flush="true"/>
<%--<jsp:include page="form_header.jsp" flush="true"/>--%>

<table width="100%" border="0" cellspacing="6" cellpadding="0" class="contentTitleFont">
<tr>
  <td>
    <c:choose>
      <c:when test="${form.type eq 'Edit'}">
        <fmt:message key='com.tms.quotation.customer.edit'/>
      </c:when>
      <c:when test="${form.type eq 'Add' }">
        <fmt:message key='com.tms.quotation.customer.add'/>
      </c:when>
    </c:choose>
  </td>
</tr>
</table>

<table width="100%" border="0" cellspacing="6" cellpadding="0" class="contentBgColor">
<tr>
  <td colspan="2"><x:display name="${form.childMap.errorMsg.absoluteName}"/></td>
</tr>
<!---<tr>
  <td colspan="2" valign="top" height="20"><b>Subtitle</b></td>
</tr>--->
<tr>
  <td align="right" valign="top" width="30%"><b><fmt:message key='com.tms.quotation.customer.firstName'/>&nbsp;*<!---marker---></b></td>
  <td><x:display name="${form.childMap.CustomercontactFirstName.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="30%"><b><fmt:message key='com.tms.quotation.customer.lastName'/>&nbsp;*<!---marker---></b></td>
  <td><x:display name="${form.childMap.CustomercontactLastName.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="30%"><b><fmt:message key='com.tms.quotation.customer.companyName'/>&nbsp;*<!---marker---></b></td>
  <td><x:display name="${form.childMap.CustomercompanyName.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="30%"><b><fmt:message key='com.tms.quotation.customer.address'/>&nbsp;*<!---marker---></b></td>
  <td><x:display name="${form.childMap.Customeraddress1.absoluteName}"/></td>
</tr>
<%--
<tr>
  <td align="right" valign="top" width="30%"><b><fmt:message key='com.tms.quotation.customer.address2'/><!---marker---></b></td>
  <td><x:display name="${form.childMap.Customeraddress2.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="30%"><b><fmt:message key='com.tms.quotation.customer.address3'/><!---marker---></b></td>
  <td><x:display name="${form.childMap.Customeraddress3.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="30%"><b><fmt:message key='com.tms.quotation.customer.postCode'/><!---marker---></b></td>
  <td><x:display name="${form.childMap.Customerpostcode.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="30%"><b><fmt:message key='com.tms.quotation.customer.state'/><!---marker---></b></td>
  <td><x:display name="${form.childMap.Customerstate.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="30%"><b><fmt:message key='com.tms.quotation.customer.country'/><!---marker---></b></td>
  <td><x:display name="${form.childMap.Customercountry.absoluteName}"/></td>
</tr>
--%>
<tr>
  <td align="right" valign="top" width="30%"><b><fmt:message key='com.tms.quotation.customer.gender'/><!---marker---></b></td>
  <td><x:display name="${form.childMap.Customergender.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="30%"><b><fmt:message key='com.tms.quotation.customer.salutation'/>&nbsp;*<!---marker---></b></td>
  <td><x:display name="${form.childMap.Customersalutation.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="30%"><b><!---marker---></b></td>
  <td><x:display name="${form.childMap.Customeractive.absoluteName}"/></td>
</tr>
<tr>
  <td colspan="2" height="10"></td>
</tr>
<tr>
  <td align="right" valign="top"></td>
  <td>
    <x:display name="${form.childMap.submit.absoluteName}"/>
    <x:display name="${form.childMap.reset.absoluteName}"/>
    <x:display name="${form.childMap.cancel.absoluteName}"/>
  </td>
</tr>
<tr>
  <td colspan="2" height="5"></td>
</tr>
</table>

<!--- Uncomment if template directory = [root]\WEB-INF\templates\default\[directory] --->
<jsp:include page="../form_footer.jsp" flush="true"/>
<%--<jsp:include page="form_footer.jsp" flush="true"/>--%>