<%@include file="/common/header.jsp"%>
<x:config >
     <page name="editresourceform">
        <com.tms.collab.resourcemanager.ui.ResourceInfoForm name="resourceform"/>
     </page>
</x:config>
<%-- Initializing Widget --%>
<c:if test="${!(empty param.resourceId)}">
    <x:set name="editresourceform.resourceform" property="id" value="${param.resourceId}"/>
</c:if>
<%-- Event Handling --%>
<c:if test="${forward.name=='cancel'}" >
    <script>
        document.location = "<c:url value="/ekms/resourcemanager/"/>resourcemanager.jsp";
    </script>
</c:if>
<c:if test="${forward.name == 'updated'}">
    <script>
        alert("<fmt:message key='resourcemanager.label.resourceUpdated'/>");
       document.location = "<c:url value="/ekms/resourcemanager/"/>resourceview.jsp?id=<c:out value="${id}" />";
    </script>
</c:if>

<c:if test="${forward.name == 'resource added'}" >
    <script>
        alert("<fmt:message key='resourcemanager.label.resourceAdded'/>");
       document.location = "<c:url value="/ekms/resourcemanager/"/>resourceview.jsp?id=<c:out value="${id}" />";
    </script>

</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='resourcemanager.label.resourceListing'/> > <fmt:message key='resourcemanager.label.Edit'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
 <x:display name="editresourceform" ></x:display>
 </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="calendarFooter">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
