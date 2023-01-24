package com.example.apptracker_v1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class CheckSite {
    public static boolean isConnect(String site_url, int timeout) throws IOException {
        site_url.replace("https", "http");

        HttpURLConnection connection = (HttpURLConnection) new URL(site_url).openConnection();
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        connection.setRequestMethod("HEAD");
        int responseCode = connection.getResponseCode();

        if (responseCode != 200)
            return false;
        return true;
    }
}
