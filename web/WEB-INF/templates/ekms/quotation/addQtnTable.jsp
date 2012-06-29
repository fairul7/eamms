<!--- template directory = [root]\WEB-INF\templates\default --->
<!--- addQtnTable.jsp --->
<!--- editQtnTable.jsp --->

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<!--- Uncomment if template directory = [root]\WEB-INF\templates\default\[directory] --->
<jsp:include page="../form_header.jsp" flush="true"/>

<table width="100%" border="0" cellspacing="6" cellpadding="0" class="contentTitleFont">
<tr>
  <td>
  <c:choose>
    <c:when test="${form.type eq 'Edit'}">
      <fmt:message key='com.tms.quotation.table.edit'/>
    </c:when>
    <c:otherwise>
      <fmt:message key='com.tms.quotation.table.add'/>    
    </c:otherwise>
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
  <td align="right" valign="top" width="15%"><b><fmt:message key='com.tms.quotation.table.description'/>&nbsp;*<!---marker---></b></td>
  <td><x:display name="${form.childMap.QtnTabletableDescription.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="15%"><b><fmt:message key='com.tms.quotation.table.caption'/><!---marker---></b></td>
  <td><x:display name="${form.childMap.QtnTabletableCaption.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="15%"></td>
  <td><x:display name="${form.childMap.QtnTableActive.absoluteName}"/></td>
</tr>
<%--
<tr>
  <td align="right" valign="top" width="30%"><b>TableStyle<!---marker---></b></td>
  <td><x:display name="${form.childMap.QtnTabletableStyle.absoluteName}"/></td>
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
  </td>
</tr>
<tr>
  <td colspan="2" height="5"></td>
</tr>
</table>

<!--- Uncomment if template directory = [root]\WEB-INF\templates\default\[directory] --->
<jsp:include page="../form_footer.jsp" flush="true"/>

<%--
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%" border="0" cellspacing="6" cellpadding="0" class="contentTitleFont">
<tr>
  <td>Add Quotation Table</td>
</tr>
</table>

<table width="100%" border="0" cellspacing="6" cellpadding="0" class="contentBgColor">

<tr>
  <td align="right" valign="top" width="20%"><b>Table Description *</b></td>
  <td><x:display name="${form.childMap.setupTableDesc.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="20%"><b>Table Caption</b></td>
  <td><x:display name="${form.childMap.setupTableCaption.absoluteName}"/></td>
</tr>
<tr>
  <td></td>
  <td>
    <x:display name="${form.childMap.reset.absoluteName}"/>
    <x:display name="${form.childMap.cancel.absoluteName}"/>
    <x:display name="${form.childMap.submit.absoluteName}"/>    
  </td>
</tr>
<tr>
  <td colspan="2" height="5"></td>
</tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
--%>