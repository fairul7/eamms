<%@ page import="com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject,
				com.tms.hr.orgChart.model.OrgChartHandler,
				com.tms.hr.orgChart.ui.StaffHierachyForm,
				java.util.Collection,
				java.util.Iterator,
				kacang.Application"%>
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
		<com.tms.hr.orgChart.ui.StaffHierachyForm name="staffForm"/>
    </page>
</x:config>

<c:choose>
	<c:when test="${!empty param.userId}">
		<x:set name="orgChart.staffForm" property="userId" value="${param.userId }" />
	</c:when>
	<c:otherwise>
		<x:set name="orgChart.staffForm" property="userId" value="" />
	</c:otherwise>
</c:choose>

<c-rt:set var="forward_saved" value="<%=StaffHierachyForm.FORWARD_SAVED%>"/>
<c-rt:set var="forward_exist" value="<%=StaffHierachyForm.FORWARD_EXIST%>"/>
<c-rt:set var="forward_updated" value="<%=StaffHierachyForm.FORWARD_UPDATED%>"/>
<c-rt:set var="forward_recovered" value="<%=StaffHierachyForm.FORWARD_RECOVERED%>"/>

<c:if test="${forward.name=='cancel_form_action'}">
    <c:redirect url="listHierachy.jsp"/>
</c:if>

<c:if test="${forward.name==forward_saved}">
    <script type="text/javascript">
        alert("<fmt:message key="orgChart.hierachy.alert.saved"/>");
        document.location="listHierachy.jsp";
    </script>
</c:if>

<c:if test="${forward.name==forward_exist}">
    <script type="text/javascript">
        alert("<fmt:message key="orgChart.hierachy.alert.exist"/>");
        document.location="listHierachy.jsp";
    </script>
</c:if>

<c:if test="${forward.name==forward_updated}">
    <script type="text/javascript">
        alert("<fmt:message key="orgChart.hierachy.alert.updated"/>");
        document.location="listHierachy.jsp";
    </script>
</c:if>

<c:if test="${forward.name==forward_recovered}">
    <script type="text/javascript">
        alert("<fmt:message key="orgChart.hierachy.alert.recovered"/>");
        document.location="listHierachy.jsp";
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
		<c:set var="formWidgetName" value="${widgets['orgChart.staffForm'].absoluteName}" />
		<c:set var="deptWidgetName" value="${widgets['orgChart.staffForm'].selDept.absoluteName}" />
		<c:set var="countryWidgetName" value="${widgets['orgChart.staffForm'].selCountry.absoluteName}" />
		
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
		
		deptSelectBox.options[deptSelectBox.options.length] = new Option("<fmt:message key="general.hierachy.selectDept"/>", "---");
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

<table cellpadding="0" cellspacing="0" width="100%">
	<tr valign="MIDDLE">
		<td height="22" bgcolor="#003366" class="contentTitleFont">
        <b>
            <font color="#FFCF63" class="contentTitleFont">
            &nbsp;
            <c:choose>
            <c:when test="${!empty param.userId}">
                <fmt:message key='orgChart.hierachy.label.editStaffHierachy'/>
            </c:when>
            <c:otherwise>
                <fmt:message key='orgChart.hierachy.label.addStaffHierachy'/>
			</c:otherwise>
			</c:choose>
			</font>
		</b>
		</td>
		<td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  	</tr>
  	
  	<tr>  		
  		<td class="contentBgColor">
  			<span id="hierachyInitializationMsg" style="margin:5px; font-weight:bold; display:none;"></span>
  		</td>
  		
  	</tr>
  	
	<tr>
        <td>
            <x:display name="orgChart.staffForm"/>
        </td>
    </tr>
</table>
<script type="text/javascript">
setDeptCountryChange();

<c:if test="${hierachyRecovered eq 'true'}">
	var hierachyInitializationMsg = getObject('hierachyInitializationMsg');
	hierachyInitializationMsg.style.display = "block";
	hierachyInitializationMsg.innerHTML = "<fmt:message key="orgChart.hierachy.alert.deletedHierachyReinitialized"/>";
</c:if>
</script>
<%@ include file="/ekms/includes/footer.jsp" %>