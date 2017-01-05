package chartreuseindustries.event;

import chartreuseindustries.misc.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by eti22 on 05.01.2017.
 */
public class PlanEventJSON {

    Set<PlanEvent> eventsToday, eventsTomorrow;
    private LocalDateTime lastUpdated;

    public PlanEventJSON(JSONObject json, String type) {
        if(!isValidJSON(json, type))
            throw new IllegalArgumentException("JSONObject not a valid PlanEventJSON for type: " + type);

        eventsToday = new TreeSet<>((e1, e2) -> e1.getID() - e2.getID());
        eventsTomorrow = new TreeSet<>((e1, e2) -> e1.getID() - e2.getID());

        if(json.has("Vplansh") || json.has("Vplanlh")) {
            JSONArray arrayToday = json.getJSONArray(getArrayNameForTypeAndDay(type, false));

            for (Object obj : arrayToday) {
                JSONObject o = (JSONObject) obj;
                eventsToday.add(new PlanEvent(o));
            }
        }

        if(json.has("Vplansm") || json.has("Vplanlm")) {
            JSONArray arrayTomorrow = json.getJSONArray(getArrayNameForTypeAndDay(type, true));

            for(Object obj : arrayTomorrow){
                JSONObject o = (JSONObject) obj;
                eventsTomorrow.add(new PlanEvent(o));
            }
        }
        lastUpdated = Utils.getDateTimeFromeTimeStamp(json.getString("LastUpdated"));
    }

    public Set<PlanEvent> getEventsToday(){
        return eventsToday;
    }

    public Set<PlanEvent> getEventsTomorrow() {
        return eventsTomorrow;
    }

    public Set<PlanEvent> getEvents(){
        Set<PlanEvent> temp = new TreeSet<>((e1, e2) -> e1.getID() - e2.getID());
        temp.addAll(eventsToday);
        temp.addAll(eventsTomorrow);
        return temp;
    }

    public PlanEvent todayByID(int ID){
        Optional<PlanEvent> e = eventsToday.stream().filter(event -> event.getID() == ID).findAny();
        if(e.isPresent())
            return e.get();
        else return null;
    }

    public PlanEvent tomorrowByID(int ID){
        Optional<PlanEvent> e = eventsTomorrow.stream().filter(event -> event.getID() == ID).findAny();
        return e.isPresent() ? e.get() : null;
    }

    public boolean hasTodayEvents(){
        return !eventsToday.isEmpty();
    }

    public boolean hasTomorrowEvents(){
        return !eventsTomorrow.isEmpty();
    }

    private boolean isValidJSON(JSONObject json, String type){
        return (type.equals("LEH") ? (json.has("Vplanlh") || json.has("Vplanlm")) :
                type.equals("SCH") ? (json.has("Vplansh") || json.has("Vplansm")) :
                        (json.has("Vplanlh") || json.has("Vplanlm") && json.has("Vplansh") || json.has("Vplansm"))
                                && json.has("LastUpdated"));
    }

    private String getArrayNameForTypeAndDay(String type, boolean tomorrow){
        if(!(type.equals("LEH") || type.equals("SCH") || type.equals("BOTH")))
            throw new IllegalArgumentException("Type not defined.");
        String s = "Vplan";
        if(type.equals("LEH")) s += "l";
        else
        if(type.equals("SCH")) s += "s";
        if(tomorrow) s += "m"; else s += "h";
        return s;
    }
}
