package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.ExamUtils;
import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    @Test
    public void convertEuroToDollar_isCorrect(){
        float oneDollar = ExamUtils.convertEuroToDollar(100);
        assertEquals(123, oneDollar, 0.0);
    }

    @Test
    public void testIfTwoListsAreSameNoMatterTheOrder_AreEquals(){
        List<String> listOne = Arrays.asList("b","a","c");
        List<String> listTwo= Arrays.asList("c","b","a");
        List<String> listThree = Arrays.asList("c", "b", "a", "c");
        assertNotEquals(listOne, listTwo);
        assertNotEquals(listOne, listThree);
        assertNotEquals(listTwo, listThree);

        // check equality between list order not mattering
        assertTrue(Utils.areSameListOrderNoMatterOrder(listOne,listTwo));
        assertFalse(Utils.areSameListOrderNoMatterOrder(listOne, listThree));
        assertFalse(Utils.areSameListOrderNoMatterOrder(listTwo, listThree));

        // verify originals list are same
        assertTrue(listOne.get(0).equals("b") && listOne.get(1).equals("a") && listOne.get(2).equals("c") && listOne.size() == 3);
        assertTrue(listTwo.get(0).equals("c") && listTwo.get(1).equals("b") && listTwo.get(2).equals("a") && listTwo.size() == 3);
        assertTrue(listThree.get(0).equals("c") && listThree.get(1).equals("b") &&
                listThree.get(2).equals("a") && listThree.get(3).equals("c") && listThree.size() == 4);
    }

    @Test
    public void testIfListContainsElementsAnotherList(){
        List<String> listOne = Arrays.asList("b", "a","c");
        List<String> listTwo= Arrays.asList("a","c"); // filter

        for (int i = 0; i <listOne.size() ; i++) {
            assertTrue(listOne.containsAll(listTwo));
            assertFalse(listTwo.containsAll(listOne));
        }

    }
}