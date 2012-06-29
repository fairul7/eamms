
 <%@include file="/common/header.jsp" %>

<x:config >
     <page name="rview">
        <com.tms.collab.resourcemanager.ui.ResourceView name="rv"/>
     </page>
</x:config>

<c:if test="${forward.name == 'View All'}" >
  <script>
     document.location= "resourcemanager.jsp";
  </script>
</c:if>


<c:if test="${param.et =='approve'}" >
    <script>
        window.open("bookingdetail.jsp?instanceId=<c:out value="${param.instanceId}"/>&resourceId=<c:out value="${param.resourceId}"/>&eventId=<c:out value="${param.eventId}"/>","","height=350,width=500,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    </script>
</c:if>



  <%@include file="/ekms/includes/header.jsp" %>
  
  
  

<jsp:include page="includes/header.jsp" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">

  <tr valign="MIDDLE">
    <td height="22" class="contentTitleFont">
      &nbsp;<fmt:message key='resourcemanager.label.resourceListing'/> > 
      <fmt:message key='resourcemanager.label.resourceView'/>
    </td>
    <td align="right" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
    <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
<x:display name="rview" />
</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
  
  
</table>
<jsp:include page="includes/footer.jsp" />



<%@include file="/ekms/includes/footer.jsp" %>
