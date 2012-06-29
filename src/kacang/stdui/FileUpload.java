package kacang.stdui;

import kacang.runtime.upload.FormFile;
import kacang.runtime.upload.MultipartRequest;
import kacang.services.storage.StorageFile;
import kacang.ui.Widget;
import kacang.ui.Event;
import kacang.ui.Forward;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * This class is used to create a HTML form input element of type - File(File-seclection form field)
 * -- an element that lets users select a file stored on their computer and send it to the server
 * when they submit the form.
 * </p>
 */
public class FileUpload extends FormField {
    private String onBlur;
    private String onChange;
    private String onFocus;
    private String size;

    /**
     * Default constructor of FileUpload. The name of the widget has to be set before
     * it's added as a child to its parent.
     */
    public FileUpload() {
        super();
    }

    /**
     * Create a FileUpload widget with the specified widget name.
     * @param name The specified name.
     */
    public FileUpload(String name) {
        super(name);
    }

    public void onRequest(Event evt) {
        // ensure that root form is multipart
        Form rootForm = getRootForm();
        if (rootForm != null && !"multipart/form-data".equalsIgnoreCase(rootForm.getEnctype())) {
            rootForm.setEnctype("multipart/form-data");
            rootForm.setMethod("POST");
        }
    }

    public Forward onSubmit(Event evt) {
        HttpServletRequest request = evt.getRequest();
        if (request instanceof MultipartRequest) {
            MultipartRequest multipartReq = (MultipartRequest) request;
            FormFile formFile = multipartReq.getFormFile(getAbsoluteName());
            if (formFile != null && formFile.getFileName().trim().length() > 0) {
                setValue(formFile.getFileName());
            }
            else {
                setValue(null);
            }
        }
        performValidation(evt);
        return null;
    }

    /**
     * Return the handle script for OnBlur event.
     * @return The handle script
     */
    public String getOnBlur() {
        return onBlur;
    }

    /**
     * Set the handle script for OnBlur event.
     * @param onBlur The handle script
     */
    public void setOnBlur(String onBlur) {
        this.onBlur = onBlur;
    }

    /**
     * Return the handle script for OnChange event.
     * @return The handle script
     */
    public String getOnChange() {
        return onChange;
    }

    /**
     * Set the handle script for OnChange event.
     * @param onChange The handle script.
     */
    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    /**
     * Return the handle script for OnFocus event.
     * @return The handle script
     */
    public String getOnFocus() {
        return onFocus;
    }

    /**
     * Set the handle script for OnFocus event.
     * @param onFocus The handle script.
     */
    public void setOnFocus(String onFocus) {
        this.onFocus = onFocus;
    }

    public String getDefaultTemplate() {
        return "fileupload";
    }

    /**
     * Retrieves the width of the object
     * @return width of the TextField
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the width of the object
     * @param size Width of the TextField
     */
    public void setSize(String size) {
        this.size = size;
    }
    
    public void setParent(Widget parent) {
        // inherited behaviour
        super.setParent(parent);
        // set parent form's enctype to multipart/form-data
        Form rootForm = getRootForm();
        if (rootForm != null) {
            rootForm.setEnctype("multipart/form-data");
            rootForm.setMethod("POST");
        }
    }

    /**
     * Return the InputStream object of the file to be uploaded.
     * @param request The HttpServletRequest request.
     * @return The InputStream object of the file.
     * @throws IOException
     */
    public InputStream getInputStream(HttpServletRequest request) throws IOException {
        if (!(request instanceof MultipartRequest)) throw new IOException("Request is not Multipart");
        MultipartRequest multipartReq = (MultipartRequest) request;
        FormFile formFile = multipartReq.getFormFile(getAbsoluteName());
        if (formFile != null)
            return formFile.getInputStream();
        else
            return null;
    }

    /**
     * Return the file to be uploaded in <code>{@link StorageFile}</code> object.
     * @param request The HttpServletRequest request.
     * @return The <code>{@link StorageFile}</code> representing the file to be uploaded.
     * @throws IOException
     */
    public StorageFile getStorageFile(HttpServletRequest request) throws IOException {
        if (!(request instanceof MultipartRequest)) throw new IOException("Request is not Multipart");
        StorageFile sf = null;
        MultipartRequest multipartReq = (MultipartRequest) request;
        FormFile formFile = multipartReq.getFormFile(getAbsoluteName());
        if (formFile != null && formFile.getFileName().trim().length() > 0) {
            sf = new StorageFile("/", formFile);
            return sf;
        } else {
            return null;
        }
    }
}