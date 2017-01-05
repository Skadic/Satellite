package chartreuseindustries.misc;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

/**
 * Created by eti22 on 04.01.2017.
 */
public class Utils {

    public static final String FILE_PLAN = getPathInWorkingDir("/jsons/plan.json");

    public static String getPathInWorkingDir(String path){
        return System.getProperty("user.dir") + path;
    }

    public static String createTimeStamp(){
        LocalDateTime dt = LocalDateTime.now(ZoneOffset.UTC);
        return String.format("%d-%02d-%02d %02d:%02d:%02d", dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(), dt.getHour(), dt.getMinute(), dt.getSecond());
    }

    public static String getTimeStampFromDateTime(LocalDateTime dt){
        return String.format("%d-%02d-%02d %02d:%02d:%02d", dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(), dt.getHour(), dt.getMinute(), dt.getSecond());
    }

    public static LocalDateTime getDateTimeFromeTimeStamp(String ts){
        String[] dateStrings = ts.split(" ")[0].split("-");
        String[] timeStrings = ts.split(" ")[1].split(":");
        LocalDate date = LocalDate.of(Integer.valueOf(dateStrings[0]), Integer.valueOf(dateStrings[1]), Integer.valueOf(dateStrings[2]));
        LocalTime time = LocalTime.of(Integer.valueOf(timeStrings[0]), Integer.valueOf(timeStrings[1]), Integer.valueOf(timeStrings[2]));
        return LocalDateTime.of(date, time);
    }

    public static void saveFile(String content, String path){
        try (PrintWriter out = new PrintWriter(path)){
            out.println(content);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFiles(){
        File jsons = new File(Utils.getPathInWorkingDir("/jsons"));
        if(!jsons.exists())
            if(jsons.mkdirs())
                try {
                    System.out.println(jsons.getCanonicalPath() + " has been created successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }
}
