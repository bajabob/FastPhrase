package fastphrase.com;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

@LargeTest
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testDefaults(){
        AppDataManager adm = new AppDataManager();
        // test that app data is created
        adm.testDefaults();
    }

    public void testRecordingsAreAdded(){
        AppDataManager adm = new AppDataManager();
        // test that recordings are added
        adm.testRecordingsAreAdded();
    }

    public void testTagsAreAdded(){
        AppDataManager adm = new AppDataManager();
        // test that tags are added
        adm.testTagsAreAdded();
    }

    public void testRecordingsHasTags(){
        AppDataManager adm = new AppDataManager();
        adm.testRecordingsHasTags();
    }

    public void testRecordingTagRemoved(){
        AppDataManager adm = new AppDataManager();
        adm.testRemoveRecordingTag();
    }

    public void testHoldsTags(){
        AppDataManager adm = new AppDataManager();
        adm.testHoldsTags();
    }

    public void testGetRecordingsByLabel(){
        AppDataManager adm = new AppDataManager();
        adm.testGetRecordingsByLabel();
    }
}