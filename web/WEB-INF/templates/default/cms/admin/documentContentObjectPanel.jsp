<%@ page import="com.tms.cms.document.DocumentStorageServlet, 
                 java.net.URLEncoder,
                 kacang.Application"%>
<%--@ taglib uri="http://java.sun.com/jstl/core" prefix="c" --%>
<%--@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" --%>
<%@ include file="/common/header.jsp"  %>
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
    <c:if test="${!empty co.description}">
    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.description'/><br>
      </td>
      <td style="vertical-align: top;"><c:out value="${co.description}"/>
      </td>
    </tr>
    </c:if>

<c:if test="${!empty co && !empty co.fileName}">
    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.fileName'/><br>
      </td>
      <c:set var="filename" value="${co.fileName}"/>
      <c:set var="filepath" value="${co.filePath}"/>
      
      <td style="vertical-align: top;">
         <% 
            //encode the filename
            String encodeFilename = URLEncoder.encode((String)pageContext.getAttribute("filename"), "UTF-8");
            //base on the encoding, space will be replace by +, but IE dun like it, so have to replace all + with %20
            encodeFilename = encodeFilename.replaceAll("\\+", "%20");
            
            String filePath = (String)pageContext.getAttribute("filepath");
            int lastIndexOfSlash = filePath.lastIndexOf("/");
            filePath = filePath.substring(0, lastIndexOfSlash+1);
            filePath += encodeFilename;
         %>
         <a target="_blank" href="<%= request.getContextPath() %>/storage<%= filePath %>"><c:out value="${co.fileName}"/></a>
         <script>
         <!--
            function doGetUrl(filepath) {
              var baseURL = "<%= DocumentStorageServlet.getServerUrl(request) %>";
              if(window.clipboardData.setData("Text", baseURL + filepath + encodeURIComponent('<c:out value="${co.fileName}"/>'))) {
                alert("The following URL has been copied to your clipboard:\n\n" + baseURL + filepath  + encodeURIComponent('<c:out value="${co.fileName}"/>'));
              }
              return false;
            }
         //-->
         </script>
         <c:url var="docUrl" value="/cms/documentstorage/${co.id}/"/>
         [<a href="<c:out value="${docUrl}${filename}"/>" onclick='return doGetUrl("<c:out value="${docUrl}"/>")'><fmt:message key='document.label.copyUrl'/></a>]
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.fileSize'/><br>
      </td>
      <td style="vertical-align: top;"><c:out value="${co.fileSize}"/>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top;"><fmt:message key='general.label.contentType'/><br>
      </td>
      <td style="vertical-align: top;"><c:out value="${co.contentType}"/>
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
	<tr>
			<td style="vertical-align: top;"><fmt:message key="taxonomy.label.taxonomizedUnder"/></td>
			<td style="vertical-align: top;">
				<x:template type="com.tms.cms.taxonomy.ui.TaxonomyMappedList" properties="contentId=${co.id}"/>
			</td>
		</tr>
    <tr>
<%
}
%>    
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


  </tbody>
</table>

