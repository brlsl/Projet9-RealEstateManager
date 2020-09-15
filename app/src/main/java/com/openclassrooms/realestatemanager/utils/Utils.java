package com.openclassrooms.realestatemanager.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String formatDateToString(Date date){
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return sdf.format(date);
    }

    public static String formatPrice(String decimalInput){
        DecimalFormat pattern  = new DecimalFormat("#,###");
        return pattern.format(Integer.parseInt(decimalInput));

    }
}
