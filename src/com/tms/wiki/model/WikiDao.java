package com.tms.wiki.model;

import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DataSourceDao;
import kacang.util.Log;
import kacang.util.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import com.tms.cms.core.model.ContentObject;

/**
 * Created by IntelliJ IDEA.
 * User: hima
 * Date: Dec 14, 2006
 * Time: 1:56:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class WikiDao extends DataSourceDao {
	
	String wikicols = "id, id, id";
	
    Log log = Log.getLog(getClass());

    public void init() throws DaoException {
        super.init();
        try {
            super.update("CREATE TABLE IF NOT EXISTS wiki_article (articleId varchar(100) NOT NULL DEFAULT '' , " +
            		" revisionId varchar(100) NOT NULL DEFAULT '' , " +
            		" story text , hostAddress varchar(15) , " +
            		" createdBy varchar(100) , " +
            		" createdOn datetime , " +
            		" category varchar(100) , " +
            		" title varchar(150) , " +
            		" tags varchar(200) , " +
            		" articleStatus char(1) DEFAULT '0' , " +
            		" lockArticle varchar(1) , PRIMARY KEY (articleId))", null);
            super.update("CREATE TABLE IF NOT EXISTS wiki_article_revision (revisionId varchar(100) NOT NULL DEFAULT '' , " +
            		" revision int(11) NOT NULL , " +
            		" editSummary varchar(100) , " +
            		" story text NOT NULL DEFAULT '' , " +
            		" modifiedBy varchar(100) NOT NULL DEFAULT '' , " +
            		" hostAddress varchar(100) , " +
            		" articleId varchar(50) , " +
            		" modifiedOn datetime , PRIMARY KEY (revisionId))", null);
            super.update("CREATE TABLE IF NOT EXISTS wiki_article_tags (articleId varchar(100) NOT NULL DEFAULT '' , " +
            		" tagId varchar(100) , userId varchar(50) , PRIMARY KEY (articleId))", null);
            super.update("CREATE TABLE IF NOT EXISTS wiki_categories (categoryId varchar(100) NOT NULL DEFAULT '' , " +
            		" category varchar(100) , createdOn datetime , " +
            		" parentId varchar(100) , currentModule varchar(150) , PRIMARY KEY (categoryId))", null);
            super.update("CREATE TABLE IF NOT EXISTS wiki_comments (commentId varchar(100) NOT NULL DEFAULT '' , " +
            		" articleId varchar(100) NOT NULL DEFAULT '' , " +
            		" comments text NOT NULL DEFAULT '' ,  " +
            		" parentId varchar(100) , " +
            		" hostAddress varchar(20) , " +
            		" name varchar(150) NOT NULL DEFAULT '' , email varchar(200) NOT NULL DEFAULT '')", null);
            super.update("CREATE TABLE IF NOT EXISTS wiki_editor (title varchar(150) DEFAULT '0' , " +
            		" story varchar(250) , category varchar(50) DEFAULT '0' , " +
            		" tags varchar(50) DEFAULT '0' , articleId varchar(50) DEFAULT '0')", null);
            super.update("CREATE TABLE IF NOT EXISTS wiki_module (moduleId varchar(100) NOT NULL DEFAULT '0' , " +
            		" moduleName varchar(100) DEFAULT '0' , " +
            		" createdOn datetime , PRIMARY KEY (moduleId))", null);
            super.update("CREATE TABLE IF NOT EXISTS wiki_subcategories (subCategoryId varchar(100) NOT NULL DEFAULT '0' , " +
            		" subCategory varchar(100) , createdOn datetime , " +
            		" categoryId varchar(100) , PRIMARY KEY (subCategoryId))", null);
            super.update("CREATE TABLE IF NOT EXISTS wiki_tags (tagId varchar(100) NOT NULL DEFAULT '' , " +
            		" tag varchar(50) NOT NULL DEFAULT '')", null);

        } catch (Exception e) {
        }       
        
    }    

    public void addArticle(WikiArticle article) throws DaoException {
        super.update("INSERT into wiki_article (articleId,  revisionId, story, " +
        		"hostAddress, createdBy, createdOn, category, articleStatus, title, tags) " +
                "VALUES (#articleId#, #revisionId#, #story#, #hostAddress#, #createdBy#, " +
                "#createdOn#, #category#, #articleStatus#, #title#, #tags#)", article);
        
        String sql_revision ="INSERT into wiki_article_revision " +
		"(revisionId, revision, editSummary, story, modifiedBy, modifiedOn, articleId) " +
		"VALUES (#revisionId#, #revision#, #editSummary#, #story#, #modifiedBy#, #modifiedOn#, #articleId#)";
       super.update(sql_revision, article);
    }
    
    public void editArticle(WikiArticle article) throws DaoException {
    	String sql = "UPDATE wiki_article SET story=#story#, category=#category#, revisionId=#revisionId#, " +
    			"tags=#tags# WHERE articleId = #articleId#";	
    	super.update(sql,article);
    	
    	String sql_revision ="INSERT into wiki_article_revision " +
    			"(revisionId, revision, editSummary, story, modifiedBy, modifiedOn, articleId, hostAddress) " +
    			"VALUES (#revisionId#, #revision#, #editSummary#, #story#, #modifiedBy#, #modifiedOn#,#articleId#, #hostAddress#)";
    	super.update(sql_revision, article);    	
    }

    public Collection selectArticle(String articleId) throws DaoException {
        String sql = "SELECT a.articleId AS articleId, title, a.story AS story, revision, c.categoryId AS categoryId, c.category AS category, " +
        		" tags, modifiedBy, modifiedOn, a.createdOn AS createdOn, createdBy, articleStatus, r.editSummary AS editSummary " +
        		" FROM wiki_article a, wiki_article_revision r,wiki_categories c " +
        		" WHERE a.revisionId=r.revisionId  AND a.category = c.categoryId AND a.articleId= ?";
        Collection list = super.select(sql, WikiArticle.class, new Object[]{articleId}, 0, -1);
        return list;
    }  

    //to add a category
    public void addCategory(WikiCategory category) throws DaoException {
        super.update("INSERT into wiki_categories (categoryId, category, parentId, currentModule, createdOn) " +
                "VALUES (#categoryId#, #category#, #parentId#, #moduleId#, #createdOn#)", category);
    }

    public Collection selectCategory(String moduleId, String parentId, String searchBy, String sort, boolean desc, int start, int rows) throws DaoException {
        String sql = "SELECT categoryId, category, createdOn, parentId, currentModule FROM wiki_categories WHERE category LIKE ? ";
        String condition = (searchBy != null) ? "%" + searchBy + "%" : "%";
        String orderBy = (sort != null) ? sort : "category";
        if(moduleId!=null){
            sql += " AND currentModule = '"+moduleId+"' ";
        }
        if (parentId != null) {
            sql = sql + " AND parentId= '" + parentId + "'";
        } else {
            sql = sql + " and parentId IS NULL ";
        }
        if (desc)
            orderBy += " DESC";
        Object args[] = {condition};
        sql += "ORDER BY " + orderBy;

        return super.select(sql, WikiCategory.class, args, start, rows);
    }
    
    public WikiCategory selectCategoryById(String categoryId) throws DaoException, DataObjectNotFoundException{
    	String sql = "SELECT categoryId, category, createdOn, parentId, currentModule AS moduleId FROM wiki_categories WHERE categoryId = ?";
    	Collection col =  super.select(sql,WikiCategory.class, new Object[]{categoryId}, 0, -1);
    	if(col.size() == 0) {
            throw new DataObjectNotFoundException(categoryId);
        }
        return (WikiCategory)col.iterator().next();
    }

    public int selectCategoryCount(String moduleId, String parentId, String searchBy) throws DaoException {
        String condition = (searchBy != null) ? "%" + searchBy + "%" : "%";
        Object[] args = {condition};
        String sql = "SELECT count(*) AS total FROM wiki_categories WHERE category LIKE ?";
        if(moduleId!=null){
            sql += " AND currentModule = '"+moduleId+"' ";
        }
        if (parentId != null) {
            sql = sql + " AND parentId= '" + parentId + "'";
        } else {
            sql = sql + " and parentId IS NULL ";
        }
        Collection list = super.select(sql, HashMap.class, args, 0, -1);
        HashMap map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }

    public void deleteCategory(String categoryId) throws DaoException {
    	Transaction tx = null;
    	try {
    		tx = getTransaction();
			tx.begin();
			//mysql 5.x now supports subqueries
			String sql = "delete from wiki_article_revision where articleId =(select articleId from wiki_article where category='"+categoryId+"')";			
			tx.update(sql, null);
			sql = "DELETE FROM wiki_article WHERE category='"+categoryId+"' ";
			tx.update(sql, null);
			sql = "DELETE FROM wiki_categories WHERE categoryId = '" + categoryId + "'";
			tx.update(sql, null);
			tx.commit();
		} catch (SQLException e) {
			Log.getLog(getClass()).error("error"+ e.toString());
		} finally {
			if (tx != null) {
                tx.rollback();
            }
		}
    	
    }

    public Collection selectSubCategories(String categoryId, String search, String sort, boolean desc, int start, int rows) throws DaoException {
        String sql = "SELECT subCategoryId, subCategory, s.createdOn AS createdOn, s.categoryId AS categoryId " +
                "FROM wiki_subcategories s, wiki_categories c WHERE s.categoryId = c.categoryId  AND s.categoryId = ?";
        if (search != null && !search.equals("")) {
            sql += " AND subCategory LIKE '%" + search + "%'";
        }
        String orderBy = (sort != null) ? sort : "subCategory";
        if (desc)
            orderBy += " DESC";

        sql += "ORDER BY " + orderBy;

        return super.select(sql, WikiCategory.class, new Object[]{categoryId}, start, rows);
    }

    public int selectSubCategoryCount(String categoryId, String search) throws DaoException {

        String sql = "SELECT count(*) AS total FROM wiki_subcategories s, wiki_categories c WHERE s.categoryId = c.categoryId AND s.categoryId = ?";
        if (search != null && !search.equals("")) {
            sql += " AND subCategory LIKE '%" + search + "%'";
        }
        Collection list = super.select(sql, HashMap.class, new Object[]{categoryId}, 0, -1);
        HashMap map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }

    public void deleteSubCategory(String subCategoryId) throws DaoException {
        String sql = "DELETE FROM wiki_subCategories WHERE subCategoryId = '" + subCategoryId + "'";
        super.update(sql, null);
    }

    public void addSubCategory(WikiCategory subCategory) throws DaoException {
        super.update("INSERT into wiki_subcategories (categoryId, subCategory, subCategoryId, createdOn) " +
                "VALUES (#categoryId#, #subCategory#, #subCategoryId#, #createdOn#)", subCategory);
    }

    public Collection getCategory(String categoryId) throws DaoException, DataObjectNotFoundException {
        Object[] args = {categoryId};
        String sql = "SELECT category, createdOn FROM wiki_categories WHERE categoryId = ?";
        return super.select(sql, WikiCategory.class, args, 0, 1);

    }

    public Collection selectModule(String searchBy, String sort, boolean desc, int start, int rows) throws DaoException {
        String sql = "SELECT moduleId, moduleName, createdOn FROM wiki_module WHERE moduleName LIKE ? ";
        String condition = (searchBy != null) ? "%" + searchBy + "%" : "%";
        String orderBy = (sort != null) ? sort : "moduleName";
        if (desc)
            orderBy += " DESC";
        Object args[] = {condition};
        sql += "ORDER BY " + orderBy;

        return super.select(sql, WikiCategory.class, args, start, rows);
    }

    public int selectModuleCount(String searchBy) throws DaoException {
        String condition = (searchBy != null) ? "%" + searchBy + "%" : "%";
        Object[] args = {condition};
        String sql = "SELECT count(*) AS total FROM wiki_module WHERE moduleName LIKE ?";
        Collection list = super.select(sql, HashMap.class, args, 0, -1);
        HashMap map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }
    
    public int selectCategoriesCountByModule(String moduleId) throws DaoException{
    	 String sql = "SELECT count(*) AS total FROM wiki_categories WHERE currentModule= ?";
    	 Collection list = super.select(sql, HashMap.class, new Object[]{moduleId}, 0, -1);
    	 HashMap map = (HashMap) list.iterator().next();
         return Integer.parseInt(map.get("total").toString());
    } 
    
    public void deleteModule(String moduleId) throws DaoException {
        String sql = "DELETE FROM wiki_module WHERE moduleId = '" + moduleId + "'";
        super.update(sql, null);
    }
    
    public int selectCategoriesCountByCategory(String categoryId) throws DaoException{
   	 String sql = "SELECT count(*) AS total FROM wiki_categories WHERE parentId = ?";
   	 Collection list = super.select(sql, HashMap.class, new Object[]{categoryId}, 0, -1);
   	 HashMap map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("total").toString());
   }

     public void addModule(WikiCategory category) throws DaoException {
        super.update("INSERT into wiki_module (moduleId, moduleName, createdOn) " +
                "VALUES (#moduleId#, #moduleName#, #createdOn#)", category);
    }
     
    public Collection getFolderList(String parentId) throws DaoException {
         StringBuffer sql = new StringBuffer("SELECT c2.categoryId AS categoryId, c2.category, c2.parentId FROM wiki_categories c1 , wiki_categories c2 " +
         		"WHERE c1.currentModule= c2.currentmodule ");
         
         if (parentId != null) {
             sql.append("AND c1.categoryId='"+parentId+"' ");
         }
         sql.append("ORDER BY category");
         return super.select(sql.toString(), WikiCategory.class, null, 0, -1);
     }
    
    public void leaveYourComments(WikiArticle article) throws DaoException {
        super.update("INSERT into wiki_comments (commentId, articleId, comments, name, email) " +
                "VALUES (#commentId#, #articleId#, #comments#, #name#, #email#)", article);    
               
    }

	public Collection selectAllComents(String articleId) throws DaoException {
		StringBuffer sql = new StringBuffer("SELECT commentId, articleId, comments, name, email " +
				"FROM wiki_comments WHERE articleId=?");
		return super.select(sql.toString(), WikiArticle.class, new Object[]{articleId}, 0, -1);	
	}
	
	public Collection selectAllArticles() throws DaoException {
		StringBuffer sql = new StringBuffer("SELECT articleId, story, a.createdOn AS createdOn, " +
				"c.category as category , c.categoryId AS categoryId, title, tags " +
				"FROM wiki_article a, wiki_categories c WHERE a.category = c.categoryId ORDER BY createdOn DESC");
		return super.select(sql.toString(), WikiArticle.class,null, 0, 5);		
	}
	
	/*public Collection selectMyArticles(String userId) throws DaoException {
		StringBuffer sql = new StringBuffer("SELECT articleId, story, a.createdOn AS createdOn, createdBy, " +
				"c.category as category , c.categoryId AS categoryId, title, tags " +
				"FROM wiki_article a, wiki_categories c WHERE createdBy=?  AND a.category = c.categoryId ORDER BY createdOn DESC");
		return super.select(sql.toString(), WikiArticle.class, new Object[]{userId}, 0, -1);		
	}*/
	
	public Collection selectMyArticles(String userId, int start, int rows) throws DaoException {
		StringBuffer sql = new StringBuffer("SELECT articleId, story, a.createdOn AS createdOn, createdBy, " +
				"c.category as category , c.categoryId AS categoryId, title, tags " +
				"FROM wiki_article a, wiki_categories c WHERE createdBy=?  AND a.category = c.categoryId ORDER BY a.createdOn DESC");
		return super.select(sql.toString(), WikiArticle.class, new Object[]{userId}, start, rows);		
	}
	
	public int selectMyArticlesCount(String userId) throws DaoException {
		StringBuffer sql = new StringBuffer("SELECT count(*) AS total " +
				"FROM wiki_article a, wiki_categories c WHERE createdBy=?  AND a.category = c.categoryId ");
		Collection list = super.select(sql.toString(), HashMap.class, new Object[]{userId}, 0, -1);
        HashMap map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("total").toString());		
	}
	
	public Collection searchArticleByCategory(String category, int start , int rows) throws DaoException{
		StringBuffer sql = new StringBuffer("SELECT articleId, story, a.createdOn AS createdOn, createdBy, title," +
				"c.category as category , c.categoryId AS categoryId, tags " +
				"FROM wiki_article a, wiki_categories c WHERE a.category = ? AND a.category = c.categoryId");
		return super.select(sql.toString(), WikiArticle.class, new Object[]{category}, start , rows);
	}
    
	public int searchArticleByCategoryCount(String category) throws DaoException{
		StringBuffer sql = new StringBuffer("SELECT count(*) AS total " +
				"FROM wiki_article a, wiki_categories c WHERE a.category = ? AND a.category = c.categoryId");

		Collection list = super.select(sql.toString(), HashMap.class, null, 0, -1);
        HashMap map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("total").toString());
	}
	
	public Collection searchArticleByTag(String tag, int start, int rows) throws DaoException{
		StringBuffer sql = new StringBuffer("SELECT articleId, story, a.createdOn AS createdOn, createdBy, title," +
				"c.category as category , c.categoryId AS categoryId, tags " +
				"FROM wiki_article a, wiki_categories c WHERE a.tags like '%"+tag+"%' AND a.category = c.categoryId");

		return super.select(sql.toString(), WikiArticle.class, null, start, rows);
	}
	
	public int searchArticleByTagCount(String tag) throws DaoException{
		StringBuffer sql = new StringBuffer("SELECT count(*) AS total " +
				"FROM wiki_article a, wiki_categories c WHERE a.tags like '%"+tag+"%' AND a.category = c.categoryId");

		Collection list = super.select(sql.toString(), HashMap.class, null, 0, -1);
        HashMap map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("total").toString());
	}
	
	public Collection searchArticlesByArticleTitle(String title) throws DaoException {
		StringBuffer sql = new StringBuffer("SELECT articleId, story, a.createdOn AS createdOn, createdBy, title," +
				"c.category as category , c.categoryId AS categoryId, tags " +
				"FROM wiki_article a, wiki_categories c WHERE Upper(a.title) = ?  AND a.category = c.categoryId");		
		return super.select(sql.toString(), WikiArticle.class, new Object[]{title.toUpperCase()}, 0 , -1);
	}
	
	public Collection articleHistory(String articleId, int start, int rows) throws DaoException{
	StringBuffer sql = new StringBuffer("SELECT a.articleId as articleId, a.revisionId AS currentRevision, r.revisionId AS revisionId, " +
			"a.title as title, createdBy,createdOn, userName, modifiedOn, r.editSummary AS editSummary " +
			"FROM wiki_article a, wiki_article_revision r, security_user s " +
			"WHERE a.articleId=r.articleId AND s.id=r.modifiedBy AND r.articleId=? ORDER BY modifiedOn DESC");
	return super.select(sql.toString(), WikiArticle.class, new Object[]{articleId}, start, rows);
		
	}
	
	public Collection selectRevision(String revisionId) throws DaoException{
		StringBuffer sql = new StringBuffer("SELECT articleId, story FROM wiki_article_revision WHERE revisionId=?");
		return super.select(sql.toString(), WikiArticle.class, new Object[]{revisionId}, 0, -1);
	}


    public void rollBack(String articleId, String revisionId, String story) throws DaoException {
    	String sql = "UPDATE wiki_article SET revisionId = ? , story= ? WHERE articleId = ?";
    	super.update(sql, new Object[]{revisionId, story, articleId});
    }
    
    public Collection selectArticleByCategory(String categoryId) throws DaoException{
    	String sql = ("SELECT title, articleId FROM wiki_article WHERE category LIKE ? ");
    	return super.select(sql, WikiArticle.class, new Object[]{categoryId}, 0, -1);    	
    }
    
    public Collection selectLockedArticles(String searchBy, String sort, boolean desc, int start, int rows) throws DaoException{
    	String sql = ("SELECT articleId, title, c.category AS category, m.moduleName AS moduleName, articleStatus " +
    			"FROM wiki_article a, wiki_categories c, wiki_module m " +
    			"WHERE a.category = c.categoryId AND c.currentModule = m.moduleId ");
    	if(searchBy!= null && !searchBy.equals("")){
    		sql += "AND title like '%"+searchBy+"%' ";
    	}
    	if(sort == null)
    		sort = "title";
    	sql += "ORDER BY "+sort;
    	if(desc)
    		sql += " DESC";
    	return super.select(sql, WikiArticle.class, null, start, rows);
    }
    
    public int selectLockedArticlesCount(String searchBy) throws DaoException{
    	String sql = ("SELECT count(*) AS total FROM wiki_article a, wiki_categories c, wiki_module m " +
    			"WHERE a.category = c.categoryId AND c.currentModule = m.moduleId");
    	Collection list = super.select(sql, HashMap.class, null, 0, -1);
        HashMap map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("total").toString());
    }
    
    public void lockArticle(String articleId, Boolean locked) throws DaoException {
    	String sql="Update wiki_article SET articleStatus=? WHERE articleId = ?";
    			
    	super.update(sql, new Object[]{locked, articleId});
    }
    
    public Collection selectIndexArticles(String key) throws DaoException {
		String sql = "SELECT articleId, story, a.createdOn AS createdOn, " +
				"c.category as category , c.categoryId AS categoryId, title, tags " +
				"FROM wiki_article a, wiki_categories c WHERE a.category = c.categoryId ";
				
		if (key == null) {
            key="A";
        }
		sql += "AND Upper(title) LIKE '"+key.toUpperCase().trim()+"%' ";
		sql += " ORDER BY createdOn DESC";
		return super.select(sql.toString(), WikiArticle.class,null , 0, -1);		
	}
    
    public Collection selectRecentChanges() throws DaoException{
    	StringBuffer sql = new StringBuffer(
    			"SELECT r.articleId AS articleId, r.story AS story, r.modifiedBy as modifiedBy, r.modifiedOn AS modifiedOn,  " +
				"c.category as category , c.categoryId AS categoryId, title, tags " +
				"FROM wiki_article a, wiki_categories c, wiki_article_revision r " +
				"WHERE r.revisionId = a.revisionId AND a.category = c.categoryId " +
				"ORDER BY r.modifiedOn DESC");
		return super.select(sql.toString(), WikiArticle.class,null , 0, 5);
    }
    
    public Collection advanceSearch(String search, boolean categories, boolean tagsCollection, boolean titleCollection, boolean articleCollection,int start , int rows) throws DaoException {
        String sql = "SELECT articleId, story, a.createdOn AS createdOn, " +
				"c.category as category , c.categoryId AS categoryId, title, tags " +
				"FROM wiki_article a, wiki_categories c WHERE a.category = c.categoryId ";
        
        String condition = "";
                
        if(categories){
        	if(condition.length()==0){
        		condition += " AND (";
        	} else {
        		condition += " OR";
        	}
        	condition += " Upper(c.category) like '%"+search.toUpperCase().trim()+"%' ";
        } 
        if(tagsCollection){
        	if(condition.length()==0){
        		condition += " AND (";
        	} else {
        		condition += " OR";
        	}
        	condition += " Upper(tags) like '%"+search.toUpperCase().trim()+"%' ";
        } 
        if(titleCollection){
        	if(condition.length()==0){
        		condition += " AND (";
        	} else {
        		condition += " OR";
        	}
        	condition += " Upper(title) like '%"+search.toUpperCase().trim()+"%' ";
        }
        
        if(articleCollection){
        	if(condition.length()==0){
        		condition += " AND (";
        	} else {
        		condition += " OR";
        	}
        	condition += " Upper(story) like '%"+search.toUpperCase().trim()+"%' ";
        }
        
        if(condition.length()>0){
        	sql += condition+ " ) ";
        }       
       
        Collection list = super.select(sql, WikiArticle.class, null, start, rows);
        return list;
    }
    
    public int advanceSearchCount(String search, boolean categories, boolean tagsCollection, boolean titleCollection, boolean articleCollection) throws DaoException{
    	String sql = "SELECT count(*) as total " +
		"FROM wiki_article a, wiki_categories c WHERE a.category = c.categoryId ";
    	
    	String condition = "";
        if(search!=null){
	        if(categories){
	        	if(condition.length()==0){
	        		condition += " AND (";
	        	} else {
	        		condition += " OR";
	        	}
	        	condition += " Upper(c.category) like '%"+search.toUpperCase().trim()+"%' ";
	        } 
	        if(tagsCollection){
	        	if(condition.length()==0){
	        		condition += " AND (";
	        	} else {
	        		condition += " OR";
	        	}
	        	condition += " Upper(tags) like '%"+search.toUpperCase().trim()+"%' ";
	        } 
	        if(titleCollection){
	        	if(condition.length()==0){
	        		condition += " AND (";
	        	} else {
	        		condition += " OR";
	        	}
	        	condition += " Upper(title) like '%"+search.toUpperCase().trim()+"%' ";
	        }
	        
	        if(articleCollection){
	        	if(condition.length()==0){
	        		condition += " AND (";
	        	} else {
	        		condition += " OR";
	        	}
	        	condition += " Upper(story) like '%"+search.toUpperCase().trim()+"%' ";
	        }
        }     
        if(condition.length()>0){
        	sql += condition+ " ) ";
        }       
               
        Collection list = super.select(sql, HashMap.class, null, 0, -1);
        HashMap map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("total").toString());
    	
    }
    
    public Collection selectHotTopics() throws DaoException {
		StringBuffer sql = new StringBuffer("SELECT title, createdOn, a.articleId AS articleId, count(commentId) AS comments from wiki_comments c, " +
				" wiki_article a WHERE c.articleId = a.articleId GROUP BY articleId order by comments DESC");
		return super.select(sql.toString(), WikiArticle.class, null, 0, 5);		
	}
    
    public void deleteArticle(String articleId) throws DaoException {
        String sql = "DELETE FROM wiki_article WHERE articleId = '" + articleId + "'";
        super.update(sql, null);
    }  
        
    public int selectVersionCount(String articleId) throws DaoException{
    	StringBuffer sql = new StringBuffer("SELECT count(*) as total " +
    			"FROM wiki_article a, wiki_article_revision r, security_user s " +
			"WHERE a.articleId=r.articleId AND s.id=r.modifiedBy AND r.articleId=?");
    	Collection list = super.select(sql.toString(), HashMap.class, new Object[]{articleId}, 0, -1);
        HashMap map = (HashMap) list.iterator().next();
        return Integer.parseInt(map.get("total").toString());
    		
    }
    
    public Collection getArticleCategories(String moduleId) {
    	Collection col = new ArrayList();
    	String sql = "SELECT " +
    			"categoryId, category,parentId FROM wiki_categories WHERE currentModule=? order by createdOn,category";
    	try {
    		col = super.select(sql, WikiCategory.class, new Object[]{moduleId}, 0, -1);
		} catch (DaoException e) {
			e.printStackTrace();
		}
    	return col;    	
    }
    
    public Collection selectModules() throws DaoException {
        String sql = "SELECT moduleId, moduleName from wiki_module ORDER BY moduleName";
        Collection list = super.select(sql, WikiArticle.class, null, 0, -1);
        return list;
    }        
    
    public void deleteComment(String commentId) throws DaoException {
        String sql = "DELETE FROM wiki_comments WHERE commentId = '" + commentId + "'";
        super.update(sql, null);
    }
}
