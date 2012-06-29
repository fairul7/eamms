<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerContent.jsp" flush="true" />




<table cellpadding="0" cellspacing="0" border="0"
 style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td style="vertical-align: top; width: 250px;">

        <jsp:include page="/cmsadmin/includes/sideContentTree.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;"> <table cellpadding="2" cellspacing="2" border="0"
 style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;">
                <br>
                  <%-- Content Confirm Action Table --%>
                  <x:display name="cms.contentConfirmAction"/>
                <p> <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
              </td>
            </tr>
          </tbody>
        </table></td>
    </tr>
  </tbody>
</table>


<jsp:include page="/cmsadmin/includes/footerContent.jsp" flush="true" />

<%@ include file="/cmsadmin/includes/footerAdmin.jsp" %>
