<%@ page import="kacang.ui.Widget,
                 com.tms.cms.ad.ui.DisplayAd,
                 com.tms.cms.ad.model.Ad,
                 com.tms.cms.ad.model.AdLocation"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%
    DisplayAd w = (DisplayAd) request.getAttribute("widget");
    String imageDimension = "";

    if(w.getAdLocation()!=null) {
        switch(w.getAdLocation().getAdType()) {
            case AdLocation.AD_TYPE_FULL_BANNER_468_x_60:
                imageDimension = "width=468 height=60"; break;

            case AdLocation.AD_TYPE_HALF_BANNER_234_x_60:
                imageDimension = "width=234 height=60";
                break;

            case AdLocation.AD_TYPE_MICRO_BAR_88_x_31:
                imageDimension = "width=88 height=31";
                break;

            case AdLocation.AD_TYPE_BUTTON_1_120_x_90:
                imageDimension = "width=120 height=90";
                break;

            case AdLocation.AD_TYPE_BUTTON_2_120_x_60:
                imageDimension = "width=120 height=60";
                break;

            case AdLocation.AD_TYPE_WIDE_SKYSCRAPER_160_x_600:
                imageDimension = "width=160 height=600";
                break;

            case AdLocation.AD_TYPE_SKYSCRAPER_120_x_600:
                imageDimension = "width=120 height=600";
                break;

            case AdLocation.AD_TYPE_SQUARE_BUTTON_125_x_125:
                imageDimension = "width=125 height=125";
                break;

            case AdLocation.AD_TYPE_RECTANGLE_180_x_150:
                imageDimension = "width=180 height=150";
                break;

            case AdLocation.AD_TYPE_VERTICAL_BANNER_120_x_240:
                imageDimension = "width=120 height=240";
                break;

            case AdLocation.AD_TYPE_MEDIUM_RECTANGLE_300_x_250:
                imageDimension = "width=300 height=250";
                break;

            case AdLocation.AD_TYPE_SQUARE_POP_UP_250_x_250:
                imageDimension = "width=250 height=250";
                break;

            case AdLocation.AD_TYPE_VERTICAL_RECTANGLE_240_x_400:
                imageDimension = "width=240 height=400";
                break;

            case AdLocation.AD_TYPE_LARGE_RECTANGLE_336_x_280:
                imageDimension = "width=336 height=280";
                break;

            case AdLocation.AD_TYPE_CUSTOM_FREE_SIZE:
            default:
                imageDimension = "";
        }
    }

%>
<%--
<c:if test="${widget.errorMessage ne null}">
    <font color=red>{ DisplayAd: <c:out value="${widget.errorMessage}" /> }</font>
</c:if>
--%>
<c:if test="${!empty widget.ad && widget.errorMessage eq null}">
    <%-- scripted ad --%>
    <c:if test="${widget.ad.useScript}">
        <c:out value="${widget.ad.script}" escapeXml="false"  />
    </c:if>

    <%-- image (normal) ad --%>
    <c:if test="${!widget.ad.useScript}">
        <%-- display image --%>
        <a
            <c:if test="${widget.ad.newWindow}">target="adWindow"</c:if>
            href="<c:out value="${widget.adClickUrl}?adId=${widget.ad.adId}" /><c:if test="${widget.preview}">&preview=true</c:if>"
        ><img
            src="<c:url value="/storage/${widget.ad.imageFile}" />"
            alt="<c:out value="${widget.ad.alternateText}" />"
            <%= imageDimension %>
            border="0"></a>
    </c:if>
</c:if>