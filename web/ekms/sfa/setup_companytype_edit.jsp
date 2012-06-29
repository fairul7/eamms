<%@include file="/common/header.jsp" %>
<x:permission var="isAuthorized" module="com.tms.crm.sales.model.AccountManagerModule" permission="com.tms.crm.sales.SalesAdmin"/>

<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>
<x:config>
    <page name="jsp_companytype_edit">

     	<com.tms.crm.sales.ui.CompanyTypeEditForm name="form1"  width="100%" />
    </page>
</x:config>

<c:if test="${! empty param.id}" >
    <x:set name="jsp_companytype_edit.form1" property="id" value="${param.id}"/>         
</c:if>

<c:if test="${forward.name == 'cancel' or forward.name == 'updated'}" >
    <c:redirect url="/ekms/sfa/setup_companytype.jsp" />
</c:if>


    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.setup'/> > <fmt:message key='sfa.message.editCompanyType'/>
              </td>
    </tr>
    <tr>
    <td class="sfaRow">
    <c:if test="${forward.name == 'duplicated'}">
        <font color="red" ><fmt:message key='sfa.message.companytypealreadyexisted'/></font>
    </c:if>
    
        	<x:display name="jsp_companytype_edit.form1"></x:display>
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
