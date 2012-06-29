<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="f" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="2" cellspacing="0" border="0" style="text-align: left; width: 100%;">

    <tr>
      <td style="vertical-align: top;" colspan="2" class="contentTitleFont">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
            <c:if test="${f.newForm}"><fmt:message key='maillist.label.newMailTemplate'/></c:if>
            <c:if test="${!f.newForm}"><fmt:message key='maillist.label.editingMailTemplate'/>: <c:out value="${f.mailTemplate.name}" /></c:if>
            <c:if test="${f.formSaved}"><font color="green"><i><fmt:message key='maillist.label.saved'/></i></font></c:if>
        <%-- /span --%>
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='general.label.name'/>&nbsp; *</td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffName.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right"><fmt:message key='general.label.description'/></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffDescription.absoluteName}" />
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" colspan="2" class="contentTitleFont">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
          <fmt:message key='maillist.label.templateDetails'/>
        <%-- /span --%> 
      </td>
    </tr>

    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">
      <fmt:message key='maillist.label.htmlTemplate'/></td>
      <td style="vertical-align: top;" class="classRow">
        <x:display name="${f.ffHtmlYes.absoluteName}" />
        <x:display name="${f.ffHtmlNo.absoluteName}" />
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


    <tr>
      <td style="vertical-align: top;" class="classRowLabel" align="right">&nbsp;</td>
      <td style="vertical-align: top;" class="classRow">
        <input type="submit" class="button" value="<fmt:message key='general.label.save'/>">
        <input type="reset" class="button" value="<fmt:message key='general.label.reset'/>">
      </td>
    </tr>

</table>
<jsp:include page="../form_footer.jsp" flush="true"/>