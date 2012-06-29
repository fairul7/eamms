<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<jsp:include page="mailListFormHeader.jsp" flush="true" />
    <tr>
      <td style="vertical-align: top;" colspan="2" class="contentTitleFont">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
          <fmt:message key='maillist.label.composedDetails'/>
        <%-- /span --%>
      </td>
    </tr>

    <tr>
      <td align="right" class="classRowLabel" style="vertical-align: top;" width="150px"><fmt:message key='maillist.label.body'/></td>
      <td class="classRow" style="vertical-align: top;">
        <x:display name="${f.ffBody.absoluteName}" />
      </td>
    </tr>
<jsp:include page="mailListFormFooter.jsp" flush="true" />
