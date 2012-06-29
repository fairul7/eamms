package com.tms.wiki.model;

import com.tms.wiki.model.WikiException;
import kacang.model.DaoException;
import kacang.model.DataObjectNotFoundException;
import kacang.model.DefaultModule;
import kacang.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.SequencedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: hima
 * Date: Dec 14, 2006
 * Time: 1:56:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class WikiModule extends DefaultModule {
	public static final String ROOT_CATEGORY_ID="";
    public static final String ROOT_CATEGORY_NAME="Select Category";
    public static final int STORY_SUMMARY_LENGTH=200;

   
    public void editArticle(WikiArticle article) {
        WikiDao dao = (WikiDao) getDao();
        try {
            dao.editArticle(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   

       public void rollBack(String revisionId) throws WikiException {
        WikiDao dao = (WikiDao) getDao();
        try {
        	Collection col = dao.selectRevision(revisionId);
        	WikiArticle article = null;
        	if(col!=null && col.size()>0){
        		article = (WikiArticle) col.iterator().next();     
        	
        		dao.rollBack(article.getArticleId(), revisionId, article.getStory());
        	}
        } catch (DaoException e) {
            throw new WikiException();
        }
    }

    public void addCategory(WikiCategory category) throws WikiException {
        WikiDao dao = (WikiDao) getDao();
        try {
            dao.addCategory(category);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategory(String categoryId) throws WikiException {
        WikiDao dao = (WikiDao) getDao();
        try {
        	if(dao.selectCategoriesCountByCategory(categoryId)>0){
        		throw new WikiException("Subcategories for category exist...");
        	}                	
        	dao.deleteCategory(categoryId);
        } catch (DaoException e) {
            e.printStackTrace();
        }      
        
    }

    public void deleteSubCategory(String subCategoryId) throws WikiException {
        WikiDao dao = (WikiDao) getDao();
        try {
            dao.deleteSubCategory(subCategoryId);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public void addArticle(WikiArticle article) throws WikiException {
        WikiDao dao = (WikiDao) getDao();
        try {
            dao.addArticle(article);
        } catch (Exception e) {
            throw new WikiException("Error creating article " + article + " " + e.toString());
        }
    }

    public WikiArticle viewArticle(String articleId) throws WikiException {
        WikiDao dao = (WikiDao) getDao();
        WikiArticle article = null;
        try {
            Collection col = dao.selectArticle(articleId);
            if (col.size() > 0)
                article = (WikiArticle) col.iterator().next();
        } catch (Exception e) {
            throw new WikiException("Error viewing article " + articleId + " " + e.toString());
        }
        return article;
    }

    public Collection getCategories(String moduleId, String parentId, String searchBy, String sort, boolean desc, int start, int rows) throws WikiException {
        Collection col = new ArrayList();
        WikiDao dao = (WikiDao) getDao();
        try {
            col = dao.selectCategory(moduleId, parentId, searchBy, sort, desc, start, rows);
        } catch (Exception e) {
            throw new WikiException("Error listing category details" + e.toString());
        }
        return col;
    }

    public int getCategoryCount(String moduleId, String parentId, String searchBy) throws WikiException {
        int count = 0;
        WikiDao dao = (WikiDao) getDao();
        try {
            count = dao.selectCategoryCount(moduleId, parentId, searchBy);
        } catch (Exception e) {
            throw new WikiException("Error counting category details" + e.toString());
        }
        return count;
    }

    public Collection getSubCategories(String categoryId, String searchBy, String sort, boolean desc, int start, int rows) throws WikiException {
        Collection col = new ArrayList();
        WikiDao dao = (WikiDao) getDao();
        try {
            col = dao.selectSubCategories(categoryId, searchBy, sort, desc, start, rows);
        } catch (Exception e) {
            throw new WikiException("Error listing subCategory details" + e.toString());
        }
        return col;
    }

    public int getSubCategoryCount(String categoryId, String searchBy) throws WikiException {
        int count = 0;
        WikiDao dao = (WikiDao) getDao();
        try {
            count = dao.selectSubCategoryCount(categoryId, searchBy);
        } catch (Exception e) {
            throw new WikiException("Error counting subCategory details" + e.toString());
        }
        return count;
    }

    public void addSubCategory(WikiCategory subCategory) throws WikiException {
        WikiDao dao = (WikiDao) getDao();
        try {
            dao.addSubCategory(subCategory);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public WikiCategory selectCategory(String categoryId) {
        WikiDao dao = (WikiDao) getDao();
        WikiCategory sdo = null;
        try {

            Collection results = dao.getCategory(categoryId);
            if (results.size() == 0) {
                throw new DataObjectNotFoundException();
            } else {
                sdo = (WikiCategory) results.iterator().next();
            }

        } catch (DaoException e) {
            e.printStackTrace();
        } catch (DataObjectNotFoundException e) {
            Log.getLog(getClass()).error(e.toString());
        }
        return sdo;
    }

    public Collection getModules(String searchBy, String sort, boolean desc, int start, int rows) throws WikiException {
        Collection col = new ArrayList();
        WikiDao dao = (WikiDao) getDao();
        try {
            col = dao.selectModule(searchBy, sort, desc, start, rows);
        } catch (Exception e) {
            throw new WikiException("Error listing modules.." + e.toString());
        }
        return col;
    }

    public int getModuleCount(String searchBy) throws WikiException {
        int count = 0;
        WikiDao dao = (WikiDao) getDao();
        try {
            count = dao.selectModuleCount(searchBy);
        } catch (Exception e) {
            throw new WikiException("Error counting module details" + e.toString());
        }
        return count;
    }

    public void deleteModule(String moduleId) throws WikiException {
        WikiDao dao = (WikiDao) getDao();
        try {
        	if(dao.selectCategoriesCountByModule(moduleId)>0){
        		throw new WikiException("Categories for module exist...");
        	}                	
            dao.deleteModule(moduleId);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public void addModule(WikiCategory category) throws WikiException {
        WikiDao dao = (WikiDao) getDao();
        try {
            dao.addModule(category);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
    
    public WikiCategory getFolderTree(String categoryId){
        WikiDao dao = (WikiDao) getDao();
        try {
            Collection folders = dao.getFolderList(categoryId);
            WikiCategory  root = new WikiCategory();
            root.setId(ROOT_CATEGORY_ID);
            root.setCategory(ROOT_CATEGORY_NAME);

            Map folderMap = new SequencedHashMap();
            for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
            	WikiCategory rep = (WikiCategory) iterator.next();
                folderMap.put(rep.getCategoryId(), rep);
            }
            WikiCategory parent = new WikiCategory();
            for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
            	WikiCategory folder = (WikiCategory) iterator.next();
                parent = (WikiCategory) folderMap.get(folder.getParentId());
                if (parent == null) {
	                parent = root;
	            }
                Collection subfolders = parent.getChildren();
	            if (subfolders == null) {
	                subfolders = new ArrayList();
	                parent.setChildren(subfolders);
	            }
	            subfolders.add(folder);
	            parent.setChildren(subfolders);
            }
            return root;
        } catch (DaoException e) {
            Log.getLog(getClass()).error(e);
        }
        return null;
    }
    
    public void addComments(WikiArticle article) throws WikiException {
        WikiDao dao = (WikiDao) getDao();
        try {
            dao.leaveYourComments(article);
        } catch (Exception e) {
            throw new WikiException("Error leaving comments" + article + " " + e.toString());
        }
    }

	public Collection getComments(String articleId) {
		WikiDao dao = (WikiDao) getDao();
		
		try {
			return dao.selectAllComents(articleId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e);
		}
		return null;
	}
	
	public Collection getArticles(){
		WikiDao dao = (WikiDao) getDao();
		
		try {
			return dao.selectAllArticles();
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e);
		}
		return null;
	}
	
	public Collection getMyArticles(String userId, int start, int rows){
		WikiDao dao = (WikiDao) getDao();
		
		try {
			return dao.selectMyArticles(userId, start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e);
		}
		return null;
	}
	
	
	public Collection searchArticleByCategory(String category, int start, int rows){
		WikiDao dao = (WikiDao) getDao();
		
		try {
			return dao.searchArticleByCategory(category, start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e);
		}
		return null;
	}
	
	public Collection searchArticleByTag(String tag, int start, int rows){
		WikiDao dao = (WikiDao) getDao();
		
		try {
			return dao.searchArticleByTag(tag, start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e);
		}
		return null;
	}
	
	public WikiArticle searchArticlesByArticleTitle(String title){
		WikiDao dao = (WikiDao) getDao();
		WikiArticle article = null;
		
		try {
			Collection  col =  dao.searchArticlesByArticleTitle(title);
			if(col.size()>0)
				article = (WikiArticle) col.iterator().next();
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e);
		}
		return article;
	}
	
	public Collection articleHistory(String articleId, int start, int rows){
		WikiDao dao = (WikiDao) getDao();
		
		try {
			return dao.articleHistory(articleId, start, rows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e);
		}
		return null;
	}
	
	public Collection selectArticleByCategory(String categoryId){
		WikiDao dao = (WikiDao) getDao();
		
		try {
			return dao.selectArticleByCategory(categoryId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(" cannot select article from category "+e);
		}
		return null;
	}
	
	public Collection getLockedArticles(String searchBy, String getSort, boolean isDesc, int getStart, int getRows)throws WikiException{
		WikiDao dao = (WikiDao) getDao();
		Collection col = new ArrayList();
		try {
			col=dao.selectLockedArticles(searchBy, getSort, isDesc, getStart, getRows);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(" cannot select data "+e);
		}
		return col;
	}
	
	public int getLockedArticlesCount(String searchBy) throws WikiException{
		WikiDao dao = (WikiDao) getDao();
		int count=0;
		try {
			count=dao.selectLockedArticlesCount(searchBy);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(" cannot retrive data "+e);
		}
		return count;
	}
	
	 public void lockArticle(String articleId, Boolean locked) throws WikiException {
        WikiDao dao = (WikiDao) getDao();
        try {
            dao.lockArticle(articleId, locked);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
	 
	public Collection getIndexArticles(String key){
		WikiDao dao = (WikiDao) getDao();
		
		try {
			return dao.selectIndexArticles(key);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e);
		}
		return null;
	}
	
	public Collection getRecentChanges() {
		WikiDao dao = (WikiDao)getDao();
		try{
			return dao.selectRecentChanges();
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e);
		}
		return null;
	}
	
	public Collection advanceSearch(String search, boolean categories, boolean tagsCollection, boolean titleCollection, boolean articleCollection, int start, int rows)  {
        WikiDao dao = (WikiDao) getDao();
        
        try {
            return dao.advanceSearch(search , categories, tagsCollection, titleCollection, articleCollection,start, rows);
            
        } catch (DaoException e) {
        	Log.getLog(getClass()).error(e);
        }
        return null;
    }
	
	public int advanceSearchCount(String search, boolean categories, boolean tagsCollection, boolean titleCollection, boolean articleCollection)  {
        WikiDao dao = (WikiDao) getDao();
        
        try {
            return dao.advanceSearchCount(search , categories, tagsCollection, titleCollection, articleCollection);
            
        } catch (DaoException e) {
        	Log.getLog(getClass()).error(e);
        }
        return 0;
    }

	public Collection search(String searchText, int start, int rows) {
		return advanceSearch(searchText, true, true, true, true, start, rows);
	}
	
	public int searchCount(String searchText){
		return advanceSearchCount(searchText, true, true, true, true);
	}
	
	public Collection getHotTopics(){
		WikiDao dao = (WikiDao) getDao();
		
		try {
			return dao.selectHotTopics();
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e);
		}
		return null;
	}
	
	public String viewCategoryPath(String categoryId){
		WikiDao dao = (WikiDao) getDao();
		String result =  null;
		
		try {
			if(categoryId!=null){
				WikiCategory category = dao.selectCategoryById(categoryId);
				
				result = "<a class='contentPathLink' href='viewCategories.jsp?categoryId="+category.getCategoryId()+"'> "+category.getCategory() +" </a>";
				
				while(category.getParentId()!=null && category.getParentId().trim().length()>0){
					category = dao.selectCategoryById(category.getParentId());
					result = "<a class='contentPathLink' href='viewCategories.jsp?categoryId="+category.getCategoryId()+"'> "+category.getCategory() +" </a> > " + result;				
				}
				
				if(category.getParentId()==null ){
				
					result = "<a class='contentPathLink' href='viewCategories.jsp?moduleId="+category.getModuleId()+"'> wiki </a> > " + result;
				}			
			}
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e);
		} catch (DataObjectNotFoundException e) {
			Log.getLog(getClass()).error(e);
		} 
		
		return result;
	}
	
	public void deleteArticle(String articleId){
		WikiDao dao = (WikiDao) getDao();				
		try		
		{
			WikiArticle article = viewArticle(articleId);
			if(!article.isArticleStatus())
				dao.deleteArticle(articleId);
		} catch (DaoException e) {
			Log.getLog(getClass()).error(e);		
		} catch (WikiException e) {
			Log.getLog(getClass()).error(e);
		}	
	}
	
	public Collection getArticleCategories(String moduleId) {
		WikiDao dao = (WikiDao) getDao();
		return dao.getArticleCategories(moduleId);	
		
	}
	
	public WikiCategory getArticleCategoriesList(String moduleId) {
		WikiDao dao = (WikiDao) getDao();
		Collection folders =  dao.getArticleCategories(moduleId);
		WikiCategory  root = new WikiCategory();
        root.setId(ROOT_CATEGORY_ID);
        root.setCategory(ROOT_CATEGORY_NAME);

        Map folderMap = new SequencedHashMap();
        for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
        	WikiCategory rep = (WikiCategory) iterator.next();
            folderMap.put(rep.getCategoryId(), rep);
            
        }
        WikiCategory parent = new WikiCategory();
        for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
        	WikiCategory folder = (WikiCategory) iterator.next();
            parent = (WikiCategory) folderMap.get(folder.getParentId());
            
            if (parent == null) {
                parent = root;
            }   
            
            Collection subfolders = parent.getChildren();
            if (subfolders == null) {            	
                subfolders = new ArrayList();
                parent.setChildren(subfolders);
                
            }
            subfolders.add(folder);
            parent.setChildren(subfolders);
        }
        
        return root;   
	}
	
	public void deleteComment(String commentId) throws WikiException {
        WikiDao dao = (WikiDao) getDao();
        try {
            dao.deleteComment(commentId);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
		
}
