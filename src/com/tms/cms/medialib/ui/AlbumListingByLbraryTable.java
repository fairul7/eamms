/*
 * AlbumListingByLbraryTable
 * Date Created: Jul 6, 2005
 * Author: Tien Soon, Law
 * Description: TODO Change the class descriptions
 * Company: TMS Berhad
 */
package com.tms.cms.medialib.ui;

import java.util.Collection;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;

import com.tms.cms.medialib.model.AlbumModule;


public class AlbumListingByLbraryTable extends Table {
    private String libraryId = "";
    
    public void init() {
        setWidth("100%");
        setModel(new AlbumListingByLbraryTableModel());
    }
    
    public void onRequest(Event evt) {
        init();
    }
    
    public class AlbumListingByLbraryTableModel extends TableModel {
        public AlbumListingByLbraryTableModel() {
            Application app = Application.getInstance();
            
            TableColumn nameCol = new TableColumn("name", app.getMessage("medialib.label.name", "Name"));
            nameCol.setUrlParam("id");
            addColumn(nameCol);
            
            addColumn(new TableColumn("description", app.getMessage("medialib.label.description", "Description")));
            
            TableColumn eventDateCol = new TableColumn("eventDate", app.getMessage("medialib.label.eventDate", "Event Date"));
            TableFormat dateFormat = new TableDateFormat(Application.getInstance().getProperty("globalDatetimeLong"));
            eventDateCol.setFormat(dateFormat);
            addColumn(eventDateCol);

            // Text field for value of filter
            TableFilter name = new TableFilter("name");
            addFilter(name);
        }

        public Collection getTableRows() {
            String name = (String) getFilterValue("name");

            Application application = Application.getInstance();
            AlbumModule module = (AlbumModule) application.getModule(AlbumModule.class);
            
            return module.findAlbum(name, false, "name", getSort(), libraryId, isDesc(), getStart(), getRows());
        }

        public int getTotalRowCount() {
            String name = (String) getFilterValue("name");
            
            Application application = Application.getInstance();
            AlbumModule module = (AlbumModule) application.getModule(AlbumModule.class);
            return module.countAlbum(name, false, "name", libraryId);
        }

        public String getTableRowKey() {
            return "id";
        }
    }
    
    public String getLibraryId() {
        return libraryId;
    }
    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }
}
