<%@ include file="/common/header.jsp" %>

<jsp:include page="includes/header.jsp" flush="true"  />

<x:template
	name="mobileChannel"
    type="com.tms.cms.tdk.DisplayMobileChannel"
    properties="id=${param.id}" />

<%--
<x:cache time="30" key="mobileHeadlines" scope="application">
<x:template
	name="mobileHeadlines"
    type="com.tms.cms.tdk.DisplayVSection"
    properties="id=com.tms.cms.vsection.VSection_3c228f2a-7f000010-bde8a700-4962ca79"
    body="custom">

<table width="100%" border=0>
<c:forEach items="${mobileHeadlines.contentObject.contentObjectList}" var="co">
<tr>
    <td width="1%" valign=top> <img src="images/point.gif"> </td>
    <td><A HREF="content.jsp?id=<c:out value='${co.id}'/>"><font size="-1" face="Arial, Helvetica" color="000000"><c:out value='${co.name}'/></font></a>
    </td>
</tr>
</c:forEach>
</table>

</x:template>
</x:cache>
--%>

<jsp:include page="includes/footer.jsp" flush="true"  />

