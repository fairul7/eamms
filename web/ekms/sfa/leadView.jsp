<%@ page import="kacang.services.security.SecurityService,
                 kacang.services.security.User,
                 com.tms.crm.sales.misc.AccessUtil,
                 com.tms.crm.sales.model.LeadModule,
                 com.tms.crm.sales.model.Lead,
                 com.tms.collab.calendar.ui.UserUtil,
                 com.tms.collab.forum.model.StringUtil,
                 org.apache.commons.lang.StringUtils"%>
<%@ include file="/common/header.jsp" %>

<%
            String userId = ((User)session.getAttribute(SecurityService.SESSION_KEY_USER)).getId();


%>

    <c:if test="${!empty param.leadId}" >
     <c:set var="leadId" value="${param.leadId}"/>
    </c:if>

<c:if test="${param.action == 'del' && !empty param.leadId}" >
    <%

        LeadModule lm = (LeadModule) Application.getInstance().getModule(LeadModule.class);
        lm.deleteLead(userId,pageContext.getAttribute("leadId").toString());
    %>
    <c:redirect url="/ekms/sfa/leadTable.jsp" />

</c:if>




    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
    <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
        <tr valign="top">
            <td align="left" valign="top" class="sfaHeader">
                Lead
            </td>
    </tr>
    <tr>
    <td class="sfaRow">

<%
    if(AccessUtil.isExternalSalesPerson(userId)||AccessUtil.isSalesManager(userId)||AccessUtil.isSalesPerson(userId))
    {

%>
    <%
        String leadId = (String) pageContext.getAttribute("leadId");
        if(leadId==null||leadId.trim().length()==0){
    %>
        Invalid Lead id.
    <%
        }else{
            LeadModule lm = (LeadModule) Application.getInstance().getModule(LeadModule.class);
            Lead lead = lm.getLead(leadId);
            if(lead !=null){
                pageContext.setAttribute("lead",lead);
    %>

<table border="1" cellpadding="2" cellspacing="0" class="formStyle" width="100%">
    <tr>
        <td valign="top" width="20%"><B>Company</B></td>
        <td > <c:out value="${lead.companyName}"/>
        </td>
    </tr>

    <tr>
        <td valign="top">
            <B>Contact No.</B>
        </td>
        <td valign="top">
            <c:out value="${lead.tel}" />   &nbsp;
        </td>

    </tr>
    <tr>
            <td valign="top"><B>Contact Person</B></td>
            <td valign="top">
                <c:out value="${lead.contactName}" />  &nbsp;
            </td>
    </tr>


    <tr>
        <td valign="top"><B>Remarks</B></td>
        <td  valign="top">
            <c:set var="remarks" value="${lead.remarks}"/>
                <%=StringUtils.replace((String)pageContext.getAttribute("remarks"),"\n","<br>")%>
           <%--<c:out value="${remarks}" />
           --%> &nbsp;
        </td>

    </tr>

    <tr>
        <td valign="top"><B>Source</B> </td>
        <td valign="top">

            <c:out value="${lead.propertyMap['sourceName']}" /> &nbsp;

        </td>
    </tr>

    <tr>
        <td valign="top"><B>Created On </B> </td>
        <td valign="top">
            <fmt:formatDate value="${lead.creationDate}" pattern="${globalDateLong}" />
        </td>
    </tr>

    <tr>
        <td valign="top"><B>Last Modified</B> </td>
        <td valign="top">
            <fmt:formatDate value="${lead.modifiedDate}" pattern="${globalDatetimeLong}" /> by
            <%
                pageContext.setAttribute("modifiedBy",UserUtil.getUser(lead.getModifiedBy()).getName());
            %> <c:out value="${modifiedBy}" />
        </td>
    </tr>

    <tr>
        <td >&nbsp; </td>
        <td>
            <%
                if(lead.getUserId().equals(userId)||AccessUtil.isSalesManager(userId)){
            %>
                    <input type="button" class="button" value="Edit" onClick="location='<c:url value="/ekms/sfa/editLead.jsp?leadId=${param.leadId}" />'"/>
                    <input type="button" class="button" value="Delete" onClick="if(confirm('Are you sure you want to delete this lead?'))location='?action=del&leadId=<c:out value="${param.leadId}"/>'"/>
                    <input type="button" class="button" value="Transfer" onClick="location = 'leadTransfer.jsp?leadId=<c:out value="${param.leadId}"/>' " />
					<input type="button" class="button" value="Cancel" onClick="location = 'leadTable.jsp'"/>
             <%   }
            %>

        </td>
    </tr>

</table>


            <%
            }else{
            %>
                The lead you request cannot be found from database or it's been deleted.

             <%
            }
        }
    %>
<%}else{
        %>
   You have no access to view this page

        <%}%>

</td>
    </tr>
    <tr>
    <td class="sfaFooter">
                         &nbsp;
    </td>
    </tr>

</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
