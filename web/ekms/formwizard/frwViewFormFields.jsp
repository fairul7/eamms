<%--<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>--%>

<%@ page import="kacang.stdui.Form,
                 kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.ViewFormFields" %>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="ekmsViewFormFieldsPage">
	        <com.tms.collab.formwizard.ui.ViewFormFields name="ekmsViewFormFields"/>
    </page>
</x:config>


<c-rt:set var="cancel" value = "<%= Form.CANCEL_FORM_ACTION %>"/>
<c:if test="${forward.name == cancel}">
	<c:redirect url = "frwFormsQuery.jsp" />
</c:if>

<c:if test="${forward.name == 'done'}">
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	ViewFormFields form = (ViewFormFields)wm.getWidget("ekmsViewFormFieldsPage.ekmsViewFormFields");
%>
<c-rt:set var="formId" value="<%=form.getFormId()%>"/>
<c:redirect url="frwViewReport.jsp?formId=${formId}" />
<%
}
%>
</c:if>

<c:if test="${!empty param.formId}">
    <x:set name="ekmsViewFormFieldsPage.ekmsViewFormFields" property="formId" value="${param.formId}"/>
</c:if>


<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;  <fmt:message key='formWizard.label.formWizard'/> > 
      <fmt:message key='formWizard.label.frwViewFormFields.queryForm'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
             <x:display name="ekmsViewFormFieldsPage.ekmsViewFormFields" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>
