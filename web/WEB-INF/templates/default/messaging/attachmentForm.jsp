<%@ page import="com.tms.collab.messaging.model.Util,
                 java.util.Map"%>
<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='messaging.message.attachments'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <blockquote>
        <br>
        <c:set var="map" value="${sessionScope.attachmentMap}" />
        <c:if test="${empty map}">
                <fmt:message key='messaging.message.selectfiles'/>.
                <br><br>
        </c:if>
        <c:if test="${!empty map}">
            <fmt:message key='messaging.message.currentlyattached'/>.
            <br><br>
            <c:forEach var="a" items="${map}">
                <c:set var="key" value="${a.key}" />
                <%
                    String key = (String) pageContext.getAttribute("key");
                    key = Util.encodeHex(key);
                    pageContext.setAttribute("encodedKey", key);
                %>
                <li><c:out value="${a.key}" /> [<x:event name="${w.absoluteName}"
                 type="remove" url=""
                 param="key=${encodedKey}"><fmt:message key='messaging.message.remove'/></x:event>]</li>
            </c:forEach>
            <br>
        </c:if>


        </blockquote>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/images/blank.gif" />" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP">

        <jsp:include page="/WEB-INF/templates/ekms/form_header.jsp" />
              <table width="100%" cellpadding="5" cellspacing="1" border="0">
                <tr>
                  <td class="contentBgColor" width="20%" valign="top" align="right"><b>
                  <fmt:message key='messaging.message.attachment1'/>
                  </b></td>
                  <td class="contentBgColor" width="80%" valign="top">
                    <x:display name="${w.fuAttach1.absoluteName}" />
                  </td>
                </tr>
                <tr>
                  <td class="contentBgColor" width="20%" valign="top" align="right"><b>
                  <fmt:message key='messaging.message.attachment2'/></b></td>
                  <td class="contentBgColor" width="80%" valign="top">
                    <x:display name="${w.fuAttach2.absoluteName}" />
                  </td>
                </tr>
                <tr>
                  <td class="contentBgColor" width="20%" valign="top" align="right"><b>
                    <fmt:message key='messaging.message.attachment3'/></b></td>
                  <td class="contentBgColor" width="80%" valign="top">
                    <x:display name="${w.fuAttach3.absoluteName}" />
                  </td>
                </tr>
                <tr>
                  <td class="contentBgColor" width="20%" valign="top" align="right"><b>
                    &nbsp; </b></td>
                  <td class="contentBgColor" width="80%" valign="top">
                    <x:display name="${w.btUpload.absoluteName}" />
                    <x:display name="${w.btDone.absoluteName}" />
                  </td>
                </tr>
                <tr> </tr>
              </table>
        <jsp:include page="/WEB-INF/templates/ekms/form_footer.jsp" />

    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/images/blank.gif" />" width="5" height="15"> </td>
  </tr>
</table>
