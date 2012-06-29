<%@ page import="kacang.services.security.SecurityService,
				 kacang.Application,
				 kacang.services.security.User,
				 com.tms.collab.formwizard.model.FormConstants"%>
<%@ include file="/common/header.jsp" %>

<%
    SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
    User user = security.getCurrentUser(request);
%>

      <table cellpadding="5" cellspacing="0" border="0" style="text-align: left; width: 225px;">
          <tbody>
            <tr>
              <td style="background: #003366; vertical-align: top;">
                <span style="color: white; font-weight: bold;"><fmt:message key='formWizard.label.formWizard'/></span>
                <br>
              </td>
            </tr>
            <tr>
              <td style="background: #DDDDDD; vertical-align: top;">

            <li><a class="option" href="frwFormsView.jsp?reload=true"><fmt:message key='formWizard.label.formsListing'/></a></li>

<%  if (security.hasPermission(user.getId(), FormConstants.FORM_ADD_PERMISSION_ID, "com.tms.collab.formwizard.model.FormModule", null)) { %>
            <li><a class="option" href="frwAddForm.jsp?reload=true"><fmt:message key='formWizard.label.newForm'/></a></li>
<%  
	}  
	if (security.hasPermission(user.getId(), FormConstants.FORM_EDIT_PERMISSION_ID, "com.tms.collab.formwizard.model.FormModule", null)) {
%>
            <li><a class="option" href="frwFormsEdit.jsp?reload=true">Edit | Delete Form</a></li>
<%
	}
%>
            <li><a class="option" href="frwFormsQuery.jsp?reload=true">Query Form Data</a></li>
            <li><a class="option" href="frwFormsDraft.jsp">Draft Submission</a></li>
            <li><a class="option" href="frwSubmittedFormHistory.jsp">Submitted Form History</a></li>
            <li><a class="option" href="frwStatusReport.jsp?reload=true">Submitted Form Status</a></li>
            <li><a class="option" href="frwApproveFormData.jsp?reload=true">Submitted Form Approval</a></li>
<%  if (security.hasPermission(user.getId(), FormConstants.FORM_ADD_PERMISSION_ID, "com.tms.collab.formwizard.model.FormModule", null)) { %>
            <li><a class="option" href="frwAddFormTemplate.jsp">New Form Template</a></li>
            <li><a class="option" href="frwTemplatesEdit.jsp">Edit | Delete Form Template</a></li>
<%
	}
%>
                <br> <br> <br> <br>
              </td>
            </tr>
          </tbody>
        </table>
        <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br>


