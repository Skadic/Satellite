package chartreuseindustries.event;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eti22 on 05.01.2017.
 */
public class PlanEvent {

    private int ID;
    private String course;
    private int std;
    private String np;
    private String vd;

    public PlanEvent(JSONObject json) {
        this.ID = json.getInt("ID");
        course = json.getString("course");
        std = json.getInt("std");
        np = json.getString("np");
        vd = json.getString("vd");
    }

    public int getID() {
        return ID;
    }

    public String getCourse() {
        return course;
    }

    public int getLesson() {
        return std;
    }

    public String getNp() {
        return np;
    }

    public String getVd() {
        return vd;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + getID() + "){" + '\n' +
                " ID = " + ID + '\n' +
                " course = " + course + '\n' +
                " std = " + std + '\n' +
                " np = " + np + '\n' +
                " vd = " + vd + '\n' +
                "}\n";
    }

    public JSONObject asJSON(){
        JSONObject obj = new JSONObject();

        obj.put("ID", String.valueOf(ID));
        obj.put("course", course);
        obj.put("std", std);
        obj.put("np", np);
        obj.put("vd", vd);

        return obj;
    }

    public List<Object> getData(){
        List<Object> data = new ArrayList<>();
        data.add(ID);
        data.add(course);
        data.add(std);
        data.add(np);
        data.add(vd);
        return data;
    }

}
