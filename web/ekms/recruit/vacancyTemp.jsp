<%@ page import="com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject,
				com.tms.hr.orgChart.model.OrgChartHandler,
				java.util.Collection,
				java.util.Iterator,
				kacang.Application"%>
				
<%@include file="/common/header.jsp"%>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="vacancyTemp">
		<com.tms.hr.recruit.ui.VacancyTempForm name="Form"/>
    </page>
</x:config>

<c:if test="${forward.name=='cancel_form_action'}">
    <c:redirect url="vacancyTempList.jsp"/>
</c:if>

<c:if test="${forward.name=='submited'}">
    <script type="text/javascript">
        alert("<fmt:message key="recruit.vacancyTemplate.alert.saved"/>");
        window.location="vacancyTempList.jsp";       
    </script>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>

<% 
	OrgChartHandler module = (OrgChartHandler) Application.getInstance().getModule(OrgChartHandler.class);
	Collection deptCountryList = module.selectDepartmentCountryAssociativity(null, null, null, null, false, 0, -1);
%>

<script type="text/javascript">
	// Check if the browser is IE4
	var ie4 = false;
	if(document.all) {
		ie4 = true;
	}
	
	// Get an object by ID
	function getObject(id) {
		if (ie4) {
			return document.all[id];
		}
		else {
			return document.getElementById(id);
		}
	}

	var deptCountrySize = <%=deptCountryList.size() + 1%>;
	var deptCode = new Array(deptCountrySize);
	var deptName = new Array(deptCountrySize);
	var countryCode = new Array(deptCountrySize);
	var countryName = new Array(deptCountrySize);
	
	<%
	if(deptCountryList != null) {
		if(deptCountryList.size() != 0) {
			DepartmentCountryAssociativityObject obj = new DepartmentCountryAssociativityObject();
			int i=0;
			for(Iterator itr=deptCountryList.iterator(); itr.hasNext(); i++) {
				obj = (DepartmentCountryAssociativityObject) itr.next();
	%>
				deptCode[<%=i+1%>] = "<%=obj.getDeptCode()%>";
				deptName[<%=i+1%>] = "<%=obj.getDeptDesc()%>";
				countryCode[<%=i+1%>] = "<%=obj.getCountryCode()%>";
				countryName[<%=i+1%>] = "<%=obj.getCountryDesc()%>";
	<%
			}
		}
	}
	%>
	
	// Once the Country select box is changed, JavaScript will look for dept(s) matching the selected country, 
	// and re-populate the Dept select box
	function setDeptCountryChange(){
		<c:set var="formWidgetName" value="${widgets['vacancyTemp.Form'].absoluteName}" />
		<c:set var="deptWidgetName" value="${widgets['vacancyTemp.Form'].sbDepartment.absoluteName}" />
		<c:set var="countryWidgetName" value="${widgets['vacancyTemp.Form'].sbCountry.absoluteName}" />
		
		var deptSelectBox = document.forms['<c:out value="${formWidgetName}"/>'].elements['<c:out value="${deptWidgetName}"/>'];
		var countrySelectBox = document.forms['<c:out value="${formWidgetName}"/>'].elements['<c:out value="${countryWidgetName}"/>'];
		var selectedValue = deptSelectBox.options[deptSelectBox.selectedIndex].value;
		
		// Clear off all the items in Dept select box
		for(i=deptSelectBox.options.length - 1; i>=0; i--){
			deptSelectBox.options[i] = null;
		}
		
		deptSelectBox.selectedIndex = -1;
		
		// Re-populate the Dept select box based on the selected Country
		var selectedCountry = countrySelectBox.options[countrySelectBox.selectedIndex].value;
		
		deptSelectBox.options[deptSelectBox.options.length] = new Option("<fmt:message key="recruit.general.hierachy.selectDept"/>", "---");
		if(selectedCountry != ""){
			for(i=1; i<=deptCountrySize; i++){
				if(selectedCountry == countryCode[i]){
				        if(deptCode[i] == selectedValue) {
						    deptSelectBox.options[deptSelectBox.options.length] = new Option(deptName[i], deptCode[i], true);
						    deptSelectBox.options[deptSelectBox.options.length - 1].selected = true;
    					}
    					else {
    						deptSelectBox.options[deptSelectBox.options.length] = new Option(deptName[i], deptCode[i]);
    					}
				}
			}
		}
	}
</script>

<c:set var="bodyTitle" scope="request">
<fmt:message key="recruit.menu.label.recruit"/> > <fmt:message key="recruit.menu.label.addVacancyTemplate"/> 
</c:set>

<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

<x:display name="vacancyTemp.Form"/>

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>

<script type="text/javascript">
setDeptCountryChange();

<c:if test="${hierachyRecovered eq 'true'}">
	var hierachyInitializationMsg = getObject('hierachyInitializationMsg');
	hierachyInitializationMsg.style.display = "block";
	hierachyInitializationMsg.innerHTML = "<fmt:message key="orgChart.hierachy.alert.deletedHierachyReinitialized"/>";
</c:if>
</script>

<%@ include file="/ekms/includes/footer.jsp" %>