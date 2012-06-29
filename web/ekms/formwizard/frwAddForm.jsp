<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.AddForm"%>
<%@ include file="/common/header.jsp" %>


<x:permission permission="com.tms.collab.formwizard.Add" module="com.tms.collab.formwizard.model.FormModule" url="noPermission.jsp" />

<!-- Declare Widgets -->
<x:config>
    <page name="ekmsAddFormPage">
        	<com.tms.collab.formwizard.ui.AddForm name="ekmsAddForm"/>
    </page>
</x:config>


<c:if test="${forward.name == 'formAdded'}">
<script>
<!--
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	AddForm form = (AddForm)wm.getWidget("ekmsAddFormPage.ekmsAddForm");
%>
    document.location = "<c:url value="frwAddFormField.jsp" />?formId=<%=form.getFormId()%>";
    myWin = window.open('frwEditPreviewForm.jsp?formId=<%=form.getFormId()%>','preview','scrollbars=yes,resizable=yes,width=450,height=380')
<%
}
%>
//-->
</script>
</c:if>



<%@ include file="/ekms/includes/header.jsp" %>
<%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;  <fmt:message key='formWizard.label.formWizard'/> >
      <fmt:message key='formWizard.label.newForm'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
			     <x:display name="ekmsAddFormPage.ekmsAddForm" ></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>