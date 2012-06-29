<%@ include file="/common/header.jsp"  %>

<x:template name="menu" type="TemplateDisplayMenu" properties="id=com.tms.cms.section.Section_Sections&hideSummary=true&orphans=true"  body="custom">

<c:set var="root" value="${menu.root}"/>

<style>
</style>
<script language="JavaScript1.2" src="includes/coolmenus4.js">
<!--
/*****************************************************************************
Copyright (c) 2001 Thomas Brattli (webmaster@dhtmlcentral.com)

DHTML coolMenus - Get it at coolmenus.dhtmlcentral.com
Version 4.0_beta
This script can be used freely as long as all copyright messages are
intact.

Extra info - Coolmenus reference/help - Extra links to help files ****
CSS help: http://192.168.1.31/projects/coolmenus/reference.asp?m=37
General: http://coolmenus.dhtmlcentral.com/reference.asp?m=35
Menu properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=47
Level properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=48
Background bar properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=49
Item properties: http://coolmenus.dhtmlcentral.com/properties.asp?m=50
******************************************************************************/
//-->
</script>
</head>
<body>

<script>
<!--
oCMenu=new makeCM("oCMenu") //Making the menu object. Argument: menuname

//Menu properties
oCMenu.pxBetween=0
oCMenu.fromLeft=10
oCMenu.fromTop=190
oCMenu.rows=0
oCMenu.menuPlacement="left"

oCMenu.offlineRoot=""
oCMenu.onlineRoot=""
oCMenu.resizeCheck=1
oCMenu.wait=1000
oCMenu.fillImg="cm_fill.gif"
oCMenu.zIndex=0

//Background bar properties
oCMenu.useBar=1
oCMenu.barWidth="menu"
oCMenu.barHeight="menu"
oCMenu.barClass="clBar"
oCMenu.barX="menu"
oCMenu.barY="menu"
oCMenu.barBorderX=0
oCMenu.barBorderY=0
oCMenu.barBorderClass=""

//Level properties - ALL properties have to be spesified in level 0
oCMenu.level[0]=new cm_makeLevel() //Add this for each new level
oCMenu.level[0].width=180
oCMenu.level[0].height=22
oCMenu.level[0].regClass="clLevel0"
oCMenu.level[0].overClass="clLevel0over"
oCMenu.level[0].borderX=1
oCMenu.level[0].borderY=1
oCMenu.level[0].borderClass="clLevel0border"
oCMenu.level[0].offsetX=0
oCMenu.level[0].offsetY=0
oCMenu.level[0].rows=0
oCMenu.level[0].arrow="images/arrow_menu.gif"
oCMenu.level[0].arrowWidth=10
oCMenu.level[0].arrowHeight=20
oCMenu.level[0].align="right"

//EXAMPLE SUB LEVEL[1] PROPERTIES - You have to specify the properties you want different from LEVEL[0] - If you want all items to look the same just remove this
oCMenu.level[1]=new cm_makeLevel() //Add this for each new level (adding one to the number)
oCMenu.level[1].width=oCMenu.level[0].width-2
oCMenu.level[1].height=22
oCMenu.level[1].regClass="clLevel1"
oCMenu.level[1].overClass="clLevel1over"
oCMenu.level[1].borderX=1
oCMenu.level[1].borderY=1
oCMenu.level[1].align="right"
//oCMenu.level[1].offsetX=-(oCMenu.level[0].width-2)/2+20
oCMenu.level[1].offsetX=0
oCMenu.level[1].offsetY=0
oCMenu.level[1].borderClass="clLevel1border"
oCMenu.level[1].align="right"
oCMenu.level[0].arrow="images/arrow_menu.gif"
oCMenu.level[0].arrowWidth=10
oCMenu.level[0].arrowHeight=20



//EXAMPLE SUB LEVEL[2] PROPERTIES - You have to spesify the properties you want different from LEVEL[1] OR LEVEL[0] - If you want all items to look the same just remove this
oCMenu.level[2]=new cm_makeLevel() //Add this for each new level (adding one to the number)
oCMenu.level[2].width=oCMenu.level[0].width
oCMenu.level[2].height=20
oCMenu.level[2].offsetX=0
oCMenu.level[2].offsetY=0
oCMenu.level[2].regClass="clLevel2"
oCMenu.level[2].overClass="clLevel2over"
oCMenu.level[2].borderClass="clLevel2border"

<c:forEach items="${root.children}" var="section" varStatus="status">
    <c:set var="name" value="${section.name}"/>
    <c:if test="${section.propertyMap.orphan}"><c:set var="name" value="* ${name}"/></c:if>
    <c:if test="${!empty section.children[0]}"><c:set var="name" value="${name}"/></c:if>
    <c:url var="url" value="/cms/content.jsp?id=${section.id}"/>
    oCMenu.makeMenu('top<c:out value="${status.index}"/>','','&nbsp;<c:out value="${name}"/>','<c:out value="${url}"/>','');
    <c:forEach items="${section.children}" var="subsection" varStatus="substatus">
        <c:set var="subname" value="${subsection.name}"/>
        <c:if test="${subsection.propertyMap.orphan}"><c:set var="subname" value="* ${subname}"/></c:if>
        <c:if test="${!empty subsection.children[0]}">
            <c:set var="subname" value="${subname}"/>
        </c:if>
        <c:url var="suburl" value="/cms/content.jsp?id=${subsection.id}"/>
        oCMenu.makeMenu('sub<c:out value="${status.index}"/><c:out value="${substatus.index}"/>','top<c:out value="${status.index}"/>','&nbsp;<c:out value="${subname}" />','<c:out value="${suburl}"/>','');

        <c:forEach items="${subsection.children}" var="subsubsection" varStatus="subsubstatus">
            <c:set var="subsubname" value="${subsubsection.name}"/>
            <c:if test="${subsubsection.propertyMap.orphan}"><c:set var="subsubname" value="* ${subsubname}"/></c:if>
            <c:url var="subsuburl" value="/cms/content.jsp?id=${subsubsection.id}"/>
            oCMenu.makeMenu('sub<c:out value="${status.index}"/><c:out value="${substatus.index}"/><c:out value="${subsubstatus.index}"/>','sub<c:out value="${status.index}"/><c:out value="${substatus.index}"/>','&nbsp;<c:out value="${subsubname}"/>','<c:out value="${subsuburl}"/>','');
        </c:forEach>

    </c:forEach>
</c:forEach>

//setTimeout(10000,oCMenu.construct())
oCMenu.construct()
//-->
</script>

<%-- Dummy table for padding --%>
<table border="0">
<c:forEach items="${root.children}" var="section" varStatus="status">
    <tr><td height="22">&nbsp;</td></tr>
</c:forEach>
</table>

</x:template>

