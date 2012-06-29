<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="portal">
        <com.tms.portlet.ui.PortalServer name="server"/>
		<kacang.ui.menu.MenuGenerator name="menu"/>
		<kacang.ui.menu.ApplicationMenuGenerator name="appMenu"/>
    </page>
</x:config>