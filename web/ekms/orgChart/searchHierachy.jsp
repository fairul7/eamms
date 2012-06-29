<%@include file="/common/header.jsp" %>
<x:config >
	<page name="orgChart2">
		<com.tms.hr.orgChart.ui.HierachyList name="searchHierachy"/>
    </page>
</x:config>

<c-rt:set var="forward_add" value="add"/>
<x:set name="orgChart2.searchHierachy" property="type" value="search" />
<x:set name="orgChart2.searchHierachy" property="query" value="" />
<c:if test="${!empty param.query}">
    <x:set name="orgChart2.searchHierachy" property="query" value="${param.query}" />
</c:if>     

<c:if test="${!empty param.userId}">
    <c:redirect url="searchStaffHierachy.jsp?userId=${param.userId}"/>        
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>

<table cellpadding="0" cellspacing="0" width="100%">
    <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont">
        <b>
            <font color="#FFCF63" class="contentTitleFont">
      &nbsp;
            <fmt:message key='orgChart.hierachy.label.listHierachyTitle'/>
            </font>
        </b>
    </td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
    <tr>
        <td>
            <x:display name="orgChart2.searchHierachy"/>
        </td>
    </tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>




