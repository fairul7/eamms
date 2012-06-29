<%@ include file="/common/header.jsp" %>
<c:set var="userWidget" value="${widgets['user.userPortlet.userForm.editUser']}"/>
<c:if test="${!empty userWidget && !userWidget.hidden}">
    <script>
    <!--
        function contentSecurityPopup() {
            window.open('<c:url value="/cmsadmin/content/contentSecurityPopup.jsp"/>?principalId=<c:out value="${userWidget.id}"/>', 'contentSecurityPopup', 'address=no,scrollbars=yes,status=yes');
        }
    //-->
    </script>
</c:if>
<c:set var="groupWidget" value="${widgets['group.groupPortlet.groupForm.editGroup']}"/>
<c:if test="${!empty groupWidget && !groupWidget.hidden}">
   <script>
   <!--
	   function contentSecurityGroupPopup() {
		   window.open('<c:url value="/cmsadmin/content/contentSecurityPopup.jsp"/>?principalId=<c:out value="${groupWidget.id}"/>&group=true', 'contentSecurityPopup', 'address=no,scrollbars=yes,status=yes');
	   }
   //-->
   </script>
</c:if>