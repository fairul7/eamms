<%@ page import="kacang.ui.WidgetManager,
                 kacang.stdui.Portlet,
                 com.tms.cms.ad.ui.AdUi"%>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.ad.ManageAds" module="com.tms.cms.ad.model.AdModule" url="noPermission.jsp" />

<x:config>
    <page name="page">
        <portlet name="portlet" text="<fmt:message key='ad.label.bannerAds'/>" width="100%" permanent="true">
            <com.tms.cms.ad.ui.AdUi name="adUi" showMenu="false" />
        </portlet>
    </page>
</x:config>

<%
    WidgetManager wm;
    Portlet portlet;
    AdUi adUi;

    wm = WidgetManager.getWidgetManager(request);
    portlet = (Portlet) wm.getWidget("page.portlet");
    adUi = (AdUi) wm.getWidget("page.portlet.adUi");
    portlet.setText(adUi.getTitle());
    String title = adUi.getTitle();
    Application app = Application.getInstance();
    String newLabel =app.getMessage("ad.label.new","New");
    String str =app.getMessage("ad.label.add","Add");
    if(title.indexOf(newLabel) != -1){
        title = str+" "+title;
    }
    pageContext.setAttribute("title",title);
%>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request"><fmt:message key="general.label.bannerAds"/> > <c:out value="${title}"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <x:display name="page.portlet.adUi" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>


