<%@ include file="/common/header.jsp" %>

<x:template type="TemplateProcessVote" />

<jsp:include page="includes/header.jsp" flush="true" />

    <!-- Display path -->
    <x:template type="TemplateDisplayPath" properties="rootId=com.tms.cms.section.Section_Sections" />
    <p>

    <x:template type="TemplateDisplayContent" properties="hideSummary=true" />

    <!-- Display related -->
    <p>
    <x:template type="TemplateDisplayRelated" />

<jsp:include page="includes/footer.jsp" flush="true" />
