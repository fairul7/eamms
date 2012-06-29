<%@ page import="com.tms.portlet.taglibs.PortalServerUtil" %>
<%@ page import="java.util.Random" %>
<%@ include file="/common/header.jsp" %>

<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>

<x:config>
    <page name="draft">

        <com.tms.hr.claim.ui.PopDraftEdit name="edit" width="100%">
        </com.tms.hr.claim.ui.PopDraftEdit>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

    <c:if test="${forward.name=='sameNameExist'}">
        <script>

            alert("<fmt:message key='claims.label.purpose.same'/>");
        </script>
    </c:if>

    <c:choose>

        <c:when test="${forward.name == 'submit'}" >
            <script>
                window.opener.location.reload();

                alert("<fmt:message key='claims.label.updated'/>");

            </script>

        </c:when>
        <c:when test="${forward.name == 'fail'}" >

            <script>

                alert("<fmt:message key='claims.label.value.toobig'/>")
            </script>
        </c:when>
    </c:choose>

    <c:if test="${!empty param.id}">

        <x:set name="draft.edit" property="id" value="${param.id}"></x:set>

    </c:if>

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="claims.label.claimListing"/> > <fmt:message key="claims.label.editExpensesItem"/>

            </font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img
                src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

            <x:display name="draft.edit"/>

        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img
                src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>

</x:permission>


