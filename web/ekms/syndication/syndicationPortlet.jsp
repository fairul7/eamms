<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="syndicationPortletPage">
        <com.tms.cms.syndication.ui.SyndicationPortlet name="syndicationPortlet" />
    </page>
</x:config>

<html>
<body>
<x:display name="syndicationPortletPage.syndicationPortlet" />
</body>
</html>
