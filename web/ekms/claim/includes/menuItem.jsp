<c:set var="link"><c:out value="${pageScope.link}" default="!Missing param.link!" /></c:set>
<c:set var="text"><c:out value="${pageScope.text}" default="!Missing param.text!" escapeXml="false" /></c:set>

<%-- BEGIN: Item --%>
<TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#1059A5" CLASS="menuBgColorHighlight"><IMG SRC="images/system/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
<TR>
    <TD HEIGHT="28" WIDTH="20%" ALIGN="CENTER"><IMG SRC="/ekms/images/folder.gif" WIDTH="15" HEIGHT="17"></TD>
    <TD HEIGHT="28" WIDTH="80%">
        <c:if test="${!empty pageScope.html}">
            <c:out value="${pageScope.html}" escapeXml="false" />
        </c:if>
        <c:if test="${empty pageScope.html}">
            <a href="<c:out value="${link}" />"><FONT COLOR="#FFFFFF" CLASS="menuFont"><b><c:out value="${text}" escapeXml="false" /></b></FONT></a>
        </c:if>
        <%
            pageContext.removeAttribute("html");
        %>
    </TD>
</TR>
<TR><TD COLSPAN="2" ALIGN="CENTER" BGCOLOR="#000000" CLASS="menuBgColorShadow"><IMG SRC="/ekms/images/blank.gif" WIDTH="5" HEIGHT="1"></TD></TR>
<%-- ENG: Item --%>
