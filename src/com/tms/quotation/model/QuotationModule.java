package com.tms.quotation.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.model.DefaultModule;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.crm.sales.model.Company;
import com.tms.crm.sales.model.CompanyModule;

public class QuotationModule extends DefaultModule {
	/* Quotation */

	public void deleteQuotation(String quotationId) {
		try {
			QuotationDao dao = (QuotationDao) getDao();
			dao.deleteQuotation(quotationId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error deleteQuotation", e);
			throw new RuntimeException("DAO Error");
		}
	}

	public QuotationDataObject getQuotation(String quotationId) {
		QuotationDataObject z = new QuotationDataObject();
		try {
			QuotationDao dao = (QuotationDao) getDao();
			Collection rows = dao.selectQuotation(quotationId);
			if (rows.size() > 0) {
				z = (QuotationDataObject) rows.iterator().next();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error getQuotation", e);
			throw new RuntimeException("DAO Error");
		}
		return z;
	}

	public void insertQuotation(QuotationDataObject z) {
      QuotationDao dao = (QuotationDao) getDao();
		if (z == null) {
			throw new RuntimeException("Parameter Error");
		}
		if (z.getQuotationId() == null) {
			z.setQuotationId(UuidGenerator.getInstance().getUuid());
		}
		if (z.getRecdate() == null) {
			z.setRecdate(new Date());
		}
		try {
			dao.insertQuotation(z);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error insertQuotation", e);
			throw new RuntimeException("DAO Error");
		}
	}

	public Collection selectQuotation(String quotationId) {
		try {
			QuotationDao dao = (QuotationDao) getDao();
			return dao.selectQuotation(quotationId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error selectQuotation", e);
			throw new RuntimeException("DAO Error");
		}
	}

	public Collection selectQuotation() {
		try {
			QuotationDao dao = (QuotationDao) getDao();
			return dao.selectQuotation();
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error selectQuotation", e);
			throw new RuntimeException("DAO Error");
		}
	}

    public void updateQuotationStatus(String status, String[] selected) {

      ArrayList arr = new ArrayList();
      QuotationDao dao = (QuotationDao) getDao();
      QuotationDataObject z;
      try {
        for( int i=0; i < selected.length; i++ ){
          arr.addAll(dao.selectQuotation(selected[i]));
        }

        for( Iterator i = arr.iterator(); i.hasNext(); ){
          z = (QuotationDataObject) i.next();
          z.setStatus( status );
          z.setCloseDate( new Date() );
          dao.updateQuotation(z);
        }
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error updateQuotation", e);
        throw new RuntimeException("DAO Error");
      }
    }
    
	public Map selectBoxQuotationstatus() {
		Collection rows = null;
		QuotationDataObject z = new QuotationDataObject();
		Map optionMap = new SequencedHashMap();
		try {
			QuotationDao dao = (QuotationDao) getDao();
			rows = dao.selectQuotationstatus();
			if (rows.size() > 0) {
				for (Iterator i = rows.iterator(); i.hasNext();) {
					z = (QuotationDataObject) i.next();
					optionMap.put(z.getStatus(),z.getStatus());
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error selectBoxQuotationstatus", e);
			throw new RuntimeException("DAO Error");
		}
		return optionMap;
	}
	
	public Map selectBoxTemplateId() {
		Collection rows = null;
		TemplateDataObject z = new TemplateDataObject();
		Map optionMap = new SequencedHashMap();
		try {
			QuotationDao dao = (QuotationDao) getDao();
			rows = dao.selectTemplateActive();
			if (rows.size() > 0) {
				for (Iterator i = rows.iterator(); i.hasNext();) {
					z = (TemplateDataObject) i.next();
					optionMap.put(z.getTemplateId(), z.getTemplateName());
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error selectBoxTemplateId", e);
			throw new RuntimeException("DAO Error");
		}
		return optionMap;
	}
	
	public void updateQuotation(QuotationDataObject z) {
		if (z == null) {
			throw new RuntimeException("Parameter Error");
		}
		try {
			z.setRecdate(new Date());
		} catch (Exception e) {
		}
		try {
			QuotationDao dao = (QuotationDao) getDao();
			dao.updateQuotation(z);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error updateQuotation", e);
			throw new RuntimeException("DAO Error");
		}
	}

	  /* QuotationItem */
	  public void deleteQuotationItem(String itemId) {
	    try {
	      QuotationDao dao = (QuotationDao)getDao();
	      dao.deleteQuotationItem(itemId);
	    } catch (DaoException e) {
	      Log.getLog(getClass()).error("Error deleteQuotationItem", e);
	      throw new RuntimeException("DAO Error");
	    }
	  }

      public void deleteQuotationIdItem(String quotationId) {
        QuotationDao dao = (QuotationDao) getDao();
        try {
          dao.deleteQuotationIdItem(quotationId);
        } catch (DaoException e) {
          Log.getLog(getClass()).error("Error deleteQuotationIdItem", e);
          throw new RuntimeException("DAO Error");
        }
      }
      
	  public QuotationItemDataObject getQuotationItem(String itemId) {
	    QuotationItemDataObject z = new QuotationItemDataObject();
	    try {
	      QuotationDao dao = (QuotationDao)getDao();
	      Collection rows = dao.selectQuotationItem(itemId);
	      if (rows.size() > 0) {z = (QuotationItemDataObject)rows.iterator().next();}
	    } catch (DaoException e) {
	      Log.getLog(getClass()).error("Error getQuotationItem", e);
	      throw new RuntimeException("DAO Error");
	    }
	    return z;
	  }

	  public void insertQuotationItem(QuotationItemDataObject z) {
	    if (z == null) {throw new RuntimeException("Parameter Error");}
	    if (z.getItemId() == null) {z.setItemId(UuidGenerator.getInstance().getUuid());}
	    try {
	      QuotationDao dao = (QuotationDao)getDao();
	      dao.insertQuotationItem(z);
	    } catch (DaoException e) {
	      Log.getLog(getClass()).error("Error insertQuotationItem", e);
	      throw new RuntimeException("DAO Error");
	    }
	  }

	  public Collection selectQuotationItem(String itemId) {
	    try {
	      QuotationDao dao = (QuotationDao)getDao();
	      return dao.selectQuotationItem(itemId);
	    } catch (DaoException e) {
	      Log.getLog(getClass()).error("Error selectQuotationItem", e);
	      throw new RuntimeException("DAO Error");
	    }
	  }

	  public Collection selectQuotationItem() {
	    try {
	      QuotationDao dao = (QuotationDao)getDao();
	      return dao.selectQuotationItem();
	    } catch (DaoException e) {
	      Log.getLog(getClass()).error("Error selectQuotationItem", e);
	      throw new RuntimeException("DAO Error");
	    }
	  }

	  public void updateQuotationItem(QuotationItemDataObject z) {
	    if (z == null) {throw new RuntimeException("Parameter Error");}
	    try {
	      QuotationDao dao = (QuotationDao)getDao();
	      dao.updateQuotationItem(z);
	    } catch (DaoException e) {
	      Log.getLog(getClass()).error("Error updateQuotationItem", e);
	      throw new RuntimeException("DAO Error");
	    }
	  }
	  
      public Collection selectQuotationItemQtnId(String quotationId) {
        QuotationDao dao = (QuotationDao) getDao();
        try {
          return dao.selectQuotationItemQtnId(quotationId);
        } catch (DaoException e) {
          Log.getLog(getClass()).error("Error selectQuotationItemQtnId", e);
          throw new RuntimeException("DAO Error");
        }
      }
	/* Template */

	public void deleteTemplate(String templateId) {
		try {
			QuotationDao dao = (QuotationDao) getDao();
			dao.deleteTemplate(templateId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error deleteTemplate", e);
			throw new RuntimeException("DAO Error");
		}
	}

	public TemplateDataObject getTemplate(String templateId) {
		TemplateDataObject z = new TemplateDataObject();
		try {
			QuotationDao dao = (QuotationDao) getDao();
			Collection rows = dao.selectTemplate(templateId);
			if (rows.size() > 0) {
				z = (TemplateDataObject) rows.iterator().next();
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error getTemplate", e);
			throw new RuntimeException("DAO Error");
		}
		return z;
	}

	public void insertTemplate(TemplateDataObject z) {
		if (z == null) {
			throw new RuntimeException("Parameter Error");
		}
		if (z.getTemplateId() == null) {
			z.setTemplateId(UuidGenerator.getInstance().getUuid());
		}
		if (z.getRecdate() == null) {
			z.setRecdate(new Date());
		}
		try {
			QuotationDao dao = (QuotationDao) getDao();
			dao.insertTemplate(z);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error insertTemplate", e);
			throw new RuntimeException("DAO Error");
		}
	}

	public Collection selectTemplate(String templateId) {
		try {
			QuotationDao dao = (QuotationDao) getDao();
			return dao.selectTemplate(templateId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error selectTemplate", e);
			throw new RuntimeException("DAO Error");
		}
	}

	public Collection selectTemplate() {
		try {
			QuotationDao dao = (QuotationDao) getDao();
			return dao.selectTemplate();
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error selectTemplate", e);
			throw new RuntimeException("DAO Error");
		}
	}

	public Map selectBoxTemplateactive() {
		Collection rows = null;
		TemplateDataObject z = new TemplateDataObject();
		Map optionMap = new SequencedHashMap();
		try {
			QuotationDao dao = (QuotationDao) getDao();
			rows = dao.selectTemplateactive();
			if (null != rows && rows.size() > 0) {
				for (Iterator i = rows.iterator(); i.hasNext();) {
					z = (TemplateDataObject) i.next();
					optionMap.put(z.getActive(), z.getActive());
				}
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error selectBoxTemplateactive", e);
			throw new RuntimeException("DAO Error");
		}
		return optionMap;
	}

	public void updateTemplate(TemplateDataObject z) {
		if (z == null) {
			throw new RuntimeException("Parameter Error");
		}
		try {
			z.setRecdate(new Date());
		} catch (Exception e) {
		}
		try {
			QuotationDao dao = (QuotationDao) getDao();
			dao.updateTemplate(z);
		} catch (DaoException e) {
			Log.getLog(getClass()).error("Error updateTemplate", e);
			throw new RuntimeException("DAO Error");
		}
	}

	  /* Customer */
	  public void deleteCustomer(String customerId) {
	    try {
	      QuotationDao dao = (QuotationDao)getDao();
	      dao.deleteCustomer(customerId);
	    } catch (DaoException e) {
	      Log.getLog(getClass()).error("Error deleteCustomer", e);
	      throw new RuntimeException("DAO Error");
	    }
	  }

	  public CustomerDataObject getCustomer(String customerId) {
	    CustomerDataObject z = new CustomerDataObject();
	    try {
	      QuotationDao dao = (QuotationDao)getDao();
	      Collection rows = dao.selectCustomer(customerId);
	      if (rows.size() > 0) {z = (CustomerDataObject)rows.iterator().next();}
	    } catch (DaoException e) {
	      Log.getLog(getClass()).error("Error getCustomer", e);
	      throw new RuntimeException("DAO Error");
	    }
	    return z;
	  }

	  public void insertCustomer(CustomerDataObject z) {
	    if (z == null) {throw new RuntimeException("Parameter Error");}
	    if (z.getCustomerId() == null) {z.setCustomerId(UuidGenerator.getInstance().getUuid());}
//	    if (z.getRecdate() == null) {z.setRecdate(new Date());}
	    try {
	      QuotationDao dao = (QuotationDao)getDao();
	      dao.insertCustomer(z);
	    } catch (DaoException e) {
	      Log.getLog(getClass()).error("Error insertCustomer", e);
	      throw new RuntimeException("DAO Error");
	    }
	  }

	  public Collection selectCustomer(String customerId) {
	    try {
	      QuotationDao dao = (QuotationDao)getDao();
	      return dao.selectCustomer(customerId);
	    } catch (DaoException e) {
	      Log.getLog(getClass()).error("Error selectCustomer", e);
	      throw new RuntimeException("DAO Error");
	    }
	  }

	  public Collection selectCustomer() {
	    try {
	      QuotationDao dao = (QuotationDao)getDao();
	      return dao.selectCustomer();
	    } catch (DaoException e) {
	      Log.getLog(getClass()).error("Error selectCustomer", e);
	      throw new RuntimeException("DAO Error");
	    }
	  }

      public HashMap customerMap() {
        CustomerDataObject z = new CustomerDataObject();
        Application application = Application.getInstance();
		CompanyModule module    = (CompanyModule) application.getModule(CompanyModule.class);
        HashMap hm = new HashMap();
        
        Collection col = module.listCompanies(null, null, false, 0, -1);
        for(Iterator i=col.iterator(); i.hasNext(); ){
        	Company com = (Company)i.next();
        	hm.put(com.getCompanyID(), com.getCompanyName());
        }
        
        return hm;
      }
      
	  public void updateCustomer(CustomerDataObject z) {
	    if (z == null) {throw new RuntimeException("Parameter Error");}
//	    try {z.setRecdate(new Date());} catch (Exception e) {}
	    try {
	      QuotationDao dao = (QuotationDao)getDao();
	      dao.updateCustomer(z);
	    } catch (DaoException e) {
	      Log.getLog(getClass()).error("Error updateCustomer", e);
	      throw new RuntimeException("DAO Error");
	    }
	  }
      
  /* QtnColumn */      
      public void insertQtnColumn(QtnColumnDataObject z) {
        QuotationDao dao = (QuotationDao) getDao();        
        try {
            dao.insertQtnColumn(z);
        } catch (DaoException e) {
            Log.getLog(getClass()).error("Error insertQtnColumn", e);
            throw new RuntimeException("DAO Error");
        }
      }
      
      public void deleteQtnColumn(String columnId) {
        QuotationDao dao = (QuotationDao) getDao();
        try {
          dao.deleteQtnColumn(columnId);
        } catch (DaoException e) {
          Log.getLog(getClass()).error("Error deleteQtnColumn", e);
          throw new RuntimeException("DAO Error");
        }
      }

      public QtnColumnDataObject getQtnColumn(String columnId) {
        QtnColumnDataObject z = new QtnColumnDataObject();
        try {
          QuotationDao dao = (QuotationDao)getDao();
          Collection rows = dao.selectQtnColumn(columnId);
          if (rows.size() > 0) {z = (QtnColumnDataObject)rows.iterator().next();}
        } catch (DaoException e) {
          Log.getLog(getClass()).error("Error getQtnColumn", e);
          throw new RuntimeException("DAO Error");
        }
        return z;
      }
      public void updateQtnColumn(QtnColumnDataObject z) {
        if (z == null) {throw new RuntimeException("Parameter Error");}
        try {
          QuotationDao dao = (QuotationDao)getDao();
          dao.updateQtnColumn(z);
        } catch (DaoException e) {
          Log.getLog(getClass()).error("Error updateQtnColumn", e);
          throw new RuntimeException("DAO Error");
        }
      }
      
      /* Select the columns attached to a specific tableId */
      public Collection selectQtnColumn(String columnId) {
        QuotationDao dao = (QuotationDao) getDao();
        try {
          return dao.selectQtnColumn(columnId);
        } catch (DaoException e) {
          Log.getLog(getClass()).error("Error selectQtnColumn", e);
          throw new RuntimeException("DAO Error");
        }
      }
      
      public Collection selectQtnTableColumn(String tableId) {
        QuotationDao dao = (QuotationDao) getDao();
        
        try {
          return dao.selectQtnTableColumn(tableId);
        } catch (DaoException e) {
          Log.getLog(getClass()).error("Error selectQtnColumn", e);
          throw new RuntimeException("DAO Error");
        }
      }
      
  /* QtnTable */
      public void insertQtnTable(QtnTableDataObject z) {
        QuotationDao dao = (QuotationDao) getDao();
        try {
          dao.insertQtnTable(z);
        } catch (DaoException e) {
          Log.getLog(getClass()).error("Error insertQtnTable", e);
          throw new RuntimeException("DAO Error");
        }
      }
      
      public void deleteQtnTable(String tableId) {
        QuotationDao dao = (QuotationDao) getDao();
        
        try {
          dao.deleteQtnTable(tableId);
        } catch (DaoException e) {
          Log.getLog(getClass()).error("Error insertQtnTable", e);
          throw new RuntimeException("DAO Error");
        }
      }
      
      public Collection selectQtnTable(String tableId) {
        QuotationDao dao = (QuotationDao) getDao();
        
        try {
          return dao.selectQtnTable(tableId);
        } catch (DaoException e) {
          Log.getLog(getClass()).error("Error selectQtnTable", e);
          throw new RuntimeException("DAO Error");
        }
      }
/*      
      public QtnTableDataObject loadQtnTable(String tableId) {
//        Collection cols;
        QtnTableDataObject z;
        z = (QtnTableDataObject) selectQtnTable(tableId).iterator().next();

        cols = selectQtnTableColumn(tableId);         
        z.setColumnList(cols);
        z.setNumOfColumns(cols.size());
        
        return z;
      }
*/      
      public QtnTableDataObject getQtnTable(String tableId) {
        QtnTableDataObject z = new QtnTableDataObject();
        try {
          QuotationDao dao = (QuotationDao)getDao();
          Collection rows = dao.selectQtnTable(tableId);
          if (rows.size() > 0) {z = (QtnTableDataObject)rows.iterator().next();}
        } catch (DaoException e) {
          Log.getLog(getClass()).error("Error getQtnTable", e);
          throw new RuntimeException("DAO Error");
        }
        return z;
      }
      
      public void updateQtnTable(QtnTableDataObject z) {
        if (z == null) {throw new RuntimeException("Parameter Error");}
//        try {z.setRecdate(new Date());} catch (Exception e) {}
        try {
          QuotationDao dao = (QuotationDao)getDao();
          dao.updateQtnTable(z);
        } catch (DaoException e) {
          Log.getLog(getClass()).error("Error updateQtnTable", e);
          throw new RuntimeException("DAO Error");
        }
      }      

    public Map selectBoxQtnTable() {
        Collection rows = null;
        QtnTableDataObject z = new QtnTableDataObject();
        Map optionMap = new SequencedHashMap();
        try {
            QuotationDao dao = (QuotationDao) getDao();
            rows = dao.selectQtnTable();
            if (null != rows && rows.size() > 0) {
                for (Iterator i = rows.iterator(); i.hasNext();) {
                    z = (QtnTableDataObject) i.next();
                    optionMap.put(z.getTableId(),z.getTableDescription());
                }
            }
        } catch (DaoException e) {
            Log.getLog(getClass()).error("Error selectBoxQtnTable", e);
            throw new RuntimeException("DAO Error");
        }
        return optionMap;
    }

/* QtnTemplateText */
    public void insertQtnTemplateText(QtnTemplateTextDataObject z) {
      QuotationDao dao = (QuotationDao)getDao();
      if(z.getTemplateId()==null) {
        z.setTemplateId(UuidGenerator.getInstance().getUuid());
      }
      try {
        dao.insertQtnTemplateText(z);
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error insertQtnTemplateText", e);
        throw new RuntimeException("DAO Error");
      }
    }
    
    public void deleteQtnTemplateText(String templateId) {
      QuotationDao dao = (QuotationDao)getDao();
      try {
        dao.deleteQtnTemplateText(templateId);
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error deleteQtnTemplateText", e);
        throw new RuntimeException("DAO Error");
      }
    }
    
    public void updateQtnTemplateText(QtnTemplateTextDataObject z) {
      QuotationDao dao = (QuotationDao)getDao();
      try {
        dao.updateQtnTemplateText(z);
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error updateQtnTemplateText", e);
        throw new RuntimeException("DAO Error");
      }
    }
    
    public Collection selectQtnTemplateText() {
      QuotationDao dao = (QuotationDao)getDao();
      try {
        return dao.selectQtnTemplateText();
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error selectQtnTemplateText", e);
        throw new RuntimeException("DAO Error");
      }
    }
    
    public Collection selectQtnTemplateText(String templateId) {
      QuotationDao dao = (QuotationDao)getDao();
      try {
        return dao.selectQtnTemplateText(templateId);
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error selectQtnTemplateText", e);
        throw new RuntimeException("DAO Error");
      }
    }

    public QtnTemplateTextDataObject getQtnTemplateText(String id) {
      QtnTemplateTextDataObject z = new QtnTemplateTextDataObject();
      QuotationDao dao = (QuotationDao)getDao();
      Collection c;
      try {
        c = dao.selectQtnTemplateText(id);
        if( c.size() > 0) {
          z = (QtnTemplateTextDataObject)c.iterator().next();
        }
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error getQtnTemplateText", e);
        throw new RuntimeException("DAO Error");
      }
      return z;
    }

    public void insertTemplateMap( TemplateMapDataObject tm ) {
      QuotationDao dao = (QuotationDao)getDao();
      if( tm.getTemplateId() == null ) {
        tm.setTemplateId(UuidGenerator.getInstance().getUuid());
      }
      try {
        dao.insertTemplateMap(tm);
      } catch (DaoException e) {
          Log.getLog(getClass()).error("Error insertTemplateMap", e);
          throw new RuntimeException("DAO Error");
      }
    }

    public TemplateMapDataObject getTemplateMap(String templateId, String textId) {
      QuotationDao dao = (QuotationDao)getDao();

      try {
        Collection c = dao.selectTemplateMap(templateId, textId);
        return (TemplateMapDataObject)c.iterator().next();        
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error getTemplateMap", e);
        throw new RuntimeException("DAO Error");
      }

      
    }
    public void deleteTemplateMap( String templateId, String templateTextId ) {
      QuotationDao dao = (QuotationDao)getDao();
      try {
        dao.deleteTemplateMap(templateId, templateTextId);
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error deleteTemplateMap", e);
        throw new RuntimeException("DAO Error");
      }
    }
    
    public void deleteTemplateMap( String templateId ) {
      QuotationDao dao = (QuotationDao)getDao();
      try {
        dao.deleteTemplateMap(templateId);
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error deleteTemplateMap", e);
        throw new RuntimeException("DAO Error");
      }
    }
    
    public Map csbGetSelectedText (String templateId, boolean select ) {
      QuotationDao dao = (QuotationDao)getDao();
      try {
        Collection c = dao.selectTemplateMap(templateId, select);
        if( c != null && c.size() > 0 ) {
          Map m = new SequencedHashMap();
          for (Iterator i = c.iterator(); i.hasNext(); ) {
            QtnTemplateTextDataObject z = new QtnTemplateTextDataObject();
            z = (QtnTemplateTextDataObject)i.next();
            m.put(z.getTemplateId(), z.getTemplateDescription() );
          }
          return m;
        }
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error selectMapTemplateText", e);
        throw new RuntimeException("DAO Error");
      }
      return null;
    }
    
    public Collection selectTemplateIdText( String templateId ) {
      QuotationDao dao = (QuotationDao) getDao();
      try {
        return dao.selectTemplateMap(templateId, true);
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error selectTemplateIdText", e);
        throw new RuntimeException("DAO Error");
      }
    }
    
    public void updateTemplateMap(TemplateMapDataObject z) {
      QuotationDao dao=(QuotationDao)getDao();
      try {
        dao.updateTemplateMap(z);
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error updateTemplateMap", e);
        throw new RuntimeException("DAO Error");
      }
    }
/* QtnContent */
    public void insertQtnContent( QtnContentDataObject z ) {
      QuotationDao dao = (QuotationDao)getDao();
      try {
        dao.insertQtnContent(z);
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error insertQtnContent", e);
        throw new RuntimeException("DAO Error");
      }
    }
    
    public Collection selectQtnContent( String quotationId ) {
      QuotationDao dao = (QuotationDao)getDao();
      try {
        return dao.selectQtnContent(quotationId);
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error selectQtnContent", e);
        throw new RuntimeException("DAO Error");
      }
    }
    
    public void deleteQtnContent( String quotationId ) {
      QuotationDao dao = (QuotationDao)getDao();
      try {
        dao.deleteQtnContent(quotationId);
      } catch (DaoException e) {
        Log.getLog(getClass()).error("Error deleteQtnContent", e);
        throw new RuntimeException("DAO Error");
      }
      
    }
}