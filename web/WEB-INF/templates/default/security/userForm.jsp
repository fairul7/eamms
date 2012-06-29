<%@ page import="java.util.ArrayList,
				 kacang.Application,
				 java.util.Collection,
				 java.util.Iterator,
				 kacang.ui.WidgetManager,
		         com.tms.fms.department.model.*,
		         kacang.services.security.ui.BaseUserForm" %>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>

<c:choose>
	<c:when test="${widget.type == 'edit'}">
		<c-rt:set var="type" value="edit" />
	</c:when>
	<c:otherwise>
		<c-rt:set var="type" value="add" />
	</c:otherwise>
</c:choose>

<%
	Collection lstUnit = null;
	String unitId = "";
	FMSDepartmentDao dao = (FMSDepartmentDao) Application.getInstance().getModule(FMSDepartmentManager.class).getDao();
	
	try{
		lstUnit = dao.selectUnit();
	} catch (Exception e){}
	

%>

<script language="JavaScript">
	var lstSize = <%=lstUnit.size() + 1%>;
	var lstDepartmentId = new Array(lstSize);
	var lstUnitId = new Array(lstSize);
	var lstUnitName = new Array(lstSize);
<c:choose>
	<c:when test="${!(empty param.id)}">
		var unitId = '<c:out value="${widget.unitId}"/>';
	</c:when>
	<c:otherwise>
		var unitId = '0';
	</c:otherwise>
</c:choose>
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
		var departmentSelectBox = document.forms['user.userPortlet.userForm.<c:out value="${type}"/>User'].elements['user.userPortlet.userForm.<c:out value="${type}"/>User.selecttype'];
		var unitSelectBox = document.forms['user.userPortlet.userForm.<c:out value="${type}"/>User'].elements['user.userPortlet.userForm.<c:out value="${type}"/>User.unit'];
		var selected = 0;
		
		for(i=unitSelectBox.options.length - 1; i>=0; i--){
			unitSelectBox.options[i] = null;
		}
		
		unitSelectBox.selectedIndex = 0;
		
		var selectedDepartment = departmentSelectBox.options[departmentSelectBox.selectedIndex].value;
		
		unitSelectBox.options[unitSelectBox.options.length] = new Option("--- NONE ---", "-1");
		if(selectedDepartment != ""){
			x=0;
			for(i=0; i<lstSize; i++){
				
				if(selectedDepartment == lstDepartmentId[i]){
					x++;
					unitSelectBox.options[unitSelectBox.options.length] = new Option(lstUnitName[i], lstUnitId[i]);
					
					if (lstUnitId[i] == unitId){
						selected = x;
					}
				}
			}
			if (unitId != '0' && unitSelectBox.selectedIndex != '-1'){
				unitSelectBox.selectedIndex = selected;
			}
		}
		
	}

</script>

<script>
	window.onload=setDepartmentChange;
</script>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	<tr>
		<td>
                        <jsp:include page="../form_header.jsp" flush="true"></jsp:include>
			<table width="100%" cellpadding="3" cellspacing="1">
			
			
			
			
			
			

<c:if test="${!(empty widget.id)}">
    <c:if test="${!(empty widget.message)}">
        <script>alert("<c:out value="${widget.message}"/>");</script>
    </c:if>
    
 		<%-- 
        <tr><td colspan="2"><span style="font-family:Arial; font-size:16px; font-weight:bold"><fmt:message key="security.label.personalInformation"/></span></td></tr>
        --%>
        
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.firstName"/>&nbsp;*</td>
            <td class="classRow"><x:display name="${widget.firstName.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.lastName"/>&nbsp;*</td>
            <td class="classRow"><x:display name="${widget.lastName.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.email"/>&nbsp;*</td>
            <td class="classRow"><x:display name="${widget.email.absoluteName}"/></td>
        </tr>

        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="fms.label.staffID"/></td>
            <td class="classRow"><x:display name="${widget.staffID.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.mobilePhone"/></td>
            <td class="classRow"><x:display name="${widget.telMobile.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="fms.label.officeNoExt"/></td>
            <td class="classRow"><x:display name="${widget.telOffice.absoluteName}"/></td>
        </tr>

        <tr><td class="contentTitleFont" colspan="2" >
        	<%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
        	<fmt:message key="security.label.contactInformation"/>
        	<%-- /span --%>
        </td></tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.address"/></td>
            <td class="classRow"><x:display name="${widget.address.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.postcode"/></td>
            <td class="classRow"><x:display name="${widget.postcode.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.city"/></td>
            <td class="classRow"><x:display name="${widget.city.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.state"/></td>
            <td class="classRow"><x:display name="${widget.stateAddress.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.country"/></td>
            <td class="classRow"><x:display name="${widget.country.absoluteName}"/></td>
        </tr>
<%-- 
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.officePhone"/></td>
            <td class="classRow"><x:display name="${widget.telOffice.absoluteName}"/></td>
        </tr>
--%>        
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.homePhone"/></td>
            <td class="classRow"><x:display name="${widget.telHome.absoluteName}"/></td>
        </tr>
<%-- 
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.mobilePhone"/></td>
            <td class="classRow"><x:display name="${widget.telMobile.absoluteName}"/></td>
        </tr>
--%>        
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.fax"/></td>
            <td class="classRow"><x:display name="${widget.fax.absoluteName}"/></td>
        </tr>
        <tr><td class="contentTitleFont" colspan="2">
        	<%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
        	<fmt:message key="security.label.authenticationInformation"/>
        	<%-- span --%></td></tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.username"/>*</td>
            <td class="classRow"><x:display name="${widget.username.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.password"/>*</td>
            <td class="classRow"><x:display name="${widget.password.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.confirmPassword"/>*</td>
            <td class="classRow"><x:display name="${widget.confirmPassword.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="classRowLabel">&nbsp;</td>
            <td class="classRow"><x:display name="${widget.active.absoluteName}"/><fmt:message key="security.label.active"/></td>
        </tr>
        
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="fms.label.department"/>*</td>
            <td class="classRow"><x:display name="${widget.department.absoluteName}"/></td>
        </tr>      
        
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="fms.label.unit"/></td>
            <td class="classRow"><x:display name="${widget.unit.absoluteName}"/></td>
        </tr>         
    
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="security.label.groups"/></td>
            <td class="classRow"><x:display name="${widget.groups.absoluteName}"/></td>
        </tr>
        <tr><td class="classRowLabel" colspan="2" align="right">&nbsp;</td></tr>
        <tr>
            <td class="classRowLabel">&nbsp;</td>
            <td class="classRow"><x:display name="${widget.userButton.absoluteName}"/> <x:display name="${widget.cancelButton.absoluteName}"/></td>
        </tr>
</c:if>






<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>
			</table>
			
		</td>
	</tr>
</table>

</td>
</tr>	
</table>




