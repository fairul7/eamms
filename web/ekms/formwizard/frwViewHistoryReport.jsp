<%
if (request.getAttribute("exportedData") != null) {
        response.setContentType("application/vnd.ms-excel; charset=unicode");

        response.setHeader("Content-Disposition", "attachment; filename=formwizard.CSV" );
        out.print(request.getAttribute("exportedData"));
        return;
}
%>

<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.ViewHistoryReport"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="ekmsViewHistoryReportPage">
    		<com.tms.collab.formwizard.ui.ViewHistoryReport name="ekmsViewHistoryReport"/>
    </page>
</x:config>

<c:if test="${!empty param.formId}">
    <x:set name="ekmsViewHistoryReportPage.ekmsViewHistoryReport" property="formId" value="${param.formId}"/>
</c:if>

<c:if test="${!empty param.id}">
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	ViewHistoryReport form = (ViewHistoryReport)wm.getWidget("ekmsViewHistoryReportPage.ekmsViewHistoryReport");
%>
<c-rt:set var="formId" value="<%=form.getFormId()%>"/>
<c:redirect url="frwHistoryData.jsp?formUid=${param.id}&formId=${formId}" />
<%
}
%>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='formWizard.label.personal'/> > 
      History Report
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
			     <x:display name="ekmsViewHistoryReportPage.ekmsViewHistoryReport" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>