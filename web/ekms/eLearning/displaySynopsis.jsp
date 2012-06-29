<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Nov 8, 2004
  Time: 5:52:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>


<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>


<%

    int randomizePage1 = (int) (100000.0 * Math.random()) + 1;
    int randomizePage2 = (int) (999999.0 * Math.random()) + 1;
    int randomizePage3 = (int) (999999.0 * Math.random()) + 1;
    String randomPage = "page_";
    boolean flip;

    randomPage += randomizePage1;
    randomPage +=  new Random().nextBoolean() ? "_1" : "_0";
    randomPage += randomizePage2;
    randomPage += "__";
    randomPage += randomizePage3;
    randomPage +=  new Random().nextBoolean() ? "1_" : "0_";
    pageContext.setAttribute("randomPage", randomPage, PageContext.PAGE_SCOPE);

%>

<c:set var="pageName" value="${randomPage}"/>



<x:config>
    <page name="<%=randomPage%>">
        <com.tms.elearning.course.ui.DisplaySynopsis name="synopsis" width="100%"/>
    </page>
</x:config>




<c:if test="${!empty param.id}">

   <x:set name="${pageName}.synopsis" property="id" value="${param.id}" ></x:set>

   </c:if>



<%@ page import="com.tms.portlet.taglibs.PortalServerUtil" %>
<%@ page import="java.util.Random"%>


<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
            Student Detail</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img
            src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="${pageName}.synopsis"/>


    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img
            src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>




