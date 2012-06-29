 <%@ include file="/common/header.jsp" %>
 <%@ page import="kacang.services.security.User,
 				com.tms.fms.department.model.FMSDepartmentManager,
 				kacang.Application,
 				java.util.*"
 %>
 
 
 
 <c:set var="form" value="${widget}"/>
 

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
                
    <c:set var="user" value="${widget.user}" scope="page"/>
     <%
     User user = new User();     
     user = (User)pageContext.getAttribute("user");
     FMSDepartmentManager manager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
     
     String userName = null;
     String firstName = null;
     String lastName = null;
     String dept = null;
     String unit = null;
     String designation = null;
     Map map = new HashMap();
     String email = null;
     String staffID = null;
     String telMobile = null;
     String telOffice = null;
     
     try{
    	 userName = user.getUsername();
    	 firstName = (String) user.getProperty("firstName");
         lastName = (String) user.getProperty("lastName");
         designation = (String) user.getProperty("designation");         
         email = (String) user.getProperty("email1");
    	 map = manager.getDeptUnitUser(user.getId());
    	 dept = (String) map.get("department");
    	 unit = (String) map.get("unit");
    	 staffID = (String) user.getProperty("staffID");
    	 telMobile = (String) user.getProperty("telMobile");
    	 telOffice = (String) user.getProperty("telOffice");
    	 System.out.print(dept);
    	 System.out.print(unit);
     
     }catch(Exception er){System.out.print("Error ON editregister.jsp:"+er);}
     
     
          
     %>
     <tr align="center" width = "100%">
                <td colspan="2" align="left" width = "100%" class="profileRow">
                    <B><fmt:message key='fms.label.personalInfo'/>&nbsp;</B>
                </td>
            </tr>
            
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top">&nbsp;</td>
        <td class="profileRow">
            
            &nbsp;
        </td>
    </tr>
       
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.label.userName'/>&nbsp; </b><FONT class="classRowLabel"></FONT></td>
        <td class="profileRow">
            
            <%=userName %>
        </td>
    </tr>
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.label.firstName'/>&nbsp; </b><FONT class="classRowLabel"></FONT></td>
        <td class="profileRow">
            
            <%=firstName %>
        </td>
    </tr>
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.label.lastName'/>&nbsp; </b><FONT class="classRowLabel"></FONT></td>
        <td class="profileRow">
            
           <%=lastName %>
        </td>
    </tr>
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.label.department'/>&nbsp; </b><FONT class="classRowLabel"></FONT></td>
        <td class="profileRow">
            
             <%=dept %>
        </td>
    </tr>
    
	<tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.label.unit'/>&nbsp; </b><FONT class="classRowLabel"></FONT></td>
        <td class="profileRow">
            
             <%=unit %>
        </td>
    </tr>
    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.label.designation'/>&nbsp; </b><FONT class="classRowLabel"></FONT></td>
        <td class="profileRow">
            
             <%=designation %>
        </td>
    </tr>

    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.label.staffID'/>&nbsp; </b><FONT class="classRowLabel"></FONT></td>
        <td class="profileRow">
             <%=staffID%>
        </td>
    </tr>    
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.label.mobileNo'/>&nbsp; </b><FONT class="classRowLabel"></FONT></td>
        <td class="profileRow">
             <%=telMobile%>
        </td>
    </tr>
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.label.officeNoExt'/>&nbsp; </b><FONT class="classRowLabel"></FONT></td>
        <td class="profileRow">
             <%=telOffice%>
        </td>
    </tr>
       
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"><b><fmt:message key='fms.label.mediaPrimaEmailAdd'/>&nbsp; </b><FONT class="classRowLabel"></FONT></td>
        <td class="profileRow">
            
            <%=email %>
        </td>
    </tr>
  
   <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top">&nbsp;</td>
        <td class="profileRow">
            
            &nbsp;
        </td>
    </tr>
        
    <tr>
        <td  class="profileRow" width="23%" height="20" valign="top" align="right"  valign="top"></FONT></td>
        <td class="classRow">
            <x:display name="${form.cancelButton.absoluteName}" />
        </td>
    </tr>
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>
