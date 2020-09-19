package com.openclassrooms.realestatemanager.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
}
