<%@ page import="kacang.ui.WidgetManager"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<!--
<x:config>
    <page name="searchFormPage">
        <com.tms.cms.core.ui.ContentSearchForm name="searchForm">
            <listener_script>
                form = event.getWidget();
                if (!form.isInvalid()) {
                    String searchResultsUrl = "./search.jsp?advQuery=true&amp;pageSize=" + form.getPageSize();
                    return new Forward(null, searchResultsUrl, true);
                }
            </listener_script>
        </com.tms.cms.core.ui.ContentSearchForm>
    </page>
</x:config>
//-->

<x:display name="searchFormPage.searchForm" body="custom">

<%
    request.setAttribute("form", WidgetManager.getWidgetManager(request).getWidget("searchFormPage.searchForm"));
%>

    <form name="<c:out value="${form.absoluteName}"/>"
          action="?"
          method="POST"
          target="<c:out value="${form.target}"/>"
          <c:if test="${!empty form.enctype}">
              enctype="<c:out value="${form.enctype}"/>"
          </c:if>
          onSubmit="<c:out value="${form.attributeMap['onSubmit']}"/>"
          onReset="<c:out value="${form.attributeMap['onReset']}"/>"
    >
    <input type="hidden" name="cn" value="<c:out value="${form.absoluteName}"/>">
<c:if test="${!(empty form.message)}">
    <script>alert("<c:out value="${form.message}"/>");</script>
</c:if>
    <table cellpadding="4" cellspacing="2" border="0">
        <tr>
          <td style="vertical-align: top;">
          <table style="width: 100%;" border="0" cellpadding="0" cellspacing="0">
            <tr valign="top">
              <td class="contentBody">
              <b><fmt:message key='cms.label.enterSearchWords'/></b> <fmt:message key='cms.label.useQuotesAroundPhrases'/><br>
              <x:display name="${form.childMap.searchField.absoluteName}"/>
                </td>
              <td class="contentBody">
              <b><fmt:message key='cms.label.appearingIn'/></b>
                <br>
                <x:display name="${form.childMap.archiveBox.absoluteName}"/>
                </td>
            </tr>
          </table>
          </td>
        </tr>
        <tr>
          <td style="vertical-align: top;">
            <x:display name="${form.childMap.andRadio.absoluteName}"/><fmt:message key='cms.label.and'/>
            <x:display name="${form.childMap.orRadio.absoluteName}"/><fmt:message key='cms.label.or'/>
            <x:display name="${form.childMap.notRadio.absoluteName}"/><fmt:message key='cms.label.not'/><br>
          </td>
        </tr>
        <tr>
          <td style="vertical-align: top;">
          <table style="width: 100%;" border="0" cellpadding="0" cellspacing="0">
            <tr valign="top">
              <td style="width: 50%;">
              <b><fmt:message key='cms.label.additionalWordsOrPhrases'/></b><br>
              <x:display name="${form.childMap.searchField2.absoluteName}"/>
                </td>
              <td width="50%">
              </td>
            </tr>
          </table>
          </td>
        </tr>
        
        <tr>
          <td style="vertical-align: top;">
            <br>
            <b><fmt:message key="cms.label_com.tms.cms.section.Section"/></b>
            <br>
            <x:display name="${form.childMap.sectionSelectBox.absoluteName}"/>         
          </td>
        </tr>
        <tr>
          <td style="vertical-align: top;">
            <br>
            <b><fmt:message key="cms.label.contentName"/></b>
            <br>
            <x:display name="${form.childMap.contentName.absoluteName}"/>         
          </td>
        </tr>
        <tr>
          <td style="vertical-align: top;">
            <br>
            <b><fmt:message key="cms.label.contentAuthor"/></b>
            <br>
            <x:display name="${form.childMap.contentAuthor.absoluteName}"/>         
          </td>
        </tr>
        <tr>
          <td style="vertical-align: top;">
            <br>
            <b><fmt:message key="cms.label.fileType"/></b>
            <br>
            <x:display name="${form.childMap.fileType.absoluteName}"/>         
          </td>
        </tr>
        <tr>
          <td style="vertical-align: top;">
            <br>
            <b><fmt:message key='cms.label.selectADateRange'/></b>
            <br>
            <x:display name="${form.childMap.dateRadio.absoluteName}"/>
            <fmt:message key='cms.label.onThisSpecificDay'/>:<br>
            <x:display name="${form.childMap.dateField.absoluteName}"/>
            <br>
            <x:display name="${form.childMap.dateRangeRadio.absoluteName}"/>
            <fmt:message key='cms.label.inADateRangeFrom'/><br>
            <x:display name="${form.childMap.startDateField.absoluteName}"/> <fmt:message key='cms.label.to'/> <x:display name="${form.childMap.endDateField.absoluteName}"/>
          </td>
        </tr>
        <tr>
          <td style="vertical-align: top;">
          <br>
          <table style="width: 100%;" border="0" cellpadding="0" cellspacing="0">
            <tr valign="top">
              <td class="contentBody">
              <br>
              <b><fmt:message key='cms.label.sortResultsBy'/></b><br>
              <x:display name="${form.childMap.sortBox.absoluteName}"/>
              </td>
              <td class="contentBody">
              <br>
              <b><fmt:message key='cms.label.resultsPerPage'/></b><br>
              <x:display name="${form.childMap.pageSizeSelectBox.absoluteName}"/>
              </td>
              <td style="vertical-align: bottom;">
                <br>
                <x:display name="${form.childMap.submitButton.absoluteName}"/>
              </td>
            </tr>
          </table>
          </td>
        </tr>
        <tr>
          <td style="vertical-align: top;"><br>
          </td>
        </tr>
    </table>

    </form>

</x:display>




