package com.tms.cms.syndication.model;

import kacang.model.DataSourceDao;
import kacang.model.DaoException;

import java.util.Collection;

public class SyndicationDao extends DataSourceDao {
    public void init() throws DaoException {
        super.update("CREATE TABLE syn_object" +
                     "(id varchar(255) NOT NULL default ''," +
                     "title varchar(255) NOT NULL default ''," +
                     "link varchar(255) NOT NULL default ''," +
                     "refreshRate int NOT NULL default 5," +
                     "userId varchar(255) NOT NULL default ''," +
                     "PRIMARY KEY(id))",null);

        super.update("CREATE TABLE syn_feed_category" +
                     "(categoryId varchar(255) NOT NULL default ''," +
                     "categoryName varchar(255) NOT NULL default ''," +
                     "PRIMARY KEY(categoryId))",null);

        super.update("INSERT INTO syn_feed_category " +
                     "(categoryId,categoryName) VALUES ('1','News')", null);

        super.update("INSERT INTO syn_feed_category " +
                     "(categoryId,categoryName) VALUES ('2','IT/Technology')", null);

        super.update("CREATE TABLE syn_feed_predefined " +
                     "(id varchar(255) NOT NULL default ''," +
                     "name varchar(255) NOT NULL default ''," +
                     "link varchar(255) NOT NULL default ''," +
                     "refreshRate int NOT NULL default 5," +
                     "categoryId varchar(255) NOT NULL default ''," +
                     "PRIMARY KEY(id))",null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId,refreshRate) VALUES ('1','Salon','http://www.salon.com/feed/RDF/salon_use.rdf','1',5)", null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId,refreshRate) VALUES ('2','CNet News.com','http://reviews.cnet.com/4924-5_7-0.xml?orderBy=-7rvDte&7rType=70-80&maxhits=10&9lwPrc=0-','1',5)", null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId,refreshRate) VALUES ('3','BBC News','http://news.bbc.co.uk/rss/newsonline_world_edition/front_page/rss091.xml','1',5)", null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId,refreshRate) VALUES ('5','ITN News','http://www.itn.co.uk/itn.rdf','1',5)", null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId,refreshRate) VALUES ('7','Slashdot','http://slashdot.org/index.rss','2',5)", null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId,refreshRate) VALUES ('8','The Register','http://theregister.co.uk/feeds/latest.rdf','2',5)", null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId,refreshRate) VALUES ('9','Internet News','http://www.internetnews.com/icom_includes/feeds/inews/xml_front-10.xml','2',5)", null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId,refreshRate) VALUES ('10','MacSlash','http://macslash.org/rss/macslash.xml','2',5)", null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId,refreshRate) VALUES ('11','LinuxToday','http://linuxtoday.com/backend/biglt.rss','2',5)", null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId,refreshRate) VALUES ('12','DaemonNews','http://bsdnews.com/ddn.rdf.php3','2',5)", null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId,refreshRate) VALUES ('13','Wired','http://www.wired.com/news/feeds/rss2/0,2610,,00.xml','2',5)", null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId,refreshRate) VALUES ('14','Kuro5hin','http://www.kuro5hin.org/backend.rdf','2',5)", null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId,refreshRate) VALUES ('15','New Scientist','http://www.newscientist.com/syndication/news.rdf','2',5)", null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId,refreshRate) VALUES ('16','Computer World','http://www.computerworld.com/news/xml/10/0,5009,,00.xml','2',5)", null);

        super.update("INSERT INTO syn_feed_predefined " +
                     "(id,name,link,categoryId) VALUES ('17','Lockergnome Updates','http://www.lockergnome.com/rss/news.php','2')", null);




    }

    public void insertSynObject(SynObject object) throws SyndicationDaoException {
        String sql;

        sql = "INSERT INTO syn_object " +
              "(id,title,link,refreshRate,userId) " +
              "VALUES " +
              "(#id#,#title#,#link#,#refreshRate#,#userId#)";

        try {
            super.update(sql,object);
        }
        catch (DaoException e) {
            throw new SyndicationDaoException("Error inserting into syn_object table",e);
        }

    }

    public void deleteSynObject(String id) throws SyndicationDaoException {
        String sql;

        sql = "DELETE FROM syn_object WHERE id = '" + id + "'";
        try {
            super.update(sql,null);
        }
        catch (DaoException e) {
            throw new SyndicationDaoException("Error deleting from syn_object table",e);
        }

    }

    public void deleteSynObject(String link, String userId) throws SyndicationDaoException {
        String sql;

        sql = "DELETE FROM syn_object WHERE link = '" + link + "' AND userId = '" + userId + "'";
        try {
            super.update(sql,null);
        }
        catch (DaoException e) {
            throw new SyndicationDaoException("Error deleting from syn_object table - userId:" + userId + " link:" + link,e);
        }

    }

    public Collection selectSynObjectByUserId(String userId) throws SyndicationDaoException {
        String sql;
        sql = "SELECT id,title,link,refreshRate,userId " +
              "FROM syn_object " +
              "WHERE userId = '" + userId + "' ORDER BY title";
        try {
            return super.select(sql,SynObject.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new SyndicationDaoException("Error querying from syn_object table- userId:" + userId,e);
        }

    }

    public Collection selectSynObject(String id) throws SyndicationDaoException {
        String sql;
        sql = "SELECT id,title,link,refreshRate,userId " +
              "FROM syn_object " +
              "WHERE id = '" + id + "'";
        try {
            return super.select(sql,SynObject.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new SyndicationDaoException("Error querying from syn_object table- id:" + id,e);
        }

    }

    public Collection selectSynObject(String link,String userId) throws SyndicationDaoException {
        String sql;
        sql = "SELECT id,title,link,refreshRate,userId " +
              "FROM syn_object " +
              "WHERE link = '" + link + "' AND userId = '" + userId + "'";
        try {
            return super.select(sql,SynObject.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new SyndicationDaoException("Error querying from syn_object table- userId:" + userId + " link:" + link,e);
        }

    }

    public void updateSynObject(SynObject object,String id) throws SyndicationDaoException {
        String sql;

        sql = "UPDATE syn_object " +
              "SET id=#id#,title=#title#,link=#link#,refreshRate=#refreshRate#,userId=#userId# " +
              "WHERE id = '" + id + "'";


        try {
            super.update(sql,object);
        }
        catch (DaoException e) {
            throw new SyndicationDaoException("Error updating syn_object table",e);
        }

    }



    public Collection selectSynFeedPredefined() throws SyndicationDaoException {
        String sql;

        sql = "SELECT SFP.id, SFP.name, SFP.link, SFP.categoryId, SFP.refreshRate, SFC.categoryName " +
              "FROM syn_feed_predefined SFP, syn_feed_category SFC " +
              "WHERE SFP.categoryId = SFC.categoryID " +
              "ORDER BY SFC.categoryName, SFP.name";

        try {
            return super.select(sql,SynFeedPredefined.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new SyndicationDaoException("Error selecting from syn_feed_predefined table",e);
        }
    }

    public Collection selectSynFeedPredefined(String id) throws SyndicationDaoException {
        String sql;

        sql = "SELECT id, name, link, categoryId, refreshRate " +
              "FROM syn_feed_predefined " +
              "WHERE id = '" +  id+ "'";

        try {
            return super.select(sql,SynFeedPredefined.class,null,0,-1);
        }
        catch (DaoException e) {
            throw new SyndicationDaoException("Error selecting from syn_feed_predefined table - id:"+id,e);
        }
    }





}
