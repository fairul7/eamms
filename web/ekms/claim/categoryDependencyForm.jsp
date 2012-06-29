<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_category">
   <com.tms.hr.claim.ui.DependencyForm name="dependency"/>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

<c:if test="${!empty param.id}">
<x:set name="jsp_category.dependency" property="categoryId" value="${param.id}"/>
</c:if>

<c:if test="${forward.name=='success'}">
    <script>
        alert("Dependencies added!");
        document.location="<c:url value="/ekms/claim/config_category.jsp" />";
    </script>
</c:if>
<c:if test="${forward.name=='fail'}">

</c:if>
<c:if test="${forward.name=='cancel'}">
    <c:redirect url="config_category.jsp"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
 <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">

			Category Dependencies


			</font></b></td>
 <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

    <tr><td colspan="2" valign="TOP"  class="contentBgColor" bgcolor="#00336F" >

	</td></tr>
    <tr><td colspan="2" valign="TOP"  class="contentBgColor" bgcolor="#00336F" >

<x:display name="jsp_category.dependency"/>

	</td></tr>


    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

</x:permission>    

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
