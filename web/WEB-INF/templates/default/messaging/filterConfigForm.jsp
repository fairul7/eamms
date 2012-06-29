<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<!--!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"-->

<!--
	Template for com.tms.collab.messaging.ui.FilterConfigForm.java
-->
<c:set var="form" value="${widget}"/>
<c:set var="tabwidth" value="10%" />

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	<tr>
		<td align="left">
			<jsp:include page="../form_header.jsp" flush="true"></jsp:include>
			<table width="100%" cellpadding="3" cellspacing="1">
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.absoluteName}.lblName" />
					</td>
					<td class="classRow">
						<x:display name="${form.absoluteName}.txtFldName" />
					</td>
				</tr>


				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<fmt:message key="messaging.filter.filterStatus" />
					</td>
					<td class="classRow">
						<x:display name="${form.absoluteName}.chkboxActive" />
					</td>
				</tr>


				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.absoluteName}.lblCondition"/>
					</td>
					<td class="classRow">
						<x:display name="${form.absoluteName}.radioAll"/>&nbsp;
						<x:display name="${form.absoluteName}.radioAny"/>&nbsp;
					</td>
				</tr>
				
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.absoluteName}.lblRules" /><br/>
						<font style="font-weight: normal;"><x:display name="${form.absoluteName}.lblRuleNote" /></font>
					</td>
					<td class="classRow">
						<table>
						<c:forEach items="${form.rulesWidgetIds}" var="rule" varStatus="status">
						<tr>
       						<td align="left">
       							<x:display name="${form.absoluteName}.chkBoxCriteria${rule}" />
       							<x:display name="${form.absoluteName}.selBoxCriteria${rule}" />
       							<x:display name="${form.absoluteName}.selBoxPredicate${rule}" />
	   							<x:display name="${form.absoluteName}.txtFldPredicate${rule}" />
	   						</td>
						</tr>
						</c:forEach>
						<tr>
							<td align="left"><x:display name="${form.absoluteName}.btnAddRule" />&nbsp;
								<x:display name="${form.absoluteName}.btnDeleteRule" /></td>
						</tr>
						</table>
					</td>
				</tr>
				
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.absoluteName}.lblAction" /><br/>
						<font style="font-weight: normal;"><x:display name="${form.absoluteName}.lblActionNote" /></font>
					</td>
					<td class="classRow">
						<table>
							<tr>
								<td><x:display name="${form.absoluteName}.radioMoveToFolder" />&nbsp;
									<x:display name="${form.absoluteName}.selBoxMoveToFolder" /></td>
							</tr>
							<tr>		
								<td><x:display name="${form.absoluteName}.radioMarkAsRead" /></td>
							</tr>
							<tr>
								<td><x:display name="${form.absoluteName}.radioDelete" /></td>
							</tr>
							<tr>
								<td><x:display name="${form.absoluteName}.radioForward" />&nbsp;
									<x:display name="${form.absoluteName}.txtFldForward" /></td>
							</tr>
							<tr>
								<td><x:display name="${form.absoluteName}.lblRememberToSave" /></td>
							</tr>
							<tr>
								<td><x:display name="${form.absoluteName}.btnSave" />&nbsp;
									<x:display name="${form.absoluteName}.btnCancel" /><br/><br/><br/></td>
							</tr>
						</table>
					</td>
				</tr>

				
				
				<tr>
					<td class="classRowLabel" valign="top" align="right">
						<x:display name="${form.absoluteName}.lblSpecialOperation" /><br/>
						<font style="font-weight: normal;"><x:display name="${form.absoluteName}.lblRun" /></font>
					</td>
					<td class="classRow">
						<x:display name="${form.absoluteName}.selBoxRunOnFolder" />&nbsp;
						<x:display name="${form.absoluteName}.btnRun" />&nbsp;<br/><br/><br/><br/>
					</td>
				</tr>
				

<jsp:include flush="true" page="../form_footer.jsp"></jsp:include>
			</table>
		</td>
	</tr>
</table>

</td>
</tr>	
</table>

