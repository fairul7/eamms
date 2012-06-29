<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp" %>


<html>
<head>
    <jsp:include page="/ekms/init.jsp" flush="true"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
<body onload="this.focus()">
<c:set var="type" value="${param.type}" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
      <c:if test="${type==1}">
      	<fmt:message key='recruit.general.label.tbJobRespon'/>
      </c:if>
      <c:if test="${type==2}">
       	<fmt:message key='recruit.general.label.tbJobRequire' />
       </c:if>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
     <c:if test="${type==1}">
        <c:out value="${widgets['vacancy.Detail'].vacancyObj.responsibilities}" escapeXml="false"/>
     </c:if>
   
      <c:if test="${type==2}">
     	 <c:out value="${widgets['vacancy.Detail'].vacancyObj.requirements}" escapeXml="false"/> 
	  </c:if>
	
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
</table>
</body>
</html>
