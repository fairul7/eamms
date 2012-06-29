package com.tms.collab.formwizard.engine;

import kacang.stdui.FileUpload;

public class FileField extends Field {
    private ValidatorNotEmptyField validatorNotEmpty;
    private FileUpload fileUpload;

    public FileUpload getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    public ValidatorNotEmptyField getValidatorNotEmpty() {
        return validatorNotEmpty;
    }

    public void setValidatorNotEmpty(ValidatorNotEmptyField validatorNotEmpty) {
        this.validatorNotEmpty = validatorNotEmpty;
    }
}
