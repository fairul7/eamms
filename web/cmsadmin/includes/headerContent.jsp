<%@ page import="com.tms.cms.core.model.ContentUtil"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<%@ include file="/cmsadmin/includes/headerCommon.jsp" %>

<%
    boolean isTreeOrList = !startsWith(request, "/cmsadmin/content/contentSummary.jsp") &&
            !startsWith(request, "/cmsadmin/content/contentSummary.jsp") &&
            !startsWith(request, "/cmsadmin/content/contentSearch.jsp") &&
            !startsWith(request, "/cmsadmin/content/contentKeywords.jsp") &&
            !startsWith(request, "/cmsadmin/content/profile") &&
            !startsWith(request, "/cmsadmin/content/contentDeleted.jsp");
%>
<TABLE width="100%" cellpadding="0" height="35" bgcolor="#336699" border="0" cellspacing="0">
    <form>
        <input type="hidden" name="cn" value="cms.searchForm">
    <TR>
      <TD valign="middle"><img width="5" height="1" src="../images/clear.gif"></TD>
      <TD valign="middle">
        <TABLE cellpadding="2" bgcolor="#4779AB" border="0" cellspacing="0" width="100%">
          <TR>
            <TD valign="middle" width="100"> | <a href="contentSummary.jsp" class="<%= (startsWith(request, "/cmsadmin/content/contentSummary.jsp")) ? "submenulink2" : "submenulink" %>"><fmt:message key='cms.label.summary'/></a></TD>
            <TD valign="middle" width="100"> | <a href="contentExplorer.jsp" class="<%= (isTreeOrList && ContentUtil.isContentTreeMode(request)) ? "submenulink2" : "submenulink" %>"><fmt:message key='cms.label.explorerView'/></a></TD>
            <TD valign="middle" width="100"> | <a href="contentList.jsp" class="<%= (isTreeOrList && !ContentUtil.isContentTreeMode(request)) ? "submenulink2" : "submenulink" %>"><fmt:message key='cms.label.listByType'/></a></TD>
            <TD valign="middle" width="100"> | <a href="contentSearch.jsp" class="<%= (startsWith(request, "/cmsadmin/content/contentSearch.jsp")) ? "submenulink2" : "submenulink" %>"><fmt:message key='cms.label.search'/></a></TD>
            <x:permission module="com.tms.cms.core.model.ContentManager" permission="com.tms.cms.ManageKeywords">
            <TD valign="middle" width="100">
                | <a href="contentKeywords.jsp" class="<%= (startsWith(request, "/cmsadmin/content/contentKeywords.jsp")) ? "submenulink2" : "submenulink" %>"><fmt:message key='cms.label.keywords'/></a>
            </TD>
            </x:permission>
            <x:permission module="com.tms.cms.core.model.ContentManager" permission="com.tms.cms.ManageProfile">
            <TD valign="middle" width="100">
                | <a href="profileList.jsp" class="<%= (startsWith(request, "/cmsadmin/content/profile")) ? "submenulink2" : "submenulink" %>"><fmt:message key='cms.label.profiles'/></a>
            </TD>
            </x:permission>
            <TD valign="middle" align="right">
            <x:permission var="accessRecycleBin" module="com.tms.cms.core.model.ContentManager" permission="com.tms.cms.AccessRecycleBin" />
            <c:if test="${!accessRecycleBin}">
                <x:permission var="accessRecycleBin" module="com.tms.cms.core.model.ContentManager" permission="com.tms.cms.ManageRecycleBin" />
            </c:if>
            <c:if test="${accessRecycleBin}">
                | <a href="contentDeleted.jsp" class="<%= (startsWith(request, "/cmsadmin/content/contentDeleted.jsp")) ? "submenulink2" : "submenulink" %>"><fmt:message key='cms.label.recycleBin'/></a>
            </c:if>
            </TD>
          </TR>
        </TABLE>
      </TD>
      <TD align="right" valign="middle"><TABLE cellpadding="2" border="0" cellspacing="0">
          <TR>
            <TD valign="middle" colspan="5">
            </TD>
          </TR>
        </TABLE></TD>
    </TR>
    </form>
</TABLE>
