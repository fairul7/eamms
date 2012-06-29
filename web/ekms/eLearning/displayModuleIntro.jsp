<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Nov 8, 2004
  Time: 5:52:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>


<x:config>
    <page name="course">
        <com.tms.elearning.course.ui.DisplayModuleIntro name="introduction" width="100%"/>
    </page>
</x:config>




<c:if test="${!empty param.id}">

   <x:set name="course.introduction" property="id" value="${param.id}" ></x:set>

   </c:if>



<%@ page import="com.tms.portlet.taglibs.PortalServerUtil" %>


<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
           <fmt:message key="eLearning.lesson.label.folderdetail"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img
            src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="course.introduction"/>


    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img
            src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>


