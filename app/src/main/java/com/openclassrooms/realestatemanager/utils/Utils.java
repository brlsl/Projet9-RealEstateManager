package com.openclassrooms.realestatemanager.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static String formatDateToString(Date date){
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return sdf.format(date);
    }

    public static Date formatStringToDate(String input) throws ParseException {
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return sdf.parse(input);
    }

    public static String formatPrice(String decimalInput){
        DecimalFormat pattern  = new DecimalFormat("#,###");
        return pattern.format(Integer.parseInt(decimalInput));

    }

    public static boolean areSameListNoMatterOrder(List<String> listOne, List<String> listTwo){
        if (listOne == null && listTwo == null){
            return true;
        }

        if(listOne == null || listTwo == null || listOne.size() != listTwo.size()){
            return false;
        }

        //to avoid messing the order of the original lists, we use a copy
        List<String> copy1, copy2;
        copy1 = new ArrayList<>(listOne);
        copy2 = new ArrayList<>(listTwo);

        Collections.sort(copy1);
        Collections.sort(copy2);
        return copy1.equals(copy2);
    }

}
