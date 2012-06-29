
	<com.tms.cms.core.ui.ContentTree name="contentTree" displayTitle="description">
        <forward name="selection" url="contentView.jsp" redirect="false"/>
    </com.tms.cms.core.ui.ContentTree>
    <com.tms.cms.core.ui.ContentModuleList name="contentModuleList">
        <listener_script>
            contentListTable = wm.getWidget("cms.contentListPortlet.contentListTable");
            contentListTable.setContentClass(event.getType());
            return new Forward(null, "contentList.jsp", true);
        </listener_script>
    </com.tms.cms.core.ui.ContentModuleList>
    <com.tms.cms.core.ui.ContentPath name="contentPath" displayTitle="description">
        <forward name="selection" url="contentView.jsp" redirect="false"/>
    </com.tms.cms.core.ui.ContentPath>
    <portlet name="contentListPortlet" text="<fmt:message key='cms.label.contentListing'/>" width="100%" permanent="true">
        <com.tms.cms.core.ui.ContentListTable name="contentListTable" width="100%" sort="date" desc="true">
            <forward name="selection" url="contentView.jsp" redirect="true"/>
        </com.tms.cms.core.ui.ContentListTable>
    </portlet>
