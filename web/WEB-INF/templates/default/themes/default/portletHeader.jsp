<%@ page import="com.tms.portlet.theme.themes.DefaultTheme,
                 com.tms.portlet.theme.ThemeManager"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%-- Initializing Constants --%>
<crt:set var="leftColumn" value="<%= DefaultTheme.COLUMN_LEFT_LABEL %>"/>
<crt:set var="rightColumn" value="<%= DefaultTheme.COLUMN_RIGHT_LABEL %>"/>
<crt:set var="cmdUp" value="<%= DefaultTheme.COMMAND_MOVE_UP %>"/>
<crt:set var="cmdDown" value="<%= DefaultTheme.COMMAND_MOVE_DOWN %>"/>
<crt:set var="cmdLeft" value="<%= DefaultTheme.COMMAND_MOVE_LEFT %>"/>
<crt:set var="cmdRight" value="<%= DefaultTheme.COMMAND_MOVE_RIGHT %>"/>
<crt:set var="cmdMaximize" value="<%= DefaultTheme.COMMAND_MAXIMIZE %>"/>
<crt:set var="cmdEdit" value="<%= DefaultTheme.COMMAND_EDIT %>"/>
<crt:set var="cmdClose" value="<%= DefaultTheme.COMMAND_CLOSE %>"/>
<crt:set var="keyLabel" value="<%= ThemeManager.LABEL_ENTITY_KEY %>"/>
<%-- Initializing Variables --%>
<c:set var="entity" value="${requestScope.entity}"/>
<c:set var="status" value="${requestScope.columnStatus}"/>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="003366" class="contentTitleFont" nowrap><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<c:out value="${entity.portlet.portletTitle}"/></font></b></td>
        <td align="right" bgcolor="003366" class="contentTitleFont">
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
                <input type="button" class="themeButton" value="Edit" onClick="document.location='<%= response.encodeURL(request.getRequestURI()) %>?<c:out value="cn=${requestScope.themeManager.absoluteName}&et=${cmdEdit}&${keyLabel}=${entity.entityId}"/>';">
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
                <table cellpadding="0" cellspacing="0" width="100%">
                    <tr><td colspan="2" valign="TOP" bgcolor="EFEFEF" class="contentBgColor"><img src="images/blank.gif" width="5" height="10"></td></tr>
                    <tr align="center">
                        <td colspan="2" valign="TOP" bgcolor="EFEFEF" class="contentBgColor">



