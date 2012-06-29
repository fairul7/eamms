/*
 * MediaFormPopupEdit
 * Date Created: Jun 28, 2005
 * Author: Tien Soon, Law
 * Description: TODO Change the class descriptions
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;


public class MediaFormPopupEdit extends MediaFormEdit {
    public String getDefaultTemplate() {
        return "medialib/popupEditMedia";
    }
    
    public void init() {
        super.init();
        
        cancel.setOnClick("closeWindow()");
    }
}
