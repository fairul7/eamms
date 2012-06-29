 <%@ page import="java.util.ArrayList,
				 kacang.Application,
				 java.util.Collection,
				 java.util.Iterator,
				 kacang.ui.WidgetManager,
				 com.tms.fms.transport.ui.DriverAssignmentForm"%>
				 
 <%@include file="/common/header.jsp" %>

<x:config>
     <page name="driverAssg">
        <com.tms.fms.transport.ui.DriverAssignmentForm name="form"/>		
     </page>
</x:config>


<x:set name="driverAssg.form" property="mode" value="<%= DriverAssignmentForm.VIEW_MODE %>"/>

<c:set var="id" value="${widgets['driverAssg.form'].id}"></c:set>


<c:if test="${forward.name == 'Back'}" >
  
  <c:redirect url="myAssignment.jsp"/>
    
</c:if>

<c:if test="${forward.name == 'Reassign'}">
    
  <script>
     document.location="reAssignment.jsp?id=<c:out value="${id}"/>";
  </script>
  
</c:if>

<c:if test="${forward.name == 'Completed'}">    
  <script>
     document.location="completeAssignment.jsp?id=<c:out value="${id}"/>";
  </script>  
</c:if>

<c:if test="${forward.name == 'BackToSearchRoster'}" >
  
  <c:redirect url="searchroster.jsp"/>
    
</c:if>

<c:if test="${forward.name == 'BackToMyRoster'}" >
  
  <c:redirect url="mydutyroster.jsp"/>
    
</c:if>

  
 <%@include file="/ekms/includes/header.jsp" %> 
  
<jsp:include page="includes/header.jsp" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">

  <tr valign="MIDDLE">
    <td height="22" class="contentTitleFont">
      &nbsp;<fmt:message key='fms.label.transport.assgDetails'/>  
      
    </td>
    <td align="right" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
    <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
	
</td>
  </tr>
	<c:if test="${! empty param.id}">
	  <tr>
	    <td colspan="2" valign="TOP" class="contentBgColor">
			<x:display name="driverAssg"> </x:display>
		</td>
	  </tr>
	</c:if>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>

  
</table>
<jsp:include page="includes/footer.jsp" />



<%@include file="/ekms/includes/footer.jsp" %>