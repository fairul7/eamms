<%@ include file="/common/header.jsp"  %>


</td>
</tr>
</table>
<!--- Close Middle : Left section & Content --->


<!--- Footer --->
<table cellpadding=0 cellspacing=0 border=0 width="780">
<tr>
<td valign=top align=left height=20><IMG SRC="images/clear.gif" height=20 width=1 border=0></td>
</tr>
<tr>
<td valign=top align=left height=3 bgcolor="CC0000"><IMG SRC="images/clear.gif" height=3 width=1 border=0></td>
</tr>
<tr>
<td valign=top align=left height=20 bgcolor="000000">
    &nbsp;
    <span class="footer">
    <x:template name="pages" type="TemplateDisplayChildren" properties="id=com.tms.cms.section.Section_Pages" body="custom">
        <c:forEach items="${pages.children}" var="page" varStatus="stat" >
            | <a class="footer" href="content.jsp?id=<c:out value='${page.id}'/>"><c:out value='${page.name}'/></a>
        </c:forEach>
    </x:template>
    </span>
    <x:template type="TemplateDisplaySpot" properties="id=com.tms.cms.spot.Spot_FooterSpot" />
</td>
</tr>
</table>
<!--- Close Footer --->

</body>
</HTML>



