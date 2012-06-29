<%
    if (request.getAttribute("exportedData") != null) {
        response.setContentType("application/vnd.ms-excel; charset=unicode");

        response.setHeader("Content-Disposition", "attachment; filename=formwizard.CSV" );
        out.print(request.getAttribute("exportedData"));
        return;
    }
%>
<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.ViewReport"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="ekmsViewReportPage">
    		<com.tms.collab.formwizard.ui.ViewReport name="ekmsViewReport"/>
    </page>
</x:config>
<x:set name="ekmsViewReportPage.ekmsViewReport" property="selected" value="${widgets['ekmsViewFormFieldsPage.ekmsViewFormFields'].selectedFields}"/>
<x:set name="ekmsViewReportPage.ekmsViewReport" property="formName" value="${widgets['ekmsViewFormFieldsPage.ekmsViewFormFields'].formName}"/>

<c:if test="${!empty param.formId}">
    <x:set name="ekmsViewReportPage.ekmsViewReport" property="formId" value="${param.formId}"/>
</c:if>


<c:if test="${!empty param.id}">
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	ViewReport form = (ViewReport)wm.getWidget("ekmsViewReportPage.ekmsViewReport");
%>
<c-rt:set var="formID" value="<%=form.getFormId()%>"/>
<c:redirect url="frwEditFormData.jsp?formId=${formID}&id=${param.id}" />
<%
}
%>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
      <fmt:message key='formWizard.label.formReport'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="ekmsViewReportPage.ekmsViewReport" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>

