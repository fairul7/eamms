<%@ page import="com.tms.cms.core.model.ContentManager,
                 kacang.Application"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="co" scope="request" value="${widget}"/>

      <table cellpadding="3" cellspacing="0" border="0" style="text-align: left; width:180px;border:1px solid gray;">
          <tbody>
            <tr>
              <td class="tableRow">
                <fmt:message key='cms.label.contentOptions'/>
              </td>
            </tr>

            <c:if test="${co.permissionMap['Create']}">
            <tr>
              <td class="optioncell"><img src="../images/showdetailicon.gif">
                <x:event name="${co.absoluteName}" html="class='option'" type="Create"><fmt:message key='general.label.addNewContent'/></x:event>
              </td>
            </tr>
            </c:if>

            <c:if test="${co.permissionMap['Preview']}">
            <tr>
              <td class="optioncell">
              </td>
            </tr>
            <tr>
              <td class="optioncell"><img src="../images/showdetailicon.gif">
                <a href="../../cmspreview/content.jsp?id=<c:out value='${co.id}'/>" class="option" target="_blank"><fmt:message key='general.label.previewContent'/></a>
              </td>
            </tr>
            </c:if>

            <c:if test="${co.permissionMap['CheckOut']}">
            <tr>
              <td class="optioncell">
              </td>
            </tr>
            <tr>
              <td class="optioncell"><img src="../images/showdetailicon.gif">
                <c:choose>
                <c:when test="${co.checkIn}"><x:event name="${co.absoluteName}" html="class='option'" type="CheckOut"><fmt:message key='general.label.checkIn'/></x:event></c:when>
                <c:otherwise><x:event name="${co.absoluteName}" html="class='option'" type="CheckOut"><fmt:message key='general.label.checkOut'/></x:event></c:otherwise>
                </c:choose>
              </td>
            </tr>
            </c:if>

            <c:if test="${co.permissionMap['UndoCheckOut']}">
            <tr>
              <td class="optioncell">
              </td>
            </tr>
            <tr>
              <td class="optioncell"><img src="../images/showdetailicon.gif">
                <x:event name="${co.absoluteName}" html="class='option'" type="UndoCheckOut"><fmt:message key='general.label.undoCheckOut'/></x:event>
              </td>
            </tr>
            </c:if>

            <c:if test="${co.permissionMap['Approve']}">
            <tr>
              <td class="optioncell">
              </td>
            </tr>
            <tr>
              <td class="optioncell"><img src="../images/showdetailicon.gif">
                <x:event name="${co.absoluteName}" html="class='option'" type="Approve"><fmt:message key='general.label.approveRejectContent'/></x:event>
              </td>
            </tr>
            </c:if>

            <c:if test="${co.permissionMap['Publish']}">
            <tr>
              <td class="optioncell">
              </td>
            </tr>
            <tr>
              <td class="optioncell"><img src="../images/showdetailicon.gif">
                <x:event name="${co.absoluteName}" html="class='option'" type="Publish"><fmt:message key='general.label.publishWithdrawContent'/></x:event>
              </td>
            </tr>
            </c:if>

            <tr>
              <td class="optioncell"><img src="../images/clear.gif" border="0" height="5"></td>
            </tr>
            <tr>
              <td class="tableRow">
                <fmt:message key='cms.label.moreOptions'/> [<b><a href="#" onclick="treeToggle('contentOptionsMore_<c:out value="${widget.name}"/>');return false" id="icon_contentOptionsMore_<c:out value="${widget.name}"/>">+</a></b>]
              </td>
            </tr>
            <tr>
            <td class="optioncell">

                <div id="contentOptionsMore_<c:out value="${widget.name}"/>" style="display: none">
                <table>

                <c:if test="${co.permissionMap['Archive']}">
                <tr>
                  <td class="optioncell">
                  </td>
                </tr>
                <tr>
                  <td class="optioncell"><img src="../images/showdetailicon.gif">
                    <x:event name="${co.absoluteName}" html="class='option'" type="Archive"><fmt:message key='general.label.archiveUnarchiveContent'/></x:event>
                  </td>
                </tr>
                </c:if>

                <c:if test="${co.permissionMap['Delete']}">
                <tr>
                  <td class="optioncell">
                  </td>
                </tr>
                <tr>
                  <td class="optioncell"><img src="../images/showdetailicon.gif">
                    <x:event name="${co.absoluteName}" html="class='option'" type="Delete"><fmt:message key='general.label.deleteContent'/></x:event>
                  </td>
                </tr>
                </c:if>

                <c:if test="${co.permissionMap['Move']}">
                <tr>
                  <td class="optioncell">
                  </td>
                </tr>
                <tr>
                  <td class="optioncell"><img src="../images/showdetailicon.gif">
                    <x:event name="${co.absoluteName}" html="class='option'" type="Move"><fmt:message key='general.label.moveContent'/></x:event>
                  </td>
                </tr>
                </c:if>

                <c:if test="${co.permissionMap['Keywords']}">
                <tr>
                  <td class="optioncell">
                  </td>
                </tr>
                <tr>
                  <td class="optioncell"><img src="../images/showdetailicon.gif">
                    <x:event name="${co.absoluteName}" html="class='option'" type="Keywords"><fmt:message key='general.label.keywords'/></x:event>
                  </td>
                </tr>
                </c:if>

            <!-- add for taxonomy -->
            <%
            	boolean isTaxonomy = false;
            	try {
            		String sTaxonomy = Application.getInstance().getProperty("com.tms.cms.taxonomy");
            		if (sTaxonomy!=null && sTaxonomy.equals("true")) {
            			isTaxonomy=true;
            		}
            	}catch(Exception e) {}
            	if (isTaxonomy) {
            %>
                <c:if test="${co.permissionMap['Taxonomy']}">
                <tr>
                  <td class="optioncell">
                  </td>
                </tr>
                <tr>
                  <td class="optioncell"><img src="../images/showdetailicon.gif">
                    <x:event name="${co.absoluteName}" html="class='option'" type="Taxonomy"><fmt:message key='general.label.taxonomy'/></x:event>
                  </td>
                </tr>
                </c:if>
			<%
            	}
			%>	

                <c:if test="${co.permissionMap['Reorder']}">
                <tr>
                  <td class="optioncell">
                  </td>
                </tr>
                <tr>
                  <td class="optioncell"><img src="../images/showdetailicon.gif">
                    <x:event name="${co.absoluteName}" html="class='option'" type="Reorder"><fmt:message key='general.label.reorderContent'/></x:event>
                  </td>
                </tr>
                </c:if>

                <%
                    if (Boolean.valueOf(Application.getInstance().getProperty(ContentManager.APPLICATION_PROPERTY_CONTENT_SUBSCRIPTION)).booleanValue()) {
                %>
                <x:permission permission="com.tms.cms.ManageSubscribeContent" module="com.tms.cms.core.model.ContentManager">
                <tr>
                  <td class="optioncell">
                  </td>
                </tr>
                <tr>
                  <td class="optioncell"><img src="../images/showdetailicon.gif">
                    <x:event name="${co.absoluteName}" html="class='option'" type="Subscriptions"><fmt:message key='security.label.subscriptions'/></x:event>
                  </td>
                </tr>
                </x:permission>
                <%
                    }
                %>


                </table>
                </div>
            </td>
            </tr>

            <tr>
              <td class="optioncell"><img src="../images/clear.gif" border="0" height="5"></td>
            </tr>
            <tr>
              <td class="tableRow">
                <script src="<c:url value='/common/tree/tree.js'/>">
                </script>
                <fmt:message key='cms.label.securityAndAuditing'/> [<b><a href="#" onclick="treeToggle('contentOptionsSecurity_<c:out value="${widget.name}"/>');return false" id="icon_contentOptionsSecurity_<c:out value="${widget.name}"/>">+</a></b>]
              </td>
            </tr>
            <tr>
            <td class="optioncell">

            <div id="contentOptionsSecurity_<c:out value="${widget.name}"/>" style="display: none">
                <table>
                    <c:if test="${co.permissionMap['AclView']}">
                    <tr>
                      <td class="optioncell">
                      </td>
                    </tr>
                    <tr>
                      <td class="optioncell"><img src="../images/showdetailicon.gif">
                        <x:event name="${co.absoluteName}" html="class='option'" type="AclView"><fmt:message key='general.label.permissions'/></x:event>
                      </td>
                    </tr>
                    </c:if>

                    <c:if test="${co.permissionMap['History']}">
                    <tr>
                      <td class="optioncell">
                      </td>
                    </tr>
                    <tr>
                      <td class="optioncell"><img src="../images/showdetailicon.gif">
                        <x:event name="${co.absoluteName}" html="class='option'" type="History"><fmt:message key='general.label.versionHistory'/></x:event>
                      </td>
                    </tr>
                    </c:if>

                    <c:if test="${co.permissionMap['AuditView']}">
                    <tr>
                      <td class="optioncell">
                      </td>
                    </tr>
                    <tr>
                      <td class="optioncell"><img src="../images/showdetailicon.gif">
                        <x:event name="${co.absoluteName}" html="class='option'" type="AuditView"><fmt:message key='general.label.auditTrail'/></x:event>
                      </td>
                    </tr>
                    </c:if>

                    <c:if test="${co.permissionMap['ReportView']}">
                    <tr>
                      <td class="optioncell">
                      </td>
                    </tr>
                    <tr>
                      <td class="optioncell"><img src="../images/showdetailicon.gif">
                        <x:event name="${co.absoluteName}" html="class='option'" type="ReportView"><fmt:message key='general.label.statistics'/></x:event>
                      </td>
                    </tr>
                    </c:if>

                </table>
                </div>
            </td>
            </tr>
            <tr>
              <td class="optioncell"><img src="../images/clear.gif" border="0" height="5"></td>
            </tr>

          </tbody>
        </table>


