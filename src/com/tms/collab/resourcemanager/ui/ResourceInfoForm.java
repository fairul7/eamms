package com.tms.collab.resourcemanager.ui;

import kacang.stdui.*;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Forward;
import kacang.ui.Event;
import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DaoQuery;
import kacang.services.storage.StorageService;
import kacang.services.storage.StorageFile;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.services.security.SecurityException;
import kacang.services.security.Group;
import kacang.services.security.ui.UsersSelectBox;
import kacang.util.UuidGenerator;
import kacang.util.Log;
import com.tms.collab.resourcemanager.model.Resource;
import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.collab.calendar.ui.UserUtil;
import com.tms.collab.calendar.ui.CalendarUsersSelectBox;
import com.tms.collab.calendar.model.CalendarModule;

import java.util.*;

import org.apache.commons.collections.SequencedHashMap;


public class ResourceInfoForm extends Form {
    private Resource resource;
    private TextField nameTextField, imageUrlTextField;
    private FileUpload imageFileUpload;
    private TextBox descriptionTextBox;
    private ComboSelectBox groupSelectBox;
    private CalendarUsersSelectBox usersSelectBox;
    private Button submitButton, changeButton, cancelButton;
    private String id;
    private boolean editImage = true;
    private ButtonGroup imageBG, approveBG, classBG;
    private Radio radioAprrovalYes, radioApprovalNo, radioImageUpload, radioImageUrl;
    private Radio radioPublic, radioPrivate;
    private SelectBox categoryList;
    private TextField newCategory;
    private UsersSelectBox authorizedUsers;
    private CheckBox active;
    public static String[] IMAGE_CONTENT_TYPES = new String[]{
        "image/bmp",
        "image/x-windows-bmp",
        "image/gif",
        "image/x-icon",
        "image/jpeg",
        "image/pjpeg",
        "image/x-pcx",
        "image/x-pict",
        "image/pict",
        "image/png",
        "image/tiff",
        "image/x-tiff",
        "video/mpeg",
        "video/x-mpeg",
        "video/x-mpeq2a",
        "video/quicktime",
        "video/msvideo",
        "video/x-msvideo",
        "video/avi",
        "video/x-ms-asf",
        "windows/metafile"
    };

    public ResourceInfoForm() {

    }

    public ResourceInfoForm(Resource resource) {
        this.resource = resource;
    }

    public void init() {
        super.init();
        removeChildren();
        authorizedUsers = new UsersSelectBox("authorizedSB");
        addChild(authorizedUsers);
        authorizedUsers.init();
        Map userMap = new SequencedHashMap();
        try {
            Collection userList = UserUtil.getUserListByPermission(CalendarModule.PERMISSION_CALENDARING);
            for (Iterator i = userList.iterator(); i.hasNext();) {
                User user = (User) i.next();
                userMap.put(user.getId(), user.getName());
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        radioAprrovalYes = new Radio("approveYes", Application.getInstance().getMessage("resourcemanager.label.Yes", "Yes"));
        // radioAprrovalYes.setChecked(true);
        radioAprrovalYes.setOnClick("checkApproval();");
        radioApprovalNo = new Radio("approveNo", Application.getInstance().getMessage("resourcemanager.label.No", "No"));
        radioApprovalNo.setOnClick("checkApproval();");
        radioApprovalNo.setChecked(true);
        radioImageUpload = new Radio("imageUpload", Application.getInstance().getMessage("resourcemanager.label.UploadImageFile", "Upload Image File:"));
        radioImageUrl = new Radio("imageUrl", Application.getInstance().getMessage("resourcemanager.label.ImageUrl", "Image Url:"));
        approveBG = new ButtonGroup("approveBG", new Radio[]{radioApprovalNo, radioAprrovalYes});
        imageBG = new ButtonGroup("imageBG", new Radio[]{radioImageUpload, radioImageUrl});
        submitButton = new Button("submitButton");
        nameTextField = new TextField("nameTextField");
        nameTextField.setSize("40");
        nameTextField.addChild(new ValidatorNotEmpty("Name"));
        imageUrlTextField = new TextField("imageUrlTextField");
        imageUrlTextField.setSize("40");
        imageFileUpload = new FileUpload("ImageFileUpload");
        descriptionTextBox = new TextBox("descriptionTextBox");
        descriptionTextBox.setRows("4");
        descriptionTextBox.addChild(new ValidatorNotEmpty("Description"));
        usersSelectBox = new CalendarUsersSelectBox("usersSelectBox");
        groupSelectBox = new ComboSelectBox("groups");
        radioPublic = new Radio("radioPublic", Application.getInstance().getMessage("resourcemanager.label.Public", "Public"), true);
        radioPublic.setOnClick("checkClassification();");
        radioPrivate = new Radio("radioPrivate", Application.getInstance().getMessage("resourcemanager.label.Private", "Private"));
        radioPrivate.setOnClick("checkClassification();");
        //radioPrivate.
        classBG = new ButtonGroup("classBG", new Radio[]{radioPublic, radioPrivate});
        newCategory = new TextField("newCat");
        newCategory.setSize("30");
        addChild(newCategory);
        categoryList = new SelectBox("catList");
        categoryList.addOption("-1", Application.getInstance().getMessage("resourcemanager.label.Selectacategory", "Select a category"));
        Map cmap = null;
        try {
            cmap = ((ResourceManager) Application.getInstance().getModule(ResourceManager.class)).getResourceCategories();
        }
        catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        for (Iterator i = cmap.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            categoryList.addOption(key, (String) cmap.get(key));
        }
        addChild(categoryList);
        if (resource == null) {
            submitButton.setText(Application.getInstance().getMessage("resourcemanager.label.Submit", "Submit"));
        }
        else {
            submitButton.setText(Application.getInstance().getMessage("resourcemanager.label.Save", "Save"));
        }
        if (resource != null && resource.getImageType() != -1) {
            changeButton = new Button("ChangeImageButton");
            if (resource.getImageType() == ResourceManager.RESOURCE_IMAGE_TYPE_URL)
                changeButton.setText(Application.getInstance().getMessage("resourcemanager.label.ChangeImage", "Change Image"));
            else
                changeButton.setText(Application.getInstance().getMessage("resourcemanager.label.DeleteImage", "Delete Image"));
            addChild(changeButton);
        }
        cancelButton = new Button("cancelbutton", Application.getInstance().getMessage("resourcemanager.label.Cancel", "Cancel"));
        active = new CheckBox("active", Application.getInstance().getMessage("resourcemanager.label.Active"));
        addChild(cancelButton);
        addChild(classBG);
        addChild(radioPrivate);
        addChild(radioPublic);
        addChild(radioApprovalNo);
        addChild(radioAprrovalYes);
        addChild(radioImageUpload);
        addChild(radioImageUrl);
        addChild(approveBG);
        addChild(imageBG);
        addChild(submitButton);
        addChild(nameTextField);
        addChild(imageUrlTextField);
        addChild(imageFileUpload);
        addChild(descriptionTextBox);
        addChild(usersSelectBox);
        addChild(groupSelectBox);
        addChild(active);

        usersSelectBox.init();
        groupSelectBox.init();

    }

    public Forward onSubmit(Event event) {
        String butt = findButtonClicked(event);
        if (cancelButton.getAbsoluteName().equals(butt)) {
            id = null;
            resource = null;
            return new Forward("cancel");
        }
        return super.onSubmit(event);
    }

    public void onRequest(Event evt) {

        if (id != null && id.trim().length() > 0 && (resource == null || !resource.getId().equals(id))) {
            try {
                populateForm(evt);
            }
            catch (SecurityException e) {
                Log.getLog(getClass()).error(e);
            }
            catch (Exception e) {
                Log.getLog(getClass()).error(e);
            }
        }
        else {
            initGroups();
        }

    }


    public Forward onValidate(Event evt) {
        if (!(cancelButton.getAbsoluteName().equals(findButtonClicked(evt)))) {
            try {
                if (id == null) {
                    if (resource == null)
                        setId(UuidGenerator.getInstance().getUuid());
                    else
                        setId(resource.getId());
                }
            }
            catch (Exception e) {
                Log.getLog(getClass()).error(e);
            }
            String buttonClicked = findButtonClicked(evt);
            ResourceManager RM = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
            if (buttonClicked.equals(submitButton.getAbsoluteName())) {
                if (resource != null) {
                    updateResource();
                    Forward f = processImage(evt);
                    if (f != null) return f;
                    if (radioPrivate.isChecked()) {
                        if (usersSelectBox.getIds().length > 0 || groupSelectBox.getRightValues().size() > 0) {
                            Map userMap = new SequencedHashMap();
                            Map groupMap = groupSelectBox.getRightValues();
                            String[] ids = usersSelectBox.getIds();
                            Map calendarUsers = getUserMap();
                            for (int i=0; i<ids.length; i++) {
                                String userId = ids[i];
                                if (calendarUsers.containsKey(userId)) {
                                    userMap.put(userId, calendarUsers.get(userId));
                                }
                            }
                            RM.updateResourceAccess(resource, userMap, groupMap);
                        }
                        else {
                            resource.setClassification(ResourceManager.RESOURCE_CLASSIFICATION_TYPE_PUBLIC);
                            RM.deleteResourceAccess(resource);
                        }

                    }
                    if (resource.isRequireApproval()) {
                        String[] aUsers = this.authorizedUsers.getIds();
                        if (aUsers.length > 0) {
                            RM.updateAuthorizedMembers(resource, aUsers);
                        }
                        else {
                            resource.setRequireApproval(false);
                            RM.deleteAuthorizedUsers(resource);
                        }
                    }
                    RM.updateResource(resource);

                    evt.getRequest().setAttribute("id", id);
                    init();
                    id = null;
                    resource = null;
                    return new Forward("updated");
                }
                else {
                    resource = createNewResource();
                    Forward f = processImage(evt);
                    if (f != null)
                        return f;
                    if (resource.isRequireApproval()) {
                        String[] aUsers = this.authorizedUsers.getIds();
                        if (aUsers.length > 0) {
                                RM.addAuthorizedUsers(resource.getId(), aUsers);
                        }
                        else {
                            resource.setRequireApproval(false);
                        }
                    }

                    Map userMap = new SequencedHashMap();
                    Map groupMap = groupSelectBox.getRightValues();
                    String[] ids = usersSelectBox.getIds();
                    Map calendarUsers = getUserMap();
                    for (int i=0; i<ids.length; i++) {
                        String userId = ids[i];
                        if (calendarUsers.containsKey(userId)) {
                            userMap.put(userId, calendarUsers.get(userId));
                        }
                    }
                    RM.addResourceAccess(resource, userMap, groupMap);
                    RM.addResource(resource);
                    evt.getRequest().setAttribute("id", id);
                    id = null;
                    resource = null;
                    init();

                    return new Forward("resource added");
                    // return new Forward(null,evt.getRequest().getRequestURI()+"?resourceId="+id,false);
                }
            }
            else if (buttonClicked.equals(changeButton.getAbsoluteName())) {
                if (changeButton.getText().equals(Application.getInstance().getMessage("resourcemanager.label.ChangeImage", "Change Image"))) {
                    editImage = true;
                }
                else if (changeButton.getText().equals(Application.getInstance().getMessage("resourcemanager.label.DeleteImage", "Delete Image"))) {
                    StorageService ss = (StorageService) Application.getInstance().getService(StorageService.class);
                    try {
                        ss.delete(new StorageFile("/RM/" + id + "/"/*+resource.getImage())*/));
                        if (resource != null) {
                            resource.setImageType(0);
                            resource.setImage("");
                            RM = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
                            RM.updateResource(resource);
                        }

                    }
                    catch (Exception e) {
                    }
                    editImage = true;
                }
            }
        }
        return new Forward();
    }

    private Map getUserMap() {
        Map userMap = new SequencedHashMap();
        SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        try {
            Collection usersCol = ss.getUsersByPermission(CalendarModule.PERMISSION_CALENDARING, Boolean.TRUE, "firstName", false, 0, -1);
            User user;
            for (Iterator i = usersCol.iterator(); i.hasNext();) {
                user = (User) i.next();
                userMap.put(user.getId(), user.getName());
            }
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e);
        }
        return userMap;
    }

    private Map getGroupMap() {
        Map map = new SequencedHashMap();
        Collection list = new ArrayList();
        try {
            list = ((SecurityService) Application.getInstance().getService(SecurityService.class)).getGroups(new DaoQuery(), 0, -1, "groupName", false);
            for (Iterator i = list.iterator(); i.hasNext();) {
                Group group = (Group) i.next();
                map.put(group.getId(), group.getName());
            }
        } catch (Exception e) {
            Log.getLog(getClass()).error(e.toString(), e);
        }
        return map;
    }

    private void updateResource() {
        resource.setName((String) nameTextField.getValue());
        resource.setDescription((String) descriptionTextBox.getValue());
        resource.setRequireApproval(radioAprrovalYes.isChecked());
        resource.setModificationDate(new Date());
        resource.setModifiedBy(getWidgetManager().getUser().getId());
        resource.setClassification(radioPublic.isChecked() ? ResourceManager.RESOURCE_CLASSIFICATION_TYPE_PUBLIC :
                ResourceManager.RESOURCE_CLASSIFICATION_TYPE_PRIVATE);
        resource.setActive(active.isChecked());
        resource.setDeleted(active.isChecked() ? false : true);
        assignCategory(resource);
    }

    private Resource createNewResource() {
        boolean requireApproval;
        if (radioAprrovalYes.isChecked())
            requireApproval = true;
        else
            requireApproval = false;
        Resource resource = new Resource(id, (String) nameTextField.getValue(), (String) descriptionTextBox.getValue(),
                getWidgetManager().getUser().getId(), (String) imageUrlTextField.getValue(), requireApproval, false);
        resource.setActive(active.isChecked());
        resource.setDeleted(active.isChecked() ? false : true);
        resource.setCreationDate(new Date());
        resource.setClassification(radioPublic.isChecked() ? ResourceManager.RESOURCE_CLASSIFICATION_TYPE_PUBLIC : ResourceManager.RESOURCE_CLASSIFICATION_TYPE_PRIVATE);
        assignCategory(resource);
        SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
        try {
            if (ss.hasPermission(getWidgetManager().getUser().getId(), ResourceManager.PERMISSION_APPROVE_RESOURCE, null, null))
                resource.setApproved(true);
            else
                resource.setApproved(false);
        }
        catch (Exception e) {
            Log.getLog(getClass()).error(e);
        }
        return resource;
    }

    private void assignCategory(Resource resource) {
        Map selectedCat = categoryList.getSelectedOptions();
        if (newCategory.getValue() != null && ((String) newCategory.getValue()).trim().length() > 0) {
            String newCat = ((String) newCategory.getValue()).trim();
            Map catMap = categoryList.getOptionMap();
            if (catMap.entrySet().contains(newCat)) {
                resource.setCategory(newCat);
                for (Iterator i = catMap.keySet().iterator(); i.hasNext();) {
                    String catKey = (String) i.next();
                    if (newCat.equals(catMap.get(catKey)) && !catKey.equals("-1")) {
                        resource.setCategoryId(catKey);
                        break;
                    }
                }
            }
            else {
                ResourceManager rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
                String newCatId = UuidGenerator.getInstance().getUuid();
                rm.addNewCategory(newCatId, newCat);
                resource.setCategoryId(newCatId);
                resource.setCategory(newCat);
            }
        }
        else if (selectedCat.size() > 0 && !selectedCat.containsKey("-1")) {
            String catId = (String) selectedCat.keySet().iterator().next();
            resource.setCategoryId(catId);
            resource.setCategory(selectedCat.get(catId).toString());
        }
    }

    protected void initGroups() {
        Map availableGroupMap = getGroupMap();
        groupSelectBox.setLeftValues(availableGroupMap);
    }

    protected void populateForm(Event evt) throws SecurityException, DaoException {
        submitButton.setText(Application.getInstance().getMessage("resourcemanager.label.Update", "Update"));
        ResourceManager rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
        try {
            resource = rm.getResource(id, false);
        }
        catch (DaoException e) {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
        nameTextField.setValue(resource.getName());
        descriptionTextBox.setValue(resource.getDescription());
        if (resource.isRequireApproval()) {
            radioAprrovalYes.setChecked(true);
            radioApprovalNo.setChecked(false);
        }
        else {
            radioApprovalNo.setChecked(true);
            radioAprrovalYes.setChecked(false);
        }

        Map availableGroupMap = getGroupMap();
        Map selectedGroupMap = new SequencedHashMap();
        Collection accessCol = rm.getResourceAccessIds(resource.getId(), false);
        Collection accessUsers = new ArrayList();
        Collection accessGroups = new ArrayList();
        for (Iterator i=accessCol.iterator(); i.hasNext();) {
            String principalId = (String)i.next();
            if (principalId.startsWith(User.class.getName())) {
                accessUsers.add(principalId);
            }
            else {
                accessGroups.add(principalId);
                selectedGroupMap.put(principalId, availableGroupMap.get(principalId));
                availableGroupMap.remove(principalId);
            }
        }
        groupSelectBox.setLeftValues(availableGroupMap);
        groupSelectBox.setRightValues(selectedGroupMap);
        usersSelectBox.setIds((String[])accessUsers.toArray(new String[0]));

        int type = resource.getImageType();
        if (type != 0) {
            changeButton = new Button("ChangeImageButton");
            editImage = false;
        }
        else
            editImage = true;
        if (type == ResourceManager.RESOURCE_IMAGE_TYPE_UPLOAD) {
            changeButton.setText(Application.getInstance().getMessage("resourcemanager.label.DeleteImage", "Delete Image"));
            radioImageUpload.setChecked(true);
        }
        else if (type == ResourceManager.RESOURCE_IMAGE_TYPE_URL) {
            radioImageUrl.setChecked(true);
            changeButton.setText(Application.getInstance().getMessage("resourcemanager.label.ChangeImage", "Change Image"));
            imageUrlTextField.setValue(resource.getImage());
        }
        if (changeButton != null) {
            addChild(changeButton);
        }
        if (resource.getCategoryId() != null)
            categoryList.setSelectedOptions(new String[]{resource.getCategoryId()});
        Collection auth = resource.getAuthorities();
        String[] userIds = (String[])auth.toArray(new String[0]);
        authorizedUsers.setIds(userIds);
        radioPublic.setChecked((resource.getClassification() == ResourceManager.RESOURCE_CLASSIFICATION_TYPE_PUBLIC));
        radioPrivate.setChecked((resource.getClassification() == ResourceManager.RESOURCE_CLASSIFICATION_TYPE_PRIVATE));
        active.setChecked(resource.isDeleted());
    }


    private Forward processImage(Event evt) {
        if (resource != null && editImage) {
            changeButton = new Button("ChangeImageButton");
            //String type = evt.getRequest().getParameter(getAbsoluteName()+".imageType");
            String type = imageBG.getSelectedButton();
            if (type == null) {
                resource.setImageType(0);
                editImage = true;
            }
            else if (radioImageUpload.getAbsoluteName().equals(type)) {
                resource.setImageType(ResourceManager.RESOURCE_IMAGE_TYPE_UPLOAD);
                try {
                    StorageFile sf = imageFileUpload.getStorageFile(evt.getRequest());
                    if (sf != null) {
                        if (Arrays.asList(IMAGE_CONTENT_TYPES).contains(sf.getContentType())) {
                            StorageService ss = (StorageService) Application.getInstance().getService(new StorageService().getClass());
                            sf.setParentDirectoryPath("/RM/" + id);
                            ss.store(sf);
                        }
                        else
                            return new Forward("invalid file type");
                    }
                    resource.setImage(sf.getAbsolutePath());
                    changeButton.setText(Application.getInstance().getMessage("resourcemanager.label.DeleteImage", "Delete Image"));
                    editImage = false;
                }
                catch (Exception e) {
                    Log.getLog(getClass()).error(e);
                }

            }
            else if (radioImageUrl.getAbsoluteName().equals(type)) {
                String image = (String) imageUrlTextField.getValue();
                if (image != null && image.trim().length() > 0) {
                    resource.setImageType(ResourceManager.RESOURCE_IMAGE_TYPE_URL);
                    resource.setImage(image);
                    editImage = false;
                }
                else
                    resource.setImageType(0);
                changeButton.setText(Application.getInstance().getMessage("resourcemanager.label.ChangeImage", "Change Image"));
            }
            addChild(changeButton);
        }
        return null;
    }

    public Button getChangeButton() {
        return changeButton;
    }

    public void setChangeButton(Button changeButton) {
        this.changeButton = changeButton;
    }

    public ComboSelectBox getGroupSelectBox() {
        return groupSelectBox;
    }

    public void setGroupSelectBox(ComboSelectBox groupSelectBox) {
        this.groupSelectBox = groupSelectBox;
    }

    public String getDefaultTemplate() {
        return "resourcemanager/resourceform";
    }


    public TextField getNameTextField() {
        return nameTextField;
    }

    public void setNameTextField(TextField nameTextField) {
        this.nameTextField = nameTextField;
    }

    public TextBox getDescriptionTextBox() {
        return descriptionTextBox;
    }

    public void setDescriptionTextBox(TextBox descriptionTextBox) {
        this.descriptionTextBox = descriptionTextBox;
    }

    public FileUpload getImageFileUpload() {
        return imageFileUpload;
    }

    public void setImageFileUpload(FileUpload imageFileUpload) {
        this.imageFileUpload = imageFileUpload;
    }

    public TextField getImageUrlTextField() {
        return imageUrlTextField;
    }

    public void setImageUrlTextField(TextField imageUrlTextField) {
        this.imageUrlTextField = imageUrlTextField;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public void setSubmitButton(Button submitButton) {
        this.submitButton = submitButton;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;

    }

    public boolean isEditImage() {
        return editImage;
    }

    public void setEditImage(boolean editImage) {
        this.editImage = editImage;
    }

    public Radio getRadioAprrovalYes() {
        return radioAprrovalYes;
    }

    public void setRadioAprrovalYes(Radio radioAprrovalYes) {
        this.radioAprrovalYes = radioAprrovalYes;
    }

    public Radio getRadioApprovalNo() {
        return radioApprovalNo;
    }

    public void setRadioApprovalNo(Radio radioApprovalNo) {
        this.radioApprovalNo = radioApprovalNo;
    }

    public Radio getRadioImageUpload() {
        return radioImageUpload;
    }

    public void setRadioImageUpload(Radio radioImageUpload) {
        this.radioImageUpload = radioImageUpload;
    }

    public Radio getRadioImageUrl() {
        return radioImageUrl;
    }

    public void setRadioImageUrl(Radio radioImageUrl) {
        this.radioImageUrl = radioImageUrl;
    }

    public CalendarUsersSelectBox getUsersSelectBox() {
        return usersSelectBox;
    }

    public void setUsersSelectBox(CalendarUsersSelectBox usersSelectBox) {
        this.usersSelectBox = usersSelectBox;
    }

    public Radio getRadioPrivate() {
        return radioPrivate;
    }

    public void setRadioPrivate(Radio radioPrivate) {
        this.radioPrivate = radioPrivate;
    }

    public Radio getRadioPublic() {
        return radioPublic;
    }

    public void setRadioPublic(Radio radioPublic) {
        this.radioPublic = radioPublic;
    }

    public SelectBox getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(SelectBox categoryList) {
        this.categoryList = categoryList;
    }

    public TextField getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(TextField newCategory) {
        this.newCategory = newCategory;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public UsersSelectBox getAuthorizedUsers() {
        return authorizedUsers;
    }

    public void setAuthorizedUsers(UsersSelectBox authorizedUsers) {
        this.authorizedUsers = authorizedUsers;
    }

    public ButtonGroup getApproveBG() {
        return approveBG;
    }

    public void setApproveBG(ButtonGroup approveBG) {
        this.approveBG = approveBG;
    }

    public ButtonGroup getClassBG() {
        return classBG;
    }

    public void setClassBG(ButtonGroup classBG) {
        this.classBG = classBG;
    }

    public CheckBox getActive()
    {
        return active;
    }

    public void setActive(CheckBox active)
    {
        this.active = active;
    }
}



