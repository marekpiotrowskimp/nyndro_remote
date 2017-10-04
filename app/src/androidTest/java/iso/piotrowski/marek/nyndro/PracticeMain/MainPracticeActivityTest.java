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
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * Created by Marek on 02.10.2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainPracticeActivityTest {
    private static final String NEW_PRACTICE = "NEW PRACTICE";
    private static final String PRACTICE_COUNT = "1108";
    @Rule
    public ActivityTestRule<MainPracticeActivity> activityTestRule = new ActivityTestRule<>(MainPracticeActivity.class);

    @Test
    public void testBoomButtonAddPractice() {

        ViewInteraction frameLayout = onView(withId(R.id.boom_menu_button));
        for (int index = 0; index < Practice.practices.length; index++) {
            frameLayout.perform(click());
            TestTools.delay(500);
            onView(TestTools.withIndex(withId(R.id.button), index)).perform(click());
            TestTools.delay(500);
        }

        for (int index = 0; index < Practice.practices.length - 1; index++) {
            onView(withId(R.id.practice_main_recycleView))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));
        }

        onView(TestTools.withIndex(withId(R.id.practice_repetition_add), 0)).perform(click());
        onView(TestTools.withIndex(withId(R.id.practice_status), 0)).check(matches(withText(startsWith("108"))));
    }

    @Test
    public void testPracticeDetails() {
        onView(withId(R.id.practice_main_recycleView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.edit_toolbar)).perform(click());
        TestTools.delay(250);

        onView(withId(R.id.practice_name_edit)).perform(scrollTo(), replaceText(NEW_PRACTICE), closeSoftKeyboard());
        onView(withId(R.id.practice_status_progress_edit)).perform(scrollTo(), replaceText(PRACTICE_COUNT), closeSoftKeyboard());
        onView(withId(R.id.practice_status_maxrepetition_edit)).perform(scrollTo(), replaceText("999999"), closeSoftKeyboard());
        onView(withId(R.id.practice_repetition_edit)).perform(scrollTo(), replaceText("333"), closeSoftKeyboard());
        onView(withId(R.id.practice_description_edit)).perform(scrollTo(), replaceText("NO DESCRIPTION"), closeSoftKeyboard());
        TestTools.delay(250);
        onView(withId(R.id.edit_toolbar)).perform(click());
        TestTools.delay(250);
        onView(withId(R.id.practice_name_featured)).check(matches(withText(NEW_PRACTICE)));
        onView(withId(R.id.practice_status_progress_detail)).check(matches(withText(containsString(PRACTICE_COUNT))));
        onView(withId(R.id.practice_status_maxrepetition_detail)).check(matches(withText(containsString("999999"))));
        onView(withId(R.id.practice_repetition_detail)).check(matches(withText(containsString("333"))));
        onView(withId(R.id.practice_description_detail)).check(matches(withText(containsString("NO DESCRIPTION"))));

        pressBack();
    }

    @Test
    public void testStatistics() {
        TestTools.delay(7500);
        onView(withId(R.id.tab_statistic)).perform(click());
        TestTools.delay(250);

        onView(withId(R.id.statystics_practice_name)).check(matches(withText(NEW_PRACTICE)));
        onView(withId(R.id.statystics_practice_average_week)).check(matches(withText(containsString(PRACTICE_COUNT))));
        onView(withId(R.id.statystics_practice_average_month)).check(matches(withText(containsString(PRACTICE_COUNT))));
        onView(withId(R.id.statystics_practice_day)).check(matches(withText(containsString("1"))));
        onView(withId(R.id.statystics_practice_average_days)).check(matches(withText(containsString("0"))));
        onView(withId(R.id.statystics_practice_expected_end)).check(matches(withText(containsString("0"))));
    }

    @Test
    public void testHistory() {
        TestTools.delay(250);
        onView(withId(R.id.tab_history)).perform(click());
        TestTools.delay(250);

        onView(withId(R.id.history_practice_name_featured)).check(matches(withText(NEW_PRACTICE)));
        onView(withId(R.id.history_progress)).check(matches(withText(containsString(PRACTICE_COUNT))));
        onView(withId(R.id.history_repetition)).check(matches(withText(containsString(PRACTICE_COUNT))));
    }

    @Test
    public void testPlansRemainders() {
        TestTools.delay(250);
        onView(withId(R.id.tab_plans)).perform(click());
        TestTools.delay(250);

        ViewInteraction linearLayout = onView(TestTools.withIndex(withId(R.id.ll_sub_parrent), 11));
        linearLayout.perform(longClick());
        TestTools.delay(250);

        onView(withId(R.id.boom_menu_button)).perform(click());
        TestTools.delay(250);
        onView(TestTools.withIndex(withId(R.id.button), 0)).perform(click());


        onView(withId(R.id.repeater_button)).inRoot(isDialog()).perform(click());
        onView(withId(R.id.plans_practice_name)).check(matches(withText(NEW_PRACTICE)));
        TestTools.delay(250);
        pressBack();
    }

    @Test
    public void testTapCounter() {
        TestTools.delay(250);
        onView(withId(R.id.tab_counter)).perform(click());
        TestTools.delay(250);

        for (int index = 0; index < 10; index++) {
            onView(withId(R.id.counter_layout)).perform(click());
            TestTools.delay(100);
        }
        onView(withId(R.id.counter_text)).check(matches(withText("0010")));
        onView(withId(R.id.reset_repeats)).perform(click());
        TestTools.delay(50);
        onView(withId(R.id.counter_text)).check(matches(withText("0000")));
        TestTools.delay(100);

        for (int index = 0; index < 10; index++) {
            onView(withId(R.id.counter_layout)).perform(click());
            TestTools.delay(100);
        }
        TestTools.delay(100);
        onView(withId(R.id.ham_boom_menu_button)).perform(click());
        TestTools.delay(250);
        onView(TestTools.withIndex(withId(R.id.button), 0)).perform(click());

        TestTools.delay(250);
        onView(withId(R.id.tab_practice)).perform(click());
        TestTools.delay(250);
        onView(withId(R.id.practice_status)).check(matches(withText(containsString("1118"))));
        TestTools.delay(250);
        onView(withId(R.id.practice_main_recycleView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft()));
        TestTools.delay(2000);
    }

}