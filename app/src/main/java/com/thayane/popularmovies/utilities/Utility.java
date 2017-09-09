package com.thayane.popularmovies.utilities;

import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Thayane on 13/08/2017.
 */

public class Utility {
    public static String formatDate(String dateToFormat){
        SimpleDateFormat originalSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObject = null;
        try{
            dateObject = originalSimpleDateFormat.parse(dateToFormat);
        }catch (ParseException e){
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        return dateFormat.format(dateObject);
    }

    public static String buildImageUrl(String imagePath) {
        String imageUrl = "http://image.tmdb.org/t/p/";
        imageUrl += "w500" + imagePath;
        return imageUrl;
    }
}
