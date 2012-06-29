<%@ page import="java.util.ArrayList,
				 kacang.Application,
				 java.util.Collection,
				 java.util.Iterator,
				 kacang.ui.WidgetManager,
		         com.tms.fms.facility.ui.CategoryForm,
		         com.tms.fms.facility.ui.CategoryTable,
		         com.tms.fms.department.model.*" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.admin" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/index.jsp"/>

<c-rt:set var="forward_succ" value="<%= CategoryForm.FORWARD_EDIT_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= CategoryForm.FORWARD_EDIT_FAIL %>"/>
<c-rt:set var="forward_exist" value="<%= CategoryForm.FORWARD_ADD_EXIST %>"/>
<c-rt:set var="forward_listing_add" value="<%= CategoryTable.FORWARD_LISTING_ADD %>"/>
<c-rt:set var="forward_listing_delete" value="<%= CategoryTable.FORWARD_LISTING_DELETE %>"/>
<c-rt:set var="forward_listing_delete_fail" value="<%= CategoryTable.FORWARD_LISTING_DELETE_FAIL %>"/>

<x:config>
    <page name="EditCategory">
    	<com.tms.fms.facility.ui.CategoryForm name="form"/>
    	<com.tms.fms.facility.ui.CategoryTable name="table" width="100%"/>
    </page>
</x:config>

<c:choose>
	<c:when test="${forward.name == forward_succ}">
		<script>alert("<fmt:message key='fms.facility.msg.recordUpdated'/>");document.location = "<c:url value="CategoryListing.jsp"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_fail}">
		<script>alert("<fmt:message key='fms.facility.msg.failToUpdateRecord'/>");</script>
	</c:when>
	<c:when test="${forward.name == forward_exist}">
		<script>alert("<fmt:message key='fms.facility.msg.errorRecordNameShouldBeUnique'/>");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_add}">
		<c:redirect url="AddCategory.jsp"/>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete}">
		<script>alert("<fmt:message key='fms.facility.msg.recordDeleted'/>");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete_fail}">
		<script>alert("<fmt:message key='fms.facility.msg.failToDeleteOneOrMoreRecord'/>");</script>
	</c:when>
</c:choose>

<c:choose>
	<c:when test="${not empty(param.cid)}">
		<c:set var="categoryID" value="${param.cid}"/>
	</c:when>
	<c:otherwise>
		<c:set var="categoryID" value="${widgets['EditCategory.form'].id}"/>
	</c:otherwise>
</c:choose>

<x:set name="EditCategory.form" property="id" value="${categoryID}"/>
<x:set name="EditCategory.form" property="action" value="<%= CategoryForm.FORM_ACTION_EDIT %>"/>
<x:set name="EditCategory.form" property="cancelUrl" value="CategoryListing.jsp"/>
<x:set name="EditCategory.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>

<c:if test="${widgets['EditCategory.form'].isParent == true}">
	<c:if test="${!empty param.id}">
		<c:redirect url="EditCategory.jsp?cid=${param.id}"/>
	</c:if>
</c:if>
<%
	Collection lstUnit = null;
	FMSDepartmentDao dao = (FMSDepartmentDao) Application.getInstance().getModule(FMSDepartmentManager.class).getDao();
	try{lstUnit = dao.selectUnit();}catch(Exception e){}
%>

<script language="JavaScript">
	var lstSize = <%=lstUnit.size() + 1%>;
	var lstDepartmentId = new Array(lstSize);
	var lstUnitId = new Array(lstSize);
	var lstUnitName = new Array(lstSize);
	
	<%
	if (lstUnit.size() > 0) {
		int j=0;
    	for (Iterator i=lstUnit.iterator(); i.hasNext();) {
        	FMSUnit o = (FMSUnit)i.next();
        	%>
				lstDepartmentId[<%=j%>] = "<%=o.getDepartment_id()%>";
				lstUnitId[<%=j%>] = "<%=o.getId()%>";
				lstUnitName[<%=j%>] = "<%=o.getName()%>";
			<%
			j++;
        }
    }
	%>
	
	function setDepartmentChange(){
		var departmentSelectBox = document.forms['EditCategory.form'].elements['EditCategory.form.sbDepartment'];
		var unitSelectBox = document.forms['EditCategory.form'].elements['EditCategory.form.sbUnit'];
		
		for(i=unitSelectBox.options.length - 1; i>=0; i--){
			unitSelectBox.options[i] = null;
		}
		
		unitSelectBox.selectedIndex = -1;
		
		var selectedDepartment = departmentSelectBox.options[departmentSelectBox.selectedIndex].value;
		
		unitSelectBox.options[unitSelectBox.options.length] = new Option("--- None ---", "-1");
		if(selectedDepartment != ""){
			for(i=0; i<lstSize; i++){
				if(selectedDepartment == lstDepartmentId[i]){
					unitSelectBox.options[unitSelectBox.options.length] = new Option(lstUnitName[i], lstUnitId[i]);
				}
			}
		}
	}
	function hideShowCategory(){
		var radiobtn = document.forms['AddCategory.form'].elements['AddCategory.form.pnParent.parentGroup'];
		var tr = document.getElementById("category");

		if(radiobtn[0].checked){
			tr.style.display = 'none';
		}else{
			tr.style.display = '';
		}
			
	}
</script>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.categorySetup'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="EditCategory.form" ></x:display>

    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<c:if test="${widgets['EditCategory.form'].isParent == true}">
	<script language="JavaScript">
		document.getElementById("category").style.display = 'none';
	</script>
	<x:set name="EditCategory.table" property="parent_id" value="${categoryID}"/>
	<x:set name="EditCategory.table" property="isParent" value="${false}"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="5">
	<tr valign="middle">
	    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.childCategoryListing'/></font></b></td>
	    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
	</tr>
	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

	<x:display name="EditCategory.table" ></x:display>

	</td></tr>
	<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
	</table>
</c:if>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>