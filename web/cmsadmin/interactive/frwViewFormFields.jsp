<%@ page import="kacang.stdui.Form,
                 kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.ViewFormFields" %>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="viewFormFieldsPage">
		<portlet name="viewFormFieldsPortlet" text="Query Form" width="100%" permanent="true">
	        <com.tms.collab.formwizard.ui.ViewFormFields name="viewFormFields"/>
		</portlet>
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
	ViewFormFields form = (ViewFormFields)wm.getWidget("viewFormFieldsPage.viewFormFieldsPortlet.viewFormFields");
%>
<c-rt:set var="formId" value="<%=form.getFormId()%>"/>
<c:redirect url="frwViewReport.jsp?formId=${formId}" />
<%
}
%>
</c:if>

<c:if test="${!empty param.formId}">
    <x:set name="viewFormFieldsPage.viewFormFieldsPortlet.viewFormFields" property="formId" value="${param.formId}"/>
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


     <x:display name="viewFormFieldsPage.viewFormFieldsPortlet" ></x:display>


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

