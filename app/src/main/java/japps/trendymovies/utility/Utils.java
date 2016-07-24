package japps.trendymovies.utility;

import android.content.res.Resources;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Julio on 10/2/2016.
 */
public class Utils {

    public static String getYear(String dateStr){
        try {
            Date date = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(dateStr);
            return new SimpleDateFormat("yyyy", Locale.getDefault()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static String createStatisticsRow (String date, int duration, double rate){
        return getYear(date)+" | "+duration+" min | "+rate+"/10";
    }

    public static String getLocale(){
        return Resources.getSystem().getConfiguration().locale.toString().substring(0,2);
    }

    public static String formatGenres(ArrayList<String> genresList){
        String stringGenres = "-";
        if (genresList != null && !genresList.isEmpty()){
            stringGenres = genresList.toString().replaceAll("[\\[&\\]]", "");
        }
        return stringGenres;
    }

    public static String formatDate (String strDate){
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(strDate);
            return capitalize(DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public static String formatPopularity(double popularity){
        return String.format(Locale.getDefault(), "%.1f", popularity);
    }

    public static String formatRuntime (int runtime){
        if (runtime == 0) {
            return "-";
        }
        if (runtime >= 60 & runtime <= 119){
            if (runtime == 60){
                return "1h";
            }
            return "1h "+ (runtime - 60) + "m";
        }

        if (runtime >= 120 & runtime <= 179){
            if (runtime == 120){
                return "2h";
            }
            return "2h "+ (runtime - 120) + "m";
        }

        if (runtime >= 180 & runtime <= 239){
            if (runtime == 180){
                return "3h";
            }
            return "3h "+ (runtime - 180) + "m";
        }

        return Integer.toString(runtime);
    }

    public static String formatLanguage (String langCode){
        return capitalize(new Locale(langCode).getDisplayLanguage());
    }

    public static String capitalize (String text){
        if (!text.isEmpty()){
            return text.substring(0,1).toUpperCase()+text.substring(1);
        }
        return text;
    }

    public static String formatCurrency(long value){
        if (value == 0){
            return "-";
        }
        return NumberFormat.getCurrencyInstance().format(value);
    }

}
