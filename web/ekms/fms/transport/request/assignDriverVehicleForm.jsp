<%@ include file="/common/header.jsp" %>


<x:config>
    <page name="transport">
        <com.tms.fms.transport.ui.TransportDriverVehicleForm name="assignvehicleform" />
    </page>
</x:config>

<c:if test="${!empty param.id}" >
    <x:set name="transport.assignvehicleform" property="assignmentId" value="${param.id}" />
</c:if>

<c:if test="${!empty param.userId}" >
    <x:set name="transport.assignvehicleform" property="userId" value="${param.userId}" />
</c:if>

<c:if test="${forward.name=='submit'}" >
    <script>
   		alert('<fmt:message key="fms.label.duty.recordAdded"/>');
   		window.opener.location.reload();
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='cancel'}" >
    <script>
        window.close();
    </script>
</c:if>

<%@include file="/ekms/includes/linkCSS.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">Assign Driver to Vehicle</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="transport.assignvehicleform"/>
    
    </td></tr>
</table>


