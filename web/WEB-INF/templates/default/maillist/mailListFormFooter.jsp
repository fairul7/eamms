<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:if test="${!f.newForm && (f.mailList.mailListType==2)}">
    <tr>
      <td style="vertical-align: top;" colspan="2">
        <hr>
        <span style="font-family:Arial; font-size:16px; font-weight:bold">
          <fmt:message key='maillist.label.contentSentDetails'/>
        </span>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;"><fmt:message key='cms.label.summary'/></td>
      <td style="vertical-align: top;">
        <c:out value="${f.mailList.sentContentIdCount}"/> <fmt:message key='maillist.label.mailingListContentItems'/>
        <br>
        <c:out value="${f.mailList.unsentContentIdCount}" /> <fmt:message key='maillist.label.nextExecutionContentItems'/>
        <br>
        [ <x:event name="${f.absoluteName}" type="clearSent" html="onclick='return clearSentItems()'" ><fmt:message key='maillist.label.clearSentItems'/></x:event> ]
      </td>
    </tr>
</c:if>
    

    <script language="JavaScript">
    <!--
        var id='<c:out value="${f.id}" />';
        function clearSentItems() {
            if(confirm('Confirm clear all sent items by this mailing list?')) {
                return true;
            } else {
                return false;
            }
        }

        function doPreview() {
            window.open('ml_preview.jsp?id=' + id, 'preview', 'width=800,height=600,left=0,top=0,scrollbars=yes,resizable=yes');
        }

        function doSendNow() {
            if(confirm('Confirm send mailing list now?')) {
                window.open('ml_send.jsp?id=' + id, 'send', 'width=400,height=300,left=0,top=0,scrollbars=yes,resizable=yes');
            }
        }
    //-->
    </script>

    <tr>
      <td style="vertical-align: top;">&nbsp;</td>
      <td style="vertical-align: top;">
        <input type="submit" class="button" value="<fmt:message key='general.label.save'/>">
        <input type="reset" class="button" value="<fmt:message key='general.label.reset'/>">
        <c:if test="${!f.newForm}">
            <input type="button" class="button" value="<fmt:message key='general.label.preview'/>" onclick="doPreview()">
            <input type="button" class="button" value="<fmt:message key='general.label.sendNow'/>" onclick="doSendNow()">
        </c:if>
      </td>
    </tr>

</table>
<jsp:include page="../form_footer.jsp" flush="true"/>