package com.tms.quotation.ui;

import kacang.stdui.Label;
import kacang.stdui.TextField;

public class ItemField {
  private String name;
  private Label label;
  private TextField textField;
  private String columnId;


  public ItemField(String name) {
    this.name = name;
    label = new Label( name+"Label" );
    textField = new TextField(name+"TextField");
  }
/*  
  public ItemField(int suffix, String value) {
    label = new Label("Label"+suffix, value);
    textField = new TextField("TextField"+suffix);
  }
  
  public void setValidator(String name) {
    textField.addChild(new ValidatorNotEmpty(name, "Mandatory Field"));
  }
*/
  
  public String getName() {
    return name;
  }
  public String getColumnId() {
    return columnId;
  }
  public Label getLabel() {
    return label;
  }
  public TextField getTextField() {
    return textField;
  }
  public void setName(String name) {
    this.name = name;
  }
  public void setLabel(Label label) {
    this.label = label;
  }
  public void setTextField(TextField textField) {
    this.textField = textField;
  }
  public void setColumnId(String columnId) {
    this.columnId = columnId;
  }
  
}
