package com.example.a321projectprototype;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.a321projectprototype.User.IdentifiedBirdModel;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Before


    @Test
    public void rewardModel_isCorrect() {
        assertEquals(5, new IdentifiedBirdModel().getData("iJr5I4zgaGQFWcY4eUiPHQTfuDA2"));
    }
}