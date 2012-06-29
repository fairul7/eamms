<%@ page import="java.util.ArrayList,
				 kacang.Application,
				 java.util.Collection,
				 java.util.Iterator,
				 kacang.ui.WidgetManager,
		         com.tms.fms.register.ui.FMSRegister,
		         com.tms.fms.department.model.*" %>
		         
<%@include file="/common/header.jsp"%>
<x:config>
	<page name="register">
		<com.tms.fms.register.ui.FMSRegister name="form"/>
	</page>
</x:config>

<c:if test="${forward.name == 'AD Exist'}" >
  <script>
     alert("Your registration was successful and pending for approval  ");
     document.location="ekms/login.jsp";	
  </script>
  
</c:if>

<c:if test="${forward.name == 'AD Not Exist'}" >
  <script>
     alert("Username does not exist in Active Directory Users ");
  </script>
</c:if>

<c:if test="${forward.name == 'Cancel'}" >
  <script>
     document.location ="ekms/login.jsp";
  </script>
</c:if>

<c:if test="${forward.name == 'Registered'}" >
  <script>
     alert("This user is already registered");
  </script>
</c:if>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title><fmt:message key="general.label.ekp"/></title>

<link rel="stylesheet" href="/ekms/images/fms2008/default.css">

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
		
		var departmentSelectBox = document.forms['register.form'].elements['register.form.sbDepartment'];
		var unitSelectBox = document.forms['register.form'].elements['register.form.sbUnit'];
		
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
		var radiobtn = document.forms['register.form'].elements['register.form.pnParent.parentGroup'];
		var tr = document.getElementById("category");

		if(radiobtn[0].checked){
			tr.style.display = 'none';
		}else{
			tr.style.display = '';
		}
			
	}
</script>
</head>




<body leftmargin="0" topmargin="0" rightmargin="0" marginwidth="0" marginheight="0" class="bg">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td height="135" class="header_bg">
		<!-- Header -->
		<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
      	<tr>
        <td width="40" height="104"><img src="/ekms/images/spacer.gif" alt="" width="40" height="1"></td>
        <td width="140"><img src="/ekms/images/fms2008/logo.png" width="135" height="62"></td>
        <td class="header_title">Facility Management System</td>
        
        <td width="20"><img src="images/spacer.gif" alt="" width="20" height="1"></td>
      	</tr>
    	</table>
      	<!-- Header End -->
        
</td>
</tr>
</table>
<!-- Content -->
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>&nbsp;</td>
</tr>

<tr>
<td>&nbsp;</td>
</tr>
<tr>

<tr>
<td>&nbsp;</td>
</tr>
<tr>

<td width="100%" align="left" valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tbody>
        <tr>
            <td style="vertical-align: top; width: 0px;">      <!-- space -->          
            </td>
            <td style="vertical-align: top;">
                        
			<table width="100%" border="0" cellspacing="0" cellpadding="0">	
					<tr valign="middle">
						<td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;	<fmt:message key='fms.label.registerAsFMS'/></font></b></td>
						<td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td></tr>
						<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
						<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>
						<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><x:display name="register.form"/> </td></tr>
						<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td></tr>								
			</table>
		</td>
        </tr>
    </tbody>
	  </table>
    </td>



<!---- Footer --->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td height="58" align="center" class="footer">Copyright &copy; 2008 Media Prima Berhad</td>
</tr>
</table>

<script language="JavaScript">
	hideShowCategory();
</script>

<!--- Footer End --->
</body>
</html>
