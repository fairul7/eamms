<%@ page import="kacang.services.security.ui.Profile,
                 com.tms.hr.competency.ui.UserCompetencyForm,
                 com.tms.fms.department.model.*,
                 java.util.Collection,
                 java.util.Iterator"%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="profile">
        <kacang.services.security.ui.Profile name="profileForm"/>
    </page>
</x:config>
<c-rt:set var="forward_success" value="<%= Profile.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_cancel" value="<%= Profile.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_competency_add" value="<%= UserCompetencyForm.FORWARD_ADD %>"/>
<c:if test="${forward_success == forward.name}">
    <script>
        alert("<fmt:message key='general.label.profileUpdated'/>");
    </script>
</c:if>
<c:if test="${forward.name == forward_competency_add}">
    <script>
        window.open("<c:url value="/ekms/worms/addUserCompetency.jsp"/>", "profileWindow", "height=300,width=400,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>
<c:if test="${forward_cancel == forward.name}">
    <c:redirect url="/ekms/index.jsp"/>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<table width="100%" border="0" cellpadding="5" cellspacing="0">
    <tr><td align="center"><x:display name="profile.profileForm"/></td></tr>
</table>

<%-- 
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
		var departmentSelectBox = document.forms['profile.profileForm'].elements['profile.profileForm.sbDepartment'];
		
		var unitSelectBox = document.forms['profile.profileForm'].elements['profile.profileForm.sbUnit'];
		
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
</script>
--%>

<%@ include file="/ekms/includes/footer.jsp" %>
