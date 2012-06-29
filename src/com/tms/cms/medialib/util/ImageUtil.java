/*
 * ThumbnailGenerator
 * Date Created: Jun 22, 2005
 * Author: Tien Soon, Law
 * Description: TODO Change the class descriptions
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.util;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.util.Log;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


public class ImageUtil {
    private Log log = Log.getLog(getClass());
    
    private Image image;
    private String inFile = "";                  // The complete source image path, including root path and file name
    private String sourceFileName = "";          // The source image file name
    private String sourceRootPath = "";          // The source image root path
	private String outFile = "";                 // The complete thumbnail path, including root path and file name
	private String outFolder = "thumbnails";     // The name of folder created to store thumbnails
	private int sourceWidth = -1;                // The source image width. -1 means no image loaded
	private int sourceHeight = -1;               // The source image height. -1 means no image loaded
	private int outWidth = 120;                  // The thumbnail default width
	private int outHeight = 90;                  // The thumbnail default height
	private int quality  = 85;                   // The quality of thumbnail in percentage
	private StorageService storage;
	private StorageFile file;
	
	/**
	 * Constructor of Thumbnail class
	 * @param newFileName The absolute path of source image
	 */
	public ImageUtil(StorageFile newFile) throws Exception {
	    Application application = Application.getInstance();
        storage = (StorageService) application.getService(StorageService.class);
        file = newFile;
        
	    inFile = storage.getRootPath() + file.getAbsolutePath();
	    
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
		image = toolkit.getImage(inFile);

		MediaTracker media_tracker = new MediaTracker(new Container());
		media_tracker.addImage(image, 0);
		// Wait for the source image to be completely loaded before further operations
	    try
		{
	        media_tracker.waitForID(0);
		}
		catch (InterruptedException error)
		{
			throw new InterruptedException("Exception caught while reading image file: " + error);
		}
		
		sourceWidth = image.getWidth(null);
		sourceHeight = image.getHeight(null);
	}
	
	public boolean scaleImage(int newWidth, String newOutFolder) throws Exception {
	    resetDefault();
	    
	    if(newWidth < sourceWidth) {
	        outWidth = newWidth;
	        setOutHeight();
	    }
	    else {
	        outWidth = sourceWidth;
	        outHeight = sourceHeight;
	    }
	    outFolder = newOutFolder;
	    boolean success = generateScaledImage();
	    
	    return success;
	}
	
	public boolean scaleImage(int newWidth, String newOutFolder, int newQuality) throws Exception {
	    resetDefault();
	    
	    if(newWidth < sourceWidth) {
	        outWidth = newWidth;
	        setOutHeight();
	    }
	    else {
	        outWidth = sourceWidth;
	        outHeight = sourceHeight;
	    }
	    outFolder = newOutFolder;
	    if(newQuality == 100) {
	        quality = 85;
	    }
	    else {
	        quality = newQuality;
	    }
	    boolean success = generateScaledImage();
	    
	    return success;
	}
	
	public boolean generateThumbnail(String newOutFolder, int newQuality) throws Exception {
	    resetDefault();
	    
	    if(sourceWidth < outWidth) {
	        outWidth = sourceWidth;
	    }
	    setOutHeight();
	    outFolder = newOutFolder;
	    if(newQuality == 100) {
	        quality = 85;
	    }
	    else {
	        quality = newQuality;
	    }
	    boolean success = generateScaledImage();
	    
	    return success;
	}
	
	public boolean generateThumbnail(int newQuality) throws Exception {
	    resetDefault();
	    
	    if(sourceWidth < outWidth) {
	        outWidth = sourceWidth;
	    }
	    setOutHeight();
	    if(newQuality == 100) {
	        quality = 85;
	    }
	    else {
	        quality = newQuality;
	    }
	    boolean success = generateScaledImage();
	    
	    return success;
	} 
	
	public boolean generateThumbnail() throws Exception {
	    resetDefault();
	    
	    if(sourceWidth < outWidth) {
	        outWidth = sourceWidth;
	    }
	    setOutHeight();
	    boolean success = generateScaledImage();
	    
	    return success;
	}
	
	/**
	 * Generate a thumbnail image from the source image specified in constructor
	 * @return Returns true if thumbnail was successfully generated, or false otherwise
	 */
	private boolean generateScaledImage() throws Exception {
	    setFileAttributes();
	    boolean success = false;
	    /*
	    if (! isLandscapeImage()) {
	        int temp = outWidth;
	        outWidth = outHeight;
	        outHeight = temp;
	    }
	    */
	    if (isValidImage()) {
		    // Draw original image to thumbnail image object and
		    // scale it to the new size on-the-fly
		    BufferedImage thumbImage = new BufferedImage(outWidth, 
		            outHeight, BufferedImage.TYPE_INT_RGB);
		    Graphics2D graphics2D = thumbImage.createGraphics();
		    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
		            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		    graphics2D.drawImage(image, 0, 0, outWidth, outHeight, null);
		    
		    // Save thumbnail image to a new file
		    try {
		        if (! isThumbFolderExist()) {
		            File dir = new File(sourceRootPath + "/" + outFolder);
		            dir.mkdir();
		        }
			    BufferedOutputStream out = new BufferedOutputStream(
			            new FileOutputStream(outFile));
			    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			    JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
			    param.setQuality((float)quality / 100.0f, false);
			    encoder.setJPEGEncodeParam(param);
			    encoder.encode(thumbImage);
			    out.close();
			    
			    success = true;
		    }
		    catch (IOException error) {
		        throw new IOException("Exception caught while generating thumbnail image: " + error);
		    }
	    }
	    
	    return success;
	}
    
	private void setFileAttributes() {
		sourceFileName = file.getName();
		sourceRootPath = storage.getRootPath() + file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("/"));
		
		if(outFolder != null) {
		    outFile = sourceRootPath + "/" + outFolder + "/" + sourceFileName;
		}
		else {
		    outFile = sourceRootPath + "/" + sourceFileName;
		}
	}
	
	private void resetDefault() {
	    outFolder = "thumbnails";
		outWidth = 120;
		outHeight = 90;
		quality  = 85;
	}
	
	private boolean isValidImage() {
	    if (sourceWidth == -1 || sourceHeight == -1) {
	        log.error("The image " + inFile + " was corrupted or does not exist.");
	        return false;
	    }
	    else {
	        return true;
	    }
	}
	
	/**
	 * Check if the source image is in landscape mode
	 * @return Returns true if the source image is landscape, or false otherwise
	 */
	private boolean isLandscapeImage() {
	    if (sourceWidth > sourceHeight) {
	        return true;
	    }
	    else {
	        return false;
	    }
	}
	
	/**
	 * Check if the 'thumbnails' subfolder already exist in source image directory
	 * @return Returns true if 'thumbnails' subfolder already exist, or false otherwise
	 */
	private boolean isThumbFolderExist() {
        File dir = new File(sourceRootPath);
        File[] files = dir.listFiles();
        boolean found = false;
        
        if(outFolder != null) {
	        for (int i=0; i<files.length && found == false; i++) {
	            if (files[i].isDirectory()) {
	                if (files[i].getName().equals(outFolder)) {
	                    found = true;
	                }
	            }
	        }		
        }
        else {
            found = true;
        }
        
        return found;
    }
	
    public int getSourceHeight() {
        return sourceHeight;
    }
    public void setSourceHeight(int sourceHeight) {
        this.sourceHeight = sourceHeight;
    }
    public int getSourceWidth() {
        return sourceWidth;
    }
    public void setSourceWidth(int sourceWidth) {
        this.sourceWidth = sourceWidth;
    }
    public int getOutHeight() {
        return outHeight;
    }
	private void setOutHeight() {
	    double ratio = (double) sourceWidth / outWidth;
	    double dblOutHeight = sourceHeight / ratio;
	    outHeight = (int) dblOutHeight;
	}
    public int getOutWidth() {
        return outWidth;
    }
}