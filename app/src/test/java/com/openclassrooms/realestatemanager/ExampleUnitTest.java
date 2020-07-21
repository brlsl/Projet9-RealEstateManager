package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void convertEuroToDollar_isCorrect(){
        float oneDollar = Utils.convertEuroToDollar(100);
        assertEquals(123, oneDollar, 0.0);
    }
}