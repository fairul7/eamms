<%@include file="/common/header.jsp" %>
<%@include file="includes/accessControl.jsp" %>

<x:config >
	<page name="orgChart">
		<com.tms.hr.orgChart.ui.HierachyList name="listHierachy"/>
    </page>
</x:config>

<c-rt:set var="forward_add" value="add"/>

<c:if test="${forward.name==forward_add}">
    <script type="text/javascript">
        <c:redirect url="staffHierachy.jsp"/>
    </script>
</c:if>
<x:set name="orgChart.listHierachy" property="query" value="" />
<c:if test="${!empty param.userId}">
    <c:redirect url="staffHierachy.jsp?userId=${param.userId}"/>        
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>

<table cellpadding="0" cellspacing="0" width="100%" class="contentBgColor">
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
            <x:display name="orgChart.listHierachy"/>
        </td>
    </tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>




