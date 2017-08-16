package com.example.android.bakers;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakers.ui.MainActivity;
import com.example.android.bakers.ui.RecipeFragment;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.bakers.TestUtils.actionOnItemViewAtPosition;
import static com.example.android.bakers.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.anything;


@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {

    @Rule
    public  ActivityTestRule<MainActivity> mMainActivityTestRule= new ActivityTestRule<>(MainActivity.class);

    /*
    Test for Loading recipe cards on MainActivity
    * */
    @Test
    public void recipe_loads(){


        IdlingResource resource= RecipeFragment.getmResource();

        Espresso.registerIdlingResources(resource);

        onView(withRecyclerView(R.id.rvRecipe).atPosition(0)).check(matches(isDisplayed()));

        onView(withRecyclerView(R.id.rvRecipe)
                .atPositionOnView(1, R.id.tvRecipeName))
                .check(matches(withText("Brownies")));


        Espresso.unregisterIdlingResources(resource);
    }
    @Test
    public void recipeItemClick(){

        onView(withId(R.id.rvRecipe)).perform(actionOnItemViewAtPosition(0, R.id.ivCardRecipe, click()));

        onView(withRecyclerView(R.id.rvRecipeDetails)
                .atPositionOnView(0, R.id.tvIngredientName))
                .check(matches(withText("Graham Cracker crumbs")));
    }

}
