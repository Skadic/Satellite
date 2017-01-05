package chartreuseindustries.listener;

import org.json.JSONObject;

/**
 * Created by eti22 on 04.01.2017.
 */
@FunctionalInterface
public interface DownloadListener {

    void onFinish(JSONObject json);

    default void onInterrupt(){

    }

}
