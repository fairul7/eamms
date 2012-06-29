<!--- template directory = [root]\WEB-INF\templates\default --->
<!--- addQuotation.jsp --->
<!--- editQuotation.jsp --->

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
        <fmt:message key='com.tms.quotation.quotation.edit'/>
      </c:when>
      <c:when test="${form.type eq 'Add' }">
        <fmt:message key='com.tms.quotation.quotation.add'/>
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
  <td align="right" valign="top" width="15%"><b><fmt:message key="helpdesk.label.company"/><!---marker---></b></td>
  <td><x:display name="${form.childMap.lblQuotationCompany.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="15%"><b><fmt:message key='com.tms.quotation.quotation.customer'/><!---marker---></b></td>
  <td><x:display name="${form.childMap.lbQuotationcustomer.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="15%"><b><fmt:message key='com.tms.quotation.quotation.subject'/>&nbsp;*<!---marker---></b></td>
  <td><x:display name="${form.childMap.Quotationsubject.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="15%"><b><fmt:message key='com.tms.quotation.quotation.template'/>&nbsp;*<!---marker---></b></td>
  <td><x:display name="${form.childMap.QuotationtemplateId.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="15%"><b><fmt:message key='com.tms.quotation.quotation.table'/>&nbsp;*<!---marker---></b></td>
  <td><x:display name="${form.childMap.QuotationQtnTableId.absoluteName}"/></td>
</tr>
<%--<tr>
  <td align="right" valign="top" width="15%"><b>Content<!---marker---></b></td>
  <td><x:display name="${form.childMap.Quotationcontent.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="15%"><b>Status<!---marker---></b></td>
  <td><x:display name="${form.childMap.Quotationstatus.absoluteName}"/></td>
</tr>
--%>
<%--
<tr>
  <td align="right" valign="top" width="15%"><b>OpenDate<!---marker---></b></td>
  <td><x:display name="${form.childMap.QuotationopenDate.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="15%"><b>CloseDate<!---marker---></b></td>
  <td><x:display name="${form.childMap.QuotationcloseDate.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="15%"><b>Recdate<!---marker---></b></td>
  <td><x:display name="${form.childMap.Quotationrecdate.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top">Status<br></td>
  <td><x:display name="${form.childMap.sbQuotationstatus.absoluteName}"/>
</tr>
--%>
<tr>
  <td colspan="2" height="10"></td>
</tr>
<tr>
  <td align="right" valign="top"></td>
  <td>
    <x:display name="${form.childMap.submit.absoluteName}"/>
    <x:display name="${form.childMap.reset.absoluteName}"/>
    <x:display name="${form.childMap.cancel.absoluteName}"/>
    <x:display name="${form.childMap.addItems.absoluteName }"/>
  </td>
</tr>
<tr>
  <td colspan="2" height="5"></td>
</tr>
</table>

<!--- Uncomment if template directory = [root]\WEB-INF\templates\default\[directory] --->
<jsp:include page="../form_footer.jsp" flush="true"/>
<%--<jsp:include page="form_footer.jsp" flush="true"/>--%>
