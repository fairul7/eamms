<%
if (request.getAttribute("exportedData") != null) {
        response.setContentType("application/vnd.ms-excel");

        response.setHeader("Content-Disposition", "attachment; filename=formwizard.CSV" );
        out.print(request.getAttribute("exportedData"));
        return;
}
%>

<%@ page import="com.tms.collab.formwizard.ui.ViewHistoryReport"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="viewHistoryReportPage">
		<portlet name="viewHistoryReportPortlet" text="History Report" width="100%" permanent="true">
    		<com.tms.collab.formwizard.ui.ViewHistoryReport name="viewHistoryReport"/>
		</portlet>
    </page>
</x:config>

<c:if test="${!empty param.formId}">
    <x:set name="viewHistoryReportPage.viewHistoryReportPortlet.viewHistoryReport" property="formId" value="${param.formId}"/>
</c:if>

<c:if test="${!empty param.id}">
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	ViewHistoryReport form = (ViewHistoryReport)wm.getWidget("viewHistoryReportPage.viewHistoryReportPortlet.viewHistoryReport");
%>
<c-rt:set var="formId" value="<%=form.getFormId()%>"/>
<c:redirect url="frwHistoryData.jsp?formUid=${param.id}&formId=${formId}" />
<%
}
%>
</c:if>


<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerInteractive.jsp" flush="true" />


<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideInteractiveFW.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;"><br>

         <x:display name="viewHistoryReportPage.viewHistoryReportPortlet" ></x:display>



                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
              </td>
            </tr>
          </tbody>
        </table>
      </td>
    </tr>
  </tbody>
</table>
