<%@include file="includes/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<x:config>
    <page name="emailFilterConfigPage">
        <com.tms.collab.messaging.ui.FilterConfigForm name="filterConfigForm" />
    </page>
</x:config>

<%@include file="includes/header.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="2">
    <c:choose>
    <c:when test="${! empty param.id}">
    <tr valign="center" align="left">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
            <b><font color="#FFCF63" class="contentTitleFont">
                &nbsp;<fmt:message key='messaging.label.manageFilters'/> > <fmt:message key='messaging.title.editemailFilterConfig'/>
            </font></b>
        </td>
    <tr>
        </c:when>
        <c:otherwise>
    <tr valign="center" align="left">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
            <b><font color="#FFCF63" class="contentTitleFont">
                &nbsp;<fmt:message key='messaging.label.manageFilters'/> > <fmt:message key='messaging.title.emailFilterConfig'/>
            </font></b>
        </td>
    <tr>
        </c:otherwise>
        </c:choose>


    <tr valign="middle">
        <!--td align="center" valign="top" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td-->
        <td align="center" valign="top" bgcolor="#EFEFEF" class="contentBgColor">

            <!-- here comes the content -->
            <table valign="top" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr valign="middle">
                    <td align="center" height="22" bgcolor="#EFEFEF" class="contentBgColor">
                        <x:display name="emailFilterConfigPage.filterConfigForm" />
                    </td>
                </tr>
            </table>
            <!-- end of content -->


        </td>
        <!--td align="center" valign="top" bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td-->
    </tr>
</table>
<%@include file="includes/footer.jsp" %>


