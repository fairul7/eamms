<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.leave.ManageLeaves" module="com.tms.cms.leave.model.LeaveModule" url="/ekms/index.jsp"/>

<x:config>
    <page name="globalSettings">
          <com.tms.hr.leave.ui.GlobalSetupForm name="form" />
    </page>
</x:config>

<c:choose>
    <c:when test="${forward.name == 'success'}">
        <script>
            alert('<fmt:message key="leave.label.settingsUpdated"/>');
        </script>
    </c:when>
    <c:when test="${forward.name == 'failure'}">
        <script>
            alert('<fmt:message key="leave.label.settingsFailed"/>');
        </script>
    </c:when>
    <c:when test="${forward.name == 'carryForwardSuccess'}">
        <script>
            alert('<fmt:message key="leave.label.carryForwardProcessed"/>');
        </script>
    </c:when>
    <c:when test="${forward.name == 'carryForwardFailed'}">
        <script>
            alert('<fmt:message key="leave.label.carryForwardFailed"/>');
        </script>
    </c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" class="contentTitleFont">
        <fmt:message key='leave.label.Administration'/> > <fmt:message key='leave.label.Global'/>
        </td>
        <td align="right" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" class="contentBgColor">

        <x:display name="globalSettings.form" ></x:display>

    </td></tr>
    <tr><td colspan="2" valign="TOP" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
