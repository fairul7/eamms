<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.collab.vote.ManageVotes" module="com.tms.collab.vote.model.PollModule" url="noPermission.jsp" />

<%@ include file="/cmsadmin/includes/headerAdmin.jsp" %>

<jsp:include page="/cmsadmin/includes/headerInteractive.jsp" flush="true" />

<x:config>
           <page name="PollAdmin">
              <portlet name="pollportlet" text="<fmt:message key='vote.label.votes'/>" width="100%" permanent="true">
               <com.tms.collab.vote.ui.PollAdminView name="PollAdminView"/>

              </portlet>

           </page>
</x:config>

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
     <td style="vertical-align: top; width: 250px;">
        <jsp:include page="/cmsadmin/includes/sideInterativeVote.jsp" flush="true"/>

      </td>
      <td style="vertical-align: top;">
        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;"><br>

                  <x:display name="PollAdmin.pollportlet" />

                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
                  <br>
              </td>
            </tr>
          </tbody>
        </table>
      </td>
    </tr>
  </tbody>
</table>
