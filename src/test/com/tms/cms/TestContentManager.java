package test.com.tms.cms;

import com.tms.cms.core.model.*;
import com.tms.cms.section.Section;
import junit.framework.Test;
import junit.framework.TestSuite;
import kacang.Application;
import kacang.model.DataObjectNotFoundException;
import kacang.services.security.SecurityException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.util.JUnitTestCase;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 20, 2003
 * Time: 10:46:30 AM
 * To change this template use Options | File Templates.
 */
public class TestContentManager extends JUnitTestCase {

    public static String CONTENT_ID = "TestSection";
    private static String contentId;
    private static User author;
    private static User editor;

    static {
        // set content id
        contentId = ContentUtil.generateId(Section.class, CONTENT_ID);

        // set users
        SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
        try {
            author = security.getUser("author");
            editor = security.getUser("editor");
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public TestContentManager(String test) {
        super(test);
    }

    public void testCreateNew() throws ContentException, InvalidKeyException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

        Section section = new Section();
        section.setParentId(ContentManager.CONTENT_TREE_ROOT_ID);
        section.setId(CONTENT_ID);
        section.setName("Content Home");
        section.setDescription("Content Home");
        section.setAuthor("Content Home Author");
        section.setSummary("Content Home Summary");
        section.setContents("Content Home Contents");

        section = (Section)contentManager.createNew(section, author);

        System.out.println("testCreateNew: version" + section.getVersion());

        assertTrue("1".equals(section.getVersion()));
        assertTrue(section.isCheckedOut());
        assertTrue(!section.isSubmitted());
        assertTrue(!section.isApproved());
    }

    public void testCheckOutToEdit() throws ContentException, InvalidKeyException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

        ContentObject section = contentManager.checkOutToEdit(contentId, author);

        System.out.println("testCheckOutToEdit: version" + section.getVersion());

        assertTrue(section.isCheckedOut());
        assertTrue(!section.isSubmitted());
        assertTrue(!section.isApproved());
    }

    public void testCheckOutToEditFail() throws ContentException, InvalidKeyException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

        try {
            contentManager.checkOutToEdit(contentId, editor);
        }
        catch(Exception e) {
            System.out.println("testCheckOutToEditFail: deliberate exception: " + e.getMessage());
        }
    }

    public void testSave() throws ContentException, InvalidKeyException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

        ContentObject section = contentManager.checkOutToEdit(contentId, author);
        section.setName("Section Name 2");
        section.setDescription("Section Description 2");
        section.setAuthor("Section Author 2");
        section.setSummary("Section Summary 2");
        section.setContents("Section Contents 2");

        contentManager.save(section, author);

        System.out.println("testSave: version" + section.getVersion());

        assertTrue(section.isCheckedOut());
        assertTrue(!section.isSubmitted());
        assertTrue(!section.isApproved());
    }

    public void testSaveAgain() throws ContentException, InvalidKeyException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

        ContentObject section = contentManager.checkOutToEdit(contentId, author);
        section.setName("Section Name 3");
        section.setDescription("Section Description 3");
        section.setAuthor("Section Author 3");
        section.setSummary("Section Summary 3");
        section.setContents("Section Contents 3");

        contentManager.save(section, author);

        System.out.println("testSaveAgain: version" + section.getVersion());

        assertTrue(section.isCheckedOut());
        assertTrue(!section.isSubmitted());
        assertTrue(!section.isApproved());
    }

    public void testSubmitForApproval() throws ContentException, InvalidKeyException, DataObjectNotFoundException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

        ContentObject section = contentManager.submitForApproval(contentId, author);

        System.out.println("testSubmitForApproval: version" + section.getVersion());

        assertTrue(!section.isCheckedOut());
        assertTrue(section.isSubmitted());
        assertTrue(!section.isApproved());
    }

    public void testApprove() throws ContentException, InvalidKeyException, DataObjectNotFoundException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

        ContentObject section = contentManager.view(contentId, editor);

        contentManager.approve(section, editor);

        section = contentManager.view(contentId, editor);

        System.out.println("testApprove: version" + section.getVersion());

        assertTrue(!section.isCheckedOut());
        assertTrue(!section.isSubmitted());
        assertTrue(section.isApproved());
    }

    public void testReject() throws ContentException, InvalidKeyException, DataObjectNotFoundException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

        ContentObject section = contentManager.view(contentId, editor);

        section.setComments("Rejected");
        contentManager.reject(section, editor);

        section = contentManager.view(contentId, editor);

        System.out.println("testReject: version" + section.getVersion());

        assertTrue(!section.isCheckedOut());
        assertTrue(!section.isSubmitted());
        assertTrue(!section.isApproved());
    }

    public void testUndoCheckOut() throws ContentException, InvalidKeyException, DataObjectNotFoundException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

        contentManager.undoCheckOut(contentId, editor);

        ContentObject section = contentManager.view(contentId, editor);

        System.out.println("testUndoCheckOut: version" + section.getVersion());

    }

    public void testRollback() throws ContentException, InvalidKeyException, DataObjectNotFoundException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

        contentManager.rollback(contentId, "3", editor);

        ContentObject section = contentManager.view(contentId, editor);

        System.out.println("testRollback: version" + section.getVersion());

    }

    public void testPublish() throws ContentException, InvalidKeyException, DataObjectNotFoundException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);


        contentManager.publish(contentId, false, editor);

        ContentObject section = contentManager.view(contentId, editor);
        System.out.println("testPublish: version" + section.getVersion());
    }

    public void testWithdraw() throws ContentException, InvalidKeyException, DataObjectNotFoundException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

        contentManager.withdraw(contentId, false, editor);

        ContentObject section = contentManager.view(contentId, editor);
        System.out.println("testWithdraw: version" + section.getVersion());
    }

    public void testSearch() throws ContentException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);
        Collection results = contentManager.search("Section", null, 0, -1, author);

        System.out.println(results.toString());
    }

    public void testDestroy() throws ContentException, InvalidKeyException, DataObjectNotFoundException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

        try {
            ContentObject section = contentManager.view(contentId, editor);

            contentManager.destroy(new String[] {contentId}, editor);

            System.out.println("testDestroy: version" + section.getVersion());
        }
        catch(DataObjectNotFoundException e) {
        }
    }

    public void testClearAudit() throws ContentException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

        contentManager.clearAudit(contentId, null, null, editor);

        System.out.println("testClearAudit");
    }


/*
    public void testViewTree() throws ContentException, DataObjectNotFoundException {
        Application application = Application.getInstance();
        ContentManager contentManager = (ContentManager)application.getModule(ContentManager.class);

        ContentObject root = contentManager.viewTree("com.tms.cms.section.Section_a3a20660-c0a8c87c-11500000-23b18400", null, "userId");
        System.out.println(root.toString());
    }

*/

    public static Test suite() {
        TestSuite suite= new TestSuite();

        // clear previous results
        suite.addTest(new TestContentManager("testClearAudit"));
        suite.addTest(new TestContentManager("testDestroy"));

        // create new
        suite.addTest(new TestContentManager("testCreateNew"));

        // checkout, invalid checkout, save, save, save, submit, invalid checkout, approve
        suite.addTest(new TestContentManager("testCheckOutToEditFail"));
        suite.addTest(new TestContentManager("testSave"));
        suite.addTest(new TestContentManager("testSaveAgain"));
        suite.addTest(new TestContentManager("testSave"));
        suite.addTest(new TestContentManager("testSubmitForApproval"));
        suite.addTest(new TestContentManager("testCheckOutToEditFail"));
        suite.addTest(new TestContentManager("testApprove"));

        // checkout, save, submit, approve, publish
        suite.addTest(new TestContentManager("testCheckOutToEdit"));
        suite.addTest(new TestContentManager("testSaveAgain"));
        suite.addTest(new TestContentManager("testSubmitForApproval"));
        suite.addTest(new TestContentManager("testApprove"));
        suite.addTest(new TestContentManager("testPublish"));

        // checkout, save, submit, reject
        suite.addTest(new TestContentManager("testCheckOutToEdit"));
        suite.addTest(new TestContentManager("testSave"));
        suite.addTest(new TestContentManager("testSubmitForApproval"));
        suite.addTest(new TestContentManager("testReject"));

        // checkout, save, submit, approve, publish
        suite.addTest(new TestContentManager("testCheckOutToEdit"));
        suite.addTest(new TestContentManager("testSave"));
        suite.addTest(new TestContentManager("testSubmitForApproval"));
        suite.addTest(new TestContentManager("testApprove"));
        suite.addTest(new TestContentManager("testPublish"));

        // checkout, save, undo checkout, withdraw
        suite.addTest(new TestContentManager("testCheckOutToEdit"));
        suite.addTest(new TestContentManager("testSaveAgain"));
        suite.addTest(new TestContentManager("testUndoCheckOut"));


        // withdraw, rollback
//        suite.addTest(new TestContentManager("testWithdraw"));
//        suite.addTest(new TestContentManager("testRollback"));

        // search
//        suite.addTest(new TestContentManager("testSearch"));

        return suite;
    }

}
