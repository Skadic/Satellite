package chartreuseindustries.main;

import chartreuseindustries.event.PlanEventJSON;
import chartreuseindustries.misc.Downloader;
import chartreuseindustries.misc.Utils;

public class Main {

    public static void main(String[] args){
        Utils.createFiles();

        String type = "SCH";
        Downloader down = new Downloader(Downloader.URL_SERVER,
                "timestamp;" + Utils.createTimeStamp(),//"1337-12-24 13:33:37",
                "auth;TestPasswort1",
                "type;" + type
        );

        down.setOnDownloadListener(json -> {
            PlanEventJSON eventJSON = new PlanEventJSON(json, type);

            System.out.println("Today Events");
            eventJSON.getEventsToday().forEach(System.out::println);

            System.out.println("Tomorrow Events");
            eventJSON.getEventsTomorrow().forEach(System.out::println);

            down.softShutdown(true);
        });
        down.start();
    }


}
