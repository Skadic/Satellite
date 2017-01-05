package chartreuseindustries.event;

import chartreuseindustries.misc.Utils;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by eti22 on 04.01.2017.
 */
public class Event {

    private int ID;
    private String type;
    private LocalDateTime ts;
    private LocalDateTime start, en;
    private String loc;
    private String title;
    private String descr;
    private List<String> teach;
    private List<String> courses;
    private String ann;
    private String oth;

    Event(JSONObject obj) {
        this.ID = Integer.valueOf(obj.getString("ID"));
        this.type = obj.getString("type").replace("/", "").replace("\\", "");
        if(obj.has("ts"))this.ts = Utils.getDateTimeFromeTimeStamp(obj.getString("ts"));
        if(obj.has("start"))this.start = Utils.getDateTimeFromeTimeStamp(obj.getString("start"));
        if(obj.has("en"))this.en = Utils.getDateTimeFromeTimeStamp(obj.getString("en"));
        this.loc = obj.getString("loc").replace("/", "").replace("\\", "");
        this.title = obj.getString("title");
        this.descr = obj.getString("descr");
        this.teach = new ArrayList<>(Arrays.asList(obj.getString("teach").split("/")));
        for (String s : teach) {
            s.replace("\\", "");
            s.replace("/", "");
        }
        this.teach.remove("");
        this.courses = new ArrayList<>(Arrays.asList(obj.getString("courses").split("/")));
        for (String s : courses) {
            s.replace("\\", "");
            s.replace("/", "");
        }
        this.courses.remove("");
        this.ann = obj.getString("ann");
        this.oth = obj.getString("oth");
    }

    @Override
    public String toString() {
        return "Event(" + title + "){\n" +
                " ID = " + ID + '\n' +
                " type = " + type + '\n' +
                " ts = " + Utils.getTimeStampFromDateTime(ts) +
                " ,start = " + Utils.getTimeStampFromDateTime(start) +
                " ,en = " + Utils.getTimeStampFromDateTime(en) + '\n' +
                " loc = " + loc + '\n' +
                " title = " + title + '\n' +
                " descr = " + descr + '\n' +
                " teach = " + teach +
                " courses = " + courses + '\n' +
                " ann = " + ann + '\n' +
                " oth = " + oth + '\n' +
                '}' + "\n\n";
    }

    public JSONObject asJSON(){
        JSONObject obj = new JSONObject();

        obj.put("ID", String.valueOf(ID));
        obj.put("type", type);
        if(ts != null)obj.put("ts", Utils.getTimeStampFromDateTime(ts));
        if(start != null)obj.put("start", Utils.getTimeStampFromDateTime(start));
        if(en != null)obj.put("en", Utils.getTimeStampFromDateTime(en));
        obj.put("loc", loc);
        obj.put("title", title);
        obj.put("descr", descr);
        obj.put("teach", String.join(";", teach));
        obj.put("courses", String.join(";", courses));
        obj.put("ann", ann);
        obj.put("oth", oth);

        return obj;
    }

    public int getID() {
        return ID;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getTimeStamp() {
        return ts;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return en;
    }

    public String getLocation() {
        return loc;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return descr;
    }

    public List<String> getTeachers() {
        return teach;
    }

    public List<String> getCourses() {
        return courses;
    }

    public String getAnnotations() {
        return ann;
    }

    public String getOther() {
        return oth;
    }

    public List<Object> getData(){
        List<Object> data = new ArrayList<>();
        data.add(ID);
        data.add(type);
        data.add(Utils.getTimeStampFromDateTime(ts));
        data.add(Utils.getTimeStampFromDateTime(start));
        data.add(Utils.getTimeStampFromDateTime(en));
        data.add(loc);
        data.add(title);
        data.add(descr);
        data.add(teach.toString().substring(1, teach.toString().length()-1));
        data.add(courses.toString().substring(1, courses.toString().length()-1));
        data.add(ann);
        data.add(oth);
        return data;
    }
}
