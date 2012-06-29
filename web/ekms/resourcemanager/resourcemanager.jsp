<%@ page import="java.util.Properties"%>
<%@include file="/common/header.jsp" %>
<x:config >
    <page name="resourcemanager">
        <com.tms.collab.resourcemanager.ui.ResourceTable name="resourcetable" popupViewUrl="taskview.jsp"/>
    </page>

</x:config>

<c:if test="${forward.name=='AddResource'}" >
<script>
    document.location="<c:url value="/ekms/" />resourcemanager/addresourceform.jsp";
</script>

 </c:if>

 <c:if test="${!empty param.id}">
   <c:redirect url="/ekms/resourcemanager/resourceview.jsp?id=${param.id}" />

     <%--<script>
           document.location="<c:url value="" />resourcemanager/resourceview.jsp?id=<c:out value="${param.id}"/> ";
     </script>
--%>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='resourcemanager.label.resourceManager'/> > <fmt:message key='resourcemanager.label.resourceListing'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
<x:display name="resourcemanager" />
 </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="calendarFooter">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table><jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
