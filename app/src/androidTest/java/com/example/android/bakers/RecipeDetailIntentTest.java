package com.example.android.bakers;


import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakers.model.Step;
import com.example.android.bakers.ui.MainActivity;
import com.example.android.bakers.ui.RecipeDetailsFragment;
import com.example.android.bakers.ui.RecipeFragment;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.android.bakers.TestUtils.actionOnItemViewAtPosition;
import static com.example.android.bakers.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.allOf;
import com.example.android.bakers.ui.RecipeDetailsFragment;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailIntentTest {

    @Rule
    public IntentsTestRule<MainActivity> mIntentTestRule= new IntentsTestRule<>(MainActivity.class);

    @Test
    public void triggerRecipeDetailIntentTest(){

        IdlingResource resource= RecipeFragment.getmResource();

        Espresso.registerIdlingResources(resource);

        onView(withRecyclerView(R.id.rvRecipe).atPosition(1)).check(matches(isDisplayed()));

        Espresso.unregisterIdlingResources(resource);

        onView(withId(R.id.rvRecipe)).perform(actionOnItemViewAtPosition(1, R.id.ivCardRecipe, click()));

        onView(withRecyclerView(R.id.rvRecipeDetails).atPositionOnView(10, R.id.tvStepShortDescription))
                .check(matches(isDisplayed())).perform(click());

        intended(allOf(toPackage("com.example.android.bakers"),
                hasComponent("com.example.android.bakers.ui.RecipeStepDetailsActivity"),
                hasExtraWithKey("data")));
    }

}
