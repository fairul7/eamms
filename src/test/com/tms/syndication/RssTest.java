package test.com.tms.syndication;

import junit.framework.TestCase;
import com.tms.cms.syndication.rss.RssManager;
import com.tms.cms.syndication.rss.RssFeed;

import java.io.IOException;

import org.xml.sax.SAXException;

public class RssTest extends TestCase {
    public void testOne() throws IOException, SAXException {
        RssManager rm = RssManager.getInstance();

        RssFeed feed = new RssFeed();

        // TODO: handle this!
        feed.setUrl("http://www.sys-con.com/story/category.cfm?id=677&rss=1");

        feed.setUrl("http://www.salon.com/feed/RDF/salon_use.rdf");
        feed.setUrl("http://rss.news.yahoo.com/rss/topstories");
        feed.setUrl("http://my-symbian.com/main/b2rss.xml");
        feed.setUrl("http://rss.com.com/2547-1_3-0-20.xml");

        rm.retrieveRssFeed(feed);

        System.out.println(feed.getFormattedString());
    }
}
