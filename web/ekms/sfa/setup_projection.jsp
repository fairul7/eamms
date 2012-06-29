<%@include file="/common/header.jsp" %>
<x:permission var="isAuthorized" module="com.tms.crm.sales.model.AccountManagerModule" permission="com.tms.crm.sales.SalesAdmin"/>

<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>
<x:config>
    <page name="jsp_projection">
        <com.tms.crm.sales.ui.ProjectionTable name="table1" width="100%"/>
     	<com.tms.crm.sales.ui.ProjectionSelectForm name="form1" />
    </page>
</x:config>


<c:if test="${forward.name == 'selectProjection'}">
	<c:redirect url="projection2.jsp?userID=${widgets['jsp_projection.form1'].userID}&year=${widgets['jsp_projection.form1'].year}"/>
</c:if>
<c:if test="${forward.name == 'delete'}" >
    <script>
        alert("<fmt:message key='sfa.message.projectionDelete'/>.");
    </script>
</c:if>


 <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.projection'/>
     </td>
    </tr>
    <tr>
    <td class="sfaRow">
        <x:display name="jsp_projection.table1"></x:display>
   </td>
   </tr>
   </table>

   <br>

        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                    <fmt:message key='sfa.message.editProjection'/>
                </td>
            </tr>

      <tr>
      <td class="sfaRow">

        <x:display name="jsp_projection.form1"></x:display>
</td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
