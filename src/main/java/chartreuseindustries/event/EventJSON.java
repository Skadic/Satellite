package chartreuseindustries.event;

import chartreuseindustries.misc.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by eti22 on 04.01.2017.
 */
public class EventJSON {

    private Set<Event> events;
    private LocalDateTime lastUpdated;

    public EventJSON(JSONObject object) {
        if(!object.has("Events") || !object.has("LastUpdated"))
            throw new IllegalArgumentException("JSONObject not a valid EventJSON");
        JSONArray jsonEvents = object.getJSONArray("Events");
        events = new TreeSet<>((e1, e2) -> e1.getID() - e2.getID());
        for (Object jsonEvent : jsonEvents) {
            events.add(new Event((JSONObject) jsonEvent));
        }
        lastUpdated = Utils.getDateTimeFromeTimeStamp(object.getString("LastUpdated"));
    }

    public Set<Event> getEvents() {
        return events;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public Event byID(int ID){
        Optional<Event> e = events.stream().filter(event -> event.getID() == ID).findAny();
        if(e.isPresent())
            return e.get();
        else return null;
    }

    public void createJSON(){
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        events.forEach((event -> array.put(event.asJSON())));

        json.put("Events", array);
        json.put("LastUpdated", lastUpdated);

        Utils.saveFile(json.toString(), Utils.FILE_PLAN);
    }

}
