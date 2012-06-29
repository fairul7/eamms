<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.AddForm,
                 com.tms.collab.formwizard.ui.AddFormField"%>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.formwizard.Add" module="com.tms.collab.formwizard.model.FormModule" url="noPermission.jsp" />
<x:config>
    <page name="addFormFieldPage">
	     <portlet name="addFormFieldPortlet" text="New Field" width="100%" permanent="true">
	          <com.tms.collab.formwizard.ui.AddFormField name="addFormField"/>
	    </portlet>

    </page>
</x:config>

<x:set name="addFormFieldPage.addFormFieldPortlet.addFormField" property="fieldFormAbsoluteName" value="addG2FieldFormPage.addG2FieldForm" />

<c:if test="${!empty param.formId}">
    <x:set name="addFormFieldPage.addFormFieldPortlet.addFormField" property="formId" value="${param.formId}" />
</c:if>

<c:if test="${forward.name == 'formConfigAdded'}">
<script>
<!--
<%

WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if(wm!=null){
	AddFormField form = (AddFormField)wm.getWidget("addFormFieldPage.addFormFieldPortlet.addFormField");

%>
	document.location = "<c:url value="frwAddFormField.jsp" />?formId=<%=form.getFormId()%>";
    myWin = window.open('frwEditPreviewForm.jsp?formId=<%=form.getFormId()%>','preview','scrollbars=yes,resizable=yes,width=450,height=380')
<%
}
%>
//-->
</script>
</c:if>

<c:if test="${forward.name == 'formConfigFinish' || forward.name == 'formApproved'}">
<script>
<!--
myWin = window.open('','preview','scrollbars=yes,resizable=yes,width=450,height=380');
if (myWin != null)
    myWin.close();
document.location = 'frwAddForm.jsp?reload=true';
//-->
</script>
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



     <x:display name="addFormFieldPage.addFormFieldPortlet" ></x:display>


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




