<%@ page import="java.io.PrintWriter,
                 java.io.StringWriter,
                 com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="includes/taglib.jsp" %>


<jsp:include page="../includes/mheader.jsp"/>
  <jsp:include page="/ekms/init.jsp"/>
  <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
  <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
  <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">



<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='messaging.label.anErrorHasOccurred'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <blockquote>
            <fmt:message key='messaging.label.messagingError'/>:
            <%
                Object obj = session.getAttribute("error");

                if(obj!=null) {
                    if(obj instanceof Exception) {
                        Exception e = (Exception) obj;
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        pageContext.setAttribute("message", e.getMessage());
                        pageContext.setAttribute("exceptionTrace", sw.toString());
                    } else {
                        pageContext.setAttribute("message", obj.toString());
                    }
                }
            %>
            <ul><li><b><c:out value="${message}" default="Unspecified error!" /></b></li></ul>
            <c:if test="${exceptionTrace ne null}">
                <a href="" onclick="document.getElementById('st').style.display='block'; return false">Click here</a> to view the exception stack trace (for troubleshooting purposes only).
                <div id="st" style="display:none"><form>
                <textarea cols="80" rows="20" wrap="off"><c:out value="${exceptionTrace}" /></textarea></form></div>
                <br><br>
            </c:if>
            <input type="button" class="buttonClass" value="Continue" onclick="history.go(-1);">
            <br><br>
        </blockquote>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>


<jsp:include page="../includes/mfooter.jsp"/>
