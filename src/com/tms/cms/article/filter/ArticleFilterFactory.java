package com.tms.cms.article.filter;

/**
 * A Factory class that returns a DocumentFilter that is appropriate
 * for a specifed content MIME type.
 */
public class ArticleFilterFactory {

    /**
     * @return An array of ArticleFilter objects.
     */
    public static ArticleFilter[] getArticleFilters() {
        return new ArticleFilter[] {
            new ArticleAdFilter(),
            new ArticleStockQuoteFilter(),
        };
    }
    
}
