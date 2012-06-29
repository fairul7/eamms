<%@ include file="../includes/header.jsp" %>
<table border=0 cellspacing=1 cellpadding=1>
	<tr>
		<td valign=middle align=left width=22><IMG src="/help/images/clear.gif" height=1 width=20 border=0></td>
		<td valign=middle align=right class="header">Adding A New Content</td>
	</tr>
	<tr><td valign=top align=left colspan="2"><hr size="1"></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">There are 9 types of content. Each of the content serves specific need and function. Below are the descriptions for the 9 types of content:</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr>
		<td valign=top align=left colspan="2" class="text">
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td class="tableOutline">
						<table cellpadding="2" cellspacing="1" width="100%">
							<tr>
								<td class="tableHeader">Content Types</td>
								<td class="tableHeader">Descriptions</td>
								<td class="tableHeader">Possible Parents</td>
								<td class="tableHeader">Possible Children</td>
							</tr>
							<tr>
								<td valign="top" class="tableRow">Section</td>
								<td valign="top" class="tableRow">Section allows you to create and manage the menu seen on EKP content &amp; website.</td>
								<td valign="top" class="tableRow">Section</td>
								<td valign="top" class="tableRow">
									Section<br>
									Article<br>
									Document<br>
									Image<br>
									Bookmark<br>
									Virtual Section<br>
									Commentary<br>
									Spot<br>
									Page
								</td>
							</tr>
							<tr>
								<td valign="top" class="tableRow">Article</td>
								<td valign="top" class="tableRow">Articles allow you to create, edit, and manage content published on the EKP content &amp; website.</td>
								<td valign="top" class="tableRow">Section</td>
								<td valign="top" class="tableRow">Commentary</td>
							</tr>
							<tr>
								<td valign="top" class="tableRow">Document</td>
								<td valign="top" class="tableRow">Documents are files uploaded into the tmsPUBLSIHER E server. They can be published to be downloaded by user.</td>
								<td valign="top" class="tableRow">Section</td>
								<td valign="top" class="tableRow">Commentary</td>
							</tr>
							<tr>
								<td valign="top" class="tableRow">Image</td>
								<td valign="top" class="tableRow">Images in the form of GIF and JPEG that are uploaded.</td>
								<td valign="top" class="tableRow">Section</td>
								<td valign="top" class="tableRow">-</td>
							</tr>
							<tr>
								<td valign="top" class="tableRow">Bookmark</td>
								<td valign="top" class="tableRow">A bookmark is an URL link that hyperlinks to a piece of content inside the tmsPUBLISHER E or externally.</td>
								<td valign="top" class="tableRow">Section</td>
								<td valign="top" class="tableRow">-</td>
							</tr>
							<tr>
								<td valign="top" class="tableRow">Virtual Section</td>
								<td valign="top" class="tableRow">A virtual section lists a group of content (usually articles) from other section.</td>
								<td valign="top" class="tableRow">Section</td>
								<td valign="top" class="tableRow">-</td>
							</tr>
							<tr>
								<td valign="top" class="tableRow">Commentary</td>
								<td valign="top" class="tableRow">Commentary is a user feedback tool.</td>
								<td valign="top" class="tableRow">Section</td>
								<td valign="top" class="tableRow">Comment</td>
							</tr>
							<tr>
								<td valign="top" class="tableRow">Spot</td>
								<td valign="top" class="tableRow">A spot is defined as a dynamic piece of information that takes up specific locations and headers in the template. Locations of spots depend on the template used.</td>
								<td valign="top" class="tableRow">Section</td>
								<td valign="top" class="tableRow">-</td>
							</tr>
							<tr>
								<td valign="top" class="tableRow">Page</td>
								<td valign="top" class="tableRow">Pages are a very flexible module that allows additional content to be created and linked to other modules. Pages are similar to Articles in content authoring. Pages may be used on information such as About Us, Company Background, Disclaimer and even as a Welcome page.</td>
								<td valign="top" class="tableRow">Section</td>
								<td valign="top" class="tableRow">-</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">The flow of adding/saving content is contents created must be saved. After being saved, the content is considered checked-out. Next, the content must be submitted for approval by the content editor or the administrator. Finally, the content can be published to the front-end to be viewed by users. A published content can be withdrawn.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">There are 3 buttons to save or add new content, each of the button will reflect a different add content process flow. The 3 buttons to save or adding new contents are Save As Draft, Check In For Approval and Approve.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Save As Draft Button</td></tr>
	<tr><td valign=top align=left colspan="2" class="text">Click on the Save button will save the new content. After being saved, the content status will automatically becomes Checked-Out and there will be a version increment. However, it is yet to be approved or published. The current published version is one version lower than the saved version.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><i>Note: Once content is Checked-Out, the content cannot be edited by others unless the status is changed by selecting Undo Check-Out.</i></td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Check In For Approval Button</td></tr>
	<tr><td valign=top align=left colspan="2" class="text">Clicking on the Submit For Approval button will save the new content. The content status will be automatically Checked-Out but yet to be approved and published. Again, the current published version is one version lower than the saved version. At this point, the approval will receive an e-mail notification to approved and/or publish the content.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Approve</td></tr>
	<tr><td valign=top align=left colspan="2" class="text">Clicking on the Approve button will save the new content. The content status will automatically becomes Approved. If the content has been in “Published” status, the “Approved” content will also be “Published”. The content version is also increased by one.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><i>Note: If a content object is Approved and is not Checked-Out, the content can be edited by other users who have the edit content access for that content object.</i></td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">To Add New Content</td></tr>
    <tr>
		<td valign=top align=left width=22 class="text">1.</td>
		<td valign=top align=left class="text">Click on the Add New Content link under Content Options on top right.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">2.</td>
		<td valign=top align=left class="text">Select Section from the list of content available and click Select button.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">3.</td>
		<td valign=top align=left class="text">Type in section’s Name/Title and Abstract.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">4.</td>
		<td valign=top align=left class="text">Click on either Save As Draft, Check In For Approval or Approve button depending on permission.</td>
	</tr>
</table>
<%@ include file="../includes/footer.jsp" %>