<%@ include file="/common/header.jsp" %>
<%-- x:cache scope="application" duration="43200"--%>
<x:template
    name="tree"
    type="com.tms.cms.tdk.DisplayTaxonomyMenu"
    properties="levels=2" body="/ekms/content/includes/displayTaxonomyMenu.jsp"
/>
<%-- /x:cache--%>