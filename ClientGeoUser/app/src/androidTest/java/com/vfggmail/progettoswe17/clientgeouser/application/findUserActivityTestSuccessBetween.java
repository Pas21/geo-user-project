package com.vfggmail.progettoswe17.clientgeouser.application;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.vfggmail.progettoswe17.clientgeouser.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class findUserActivityTestSuccessBetween {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginActivityTestFindUserPositionSuccessBetween() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                allOf(withId(R.id.username_login_form),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                0)),
                                1)));
        appCompatEditText.perform(scrollTo(), replaceText("io"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                allOf(withId(R.id.username_login_form),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                0)),
                                2)));
        appCompatEditText2.perform(scrollTo(), replaceText("io"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.login_button), withText("Log in"),
                        childAtPosition(
                                allOf(withId(R.id.username_login_form),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                0)),
                                3)));
        appCompatButton2.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.Posizione), withText("Find User"),
                        childAtPosition(
                                allOf(withId(R.id.include2),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.find_username),
                        childAtPosition(
                                allOf(withId(R.id.find_page_form),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                0)),
                                1)));
        appCompatEditText3.perform(scrollTo(), replaceText("io"), closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.find_date1_buttonDate), withText("Change date"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.find_page_form),
                                        3),
                                0)));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageButton")), withContentDescription("Mese precedente"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.DayPickerView")),
                                        childAtPosition(
                                                withClassName(is("com.android.internal.widget.DialogViewAnimator")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageButton")), withContentDescription("Mese precedente"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.DayPickerView")),
                                        childAtPosition(
                                                withClassName(is("com.android.internal.widget.DialogViewAnimator")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton5.perform(scrollTo(), click());

        pressBack();

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.find_find_button), withText("Find"),
                        childAtPosition(
                                allOf(withId(R.id.find_page_form),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                0)),
                                6)));
        appCompatButton6.perform(scrollTo(), click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
