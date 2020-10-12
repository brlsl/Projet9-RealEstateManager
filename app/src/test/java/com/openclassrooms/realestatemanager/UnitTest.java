package com.openclassrooms.realestatemanager;


import com.openclassrooms.realestatemanager.utils.ExamUtils;
import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
        float dollars = ExamUtils.convertEuroToDollar(100);
        assertEquals(123, dollars, 0.0);
    }

    @Test
    public void convertDollarToEuro_isCorrect(){
        float euros = ExamUtils.convertDollarToEuro(100);
        assertEquals(81, euros, 0.0);
    }

    @Test
    public void getTodayDate_isCorrect(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        assertEquals(sdf.format(new Date()), ExamUtils.getTodayDate());
    }

    @Test
    public void convertDateToString_shouldWorks() {
        Date date = new Date();
        String todayStr = ExamUtils.getTodayDate();
        assertEquals(todayStr, Utils.formatDateToString(date));
    }

    @Test
    public void convertStringToDate_shouldWorks() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        String todayStr = ExamUtils.getTodayDate();
        Date date = Utils.formatStringToDate(todayStr);
        assertEquals(sdf.format(new Date()), sdf.format(date));
    }

    @Test
    public void twoListsAreSameNoMatterTheOrder_isCorrect(){
        List<String> listOne = Arrays.asList("b","a","c");
        List<String> listTwo= Arrays.asList("c","b","a");
        List<String> listThree = Arrays.asList("c", "b", "a", "c");
        assertNotEquals(listOne, listTwo);
        assertNotEquals(listOne, listThree);
        assertNotEquals(listTwo, listThree);

        // check equality between list order not mattering
        assertTrue(Utils.areSameListNoMatterOrder(listOne,listTwo));
        assertFalse(Utils.areSameListNoMatterOrder(listOne, listThree));
        assertFalse(Utils.areSameListNoMatterOrder(listTwo, listThree));

        // verify originals list are same
        assertTrue(listOne.get(0).equals("b") && listOne.get(1).equals("a") && listOne.get(2).equals("c") && listOne.size() == 3);
        assertTrue(listTwo.get(0).equals("c") && listTwo.get(1).equals("b") && listTwo.get(2).equals("a") && listTwo.size() == 3);
        assertTrue(listThree.get(0).equals("c") && listThree.get(1).equals("b") &&
                listThree.get(2).equals("a") && listThree.get(3).equals("c") && listThree.size() == 4);
    }

}