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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class loginActivityTestFailedUsername {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginActivityTestFailedUsername() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.login_signin), withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.username_login_form),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                0)),
                                4)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.signIn_username),
                        childAtPosition(
                                allOf(withId(R.id.username_signIn_form),
                                        childAtPosition(
                                                withId(R.id.signin_form),
                                                0)),
                                0)));
        appCompatEditText.perform(scrollTo(), replaceText("giovanni"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.signIn_password),
                        childAtPosition(
                                allOf(withId(R.id.username_signIn_form),
                                        childAtPosition(
                                                withId(R.id.signin_form),
                                                0)),
                                1)));
        appCompatEditText2.perform(scrollTo(), replaceText("codianni"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.signIn_password2),
                        childAtPosition(
                                allOf(withId(R.id.username_signIn_form),
                                        childAtPosition(
                                                withId(R.id.signin_form),
                                                0)),
                                2)));
        appCompatEditText3.perform(scrollTo(), replaceText("codianni"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.signIn_email),
                        childAtPosition(
                                allOf(withId(R.id.username_signIn_form),
                                        childAtPosition(
                                                withId(R.id.signin_form),
                                                0)),
                                3)));
        appCompatEditText4.perform(scrollTo(), replaceText("gio@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.signIn_Name),
                        childAtPosition(
                                allOf(withId(R.id.username_signIn_form),
                                        childAtPosition(
                                                withId(R.id.signin_form),
                                                0)),
                                4)));
        appCompatEditText5.perform(scrollTo(), replaceText("giovanni"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.signIn_Surname),
                        childAtPosition(
                                allOf(withId(R.id.username_signIn_form),
                                        childAtPosition(
                                                withId(R.id.signin_form),
                                                0)),
                                5)));
        appCompatEditText6.perform(scrollTo(), replaceText("codianni"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.signIn_signIn_button), withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.username_signIn_form),
                                        childAtPosition(
                                                withId(R.id.signin_form),
                                                0)),
                                6)));
        appCompatButton3.perform(scrollTo(), click());

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
