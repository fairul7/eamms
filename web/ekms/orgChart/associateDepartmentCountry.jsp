<%@ page import="com.tms.hr.orgChart.ui.AssociateDepartmentCountryForm"%>
<%@include file="/common/header.jsp" %>
<%@include file="includes/accessControl.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="orgChart">
		<com.tms.hr.orgChart.ui.AssociateDepartmentCountryForm name="associateDepartmentCountry"/>
        <com.tms.hr.orgChart.ui.DepartmentCountryAssociativityList name="departmentCountryAssociativityList"/>
    </page>
</x:config>

<c-rt:set var="forward_saved" value="<%=AssociateDepartmentCountryForm.FORWARD_SAVED%>"/>
<c-rt:set var="forward_error" value="<%=AssociateDepartmentCountryForm.FORWARD_ERROR%>"/>
<c-rt:set var="forward_association_exist" value="<%=AssociateDepartmentCountryForm.FORWARD_ASSOCIATION_EXIST%>"/>
<c-rt:set var="forward_empty_selectbox" value="<%=AssociateDepartmentCountryForm.FORWARD_EMPTY_SELECTBOX%>"/>

<c:if test="${forward.name=='cancel_form_action'}">
    <c:redirect url="listHierachy.jsp"/>
</c:if>

<c:if test="${forward.name==forward_saved}">
    <script type="text/javascript">
        alert("<fmt:message key="orgChart.association.alert.saved"/>");
    </script>
</c:if>

<c:if test="${forward.name==forward_error}">
    <script type="text/javascript">
        alert("<fmt:message key="orgChart.general.alert.error"/>");
    </script>
</c:if>

<c:if test="${forward.name==forward_association_exist}">
    <script type="text/javascript">
        alert("<fmt:message key="orgChart.association.alert.deptCountryAssociationExist"/>");
    </script>
</c:if>

<c:if test="${forward.name==forward_empty_selectbox}">
    <script type="text/javascript">
        alert("<fmt:message key="orgChart.association.alert.emptySelectbox"/>");
    </script>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<table border="0" cellpadding="0" cellspacing="0" width="100%" class="contentBgColor">
    <tr>
	    <td class="contentTitleFont" colspan="2" style="padding:5px 5px 5px 5px;">
	    	<fmt:message key='orgChart.association.label.associateDepartmentCountryMapping'/>
	    </td>
  	</tr>
    <tr>
        <td>
            <x:display name="orgChart.associateDepartmentCountry"/>
        </td>
    </tr>
    <tr>
    <tr>
	    <td class="contentTitleFont" colspan="2" style="padding:5px 5px 5px 5px;">
			<fmt:message key='orgChart.association.label.listDepartmentCountryMapping'/>
	    </td>
  	</tr>
    <tr>
        <td> 
            <x:display name="orgChart.departmentCountryAssociativityList"/>
        </td>
    </tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>




