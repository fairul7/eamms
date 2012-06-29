<%@include file="/common/header.jsp"%>
<x:permission permission="com.tms.assetmanagement.accessAssetPermission" module="com.tms.assetmanagement.model.AssetModule" url="noPermission.jsp">

<x:config>
    <page name="assetNotification">
     	<com.tms.assetmanagement.ui.AssetNotificationForm name="form" width="100%" />
    </page>
</x:config>

<!-------- Forward Message---------->
 <c:if test="${!empty param.strNumbOfNotification}">
      <x:set name="assetNotification.form" property="strNumbOfNotification" value="${param.strNumbOfNotification}"></x:set>
      <x:set name="assetNotification.form" property="strNotification" value="false"></x:set>
 </c:if>
 
  <c:if test="${!empty param.strNumb}">
      <x:set name="assetNotification.form" property="strNumbOfNotification" value="${param.strNumb}"></x:set>
 </c:if>
 
<c:if test="${forward.name=='inserted'}" >
 <script> window.close(); </script>   
</c:if>

<c:if test="${forward.name=='addNotification'}" >
 <script> window.close(); </script>   
</c:if>

<c:if test="${forward.name=='cancel'}" >
<script>
  window.close();
 </script>
</c:if>

	<jsp:include page="includes/header.jsp"/>
	<head>
	<title>Asset Notification</title>
	<link rel="stylesheet" href="/ekms/images/ekp2005/default.css">
	</head>
	<table width="100%" border="0" cellspacing="0" cellpadding="5">         
		<x:display name="assetNotification.form" />        
	</table>   

</x:permission> 
 
    <jsp:include page="includes/footer.jsp"/>