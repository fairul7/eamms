<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<jsp:include page="mailListFormHeader.jsp" flush="true" />
    <tr>
      <td class="contentTitleFont" style="vertical-align: top;" colspan="2">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
          <fmt:message key='maillist.label.contentDetails'/>
        <%-- /span --%>
      </td>
    </tr>

  <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='maillist.label.content'/><br>
      <small><fmt:message key='maillist.message.selectArticles'/></small>
      </td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffContentIds.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='maillist.label.header'/></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffHeader.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='maillist.label.footer'/></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffFooter.absoluteName}" />
      </td>
    </tr>

<%--
    <tr>
        <td align="right" valign="top"><fmt:message key='maillist.label.contentIds'/></td>
        <td>
            <x:display name="${f.ffContentIds.absoluteName}" />
            <input type="button" class="button" value="<fmt:message key='general.label.selectContent'/>"
            onclick="window.open('<%= request.getContextPath() %>/maillist/selectContent.jsp?f=<c:out value="${f.absoluteName}" />&sb=<c:out value="${f.ffContentIds.absoluteName}" />', 'selectContent')">
            <input type="button" class="button" value="<fmt:message key='general.label.resetSort'/>" id="resetSortOrder" onfocus="populate()" onclick="populate()">
            <input type="hidden" id="selectedValue">
            <input type="hidden" id="selectedName">

        <script language="Javascript">
        <!--
            function populate() {
                targetBox = document.forms['<c:out value="${f.absoluteName}" />'].elements['<c:out value="${f.ffContentIds.absoluteName}" />'];
                v = document.getElementById('selectedValue').value;
                n = document.getElementById('selectedName').value;
                values = v.split("|");
                names = n.split("|");
                for (i = 0; i < names.length; i++) {
                    if (values[i] != '' && names[i] != '') {
                        targetBox.options[i] = new Option(names[i], values[i], false, true);
                    }
                }
            }
        //-->
        </script>

        </td>
    </tr>--%>

<jsp:include page="mailListFormFooter.jsp" flush="true" />
