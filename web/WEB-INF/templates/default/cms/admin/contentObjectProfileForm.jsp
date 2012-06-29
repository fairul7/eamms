<%@ include file="/common/header.jsp" %>

<c:choose>
<c:when test="${!empty form && !empty form.childMap.profileAssignForm}">
    <tr>
      <td height="22" style="vertical-align: center;" colspan="2" class="contentTitleFont">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
            &nbsp;<fmt:message key='cms.label.profile'/>
        <%-- /span --%>
      </td>
    </tr>
    <tr>
      <td align="right" style="vertical-align: center; width: 150px;" class="classRowLabel">
      	<fmt:message key="cms.label.profile"/>&nbsp;
      </td>
      <td style="vertical-align: top">
      	<table width="100%" cellpadding="0">
       		<tr><td>
      	<x:display name="${form.childMap.profileAssignForm.absoluteName}"/>
      		</td></tr>
      	</table>
      </td>
    </tr>
</c:when>
<c:when test="${!empty form && !empty form.childMap.contentProfileForm}">
    <tr>
      <td style="vertical-align: center;" colspan="2" class="contentTitleFont">
        <%-- span style="font-family:Arial; font-size:16px; font-weight:bold" --%>
            <fmt:message key='cms.label.profile'/>
            <c:out value="${form.childMap.contentProfileForm.profileName}"/>
        <%-- /span --%>
      </td>
    </tr>
    <c:set var="profileChildren" value="${form.childMap.contentProfileForm.children}"/>
    <c:forEach items="${profileChildren}" var="wgt" varStatus="stat" step="2">
    <tr>
      <td style="vertical-align: top; width: 150px;" class="classRowLabel">
      	<x:display name="${profileChildren[stat.index].absoluteName}"/>&nbsp;
      </td>
      <td style="vertical-align: top;">
      	<table width="100%" cellpadding="0">
       		<tr><td>
      	<x:display name="${profileChildren[stat.index+1].absoluteName}"/>
      		</td></tr>
      	</table>
      </td>
    </tr>
    </c:forEach>
</c:when>
</c:choose>
