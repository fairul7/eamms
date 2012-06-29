<!--- template directory = [root]\WEB-INF\templates\default --->
<!--- addQtnTemplateText.jsp --->
<!--- editQtnTemplateText.jsp --->

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
        <fmt:message key='com.tms.quotation.templateText.edit'/>
      </c:when>
      <c:when test="${form.type eq 'Add' }">
        <fmt:message key='com.tms.quotation.templateText.add'/>
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
  <td align="right" valign="top" width="20%"><b><fmt:message key='com.tms.quotation.templateText.description'/>&nbsp;*<!---marker---></b></td>
  <td><x:display name="${form.childMap.QtnTemplateDescription.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="20%"><b><fmt:message key='com.tms.quotation.templateText.body'/><!---marker---></b></td>
  <td><x:display name="${form.childMap.QtnTemplateBody.absoluteName}"/></td>
</tr>
<tr>
  <td align="right" valign="top" width="20%"></td>
  <td><x:display name="${form.childMap.QtnTemplateactive.absoluteName}"/></td>
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
