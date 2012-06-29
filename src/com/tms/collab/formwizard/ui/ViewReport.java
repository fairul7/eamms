package com.tms.collab.formwizard.ui;

import kacang.stdui.*;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.Widget;
import kacang.Application;
import kacang.util.Log;
import kacang.model.DaoQuery;
import kacang.model.operator.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.tms.collab.formwizard.model.*;
import com.tms.collab.formwizard.xmlwidget.DateFieldElement;
import com.tms.collab.formwizard.xmlwidget.FileUploadElement;
import com.tms.collab.formwizard.xmlwidget.FormElement;
import com.tms.collab.formwizard.engine.TextFieldField;
import com.tms.util.FormatUtil;

public class ViewReport extends Table {
    private Map selected;
    private String formName;
    private String formId;


    public String getDefaultTemplate()        {
        return "formwizard/formDataTable";
    }

    public void onRequest(Event event) {
        initTable();
    }

    public ViewReport() {
    }

    public ViewReport(String s) {
        super(s);
    }

    public void initTable(){
        super.init();
        setPageSize(10);
        setModel(new ViewReportModel(getSelected()));
        setWidth("100%");
    }

    class ViewReportModel extends TableModel{
        protected Map optionMap;
        protected FormElement form = null;

        ViewReportModel() {
        }

        ViewReportModel(Map selected){
			String nodeElement = "";        	
            optionMap = selected;
            TableColumn postedByColumn = null;

            Set keySet = optionMap.keySet();
            String key = "";           
            addAction(new TableAction("export", Application.getInstance().getMessage("formWizard.label.viewReport.exportCSV","Export into CSV Format"),""));
            addAction(new TableAction("delete",Application.getInstance().getMessage("formWizard.label.viewReport.delete","Delete"),Application.getInstance().getMessage("formWizard.label.viewReport.confirmDelete","Are You Sure Delete the form data?")));
            form = Util.getFormElement(formId);
            Map map = new HashMap();
		    map.put("","N/A");
			for (Iterator valueIter = keySet.iterator(); valueIter.hasNext();) {
				key = String.valueOf(valueIter.next());
				nodeElement = Util.getNodeElement(form,key);
				
				
				String colName = String.valueOf(optionMap.get(key));
				
			    
				TableColumn tcTemp =new TableColumn(key, colName);
				if (DateFieldElement.ELEMENT_NAME.equals(nodeElement)) {
					tcTemp.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
				}
                else if (FileUploadElement.ELEMENT_NAME.equals(nodeElement)) {
                    tcTemp.setFormat(new TableGetFilenameFormat());
                }
                else {
					tcTemp.setFormat(new TableReplaceStringFormat(map));
				}
				

				addColumn(tcTemp);
			}

            postedByColumn = new TableColumn("username", Application.getInstance().getMessage("formWizard.label.viewReport.postedBy","Posted By"));
            postedByColumn.setUrlParam("id");
            postedByColumn.setFormat(new TableReplaceStringFormat(map));
			addColumn(postedByColumn);


			TableColumn postedDateColumn = new TableColumn("datePosted", Application.getInstance().getMessage("formWizard.label.viewReport.submissionDate","Submission Date"));
			postedDateColumn.setFormat(new TableDateFormat(FormatUtil.getInstance().getLongDateFormat()));
			addColumn(postedDateColumn);


            TextField tfSearch = new TextField("tfSearch");
            tfSearch.setSize("20");
            TableFilter searchFilter = new TableFilter("searchFilter");
            searchFilter.setWidget(tfSearch);
            addFilter(searchFilter);

            setOperatorFilter();
            setColumnFilter();
        }

        public void setOperatorFilter() {
            SelectBox sbOperator = new SelectBox("operator");
            sbOperator.addOption("",Application.getInstance().getMessage("security.label.pleaseSelect","--Please Select--"));
            sbOperator.addOption(FormConstants.OPERATOR_LIKE , Application.getInstance().getMessage("formWizard.operator.like","LIKE"));
            sbOperator.addOption(FormConstants.OPERATOR_EQUAL, Application.getInstance().getMessage("formWizard.operator.equal","EQUAL"));
            sbOperator.addOption(FormConstants.OPERATOR_GREATER, Application.getInstance().getMessage("formWizard.operator.greater","GREATER THAN"));
            sbOperator.addOption(FormConstants.OPERATOR_GREATER_EQUAL, Application.getInstance().getMessage("formWizard.operator.greater.equal","GREATER THAN AND EQUAL TO"));
            sbOperator.addOption(FormConstants.OPERATOR_LESS, Application.getInstance().getMessage("formWizard.operator.less","LESS THAN"));
            sbOperator.addOption(FormConstants.OPERATOR_LESS_EQUAL, Application.getInstance().getMessage("formWizard.operator.less.equal","LESS THAN AND EQUAL TO"));
            sbOperator.addOption(FormConstants.OPEARATOR_EARLIER, Application.getInstance().getMessage("formWizard.operator.earlier","EARLIER THAN (dd-mm-yyyy)"));
            sbOperator.addOption(FormConstants.OPEARATOR_LATER , Application.getInstance().getMessage("formWizard.operator.later","LATER THAN (dd-mm-yyyy)"));
            sbOperator.addOption(FormConstants.OPEARATOR_IS , Application.getInstance().getMessage("formWizard.operator.is","IS (dd-mm-yyyy)"));


            TableFilter tfColumn = new TableFilter("operator");
            tfColumn.setWidget(sbOperator);
            addFilter(tfColumn);

        }

        public void setColumnFilter() {
            String key = "", value = "";
            SelectBox sbColumn = new SelectBox("column");
            sbColumn.setMultiple(false);
            sbColumn.addOption("",Application.getInstance().getMessage("security.label.pleaseSelect","--Please Select--"));



            Set keySet = optionMap.keySet();

            for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
                key =  (String) iterator.next();
                value = (String) optionMap.get(key);
                sbColumn.addOption(key, value);
            }

            TableFilter tfColumn = new TableFilter("column");
            tfColumn.setWidget(sbColumn);
            addFilter(tfColumn);


        }

        public String getTableRowKey() {
            return "id";
        }

        public Collection getTableRows(){
            String tableName = FormModule.FORM_PREFIX + getFormName();
            Collection data =null;
            Set values = optionMap.keySet();
            StringBuffer columnBuffer = new StringBuffer();
            StringBuffer orderBuffer = new StringBuffer();
            
            for (Iterator valuesIter = values.iterator(); valuesIter.hasNext();) {
                String value = String.valueOf(valuesIter.next()).trim();
				columnBuffer.append(value).append(",");
				
                if(getSort()!=null &&  getSort() .equals(value))  {                                        
                    orderBuffer.append(" ORDER BY "+ value  );
                    if(isDesc())
                        orderBuffer.append(" DESC");
                }
            }

            columnBuffer.append(tableName).append(".formUid AS id,").append(tableName).append(".datePosted");
            
            if (orderBuffer.length() == 0 && getSort() != null) {
            	if (getSort().equals("datePosted"))
                    orderBuffer.append(" ORDER BY ").append(tableName).append(".").append(getSort() );
                else if (getSort().equals("username"))
                    orderBuffer.append(" ORDER BY ").append(getSort() );

            	if(isDesc())
    				orderBuffer.append(" DESC");
            }

            if (orderBuffer.length() == 0)
                orderBuffer.append(" ORDER BY ").append(tableName).append(".datePosted DESC");                        

            FormDao dao  = (FormDao) Application.getInstance().getModule(FormModule.class).getDao();
            try {
                data = dao.getDynamicRows(generateDaoProperties(),
                                          tableName,columnBuffer.toString(),orderBuffer.toString(),getStart(),getRows());

            } catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
            return data;
        }

        public int getTotalRowCount() {
            int rowCount = -1;
            String tableName = FormModule.FORM_PREFIX +getFormName();
            FormDao dao  = (FormDao) Application.getInstance().getModule(FormModule.class).getDao();
            try {
                rowCount =dao.getDynamicRowsCount(generateDaoProperties(), tableName);
            } catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
            return rowCount;
        }

		public DaoQuery generateDaoProperties() {
			DaoQuery properties = new DaoQuery();
			List columnList = (List) getFilterValue("column");
			List operatorList = (List) getFilterValue("operator");
			
			
			
			
			
			
			String value = "";
			String operator = FormConstants.OPERATOR_LIKE;
			
			if (columnList.iterator().hasNext())
				value = (String) columnList.iterator().next();
			
			if (operatorList.iterator().hasNext())
				operator = (String) operatorList.iterator().next();
			
			if (operator.equals(""))
				operator = FormConstants.OPERATOR_LIKE;
			
			
			
			
			// stuff i add
			{
			Object searchFilterValue = getFilterValue("searchFilter");
			if (searchFilterValue != null && (searchFilterValue.toString().trim().length() > 0) && (value==null || value.trim().length() <= 0)) {
				Map infoMap = FormDao.getTextFieldInfoMap(formId);
				for (Iterator i = infoMap.entrySet().iterator(); i.hasNext(); ) {
					Map.Entry e = (Map.Entry) i.next();
					TextFieldField textFieldField = (TextFieldField) e.getValue();
					value = textFieldField.getName();
					if (infoMap.containsKey(value)) {
						textFieldField = (TextFieldField) infoMap.get(value);
						if (textFieldField.getValidatorIsInteger() != null) {
							try {
								searchFilterValue = new Integer(searchFilterValue.toString());
							}
							catch(NumberFormatException ee) {
								searchFilterValue="";
							}
						} else if (textFieldField.getValidatorIsNumeric() != null) {
							try {
								searchFilterValue = new Float(searchFilterValue.toString());
							}
							catch(NumberFormatException ee) {
								searchFilterValue="";
							}
						}
					}

					OperatorParenthesis op = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
					if (FormConstants.OPERATOR_LIKE.equals(operator))
						op.addOperator(new OperatorLike(value, getFilterValue("searchFilter"), null));
					else if (FormConstants.OPERATOR_EQUAL.equals(operator))
						op.addOperator(new OperatorEquals(value, searchFilterValue, null));
					else if (FormConstants.OPERATOR_GREATER.equals(operator))
						op.addOperator(new OperatorGreaterThan(value, searchFilterValue, null));
					else if (FormConstants.OPERATOR_GREATER_EQUAL.equals(operator))
						op.addOperator(new OperatorGreaterThanEquals(value, searchFilterValue, null));
					else if (FormConstants.OPERATOR_LESS.equals(operator))
						op.addOperator(new OperatorLessThan(value, searchFilterValue, null));
					else if (FormConstants.OPERATOR_LESS_EQUAL.equals(operator))
						op.addOperator(new OperatorLessThanEquals(value, searchFilterValue, null));
			
					if (FormConstants.OPEARATOR_EARLIER.equals(operator)) {
						try {
							op.addOperator(new OperatorLessThan(value, parseDate(getFilterValue("searchFilter").toString()), null));
						} catch (ParseException ee) {
							Log.getLog(getClass()).error("Error parsing date - date:" + getFilterValue("searchFilter"), ee);
						}
					} else if (FormConstants.OPEARATOR_LATER.equals(operator)) {
						try {
							op.addOperator(new OperatorGreaterThan(value, parseDate(getFilterValue("searchFilter").toString()), null));
						} catch (ParseException ee) {
							Log.getLog(getClass()).error("Error parsing date - date:" + getFilterValue("searchFilter"), ee);
						}
					} else if (FormConstants.OPEARATOR_IS.equals(operator)) {
						try {
							op.addOperator(new OperatorEquals(value, parseDate(getFilterValue("searchFilter").toString()), null));
						} catch (ParseException ee) {
							Log.getLog(getClass()).error("Error parsing date - date:" + getFilterValue("searchFilter"), ee);
						}
					}
					properties.addProperty(op);
				}
				return properties;
			}
			}
			// end stuff i add
			
			if (value != null && !value.equals("")) {
				// get search filter value
				Object searchFilterValue = getFilterValue("searchFilter");
				Map infoMap = FormDao.getTextFieldInfoMap(formId);
				if (infoMap.containsKey(value)) {
					TextFieldField textFieldField = (TextFieldField) infoMap.get(value);
					if (textFieldField.getValidatorIsInteger() != null) {
                        try {
                            searchFilterValue = new Integer(searchFilterValue.toString());
                        }
						catch(NumberFormatException e1) {
                            searchFilterValue = null;
                        }
					} else if (textFieldField.getValidatorIsNumeric() != null) {
                        try {
                            searchFilterValue = new Float(searchFilterValue.toString());
                        }
						catch(NumberFormatException e2) {
                            searchFilterValue = null;
                        }
					}
				}

				OperatorParenthesis op = new OperatorParenthesis(DaoOperator.OPERATOR_AND);
				if (FormConstants.OPERATOR_LIKE.equals(operator))
					op.addOperator(new OperatorLike(value, getFilterValue("searchFilter"), null));
				else if (FormConstants.OPERATOR_EQUAL.equals(operator))
					op.addOperator(new OperatorEquals(value, searchFilterValue, null));
				else if (FormConstants.OPERATOR_GREATER.equals(operator))
					op.addOperator(new OperatorGreaterThan(value, searchFilterValue, null));
				else if (FormConstants.OPERATOR_GREATER_EQUAL.equals(operator))
					op.addOperator(new OperatorGreaterThanEquals(value, searchFilterValue, null));
				else if (FormConstants.OPERATOR_LESS.equals(operator))
					op.addOperator(new OperatorLessThan(value, searchFilterValue, null));
				else if (FormConstants.OPERATOR_LESS_EQUAL.equals(operator))
					op.addOperator(new OperatorLessThanEquals(value, searchFilterValue, null));
			
				if (FormConstants.OPEARATOR_EARLIER.equals(operator)) {
					try {
						op.addOperator(new OperatorLessThan(value, parseDate(getFilterValue("searchFilter").toString()), null));
					} catch (ParseException e) {
						Log.getLog(getClass()).error("Error parsing date - date:" + getFilterValue("searchFilter"), e);
					}
				} else if (FormConstants.OPEARATOR_LATER.equals(operator)) {
					try {
						op.addOperator(new OperatorGreaterThan(value, parseDate(getFilterValue("searchFilter").toString()), null));
					} catch (ParseException e) {
						Log.getLog(getClass()).error("Error parsing date - date:" + getFilterValue("searchFilter"), e);
					}
				} else if (FormConstants.OPEARATOR_IS.equals(operator)) {
					try {
						op.addOperator(new OperatorEquals(value, parseDate(getFilterValue("searchFilter").toString()), null));
					} catch (ParseException e) {
						Log.getLog(getClass()).error("Error parsing date - date:" + getFilterValue("searchFilter"), e);
					}
				}
				properties.addProperty(op);
			}
			return properties;
		}

        public String parseDate(String inDate) throws ParseException {
            if (inDate != null) {
                SimpleDateFormat inDateFormat = new SimpleDateFormat("dd-mm-yyyy");
                SimpleDateFormat outDateFormat = new SimpleDateFormat("yyyy-mm-dd");


                Date date = inDateFormat.parse(inDate);
                return outDateFormat.format(date);

            }

            return "";
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys) {
			Widget widget = evt.getWidget();			            
			Table table = (Table) widget;
			
			
			if(table.getSelectedAction()!= null && table.getSelectedAction().equalsIgnoreCase("delete")) {
				return deleteFormData(evt,action,selectedKeys);
			}
			else {
				exportCSV(evt);
			}
			return null;
    	}
    
    	protected Forward deleteFormData(Event evt, String action, String[] selectedKeys) {
			FormModule handler  = (FormModule) Application.getInstance().getModule(FormModule.class);
			for (int i = 0; i < selectedKeys.length; i++) {                
				try {                
			  		handler.deleteFormData(FormModule.FORM_PREFIX + getFormName(), selectedKeys[i], getFormId());
		  		}
                catch (FormDocumentException e) {
                    Log.getLog(getClass()).error(e.getMessage(),e);
                }
                catch (FormDaoException e) {
                    Log.getLog(getClass()).error(e.getMessage(),e);
                }
			}
			return super.processAction(evt,action,selectedKeys);
		}
		
		private void exportCSV(Event evt) {
			
			
			ExportCSVServlet export = new  ExportCSVServlet();
			try {			
				export.doGet(evt.getRequest(),evt.getResponse(),getFormId(),optionMap,
                             getSort(),isDesc(),generateDaoProperties());
			}
			catch (IOException e) {				
				Log.getLog(getClass()).error(e.toString());
			}
			catch (ParseException e) {
				Log.getLog(getClass()).error(e.toString());							
			}
            catch (FormDaoException e) {
                Log.getLog(getClass()).error(e.getMessage(),e);
            }
		}

    }


    

    
    public Map getSelected() {
        return selected;
    }

    public void setSelected(Map selected) {
        this.selected = selected;
    }

    public String getFormName() {        
        return this.formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

	/**
	 * @return
	 */
	public String getFormId() {
		return formId;
	}


	public void setFormId(String formId) {
		this.formId = formId;
	}



}

