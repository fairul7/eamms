package com.tms.collab.messaging.model;

import kacang.Application;
import kacang.model.DaoQuery;
import kacang.model.operator.DaoOperator;
import kacang.model.operator.OperatorEquals;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.storage.StorageService;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageException;
import kacang.ui.Event;
import kacang.util.Log;

import javax.mail.Address;
import javax.mail.Header;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.*;
import java.util.*;

/**
 * This class provides static utility methods used by this module.
 */
public class Util {
    public static final String RECIPIENT_DELIMITER = ",";
    public static final String AMPERSAND = "@";
    
    
    /**
     * Attempt to merge <code>List</code>s. Return an empty lists if <code>lists</code>
     * is empty. Return an empty list if <code>lists</code> is null.
     * 
     * @param lists
     * @return List
     */
    public static final List mergeLists(List[] lists) {
        List result = new ArrayList();
        if (lists != null) {
            for (int a=0; a< lists.length; a++) {
                result.addAll(lists[a]);
            }
        }
        return result;
    }
    
    /**
     * Convert <code>List</a> into comma separated <code>String</code>s. Uses
     * the <code>Object.toString()</code> method of each element in <code>listEntries</code>.
     * Entries that are null will be skipped / ignored. Return an empty String if 
     * <code>listEntries</code>is null. 
     * 
     * @param listEntries
     * @return String
     */
    public static final String convertListToCommaSeparatedString(List listEntries) {
        String result = "";
        boolean firstTime = true;
        if (listEntries != null) {
            for (Iterator i= listEntries.iterator(); i.hasNext(); ) {
                Object o = i.next();
                if (o != null) {
                    if (! firstTime) {
                        result = result +",";
                    }
                    result = result + o.toString().trim();
                    firstTime = false;
                }
            }
        }
        return result;
    }
    
    
    /**
     * Strip username from intranet message address (fromat [username]@intranet)
     * 
     * @param intranetMessagingAddress
     * @return username
     */
    public static final String[] stripUserNameFromIntranetMessagingAddress(String[] intranetMessagingAddress) {
        String intranetAddressPostfix = AMPERSAND + MessagingModule.INTRANET_EMAIL_DOMAIN;
        
        Set intranetUserNames = new LinkedHashSet();
        for (int a=0; a< intranetMessagingAddress.length; a++) {
        	int index = intranetMessagingAddress[a].indexOf(intranetAddressPostfix);
        	if (index <= 0) {
        		throw new IllegalArgumentException("intranet message address "+intranetMessagingAddress[a]+" must have the format of <intranetUserName>@<intranet>");
        	}
        	String username = intranetMessagingAddress[a].substring(0, index);
            intranetUserNames.add(username.trim());
        }
        return (String[]) intranetUserNames.toArray(new String[0]);
    }
    
    
    /**
     * Strip user id from message storage filename.
     * eg. message storate filename has a format of 
     * '/messaging/[userId]/[folderId]/[messageId].
     * This method will strip and return the userId part.
     * 
     * @param storageFilename
     * @return
     */
    public static final String stripUserIdFromMessageStorageFilename(String storageFilename) {
     
        if (storageFilename == null) { throw new IllegalArgumentException("storageFilename is null"); }
        
        String rootPath = null;
        String userId = null;
        
        StringTokenizer tok = new StringTokenizer(storageFilename, "/");
        while(tok.hasMoreTokens()) {
            if (rootPath == null) { 
                rootPath = (String) tok.nextToken();
                continue; 
            }
            if (userId == null) {
            	userId = (String) tok.nextToken();
                return userId;
            }
        }
        return null;
    }
    

	/**
	 * Cleans up orphan files within the messaging module. Potentially very heavy and and must
	 * be invoked with great care.
 	 * @return a boolean to denote success/failure
	 */
	public static final boolean cleanup()
	{
        long moveOk = 0, moveNotOk = 0;

		SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class);
		StorageService storage = (StorageService) Application.getInstance().getService(StorageService.class);
		MessagingModule module = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
		String storageRoot = storage.getRootPath();
		//Abort if not root is found
		if(!("".equals(storageRoot) || storageRoot == null))
		{
			try
			{
				Collection users = service.getUsers(new DaoQuery(), 0, -1, null, false);
				for (Iterator i = users.iterator(); i.hasNext();)
				{
					//Loading folder and message information for specific user
					User user = (User) i.next();
					Map files = new HashMap();
					try
					{
						Collection folders = module.getFolders(user.getId(), 0, -1, null, false, null, null);
						for (Iterator it = folders.iterator(); it.hasNext();)
						{
							Folder folder =  (Folder) it.next();
							Collection fileList = module.getMessages(folder.getFolderId());
							for (Iterator ifile = fileList.iterator(); ifile.hasNext();)
							{
								Message message = (Message) ifile.next();
								files.put(message.getStorageFilename(), "");
							}
						}
					}
					catch (MessagingException e)
					{
						Log.getLog(MessagingModule.class).error("Error Occured While Attempting To Cleanup Messaging Module", e);
						return false;
					}
					//Traversing physical directory structure and removing orphan files
					String truncation = MessagingModule.ROOT_PATH + "/" + user.getId() + "/";
					String backupRoot = "backup/" + MessagingModule.ROOT_PATH + "/" + user.getId() + "/";
					File root = new File(storageRoot, truncation);
                    if(root.list() != null)
					{
						String[] list = root.list();
                        for(int count = 0; count < list.length; count++)
						{
							String folderDir = list[count];
                            File directory = new File(root, folderDir);
                            if(directory.isDirectory())
							{
								//Removing orphan files
                                String[] fileList = directory.list();
								if(fileList != null)
								{
                                    for(int fileCount = 0; fileCount < fileList.length; fileCount++)
									{
                                        String remove = truncation + folderDir + "/" + fileList[fileCount];
										String backup = backupRoot + folderDir + "/" + fileList[fileCount];
                                        if(!(files.containsKey(remove)))
										{
											StorageFile storageFile = new StorageFile(remove);
											StorageFile backupFile = new StorageFile(backup);
											try
											{
												if(storage.move(storageFile, backupFile)) {
                                                    moveOk++;
													Log.getLog(MessagingModule.class).debug(remove + " Moved Succesfully During Purge");
                                                } else {
                                                    moveNotOk++;
													Log.getLog(MessagingModule.class).error("Unable To Move " + remove);
                                                }
											}
											catch (FileNotFoundException e)
											{
												Log.getLog(MessagingModule.class).error("Error Occured While Attempting To Cleanup Messaging Module", e);
											}
											catch (StorageException e)
											{
												Log.getLog(MessagingModule.class).error("Error Occured While Attempting To Cleanup Messaging Module", e);
											}
										}
									}
								}
							}
						}
					}
				}

                Log.getLog(MessagingModule.class).info(moveOk + " orphan files cleared successfully");
                Log.getLog(MessagingModule.class).info(moveNotOk + " orphan files failed to move");
			}
			catch (SecurityException e)
			{
				Log.getLog(MessagingModule.class).error("Error Occured While Attempting To Cleanup Messaging Module", e);
				return false;
			}
		}
		return true;
	}

    /**
     * Converts a List of email recipients into comma separated String.
     *
     * @param list Email recipient List object to be converted
     * @return Email recipients as comma separated String
     */
    public static final String convertRecipientsListToString(List list) {
        StringBuffer sb = new StringBuffer();
        String temp;
        for (int i = 0; i < list.size(); i++) {
            temp = (String) list.get(i);
            if (sb.toString().indexOf(temp) == -1) {
                if (i > 0 && sb.length() > 1)
                    sb.append(RECIPIENT_DELIMITER);
                sb.append(temp);
            }
/*
            if ((i + 1) < list.size()) {
                sb.append(RECIPIENT_DELIMITER);
            }
*/
        }

        return sb.toString();
    }

    /**
     * Converts a RECIPIENT_DELIMITER separated string into a list of
     * Internet email recipients. It will skip any Intranet addresses.
     *
     * @param s specifies a String of RECIPIENT_DELIMITER separated email
     *          recipients
     * @return List of Internet email recipients as String objects, empty
     *         List of no recipient found
     * @throws javax.mail.internet.AddressException
     *          if s contains a recipient that cannot be
     *          converted into an InternetAddress
     */
    public static final List convertStringToInternetRecipientsList(String s) throws AddressException {
        InternetAddress[] addresses;
        InternetAddress address;
        List recipientsList = new ArrayList();
        Set emailSet = new HashSet(); // to detect duplicate emails
        String addrStr;

        if (!("".equals(s) || s == null)) {
            addresses = InternetAddress.parse(s);
            for (int i = 0; i < addresses.length; i++) {
                address = addresses[i];
                addrStr = address.getAddress();
                if (!addrStr.endsWith(MessagingModule.INTRANET_EMAIL_DOMAIN)) {
                    if (addrStr == null || addrStr.trim().length() == 0) {
                    	// ignore
                    	continue;
                    }
                    // allow invalid RFC email addresses. Uncomment to check - but not recommended
                    // address.validate();
                    if (!emailSet.contains(addrStr)) {
                        emailSet.add(addrStr);
                        recipientsList.add(address.toString());
                    }
                }
            }
        }

        return recipientsList;
    }

    public static final boolean validateStringInternetAddress(String s) throws AddressException {
        InternetAddress[] addresses;
        InternetAddress address;
        String addrStr;

        addresses = InternetAddress.parse(s);
        for (int i = 0; i < addresses.length; i++) {
            address = addresses[i];
            addrStr = address.getAddress();
            if (!addrStr.endsWith(MessagingModule.INTRANET_EMAIL_DOMAIN)) {
                if (addrStr == null || addrStr.trim().length() == 0) {
                    throw new AddressException("Empty email address not allowed");
                }
                // allow invalid RFC email addresses. Uncomment to check - but not recommended
                address.validate();
            }
        }

        return true;
    }

    /**
     * Converts a RECIPIENT_DELIMITER separated string into a list of
     * Intranet email recipients. It will skip any Internet addresses.
     *
     * @param s specifies a String of RECIPIENT_DELIMITER separated email
     *          recipients
     * @return List of Intranet email recipients as String objects, empty
     *         List of no recipient found
     * @throws javax.mail.internet.AddressException
     *          if s contains a recipient that cannot be
     *          converted into an InternetAddress
     */
    public static final List convertStringToIntranetRecipientsList(String s) throws AddressException {
        InternetAddress[] addresses;
        InternetAddress address;
        List recipientsList = new ArrayList();
        Set emailSet = new HashSet(); // to detect duplicate emails
        String addrStr;

        if (!("".equals(s) || s == null)) {
            addresses = InternetAddress.parse(s);
            for (int i = 0; i < addresses.length; i++) {
                address = addresses[i];
                addrStr = address.getAddress();
                if (addrStr.endsWith(MessagingModule.INTRANET_EMAIL_DOMAIN)) {
                    if (!emailSet.contains(addrStr)) {
                        emailSet.add(addrStr);
                        recipientsList.add(address.toString());
                    }
                }
            }
        }

        return recipientsList;
    }

    public static final String convertMessageHeadersToString(Properties p) throws IOException {
        ByteArrayOutputStream baos;

        baos = new ByteArrayOutputStream();
        p.store(baos, "Message Header");

        return baos.toString();
    }

    public static final Properties convertStringToMessageHeaders(String s) throws IOException {
        Properties p;
        ByteArrayInputStream bais;

        p = new Properties();
        bais = new ByteArrayInputStream(s.getBytes());
        p.load(bais);

        return p;
    }

    public static final List convertAddressesToRecipientsList(Address[] addresses) {
        List recipientsList = new ArrayList();

        if (addresses != null) {
            for (int i = 0; i < addresses.length; i++) {
                recipientsList.add(addresses[i].toString());
            }
        }

        return recipientsList;
    }

    public static Properties convertToHeaders(Enumeration headers) {
        Properties p;
        Header header;

        p = new Properties();
        while (headers.hasMoreElements()) {
            header = (Header) headers.nextElement();
            p.setProperty(header.getName().toUpperCase(), header.getValue());
        }

        return p;
    }


    // === [ Hex codes ] =======================================================
    /**
     * for building output as Hex
     */
    private static char[] digits = {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static String encodeHex(String s) {
        return new String(encodeHex(s.getBytes()));
    }

    public static String decodeHex(String s) {
        return new String(decodeHex(s.toCharArray()));
    }

    /**
     * Converts an array of bytes into an array of characters representing the
     * hexidecimal values of each byte in order. The returned array will be
     * double the length of the passed array, as it takes two characters to
     * represent any given byte.
     */
    public static char[] encodeHex(byte[] data) {

        int l = data.length;

        char[] out = new char[l << 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = digits[(0xF0 & data[i]) >>> 4];
            out[j++] = digits[0x0F & data[i]];
        }

        return out;
    }

    /**
     * Converts an array of characters representing hexidecimal values into an
     * array of bytes of those same values. The returned array will be half the
     * length of the passed array, as it takes two characters to represent any
     * given byte. An exception is thrown if the passed char array has an odd
     * number of elements.
     */
    public static byte[] decodeHex(char[] data) {

        int l = data.length;

        if ((l & 0x01) != 0) {
            throw new RuntimeException("odd number of characters.");
        }

        byte[] out = new byte[l >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < l; i++) {
            int f = Character.digit(data[j++], 16) << 4;
            f = f | Character.digit(data[j++], 16);
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }


    // === [ Escape Java/JavaScript] ===========================================
    // Java and JavaScript
    //--------------------------------------------------------------------------
    /**
     * <p>Escapes the characters in a <code>String</code> using Java String rules.</p>
     * <p/>
     * <p>Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.) </p>
     * <p/>
     * <p>So a tab becomes the characters <code>'\\'</code> and
     * <code>'t'</code>.</p>
     * <p/>
     * <p>The only difference between Java strings and JavaScript strings
     * is that in JavaScript, a single quote must be escaped.</p>
     * <p/>
     * <p>Example:
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn't say, \"Stop!\"
     * </pre>
     * </p>
     *
     * @param str String to escape values in, may be null
     * @return String with escaped values, <code>null</code> if null string input
     */
    public static String escapeJava(String str) {
        return escapeJavaStyleString(str, false);
    }

    /**
     * <p>Escapes the characters in a <code>String</code> using Java String rules to
     * a <code>Writer</code>.</p>
     * <p/>
     * <p>A <code>null</code> string input has no effect.</p>
     *
     * @param out Writer to write escaped string into
     * @param str String to escape values in, may be null
     * @throws IllegalArgumentException if the Writer is <code>null</code>
     * @throws IOException              if error occurs on undelying Writer
     * @see #escapeJava(java.lang.String)
     */
    public static void escapeJava(Writer out, String str) throws IOException {
        escapeJavaStyleString(out, str, false);
    }

    /**
     * <p>Escapes the characters in a <code>String</code> using JavaScript String rules.</p>
     * <p>Escapes any values it finds into their JavaScript String form.
     * Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.) </p>
     * <p/>
     * <p>So a tab becomes the characters <code>'\\'</code> and
     * <code>'t'</code>.</p>
     * <p/>
     * <p>The only difference between Java strings and JavaScript strings
     * is that in JavaScript, a single quote must be escaped.</p>
     * <p/>
     * <p>Example:
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn\'t say, \"Stop!\"
     * </pre>
     * </p>
     *
     * @param str String to escape values in, may be null
     * @return String with escaped values, <code>null</code> if null string input
     */
    public static String escapeJavaScript(String str) {
        return escapeJavaStyleString(str, true);
    }

    /**
     * <p>Escapes the characters in a <code>String</code> using JavaScript String rules
     * to a <code>Writer</code>.</p>
     * <p/>
     * <p>A <code>null</code> string input has no effect.</p>
     *
     * @param out Writer to write escaped string into
     * @param str String to escape values in, may be null
     * @throws IllegalArgumentException if the Writer is <code>null</code>
     * @throws IOException              if error occurs on undelying Writer
     * @see #escapeJavaScript(java.lang.String)
     */
    public static void escapeJavaScript(Writer out, String str) throws IOException {
        escapeJavaStyleString(out, str, true);
    }

    private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes) {
        if (str == null) {
            return null;
        }
        try {
            StringPrintWriter writer = new StringPrintWriter(str.length() * 2);
            escapeJavaStyleString(writer, str, escapeSingleQuotes);
            return writer.getString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            ioe.printStackTrace();
            return null;
        }
    }

    private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null) {
            return;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                out.write("\\u" + hex(ch));
            } else if (ch > 0xff) {
                out.write("\\u0" + hex(ch));
            } else if (ch > 0x7f) {
                out.write("\\u00" + hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.write('\\');
                        out.write('b');
                        break;
                    case '\n':
                        out.write('\\');
                        out.write('n');
                        break;
                    case '\t':
                        out.write('\\');
                        out.write('t');
                        break;
                    case '\f':
                        out.write('\\');
                        out.write('f');
                        break;
                    case '\r':
                        out.write('\\');
                        out.write('r');
                        break;
                    default :
                        if (ch > 0xf) {
                            out.write("\\u00" + hex(ch));
                        } else {
                            out.write("\\u000" + hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '\'':
                        if (escapeSingleQuote) out.write('\\');
                        out.write('\'');
                        break;
                    case '"':
                        out.write('\\');
                        out.write('"');
                        break;
                    case '\\':
                        out.write('\\');
                        out.write('\\');
                        break;
                    default :
                        out.write(ch);
                        break;
                }
            }
        }
    }

    /**
     * <p>Returns an upper case hexadecimal <code>String</code> for the given
     * character.</p>
     *
     * @param ch The character to convert.
     * @return An upper case hexadecimal <code>String</code>
     */
    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase();
    }

    /**
     * <p>Unescapes any Java literals found in the <code>String</code>.
     * For example, it will turn a sequence of <code>'\'</code> and
     * <code>'n'</code> into a newline character, unless the <code>'\'</code>
     * is preceded by another <code>'\'</code>.</p>
     *
     * @param str the <code>String</code> to unescape, may be null
     * @return a new unescaped <code>String</code>, <code>null</code> if null string input
     */
    public static String unescapeJava(String str) {
        if (str == null) {
            return null;
        }
        try {
            StringPrintWriter writer = new StringPrintWriter(str.length());
            unescapeJava(writer, str);
            return writer.getString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            ioe.printStackTrace();
            return null;
        }
    }

    /**
     * <p>Unescapes any Java literals found in the <code>String</code> to a
     * <code>Writer</code>.</p>
     * <p/>
     * <p>For example, it will turn a sequence of <code>'\'</code> and
     * <code>'n'</code> into a newline character, unless the <code>'\'</code>
     * is preceded by another <code>'\'</code>.</p>
     * <p/>
     * <p>A <code>null</code> string input has no effect.</p>
     *
     * @param out the <code>Writer</code> used to output unescaped characters
     * @param str the <code>String</code> to unescape, may be null
     * @throws IllegalArgumentException if the Writer is <code>null</code>
     * @throws IOException              if error occurs on undelying Writer
     */
    public static void unescapeJava(Writer out, String str) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null) {
            return;
        }
        int sz = str.length();
        StringBuffer unicode = new StringBuffer(4);
        boolean hadSlash = false;
        boolean inUnicode = false;
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            if (inUnicode) {
                // if in unicode, then we're reading unicode
                // values in somehow
                unicode.append(ch);
                if (unicode.length() == 4) {
                    // unicode now contains the four hex digits
                    // which represents our unicode chacater
                    try {
                        int value = Integer.parseInt(unicode.toString(), 16);
                        out.write((char) value);
                        unicode.setLength(0);
                        inUnicode = false;
                        hadSlash = false;
                    } catch (NumberFormatException nfe) {
                        throw new RuntimeException("Unable to parse unicode value: " + unicode);
                    }
                }
                continue;
            }
            if (hadSlash) {
                // handle an escaped value
                hadSlash = false;
                switch (ch) {
                    case '\\':
                        out.write('\\');
                        break;
                    case '\'':
                        out.write('\'');
                        break;
                    case '\"':
                        out.write('"');
                        break;
                    case 'r':
                        out.write('\r');
                        break;
                    case 'f':
                        out.write('\f');
                        break;
                    case 't':
                        out.write('\t');
                        break;
                    case 'n':
                        out.write('\n');
                        break;
                    case 'b':
                        out.write('\b');
                        break;
                    case 'u':
                        {
                            // uh-oh, we're in unicode country....
                            inUnicode = true;
                            break;
                        }
                    default :
                        out.write(ch);
                        break;
                }
                continue;
            } else if (ch == '\\') {
                hadSlash = true;
                continue;
            }
            out.write(ch);
        }
        if (hadSlash) {
            // then we're in the weird case of a \ at the end of the
            // string, let's output it anyway.
            out.write('\\');
        }
    }

    /**
     * <p>Unescapes any JavaScript literals found in the <code>String</code>.</p>
     * <p/>
     * <p>For example, it will turn a sequence of <code>'\'</code> and <code>'n'</code>
     * into a newline character, unless the <code>'\'</code> is preceded by another
     * <code>'\'</code>.</p>
     *
     * @param str the <code>String</code> to unescape, may be null
     * @return A new unescaped <code>String</code>, <code>null</code> if null string input
     * @see #unescapeJava(String)
     */
    public static String unescapeJavaScript(String str) {
        return unescapeJava(str);
    }

    /**
     * <p>Unescapes any JavaScript literals found in the <code>String</code> to a
     * <code>Writer</code>.</p>
     * <p/>
     * <p>For example, it will turn a sequence of <code>'\'</code> and <code>'n'</code>
     * into a newline character, unless the <code>'\'</code> is preceded by another
     * <code>'\'</code>.</p>
     * <p/>
     * <p>A <code>null</code> string input has no effect.</p>
     *
     * @param out the <code>Writer</code> used to output unescaped characters
     * @param str the <code>String</code> to unescape, may be null
     * @throws IllegalArgumentException if the Writer is <code>null</code>
     * @throws IOException              if error occurs on undelying Writer
     * @see #unescapeJava(Writer,String)
     */
    public static void unescapeJavaScript(Writer out, String str) throws IOException {
        unescapeJava(out, str);
    }


    // === [ Module Util ] =====================================================
    /**
     * Request attribute key constant
     */
    public static final String RA_HAS_INTRANET_ACCOUNT = "hasIntranetAccount";

    /**
     * Utility method to get the Messaging module.
     *
     * @return the MessagingModule object
     */
    public static MessagingModule getMessagingModule() {
        return (MessagingModule) Application.getInstance().getModule(MessagingModule.class);
    }

    /**
     * Checks whether the intranet account for current user (in session) exist.
     *
     * @param event
     * @return true if current user has intranet account, false otherwise
     */
    public static boolean hasIntranetAccount(Event event) {
        return hasIntranetAccount(event.getRequest());
    }

    public static int countFolderUnreadMails(HttpServletRequest request, String folderName) {
        MessagingModule mm = getMessagingModule();
        if (folderName != null) {
            try {
                User user = Util.getUser(request);
                Folder folder = mm.getSpecialFolder(user.getId(), folderName);
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorEquals("folderId", folder.getId(), DaoOperator.OPERATOR_AND));
                query.addProperty(new OperatorEquals("readFlag", "0", DaoOperator.OPERATOR_AND));
                return mm.getMessagesCount(query);
            } catch (MessagingException e) {
                Log.getLog(Util.class).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }
        return -1;
    }

    public static int countFolderUnreadMailsByFolderId(HttpServletRequest request, String folderId) {
        MessagingModule mm = getMessagingModule();
        if (folderId != null) {
            try {
                User user = Util.getUser(request);
                DaoQuery query = new DaoQuery();
                query.addProperty(new OperatorEquals("folderId", folderId, DaoOperator.OPERATOR_AND));
                query.addProperty(new OperatorEquals("readFlag", "0", DaoOperator.OPERATOR_AND));
                return mm.getMessagesCount(query);
            } catch (MessagingException e) {
                Log.getLog(Util.class).error(e);  //To change body of catch statement use Options | File Templates.
            }
        }
        return -1;
    }


    public static boolean hasIntranetAccount(HttpServletRequest request) {
        User user;
        MessagingModule mm;
        IntranetAccount account;
        boolean intranetAccountExist = false;

        // check request attribute for cached data
        Object o = request.getAttribute(RA_HAS_INTRANET_ACCOUNT);
        if (o != null) {
            Boolean b = (Boolean) o;
            return b.booleanValue();
        }

        user = getUser(request);
        mm = (MessagingModule) Application.getInstance().getModule(MessagingModule.class);

        try {
            account = mm.getIntranetAccountByUserId(user.getId());

            if (account == null) {
                intranetAccountExist = false;
            } else {
                intranetAccountExist = true;
            }

            // store result to request attribute as cache
            request.setAttribute(RA_HAS_INTRANET_ACCOUNT, new Boolean(intranetAccountExist));

        } catch (MessagingException e) {
            Log.getLog(Util.class).error(e.getMessage(), e);
        }

        return intranetAccountExist;

    }

    // === [ Kacang Util ] =====================================================
    /**
     * Returns the current user from Event object.
     *
     * @param event
     * @return a User object
     */
    public static User getUser(Event event) {
        return getUser(event.getRequest());
    }

    public static User getUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(SecurityService.SESSION_KEY_USER);
    }

    /**
     * Returns true if the browser supports use of rich text editor, false
     * otherwise.
     *
     * @param request
     * @return
     */
    public static boolean isRichTextCapable(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        userAgent = userAgent == null ? "" : userAgent;

        for (int i = 0; i < richTextNotCapableStrings.length; i++) {
            String s = richTextNotCapableStrings[i];
            if(userAgent.indexOf(s) != -1) {
                // failed!!
                return false;
            }
        }

        for (int i = 0; i < richTextCapableStrings.length; i++) {
            String s = richTextCapableStrings[i];
            if(userAgent.indexOf(s) != -1) {
                // pass!
                return true;
            }
        }

        return false;
    }
    private static final String[] richTextCapableStrings = {
        "Gecko",
        "MSIE"
    };
    private static final String[] richTextNotCapableStrings = {
        "Opera"
    };


    /**
     * Converts HTML contents into text.
     *
     * @param htmlContents
     * @return
     * @throws IOException
     */
    public static String html2Text(String htmlContents) throws IOException {
        ParserDelegator parser = new ParserDelegator();
        Html2TextParserCallback callback = new Html2TextParserCallback();
        StringReader stringReader = new StringReader(htmlContents);
        parser.parse(stringReader, callback, true);

        return callback.getText();
    }

    public static String html2Text(Reader htmlContents) throws IOException {
        ParserDelegator parser = new ParserDelegator();
        Html2TextParserCallback callback = new Html2TextParserCallback();
        parser.parse(htmlContents, callback, true);

        return callback.getText();
    }

    public final static String plainTextToHtml(String str) {
        str = str==null ? "" : str;

        //First, convert all the special chars...
        str = htmlEncode(str);

        //Convert all leading whitespaces
        str = leadingSpaces(str);

        //Then convert all line breaks...
        str = br(str);

        return str;
    }

    private final static String htmlEncode(String s) {
        s = s==null ? "" : s;

        StringBuffer str = new StringBuffer();

        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);

            // encode standard ASCII characters into HTML entities where needed
            if (c < '\200') {
                switch (c) {
                case '"':
                    str.append("&quot;");

                    break;

                case '&':
                    str.append("&amp;");

                    break;

                case '<':
                    str.append("&lt;");

                    break;

                case '>':
                    str.append("&gt;");

                    break;

                default:
                    str.append(c);
                }
            }
            // encode 'ugly' characters (ie Word "curvy" quotes etc)
            else if (c < '\377') {
                String hexChars = "0123456789ABCDEF";
                int a = c % 16;
                int b = (c - a) / 16;
                String hex = "" + hexChars.charAt(b) + hexChars.charAt(a);
                str.append("&#x" + hex + ";");
            }
            //add other characters back in - to handle charactersets
            //other than ascii
            else {
                str.append(c);
            }
        }

        return str.toString();
    }

    private final static String leadingSpaces(String s) {
        s = s==null ? "" : s;

        StringBuffer str = new StringBuffer();
        boolean justAfterLineBreak = true;

        for (int i = 0; i < s.length(); i++) {
            if (justAfterLineBreak) {
                if (s.charAt(i) == ' ') {
                    str.append("&nbsp;");
                } else {
                    str.append(s.charAt(i));
                    justAfterLineBreak = false;
                }
            } else {
                if (s.charAt(i) == '\n') {
                    justAfterLineBreak = true;
                }

                str.append(s.charAt(i));
            }
        }

        return str.toString();
    }

    private final static String br(String s) {
        s = s==null ? "" : s;

        StringBuffer str = new StringBuffer();

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\n') {
                str.append("<br/>");
            }

            str.append(s.charAt(i));
        }

        return str.toString();
    }
}

/**
 * <p>A PrintWriter that maintains a String as its backing store.</p>
 * <p/>
 * <p>Usage:
 * <pre>
 * StringPrintWriter out = new StringPrintWriter();
 * printTo(out);
 * System.out.println( out.getString() );
 * </pre>
 * </p>
 *
 * @author Alex Chaffee
 * @author Scott Stanchfield
 * @since 2.0
 */
class StringPrintWriter extends PrintWriter {

    public StringPrintWriter() {
        super(new StringWriter());
    }

    public StringPrintWriter(int initialSize) {
        super(new StringWriter(initialSize));
    }

    /**
     * <p>Since toString() returns information *about* this object, we
     * want a separate method to extract just the contents of the
     * internal buffer as a String.</p>
     *
     * @return the contents of the internal string buffer
     */
    public String getString() {
        flush();
        return out.toString();
    }

}


class Html2TextParserCallback extends HTMLEditorKit.ParserCallback {
    private StringBuffer text;

    public Html2TextParserCallback() {
        text = new StringBuffer();
    }

    public void handleText(char[] data, int pos) {
        text.append(data);
    }

    /**
     * Returns the converted text. This method should be called after
     * conversion.
     *
     * @return
     */
    public String getText() {
        return text.toString();
    }
}