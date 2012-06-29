<%@include file="/common/header.jsp" %>
<%@ page import="com.tms.portlet.theme.themes.DefaultTheme,
                 com.tms.portlet.theme.ThemeManager"%>
<%-- Initializing Constants --%>
<c-rt:set var="leftColumn" value="<%= DefaultTheme.COLUMN_LEFT_LABEL %>"/>
<c-rt:set var="rightColumn" value="<%= DefaultTheme.COLUMN_RIGHT_LABEL %>"/>
<c-rt:set var="cmdUp" value="<%= DefaultTheme.COMMAND_MOVE_UP %>"/>
<c-rt:set var="cmdDown" value="<%= DefaultTheme.COMMAND_MOVE_DOWN %>"/>
<c-rt:set var="cmdLeft" value="<%= DefaultTheme.COMMAND_MOVE_LEFT %>"/>
<c-rt:set var="cmdRight" value="<%= DefaultTheme.COMMAND_MOVE_RIGHT %>"/>
<c-rt:set var="cmdMaximize" value="<%= DefaultTheme.COMMAND_MAXIMIZE %>"/>
<c-rt:set var="cmdEdit" value="<%= DefaultTheme.COMMAND_EDIT %>"/>
<c-rt:set var="cmdClose" value="<%= DefaultTheme.COMMAND_CLOSE %>"/>
<c-rt:set var="keyLabel" value="<%= ThemeManager.LABEL_ENTITY_KEY %>"/>
<%-- Initializing Variables --%>
<c:set var="entity" value="${requestScope.entity}"/>
<c:set var="status" value="${requestScope.columnStatus}"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" class="contentTitleFont" bgcolor="E5E5E5" nowrap><b>&nbsp;<c:out value="${entity.portlet.portletTitle}"/></b></td>
        <td align="right" class="contentTitleFont" bgcolor="E5E5E5" nowrap>
            <c:if test="${entity.entityLocation != leftColumn}">
                <input type="button" value="<" class="themeButton" onClick="document.location='<%= response.encodeURL(request.getRequestURI()) %>?<c:out value="cn=${requestScope.themeManager.absoluteName}&et=${cmdLeft}&${keyLabel}=${entity.entityId}"/>';">
            </c:if>
            <c:if test="${entity.entityLocation != rightColumn}">
                <input type="button" value=">" class="themeButton" onClick="document.location='<%= response.encodeURL(request.getRequestURI()) %>?<c:out value="cn=${requestScope.themeManager.absoluteName}&et=${cmdRight}&${keyLabel}=${entity.entityId}"/>';">
            </c:if>
            <c:if test="${! status.first}">
                <input type="button" value="^" class="themeButton" onClick="document.location='<%= response.encodeURL(request.getRequestURI()) %>?<c:out value="cn=${requestScope.themeManager.absoluteName}&et=${cmdUp}&${keyLabel}=${entity.entityId}"/>';">
            </c:if>
            <c:if test="${! status.last}">
                <input type="button" value="v" class="themeButton" onClick="document.location='<%= response.encodeURL(request.getRequestURI()) %>?<c:out value="cn=${requestScope.themeManager.absoluteName}&et=${cmdDown}&${keyLabel}=${entity.entityId}"/>';">
            </c:if>
            <c:if test="${!(empty entity.preferences.maximize)}">
                <input type="button" class="themeButton" value="Detach" onClick="window.open('<c:out value="${entity.preferences['maximize'].preferenceValue}"/>');">
            </c:if>
            <c:if test="${!(empty entity.preferences.edit)}">
                <c:choose>
                    <c:when test="${empty entity.preferences.edit.preferenceValue}">
                        <c:set var="editUrl"><c:url value="/ekms/portalserver/entityPreferences.jsp"/>?entityId=<c:out value="${entity.entityId}"/></c:set>
                    </c:when>
                    <c:otherwise>
                        <c:set var="editUrl"><c:out value="${entity.preferences.edit.preferenceValue}"/>?entityId=<c:out value="${entity.entityId}"/></c:set>
                    </c:otherwise>
                </c:choose>
                <input type="button" class="themeButton" value="<fmt:message key='portlet.label.edit'/>" onClick="portletEdit('<c:out value="${editUrl}"/>');">
            </c:if>
            <input type="button" value="-" class="themeButton" onClick="blockToggle('<c:out value="${entity.entityId}"/>');" name="toggle_<c:out value="${entity.entityId}"/>" id="toggle_<c:out value="${entity.entityId}"/>">
            <input type="button" value="X" class="themeButton"
                onClick="
                    <c:choose>
                        <c:when test="">
                            window.open(<c:out value="${entity.preferences.edit.preferenceValue}?${keyLabel}=${entity.entityId}"/>, 'entityPreferenceWindow', 'height=200,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');
                        </c:when>
                        <c:otherwise>
                            document.location='<%= response.encodeURL(request.getRequestURI()) %>?<c:out value="cn=${requestScope.themeManager.absoluteName}&et=${cmdClose}&${keyLabel}=${entity.entityId}"/>';
                        </c:otherwise>
                    </c:choose>
                    "
            >&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="EFEFEF" class="contentBgColor">
            <div id="<c:out value="${entity.entityId}"/>" style="display: block">
                <table cellpadding="0" cellspacing="0" width="100%" bgcolor="FFFFFF">
                    <tr><td colspan="2" valign="TOP" class="contentBgColor">&nbsp;</td></tr>
                    <tr align="center">
                        <td colspan="2" valign="TOP" class="contentBgColor">