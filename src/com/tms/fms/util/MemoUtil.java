package com.tms.fms.util;
import kacang.Application;
import kacang.util.*;
import com.tms.collab.messaging.model.*;

import java.util.*;

public class MemoUtil {
	
	public void sendMemo(String recipientUserId, ArrayList ccUserIDs, String subject, String message) {
		Application application = Application.getInstance();
		
		MemoUtil memoUtil = new MemoUtil();
		MemoUtil.MemoAddress sender = memoUtil.createIntranetAddress("fms@intranet");
		
		ArrayList recepientUserIds = new ArrayList();
		recepientUserIds.add(recipientUserId);
		
		memoUtil.sendMemo(sender, recepientUserIds, ccUserIDs, subject, message, true);
	}
	public void sendMemo(MemoAddress sender, String recipientUserId, String subject, String messageBody, boolean isHTML) {
		ArrayList recepientUserIds = new ArrayList();
		recepientUserIds.add(recipientUserId);
		
		sendMemo(sender, recepientUserIds, null, subject, messageBody, isHTML);
	}
	
	public void sendMemo(MemoAddress sender, ArrayList recipientUserIds, ArrayList ccUserIds, String subject, String messageBody, boolean isHTML) {
		Application application = Application.getInstance();
		MessagingModule mm = (MessagingModule) application.getModule(MessagingModule.class);
		
		try {
			String senderIntranetAddress = null;
			String senderUserId = null;
			if (sender instanceof IntranetAddress) {
				senderIntranetAddress = sender.getAddress();
			} else {
				senderUserId = sender.getAddress();
			}
			
			ArrayList recipientList = new ArrayList();
			addIntranetAddressToList(recipientList, recipientUserIds);
			
			ArrayList ccList = new ArrayList();
			addIntranetAddressToList(ccList, ccUserIds);
			
			Log.getLog(getClass()).debug(
					"\r\n  Recipient: " + recipientList +
					(ccList.size() != 0 ? 
					"\r\n  CC:        " + ccList : "") +
					"\r\n  Subject: " + subject +
					"\r\n  Message: " + messageBody
			);
			
			Message message = new Message();
			message.setMessageId(UuidGenerator.getInstance().getUuid());
			message.setDate(new Date());
			message.setFrom(senderIntranetAddress);
			message.setToIntranetList(recipientList);
			message.setCcIntranetList(ccList);
			message.setSubject(subject);
			message.setBody(messageBody);
			message.setMessageFormat(isHTML ? Message.MESSAGE_FORMAT_HTML : Message.MESSAGE_FORMAT_TEXT);
			
			SmtpAccount smtpAccount = getDummySmtpAccount();
			mm.sendMessage(smtpAccount, message, senderUserId, false);
		} catch (MessagingException e) {
			Log.getLog(getClass()).error(e.toString(), e);
		}
	}
	
	public UserIdAddress createUserIdAddress(String userId) {
		return new UserIdAddress(userId); 
	}
	
	public IntranetAddress createIntranetAddress(String intranetAddress) {
		return new IntranetAddress(intranetAddress); 
	}
	
	public SmtpAccount getDummySmtpAccount() {
		SmtpAccount smtpAccount = new SmtpAccount();
		smtpAccount.setSmtpAccountId("dummy");
		smtpAccount.setUserId("");
		smtpAccount.setName("dummy");
		smtpAccount.setServerName("127.0.0.1");
		smtpAccount.setServerPort(25);
		smtpAccount.setAnonymousAccess(false);
		smtpAccount.setUsername("");
		smtpAccount.setPassword("");
		return smtpAccount;
	}
	
	private void addIntranetAddressToList(List list, List userIds) throws MessagingException {
		Application application = Application.getInstance();
		MessagingModule mm = (MessagingModule) application.getModule(MessagingModule.class);
		
		if (userIds != null) {
			for (int i = 0; i < userIds.size(); i++) {
				IntranetAccount intranetAccount = mm.getIntranetAccountByUserId((String) userIds.get(i));
				if (intranetAccount != null) {
					String add = intranetAccount.getIntranetUsername() + "@" + MessagingModule.INTRANET_EMAIL_DOMAIN;
					list.add(add);
				}
			}
		}
	}
	
	public abstract class MemoAddress {
		private String address;
		
		public String getAddress() {
			return address;
		}
		
		public void setAddress(String address) {
			this.address = address;
		}
	}
	
	public class UserIdAddress extends MemoAddress {
		public UserIdAddress(String userId) {
			setAddress(userId);
		}
	}
	
	public class IntranetAddress extends MemoAddress {
		public IntranetAddress(String intranetAddress) {
			setAddress(intranetAddress);
		}
	}
}
