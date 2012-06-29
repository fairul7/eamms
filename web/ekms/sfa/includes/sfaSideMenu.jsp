<%@ page import="kacang.ui.WidgetManager,
                 com.tms.crm.sales.misc.AccessUtil"%>
<table width="100%" border="0" cellspacing="0" cellpadding="1" class="menuBgOutline">
    <tr>
        <td>
            <table width="100%" border="0" cellpadding="1" cellspacing="0" class="menuBgBackground">
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="menuBgColor">
                            <%--START: CALENDAR menu title
                            <%
                                WidgetManager wm = (WidgetManager)request.getAttribute("wm");
                                String userId = wm.getUser().getId();

                                SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
                                if(ss.hasPermission(userId, CalendarModule.PERMISSION_CALENDARING,
                                        null,null)){
                            %>--%>
                            <tr>
                                <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
                                    <table cellpadding="0" cellspacing="0" width="100%">
                                        <tr>
                                            <td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
                                            <td class="menuHeader" ><fmt:message key='sfa.message.salesForceAutomation'/></td>
                                            <td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/system/blank.gif" width="5" height="1"></td></tr>
                            <%--END: CALENDAR menu title--%>
                            <%--START: Intelligence Summary--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/main.jsp" onclick="document.location ='<c:url value="/ekms" />/calendar/appointmentform.jsp?init=1';"><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.main'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
<%--
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
--%>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Intelligence Summary--%>
                                   <%    WidgetManager wm     = WidgetManager.getWidgetManager(request);
                            	String        userID = wm.getUser().getId();
                                           if(AccessUtil.isSalesPerson(userID)||AccessUtil.isSalesManager(userID)||AccessUtil.isExternalSalesPerson(userID))
                                           {
                                      %>


                            <tr>
                                <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
                                    <table cellpadding="0" cellspacing="0" width="100%">
                                        <tr>
                                            <td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
                                            <td class="menuHeader" ><fmt:message key='sfa.message.opportunities'/></td>
                                            <td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/system/blank.gif" width="5" height="1"></td></tr>


                            <%--START: New Opportunity--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/newopportunity_company_list.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.newOpportunity'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: View All Opportunites--%>
                   <%}%>
                  <%


                                if(AccessUtil.isSalesManager(userID))
                                {
                           %>
                            <%--START: View All Opportunites--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/allopportunitytable.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.viewAllOpportunites'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: View All Opportunites--%>
                            <%}%>
                            <%--START: View My Opportunites--%>
                            <%
                            ///	WidgetManager wm     = WidgetManager.getWidgetManager(request);
                            //	String        userID = wm.getUser().getId();

                                if(AccessUtil.isSalesPerson(userID)||AccessUtil.isSalesManager(userID)||AccessUtil.isExternalSalesPerson(userID))
                                {
                           %>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/myopportunitytable.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.viewMyOpportunities'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%
                                }
                            %>
                            <%--END: View My Opportunites--%>

                            <%--START: View All Closed Sales--%>
                            <%
                                if(AccessUtil.isSalesManager(userID))
                                {
                           %>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/view_all_sales.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.viewAllSales'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%
                                }
                            %>
                            <%--END: View All Closed Sales--%>

                            <%--START: View My Closed Sales--%>
                            <%
                                if(AccessUtil.isSalesPerson(userID)||AccessUtil.isSalesManager(userID)||AccessUtil.isExternalSalesPerson(userID))
                                {
                           %>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/view_my_sales.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.viewMySales'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
<%--
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
--%>
                            <%
                                }
                            %>
                            <%--END: View My Closed Sales--%>

 <%
                                if(AccessUtil.isSalesPerson(userID)||AccessUtil.isSalesManager(userID)||AccessUtil.isExternalSalesPerson(userID))
                                {
                           %>

                            <%--START: Leads menu title--%>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/system/blank.gif" width="5" height="1"></td></tr>
                            <tr>
                                <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
                                    <table cellpadding="0" cellspacing="0" width="100%">
                                        <tr>
                                            <td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
                                            <td class="menuHeader" ><fmt:message key='sfa.message.leads'/></td>
                                            <td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/system/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Leads menu title--%>

                            <%--START: New Lead--%>

                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/addLead.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.newLead'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: New Lead --%>

                            <%--START: View All Leads--%>

                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/leadTable.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.viewAllLeads'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: View All Leads --%>

                            <%--START: View My Leads--%>

                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/leadTableOwn.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.viewMyLeads'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: View My Leads --%>




                            <%--START: Companies menu title--%>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/system/blank.gif" width="5" height="1"></td></tr>
                            <tr>
                                <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
                                    <table cellpadding="0" cellspacing="0" width="100%">
                                        <tr>
                                            <td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
                                            <td class="menuHeader" ><fmt:message key='sfa.message.menuCompanies'/></td>
                                            <td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/system/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Companies menu title--%>

                            <%--START: Companies Listing--%>

                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/companies_listing.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.companiesListing'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Companies Listing--%>

                            <%--START: New Company--%>

                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/newcompany.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.newCompany'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: New Company--%>


                            <%--START: Contact Listing--%>

                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/contact_listing.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.contactListing'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Contact Listing--%>


                            <%--START: New Contact--%>

                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/newcontact_contact_list.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.newContact'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
<%--
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
--%>
                            <%--END: New Contact--%>


                           <%
                                }
                            %>
                        <%
                                    if(AccessUtil.isSalesManager(userID))
                                    {
                                               %>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>

                            <tr>
                                <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
                                    <table cellpadding="0" cellspacing="0" width="100%">
                                        <tr>
                                            <td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
                                            <td class="menuHeader" ><fmt:message key='sfa.message.report'/></td>
                                            <td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/system/blank.gif" width="5" height="1"></td></tr>

                            <%--START: Opportunity By Company--%>

                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/reportByCompanyForm.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.opportuniyReport-ByCompany'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Opportunity By Company--%>

                            <%--START: Opportunity By Individuals--%>

                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/reportByIndividualsForm.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.opportuniyReport-ByIndividuals'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Opportunity By  Individuals--%>

                            <%--START: Completed Sales Report--%>

                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/reportCompletedSalesForm.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.completedSalesReport'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Completed Sales Report--%>


                            <%--START: Opportunity By Individuals--%>

                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/reportLostSalesForm.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.lostOpportuniyReport'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Opportunity By  Individuals--%>


                            <%--START: Company Type--%>




                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>

                            <tr>
                                <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
                                    <table cellpadding="0" cellspacing="0" width="100%">
                                        <tr>
                                            <td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
                                            <td class="menuHeader" ><fmt:message key='sfa.message.setup'/></td>
                                            <td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/system/blank.gif" width="5" height="1"></td></tr>


                            <%--START: Contact Type--%>

                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/setup_contacttype.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.contactType'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Contact Type--%>

                            <%--START: Company Type--%>

                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/setup_companytype.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.companyType'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Company Type--%>


                            <%--START: Group--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/setup_group.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.group'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Group--%>

                            <%--START: Product--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/setup_product.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.product'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Product--%>

                            <%--START: Projection--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/setup_projection.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.projection'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Projection--%>

                            <%--START: Salutation--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/setup_salutation.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.salutation'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Salutation--%>


                            <%--START: Source--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/sfa/setup_source.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='sfa.message.source'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Source--%>

                            <%
                                    }else{

                            %>
                                                        <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                                                        <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                                                        <%}%>



<%--
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
--%>
                            <tr><td colspan="3" height="28" align="center">&nbsp;</td></tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
