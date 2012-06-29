									<%@ include file="/common/header.jsp"%>
									</td>
								</tr>
							</table>
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr><td bgcolor="959385"><img src="/ekms/images/clear.gif"></td></tr>
							</table>
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr><td bgcolor="D7D4C5"><img src="/ekms/images/clear.gif"></td></tr>
							</table>
							<table width="100%" border="0" cellpadding="0" cellspacing="0" height="25" class="searchBgColor">
								<tr>
									<td>
										<table width="100%" border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td valign="middle">
													<table border="0" cellspacing="0" cellpadding="0">
														<tr align="center">
															<td nowrap>&nbsp;&nbsp;<a href="<c:url value="/ekms/"/>" class="footerFont">My Dashboard</a></td>
														</tr>
													</table>
												</td>
												<td align="right" valign="middle">
													<table border="0" cellspacing="0" cellpadding="0">
														<tr align="center" >
															<td><span class="footerFont"><c:out value="${sessionScope.currentUser.propertyMap.firstName}"/> <c:out value="${sessionScope.currentUser.propertyMap.lastName}"/>&nbsp;&nbsp;</span></td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr><td bgcolor="CCCCCC"><img src="/ekms/images/clear.gif"></td></tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr><td><img src="/ekms/images/clear.gif" height="7" width="1"></td></tr>
	</table>
</body>
</html>