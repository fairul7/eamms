package com.tms.util;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MD5Util {
	public static final int PAD_LENGTH = 50;
	public static final int KB = 1024;
	public static final String CR_LF = "\r\n";
	
	private int maxKb = 50;
	private ArrayList skippedDirs = null;
	private ArrayList skippedExts = null;
	
	public MD5Util() {
		skippedDirs = new ArrayList();
		skippedDirs.add("/.svn/");
		skippedDirs.add("/data/");
		skippedDirs.add("/work/");
		
		skippedExts = new ArrayList();
		skippedExts.add(".scc");
		skippedExts.add(".bak");
		skippedExts.add(".disabled");
		skippedExts.add(".optional");
	}
	
	public int getMaxKb() {
		return maxKb;
	}
	
	public void setMaxKb(int maxKb) {
		this.maxKb = maxKb;
	}
	
	public ArrayList getSkippedDirs() {
		return skippedDirs;
	}
	
	public void setSkippedDirs(ArrayList skippedDirs) {
		this.skippedDirs = skippedDirs;
	}
	
	public void processDir(Writer writer, File dir, boolean verbose) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd hh:mm a");
		DecimalFormat df = new DecimalFormat("#,###");
		
		Date startDate = new Date();
		
		String path = dir.getAbsolutePath();
		processDir(writer, dir, path, verbose);
		
		Date endDate = new Date();
		long elapsed = endDate.getTime() - startDate.getTime();
		
		try {
			writer.write("generated: " + sdf.format(endDate) + " (elapsed: " + df.format(elapsed) + " ms)" + CR_LF);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void processDir(Writer writer, File dir, String root, boolean verbose) {
		DecimalFormat df = new DecimalFormat("#,###");
		
		// read directory
		File[] dirListing = dir.listFiles();
		
		// exit if directory empty
		if (dirListing.length == 0) {
			return;
		}
		
		// sort directory
		sortFiles(dirListing);
		
		ArrayList subDirs = new ArrayList();
		try {
			// show directory
			String path = getPath(dir, root);
			writer.write(path + CR_LF);
			
			// check: skipped directory
			if (isSkippedDir(path)) {
				writer.write("  SKIPPED_DIR"+ CR_LF);
				writer.write(CR_LF);
				return;
			}
			
			// filter files
			int filesSkipped = 0;
			int filesHashed = 0;
			ArrayList selected = new ArrayList();
			for (int i = 0; i < dirListing.length; i++) {
				File file = dirListing[i];
				
				// check: file
				if (file.isFile()) {
					// check: within size
					if (!isSkippedFile(file)) {
						selected.add(file);
						filesHashed++;
						
						if (verbose) {
							writer.write("  " + padRight(file.getName(), PAD_LENGTH) + "  " + getFileHash(file) + CR_LF);
						}
					} else {
						filesSkipped++;
						
						if (verbose) {
							writer.write("  " + padRight(file.getName(), PAD_LENGTH) + "  SKIPPED (" + df.format(file.length()) + " bytes)" + CR_LF);
						}
					}
				} else {
					subDirs.add(file);
				}
			}
			dirListing = null;
			
			// get hash for directory
			if (!verbose) {
				File[] hashFiles = new File[selected.size()];
				for (int i = 0; i < selected.size(); i++) {
					hashFiles[i] = (File) selected.get(i);
				}
				String hash = getDirHash(hashFiles);
				if (hash != null) {
					writer.write("  " + hash + CR_LF);
				}
			}
			
			// show directory summary
			writer.write("  HASHED=" + filesHashed);
			if (filesSkipped > 0) {
				writer.write(" SKIPPED=" + filesSkipped);
			}
			writer.write(CR_LF + CR_LF);
			writer.flush();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// recursive
		for (int i = 0; i < subDirs.size(); i++) {
			File sub = (File) subDirs.get(i);
			processDir(writer, sub, root, verbose);
		}
	}
	
	protected void sortFiles(File[] dirListing) {
		if (dirListing != null && dirListing.length != 0) {
			// copy to list
			ArrayList list = new ArrayList();
			for (int i = 0; i < dirListing.length; i++) {
				list.add(dirListing[i]);
			}
			
			// sort list
			Collections.sort(list);
			
			// transfer back to array
			for (int i = 0; i < dirListing.length; i++) {
				dirListing[i] = (File) list.get(i);
			}
		}
	}
	
	private String getPath(File dir, String root) {
		String path = dir.getAbsolutePath();
		if (path.length() > root.length()) {
			path = path.substring(root.length() + 1).replace('\\', '/') + "/";
		} else if (path.length() == root.length()) {
			path = "";
		}
		return "./" + path;
	}
	
	private boolean isSkippedDir(String path) {
		if (skippedDirs != null && skippedDirs.size() > 0) {
			for (int i = 0; i < skippedDirs.size(); i++) {
				String keyword = (String) skippedDirs.get(i);
				
				if (path.toLowerCase().indexOf(keyword.toLowerCase()) >= 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isSkippedFile(File file) {
		if (file.length() == 0 || file.length() > (maxKb * KB)) {
			return true;
		}
		
		//TODO: check for file extensions
		if (skippedExts != null && skippedExts.size() > 0) {
			for (int i = 0; i < skippedExts.size(); i++) {
				String ext = (String) skippedExts.get(i);
				
				if (file.getName().toLowerCase().endsWith(ext.toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private String padRight(String str, int num) {
		if (str.length() < num) {
			String pad = spaces(num - str.length());
			return str + pad;
		}
		return str;
	}
	
	private String spaces(int num) {
		if (num > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < num; i++) {
				sb.append(" ");
			}
			return sb.toString();
		}
		return "";
	}
	
	public static String getFileHash(File file) throws NoSuchAlgorithmException {
		if (file.isFile()) {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			
			updateDigest(md, file);
			
			return hexMD5(md.digest());
		}
		return null;
	}
	
	public static String getDirHash(File[] fileListing) throws NoSuchAlgorithmException {
		if (fileListing.length > 0) {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			
			for (int i = 0; i < fileListing.length; i++) {
				File file = fileListing[i];
				updateDigest(md, file);
			}
			
			return hexMD5(md.digest());
		}
		return null;
	}
	
	public static void updateDigest(MessageDigest md, File file) {
		byte[] buffer = new byte[1024];
		
		if (file.isFile()) {
			try {
				int readSize;
				InputStream inFile = new BufferedInputStream(new FileInputStream(file));
				while ((readSize = inFile.read(buffer)) != -1) {
					md.update(buffer, 0, readSize);
				}
				inFile.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String hexMD5(byte[] digest) {
		StringBuffer hexString = new StringBuffer();
		for (int i=0;i<digest.length;i++) {
			String hex = Integer.toHexString(0xFF & digest[i]);
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
