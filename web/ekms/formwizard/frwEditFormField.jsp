<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.AddForm,
                 com.tms.collab.formwizard.ui.AddFormField,
                 com.tms.collab.formwizard.ui.EditFormField"%>
<%@ include file="/common/header.jsp" %>


<x:config>
    <page name="ekmsEditFormFieldPage">
          <com.tms.collab.formwizard.ui.AddFormField name="ekmsEditFormField"/>
    </page>
</x:config>

<x:set name="ekmsEditFormFieldPage.ekmsEditFormField" property="fieldFormAbsoluteName" value="ekmsAddG2FieldFormPage.ekmsAddG2FieldForm" />

<c:if test="${!empty param.formId}">
	<x:set name="ekmsEditFormFieldPage.ekmsEditFormField" property = "formId" value = "${param.formId}"/>
</c:if>



<c:if test="${forward.name == 'formConfigAdded'}">
<script>
<!--
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if(wm!=null){
	AddFormField form = (AddFormField)wm.getWidget("ekmsEditFormFieldPage.ekmsEditFormField");
%>
window.open('frwEditPreviewForm.jsp?formId=<%=form.getFormId()%>','preview','scrollbars=yes,resizable=yes,width=700,height=500')
document.location = "<c:url value="frwEditFormField.jsp" />?formId=<%=form.getFormId()%>";
<%
}
%>
//-->
</script>
</c:if>

<c:if test="${forward.name == 'formConfigFinish'}">
    <script>
        document.location= "<c:url value="frwFormsEdit.jsp"/>";
    </script>
</c:if>

<c:if test="${forward.name == 'fieldNotAdded'}">
    <script>
        <%
             WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
             if(wm!=null){
                 AddFormField form = (AddFormField)wm.getWidget("ekmsEditFormFieldPage.ekmsEditFormField");
         %>
        alert('Field Not Added');
        document.location= "frwEditForm.jsp"?formId=<%=form.getFormId()%>"";
        <%
             }
        %>
    </script>
</c:if>





<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
      <fmt:message key='formWizard.label.newField'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
     <x:display name="ekmsEditFormFieldPage.ekmsEditFormField" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>
