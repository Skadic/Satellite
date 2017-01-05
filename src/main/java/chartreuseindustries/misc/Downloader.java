package chartreuseindustries.misc;

import chartreuseindustries.listener.DownloadListener;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Downloader {
//index2_3.php
    public static final String URL_SERVER = "http://test-68482.alfa3044.alfahosting-server.de/index2_3.php";
    public static final String URL_PASTEBIN = "http://pastebin.com/raw/NEMFnqYr";

    private ExecutorService service;
    private Future<?> future;

    private List<DownloadListener> listeners;

    private String url;
    private HttpEntity entity;

    public Downloader(String url, String... params) {
        this.service = Executors.newSingleThreadExecutor();
        this.url = url;
        this.listeners = new ArrayList<>();

        extractParams(params);
    }

    public void setOnDownloadListener(DownloadListener listener){
        listeners.add(listener);
    }

    public Future<?> getFuture(){
        return future;
    }

    public boolean isDone() {
        return future != null && future.isDone();
    }

    private JSONObject readJsonFromUrl() throws IOException {
        InputStream is = entity.getContent();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        StringBuilder sb = new StringBuilder();

        int cp;
        while ((cp = br.read()) != -1)
            sb.append((char) cp);
        System.out.println("sb.toString() = " + sb.toString());
        is.close();

        return new JSONObject(sb.toString());
    }

    public void start(){
        if(!service.isShutdown()) {
            softShutdown(false);
            service = Executors.newSingleThreadScheduledExecutor();
        }
        future = service.submit(() -> {
            JSONObject json;
            try {
                json = readJsonFromUrl();
                for (DownloadListener l : listeners) {
                    l.onFinish(json);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            softShutdown(false);
        });
    }

    private void extractParams(String[] params){
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> pars = new ArrayList<>();
            for (String param : params) {
                pars.add(new BasicNameValuePair(param.split(";")[0], param.split(";")[1]));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pars, "UTF-8"));

            HttpResponse response = httpClient.execute(httpPost);
            entity = response.getEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void softShutdown(boolean debug){
        try{
            if(debug)System.out.println(MessageFormat.format("Attempting to shutdown [{0}]", service.toString()));
            service.shutdown();
            if(debug)System.out.println("Awaiting shutdown...");
            service.awaitTermination(3, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            if(debug)System.out.println("Thread interrupted.");
        }finally {
            if(debug)
            if(!service.isTerminated())
                System.out.println(MessageFormat.format("Now shutting down [{0}]", service.toString()));
            else
                System.out.println(MessageFormat.format("Shutdown of [{0}] successful", service.toString()));
            service.shutdownNow();
        }
    }
}
