<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" scope="request" value="${widget.contentObject}"/>

<x:template type="TemplateDisplayFrontEndEdit" properties="id=${co.id}" />

<p>
<span class="contentName"><c:out value="${co.name}"/></span>
<br>
<span class="contentDate"><fmt:formatDate pattern="${globalDatetimeLong}" value="${co.date}"/></span>
<br>
<span class="contentAuthor"><c:out value="${co.author}"/></span>
</p>

<p>
<span class="contentBody">
<c:out value="${co.summary}" escapeXml="false" />
</span>
</p>

<c:if test="${!empty co.fileName}">

<hr size=1>
<a name=preview><b>Online Multimedia Preview</b> <small>(Content type: <c:out value="${co.contentType}" />)</small>
<br>

<br>

<c:choose>
    <c:when test="${co.contentType eq 'image/jpeg' || co.contentType eq 'image/pjpeg' || co.contentType eq 'image/gif' || co.contentType eq 'image/png' }">
        <img src="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>" border="0">
    </c:when>
    <c:when test="${co.contentType eq 'application/x-ms-reader'}">
        <a href="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>">Click here</a> to read the e-book.
        <br><br>
        <A HREF="http://www.microsoft.com/reader/downloads/default.asp"><IMG SRC="<%= request.getContextPath() %>/cms_library/images/download_logo.gif" border="0" width="85" height="32" alt="Microsoft Reader: Download Now"> Download Microsoft Reader eBooks</A>
    </c:when>
    <c:when test="${co.contentType eq 'audio/x-pn-realaudio'}">
        <EMBED SRC="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>" WIDTH=320 HEIGHT=240 NOJAVA=true CONTROLS=ImageWindow CONSOLE=one>
        <EMBED SRC="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>" WIDTH=320 HEIGHT=100 NOJAVA=true CONTROLS=All CONSOLE=one>
    </c:when>
    <c:when test="${co.contentType eq 'video/quicktime'}">
        <EMBED src="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>" href="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>"
        width="640" height="480"
        ></EMBED>
    </c:when>
    <c:when test="${co.contentType eq 'application/pdf'}">
        <EMBED src="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>" href="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>"
        width="640" height="480"
        ></EMBED>
    </c:when>
    <c:when test="${co.contentType eq 'video/mpeg' || co.contentType eq 'video/avi' || co.contentType eq 'video/x-msvideo' || co.contentType eq 'video/x-ms-wmv'}" >
        <OBJECT ID="MediaPlayer" classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95"
          CODEBASE="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=5,1,52,701"
          width=320 height=240
          standby="Loading Microsoft Windows Media Player components..."
          type="application/x-oleobject">
            <PARAM NAME="FileName"           VALUE="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>">
            <PARAM NAME="TransparentAtStart" Value="false">
            <PARAM NAME="AutoStart"          Value="false">
            <PARAM NAME="AnimationatStart"   Value="false">
            <PARAM NAME="ShowControls"       Value="true">
            <PARAM NAME="autoSize" 		 Value="false">
            <PARAM NAME="displaySize" 	 Value="0">
            <Embed type="application/x-mplayer2"
              pluginspage="http://www.microsoft.com/Windows/MediaPlayer/"
              src="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>"
              Name=MediaPlayer
              AutoStart=0
              Width=320
              Height=240
              transparentAtStart=0
              autostart=0
              animationAtStart=0
              ShowControls=0
              autoSize=0
              displaySize=0></embed>
        </OBJECT>
    </c:when>
    <c:when test="${co.contentType eq 'audio/wav'}" >
        <OBJECT ID="MediaPlayer" classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95"
          CODEBASE="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=5,1,52,701"
          width=320 height=50
          standby="Loading Microsoft Windows Media Player components..."
          type="application/x-oleobject">
            <PARAM NAME="FileName"           VALUE="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>">
            <PARAM NAME="TransparentAtStart" Value="true">
            <PARAM NAME="AutoStart"          Value="false">
            <PARAM NAME="AnimationatStart"   Value="false">
            <PARAM NAME="ShowControls"       Value="true">
            <PARAM NAME="autoSize" 		 Value="false">
            <PARAM NAME="displaySize" 	 Value="0">
            <Embed type="application/x-mplayer2"
              pluginspage="http://www.microsoft.com/Windows/MediaPlayer/"
              src="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>"
              Name=MediaPlayer
              AutoStart=0
              Width=320
              Height=50
              transparentAtStart=0
              autostart=0
              animationAtStart=0
              ShowControls=0
              autoSize=0
              displaySize=0></embed>
        </OBJECT>
    </c:when>

    <c:otherwise>
        <span class="contentSubheader">No viewer is associated to this file type.
        You should download this digital content for offline usage.
        See "Download Digital Content" section below.</span>
    </c:otherwise>
</c:choose>


<p>
    <hr size=1>
    <a name=download><span class="contentHeader">Download Digital Content</span>
    <p>
    <a class="contentOptionLink" href="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>" target="_blank"><c:out value="${co.fileName}"/></a>
    <span class="contentOption">
    <br>
    <c:out value="${co.fileSize}"/> bytes
    <br>
    <c:out value="${co.contentType}"/>
    </span>
</p>
</c:if>

<%--Display commentary--%>
<p>
<jsp:include page="displayContentCommentary.jsp" flush="true" />
</p>
