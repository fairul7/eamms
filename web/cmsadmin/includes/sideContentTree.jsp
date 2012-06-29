<%@ page import="com.tms.cms.core.model.ContentUtil"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<%
    pageContext.setAttribute("treeMode", new Boolean(ContentUtil.isContentTreeMode(request)));
%>

      <table cellpadding="5" cellspacing="0" border="0" style="text-align: left; width: 225px;">
          <tbody>
            <tr>
              <td style="background: #003366; vertical-align: top;"><span style="color: white; font-weight: bold;">
              <c:choose>
              <c:when test="${treeMode}">
                    <fmt:message key='cms.label.contentTree'/>
              </c:when>
              <c:otherwise>
                    <fmt:message key='cms.label.contentModules'/>
              </c:otherwise>
              </c:choose>
                </span><br>
              </td>
            </tr>
            <tr>
              <td style="background: #DDDDDD; vertical-align: top;">

              <c:choose>
              <c:when test="${treeMode}">
                    <x:display name="cms.contentTree"/>
              </c:when>
              <c:otherwise>
                    <x:display name="cms.contentModuleList"/>
              </c:otherwise>
              </c:choose>

                <br> <br> <br> <br> <br>
              </td>
            </tr>
          </tbody>
        </table>

        <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br>

