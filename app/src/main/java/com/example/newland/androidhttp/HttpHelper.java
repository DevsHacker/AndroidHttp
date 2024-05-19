package com.example.newland.androidhttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpHelper {
    static String token;
    static String value;

    public static String login(){
        String s = "http://192.168.18.239:81/Users/Login";
        try {
            URL url = new URL(s);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);

            conn.connect();

            OutputStream out = conn.getOutputStream();
            String er = "{\"Account\":\"18306821670\",\"Password\":\"123456789\"}";
            out.write(er.getBytes());
            out.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String msg = "";
            String line = "";
            while((line = reader.readLine())!=null){
                msg = line + "\n";
            }
            System.out.println(msg);
            JSONObject a = new JSONObject(msg);
            JSONObject b = new JSONObject(a.getString("ResultObj"));
            token = b.getString("AccessToken");
            System.out.println(token);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return token;
    }
    public static String get_Data(String ApiTags){
        if(token == null){
            login();
        }
        String s = "http://192.168.18.239:81/devices/147461/datas?apitags="+ApiTags+"&AccessToken="+token;
        try {
            URL url = new URL(s);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);

            conn.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String msg = "";
            String line = "";
            while((line = reader.readLine())!= null){
                msg = line + "\n";
            }
            System.out.println(msg);
            JSONObject a = new JSONObject(msg);
            JSONObject b = new JSONObject(a.getString("ResultObj"));
            JSONArray c = new JSONArray(b.getString("DataPoints"));
            JSONObject d = new JSONObject(c.getString(0));
            JSONArray e = new JSONArray(d.getString("PointDTO"));
            JSONObject f = new JSONObject(e.getString(0));
            value = f.getString("Value");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }
    public static void turn(int deviceId,String apiTag, String q){
        String s = "http://192.168.18.239:81/Cmds"+"?deviceId="+deviceId+"&apiTag="+apiTag+"&AccessToken="+token;
        try {
            URL url = new URL(s);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);

            conn.connect();

            OutputStream out = conn.getOutputStream();
            String er = q;
            out.write(er.getBytes());
            out.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
