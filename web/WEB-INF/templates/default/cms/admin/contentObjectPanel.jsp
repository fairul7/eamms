<%@ page import="kacang.Application"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %> 
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" scope="request" value="${widget.contentObject}"/>

<table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 100px; font-size:12pt; font-weight: bold"><fmt:message key='general.label.name'/><br>
      </td>
      <td style="vertical-align: top; font-size:12pt; font-weight: bold"><c:out value="${co.name}"/>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.type'/><br>
      </td>
      <td style="vertical-align: top;"><fmt:message key="cms.label.iconLabel_${co.className}"/>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.id'/><br>
      </td>
      <td style="vertical-align: top;"><c:out value="${co.id}"/>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.version'/><br>
      </td>
      <td style="vertical-align: top;"><c:out value="${co.version}"/>
      </td>
    </tr>
    <c:set var="contentProfile" value="${widget.contentProfile}"/>
    <c:if test="${!empty contentProfile}">
    <tr>
      <td style="vertical-align: top;"><fmt:message key='cms.label.profile'/><br>
      </td>
      <td style="vertical-align: top;"><c:out value="${contentProfile.name}"/>
      </td>
    </tr>
    </c:if>
    <c:if test="${!empty co.description}">
    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.description'/><br>
      </td>
      <td style="vertical-align: top;"><c:out value="${co.description}"/>
      </td>
    </tr>
    </c:if>

<%--
    <tr>
      <td style="vertical-align: top;">&nbsp;<br>
      </td>
      <td style="vertical-align: top;">&nbsp;
      </td>
    </tr>
--%>

<%
String s = Application.getInstance().getProperty("com.tms.cms.taxonomy");
if (s!=null && !s.equals("")) {
%>

<%-- <c:if test="${co.className =='com.tms.cms.article.Article' }"> --%>
		<tr>
			<td style="vertical-align: top;"><fmt:message key="taxonomy.label.taxonomizedUnder"/>
			</td>
			<td style="vertical-align: top;">
				<x:template type="com.tms.cms.taxonomy.ui.TaxonomyMappedList" properties="contentId=${co.id}"/>
			</td>
		</tr>
	
<%-- </c:if> --%>
    
<%
}
%>    


    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.status'/><br>
      </td>
      <td style="vertical-align: top;">

        <table cellpadding="4" cellspacing="4" width="100%">
          <tr>
            <c:if test="${co.checkedOut}">
            <td width="25%" style="background: silver"><fmt:message key='general.label.checkedOut'/></td>
            </c:if>
            <c:if test="${co.submitted}">
            <td width="25%" style="background: silver"><fmt:message key='general.label.submitted'/></td>
            </c:if>
            <c:if test="${co.approved}">
            <td width="25%" style="background: silver"><fmt:message key='general.label.approved'/></td>
            </c:if>
            <c:if test="${co.published}">
            <td width="25%" style="background: silver"><fmt:message key='general.label.published'/></td>
            </c:if>
            <c:if test="${co.archived}">
            <td width="25%" style="background: silver"><fmt:message key='general.label.archived'/></td>
            </c:if>
          </tr>
          <tr>
            <c:if test="${co.checkedOut}">
            <td>
                    <img src="<%= request.getContextPath() %>/common/table/booleantrue.gif">
                    <c:out value="${co.checkOutUser}"/>,
                    <fmt:formatDate value="${co.checkOutDate}" pattern="${globalDateLong}" />
            </td>
            </c:if>
            <c:if test="${co.submitted}">
            <td>
                    <img src="<%= request.getContextPath() %>/common/table/booleantrue.gif">
                    <c:out value="${co.submissionUser}"/>,
                    <fmt:formatDate value="${co.submissionDate}" pattern="${globalDateLong}" />
            </td>
            </c:if>
            <c:if test="${co.approved}">
            <td>
                    <img src="<%= request.getContextPath() %>/common/table/booleantrue.gif">
                    <c:out value="${co.approvalUser}"/>,
                    <fmt:formatDate value="${co.approvalDate}" pattern="${globalDateLong}" />
            </td>
            </c:if>
            <c:if test="${co.published}">
            <td>
                    <img src="<%= request.getContextPath() %>/common/table/booleantrue.gif">
                    <fmt:message key='general.label.version'/> <c:out value="${co.publishVersion}"/>,
                    <c:out value="${co.publishUser}"/>,
                    <fmt:formatDate value="${co.publishDate}" pattern="${globalDateLong}" />
            </td>
            </c:if>
            <c:if test="${co.archived}">
            <td>
                    <img src="<%= request.getContextPath() %>/common/table/booleantrue.gif">
            </td>
            </c:if>
          </tr>
        </table>

      </td>
    </tr>

<%--
    <c:if test="${co.checkedOut}">
    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.checkedOut'/><br>
      </td>
      <td style="vertical-align: top;">
        <c:out value="${co.checkOutUser}"/>,
        <fmt:formatDate value="${co.checkOutDate}" pattern="dd MMM yyyy" />
      </td>
    </tr>
    </c:if>

    <c:if test="${co.submitted}">
    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.submitted'/><br>
      </td>
      <td style="vertical-align: top;">
        <c:out value="${co.submissionUser}"/>,
        <fmt:formatDate value="${co.submissionDate}" pattern="dd MMM yyyy" />
      </td>
    </tr>
    </c:if>

    <c:if test="${co.approved}">
    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.approved'/><br>
      </td>
      <td style="vertical-align: top;">
        <c:out value="${co.approvalUser}"/>,
        <fmt:formatDate value="${co.approvalDate}" pattern="dd MMM yyyy" />
      </td>
    </tr>
    </c:if>

    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.published'/><br>
      </td>
      <td style="vertical-align: top;"><c:out value="${co.published}"/>
      </td>
    </tr>
--%>
  </tbody>
</table>

