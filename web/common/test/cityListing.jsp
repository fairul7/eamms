<%@ include file="/common/header.jsp" %>

<!-- Declare Widgets -->
<x:config>
    <page name="cityListing">    
    	<sul.school.city.ui.CityTable name="table" width="100%" />
    </page> 
 </x:config>
<x:permission permission="sul.school.admin" module="sul.school.city.model.cityModule" var="schoolAdmin" />    
	<c:if test="${!schoolAdmin}">
  		<script>
  				location = "index.jsp";
  		</script>
  	</c:if> 

<c:if test="${not empty(param.cityId) }">
	<script>   
		location = 'cityEdit.jsp?cityId=<c:out value ="${param.cityId}" />';
	</script>  
</c:if>

<c:if test="${forward.name =='add' }">
	<script>   
		location = "cityAdd.jsp";
	</script>
</c:if>

 <!-- Display Page -->
 
 <div id="content" >
<h1 >List of city</h1>
  <x:display name="cityListing.table"/>
  </div>
