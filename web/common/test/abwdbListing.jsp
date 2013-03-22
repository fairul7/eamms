<%@ include file="/common/header.jsp" %>

<link rel="stylesheet" href="/ekms/images/fms2008/default.css" type="text/css">
<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" var="hasPermission" />
<c:if test="${not hasPermission}">
	<script>
		location = "/ekms/login.jsp";
	</script>
	<%
		if (1 != 2) return;
	%>
</c:if>

<!-- Declare Widgets -->

<x:config>
    <page name="abwdb">
          	<com.tms.fms.abw.ui.AbwProjectTable name="abw_project"/>         
          	<com.tms.fms.abw.ui.AbwTableContent name="abwTable"/>       
                 
    </page>
</x:config>
 <c:if test="${not empty (param.namax) }">	
<x:set name="abwdb.abwTable" property="tableName" value="${ param.namax}" />
<x:set name="abwdb.abwTable" property="sort" value="" />
<x:set name="abwdb.abwTable" property="desc" value="${ false }" />

</c:if>
 <!-- Display Page -->
 
 <div id="content" >
 <script>
 	var showListing = true;
	function toggleListing() {
		if (showListing) {
			document.getElementById("side_menu").style.display = "none";
			showListing = false;
		} else {
			document.getElementById("side_menu").style.display = "block";
			showListing = true;
		}
	}
 </script>
 
  <table>
  <tr>
	  <td valign="top" id="side_menu"><x:display name="abwdb.abw_project"/></td>
	  <td valign="top">
	  <b><c:out value="${param.namax}" /></b>
	  &nbsp;<a href="javascript: toggleListing();">toggle Table Listing</a>
	  <x:display name="abwdb.abwTable"/> </td>
  </tr>
  </table>
    
 </div>
