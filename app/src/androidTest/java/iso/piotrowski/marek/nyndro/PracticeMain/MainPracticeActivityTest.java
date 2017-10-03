package iso.piotrowski.marek.nyndro.PracticeMain;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.KeyEvent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.TestTools;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * Created by Marek on 02.10.2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainPracticeActivityTest {

    @Rule
    public ActivityTestRule<MainPracticeActivity> activityTestRule = new ActivityTestRule<>(MainPracticeActivity.class);

    @Test
    public void testBoomButtonAddPractice() {

        ViewInteraction frameLayout = onView(withId(R.id.boom_menu_button));
        for (int index = 0; index < Practice.practices.length; index++) {
            frameLayout.perform(click());
            TestTools.delay(700);
            onView(TestTools.withIndex(withId(R.id.button), index)).perform(click());
            TestTools.delay(700);
        }

        for (int index = 0; index < Practice.practices.length - 1; index++) {
            onView(withId(R.id.practice_main_recycleView))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));
        }

        onView(TestTools.withIndex(withId(R.id.practice_repetition_add),0)).perform(click());
        onView(TestTools.withIndex(withId(R.id.practice_status),0)).check(matches(withText(startsWith("108"))));

        onView(withId(R.id.practice_main_recycleView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.edit_toolbar)).perform(click());
        TestTools.delay(300);
        onView(withId(R.id.practice_name_edit)).perform(scrollTo(), replaceText("NEW PRACTICE"), closeSoftKeyboard());
        onView(withId(R.id.practice_status_progress_edit)).perform(scrollTo(), replaceText("1108"), closeSoftKeyboard());
        TestTools.delay(300);
        onView(withId(R.id.edit_toolbar)).perform(click());
        TestTools.delay(300);
        onView(withId(R.id.practice_name_featured)).check(matches(withText("NEW PRACTICE")));
        onView(withId(R.id.practice_status_progress_detail)).check(matches(withText(containsString("1108"))));

        pressBack();
    }

}