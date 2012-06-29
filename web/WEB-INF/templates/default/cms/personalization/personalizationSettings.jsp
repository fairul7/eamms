<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${requestScope.personalizationSettingsForm}"/>
<c:if test="${!(empty(widget.setting))}">
    <script language="javascript">
        //Adding Members
        function addMember(strForm, strNonMembers, strMembers)
        {
            members = document.forms[strForm].elements[strMembers];
            nonMembers = document.forms[strForm].elements[strNonMembers];
            if(nonMembers.length>0 && nonMembers.options[0].value==-1) return;
            for(c=0; c<nonMembers.length; c++)
            {
                if(nonMembers.options[c].selected)
                {
                    if(members.length>0 && members.options[0].value==-1) members.options[0] = null;
                    members.options[members.length] = new Option();
                    for(c2=members.length-1; c2>0; c2--)
                    {
                        members.options[c2].text = members.options[c2-1].text;
                        members.options[c2].value = members.options[c2-1].value;
                    }
                    o = new Option(nonMembers.options[c].text, nonMembers.options[c].value, false, true);
                    members.options[0] = o;
                    nonMembers.options[c--] = null;
                }
            }
            /**
            if(nonMembers.length==0)
            {
                nonMembers.options[0] = new Option();
                nonMembers.options[0].text = '-<fmt:message key='personalization.label.notAvailable'/>-';
                nonMembers.options[0].value= '-1';
            }
            */
        }
        //Remove Members
        function removeMember(strForm, strNonMembers, strMembers)
        {
            members = document.forms[strForm].elements[strMembers];
            nonMembers = document.forms[strForm].elements[strNonMembers];
            if(members.length>0 && members.options[0].value==-1) return;
            for(c=0; c<members.length; c++)
            {
                if(members.options[c].selected)
                {
                    if(nonMembers.length>0 && nonMembers.options[0].value==-1) nonMembers.options[0] = null;
                    nonMembers.options[nonMembers.length] = new Option();
                    for(c2=nonMembers.length-1; c2>0; c2--)
                    {
                        nonMembers.options[c2].text = nonMembers.options[c2-1].text;
                        nonMembers.options[c2].value = nonMembers.options[c2-1].value;
                    }
                    o = new Option(members.options[c].text, members.options[c].value, false, true);
                    nonMembers.options[0] = o;
                    members.options[c--] = null;
                }
            }
            /**
            if(members.length==0)
            {
                members.options[0] = new Option();
                members.options[0].text = '-<fmt:message key='personalization.label.notAvailable'/>-';
                members.options[0].value= '-1';
            }
            */
        }
        //Select Members
        function selectMembers(strForm, strNonMembers, strMembers)
        {
            members = document.forms[strForm].elements[strMembers];
            nonMembers = document.forms[strForm].elements[strNonMembers];

            for(i=0; i < nonMembers.length; i++) nonMembers.options[i].selected = true;
            for(c=0; c < members.length; c++) members.options[c].selected = true;

            return true;
        }
    </script>
    <form method="post" action="<c:out value="${widget.url}"/>" name="frmPersonalization" onSubmit="selectMembers('frmPersonalization', 'excludedForums', 'includedForums'); selectMembers('frmPersonalization', 'excludedSections', 'includedSections');">
        <table width="100%" cellpadding="2" cellspacing="0">
            <tr>
                <td class="personalizationLabel" width="30%" valign="top"><fmt:message key='general.label.sections'/></td>
                <td class="personalizationLabel">
                    <table>
                        <tr>
                            <td width="45%" align="center" class="personalizationLabel">
                                <b><fmt:message key='personalization.label.availableSections'/></b><br>
                                <select name="excludedSections" size="10" multiple>
                                    <c:forEach items="${widget.availableSections}" var="option">
                                        <option value="<c:out value="${option.id}"/>"><c:out value="${option.name}"/></option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td width="10%" align="center" class="personalizationLabel">
                                <input type="button" class="button" name="sectionLeftButton" value=">>" onClick="addMember('frmPersonalization', 'excludedSections', 'includedSections');">
                                <br><br>
                                <input type="button" class="button" name="sectionRightButton" value="<<" onClick="removeMember('frmPersonalization', 'excludedSections', 'includedSections');">
                            </td>
                            <td width="45%" align="center" class="personalizationLabel">
                                <b><fmt:message key='personalization.label.selectedSections'/></b><br>
                                <select name="includedSections" size="10" multiple>
                                    <c:forEach items="${widget.setting.sections}" var="option">
                                        <option value="<c:out value="${option.id}"/>"><c:out value="${option.name}"/></option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr><td class="personalizationLabel" colspan="2">&nbsp;</td></tr>
            <tr>
                <td class="personalizationLabel" width="30%" valign="top"><fmt:message key='general.label.forums'/></td>
                <td class="personalizationLabel">
                    <table>
                        <tr>
                            <td width="45%" align="center" class="personalizationLabel">
                                <b><fmt:message key='personalization.label.availableForums'/></b><br>
                                <select name="excludedForums" size="10" multiple>
                                    <c:forEach items="${widget.availableForums}" var="option">
                                        <option value="<c:out value="${option.forumId}"/>"><c:out value="${option.name}"/></option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td width="10%" align="center" class="personalizationLabel">
                                <input type="button" class="button" name="forumLeftButton" value=">>" onClick="addMember('frmPersonalization', 'excludedForums', 'includedForums');" class="personalizationButton">
                                <br><br>
                                <input type="button" class="button" name="forumRightButton" value="<<" onClick="removeMember('frmPersonalization', 'excludedForums', 'includedForums');" class="personalizationButton">
                            </td>
                            <td width="45%" align="center" class="personalizationLabel">
                                <b><fmt:message key='personalization.label.selectedForums'/></b><br>
                                <select name="includedForums" size="10" multiple>
                                    <c:forEach items="${widget.setting.forums}" var="option">
                                        <option value="<c:out value="${option.forumId}"/>"><c:out value="${option.name}"/></option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td class="personalizationLabel" width="30%" valign="top"><fmt:message key='personalization.label.articlesToDisplay'/>:</td>
                <td class="personalizationLabel">
                    <select name="numberArticles">
                        <option value="5" <c:if test="${widget.setting.numberArticles == 5}">SELECTED</c:if>>5</option>
                        <option value="10" <c:if test="${widget.setting.numberArticles == 10}">SELECTED</c:if>>10</option>
                        <option value="15" <c:if test="${widget.setting.numberArticles == 15}">SELECTED</c:if>>15</option>
                        <option value="20" <c:if test="${widget.setting.numberArticles == 20}">SELECTED</c:if>>20</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="personalizationLabel" width="30%" valign="top"><fmt:message key='personalization.label.topicsToDisplay'/>:</td>
                <td class="personalizationLabel">
                    <select name="numberTopics">
                        <option value="5" <c:if test="${widget.setting.numberTopics == 5}">SELECTED</c:if>>5</option>
                        <option value="10" <c:if test="${widget.setting.numberTopics == 10}">SELECTED</c:if>>10</option>
                        <option value="15" <c:if test="${widget.setting.numberTopics == 15}">SELECTED</c:if>>15</option>
                        <option value="20" <c:if test="${widget.setting.numberTopics == 20}">SELECTED</c:if>>20</option>
                    </select>
                </td>
            </tr>
            <tr><td colspan="2">&nbsp;</td></tr>
            <tr>
                <td>&nbsp;</td>
                <td>
                    <input type="hidden" name="action" value="Personalize">
                    <input type="submit" class="button" value="<fmt:message key='personalization.label.personalize'/>" class="personalizationButton">
                </td>
            </tr>
        </table>
    </form>
</c:if>