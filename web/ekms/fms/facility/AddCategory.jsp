<%@ page import="java.util.ArrayList,
				 kacang.Application,
				 java.util.Collection,
				 java.util.Iterator,
				 kacang.ui.WidgetManager,
		         com.tms.fms.facility.ui.CategoryForm,
		         com.tms.fms.department.model.*" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.admin" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/home.jsp"/>

<c-rt:set var="forward_succ" value="<%= CategoryForm.FORWARD_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= CategoryForm.FORWARD_ADD_FAIL %>"/>
<c-rt:set var="forward_exist" value="<%= CategoryForm.FORWARD_ADD_EXIST %>"/>

<x:config>
    <page name="AddCategory">
    	<com.tms.fms.facility.ui.CategoryForm name="form"/>
    </page>
</x:config>

<c:choose>
	<c:when test="${forward.name == forward_succ}">
		<script>alert("<fmt:message key='fms.facility.msg.recordAdded'/>");document.location = "<c:url value="CategoryListing.jsp"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_fail}">
		<script>alert("<fmt:message key='fms.facility.msg.failToAddRecord'/>");</script>
	</c:when>
	<c:when test="${forward.name == forward_exist}">
		<script>alert("<fmt:message key='fms.facility.msg.errorRecordNameShouldBeUnique'/>");</script>
	</c:when>
</c:choose>

<x:set name="AddCategory.form" property="action" value="<%= CategoryForm.FORM_ACTION_ADD %>"/>
<x:set name="AddCategory.form" property="cancelUrl" value="index.jsp"/>
<x:set name="AddCategory.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>

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
		var departmentSelectBox = document.forms['AddCategory.form'].elements['AddCategory.form.sbDepartment'];
		var unitSelectBox = document.forms['AddCategory.form'].elements['AddCategory.form.sbUnit'];
		
		for(i=unitSelectBox.options.length - 1; i>=0; i--){
			unitSelectBox.options[i] = null;
		}
		
		unitSelectBox.selectedIndex = -1;
		
		var selectedDepartment = departmentSelectBox.options[departmentSelectBox.selectedIndex].value;
		
		unitSelectBox.options[unitSelectBox.options.length] = new Option("--- NONE ---", "-1");
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

        <x:display name="AddCategory.form" ></x:display>
        
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>


<script language="JavaScript">
	hideShowCategory();
	setDepartmentChange();
</script>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>