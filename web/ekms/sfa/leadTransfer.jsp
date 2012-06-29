<%@ page import="com.tms.crm.sales.model.LeadModule,
                 kacang.Application,
                 com.tms.crm.sales.model.Lead"%>
<%@ include file="/common/header.jsp" %>

<c:set var="leadId" value="${param.leadId}"/>

<%
    String leadId= (String) pageContext.getAttribute("leadId");
    if(leadId!=null&&leadId.trim().length()>0){
        LeadModule lm = (LeadModule) Application.getInstance().getModule(LeadModule.class);
        Lead lead = lm.getLead(leadId);
        if(lead!=null){
            if(lead.getCompanyId()==null||lead.getCompanyId().trim().length()==0){
            %>
                <c:redirect url="/ekms/sfa/leadOppCompanyForm.jsp?leadId=${leadId}" />
            <%
            }else{
                pageContext.setAttribute("companyId",lead.getCompanyId());
            %>
                <c:redirect url="/ekms/sfa/leadopportunity_contact_list.jsp?leadId=${leadId}&companyId=${companyId}" />
            <%
            }
        }
    }
%>