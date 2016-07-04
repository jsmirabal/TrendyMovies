package japps.trendymovies.utilities;

import android.content.res.Resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Julio on 10/2/2016.
 */
public class Utils {

    public static String getYear(String dateStr){
        try {
            Date date = new SimpleDateFormat("yyyy-mm-dd").parse(dateStr);
            return new SimpleDateFormat("yyyy").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String createStatisticsRow (String date, int duration, double rate){
        return getYear(date)+" | "+duration+" min | "+rate+"/10";
    }

    public static String getLocale(){
        return Resources.getSystem().getConfiguration().locale.toString().substring(0,2);
    }
}
