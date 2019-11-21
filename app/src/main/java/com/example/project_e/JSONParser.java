package com.example.project_e;

import android.os.Message;
import android.util.JsonReader;
import android.util.JsonToken;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class JSONParser {

    JsonReader readFile;

    public JSONParser(String f){

        try {
            String fpath = "/res/enigma-res/" + f + ".json";
            InputStream inputStreamFile = new FileInputStream(fpath);
            readFile = new JsonReader(new InputStreamReader(inputStreamFile,"UTF-8"));

        }catch (IOException e){
            System.out.println("Can't read file");
        }
    }

    public List<String> getDialog(String key) throws IOException {

        List<String> dialog = new CopyOnWriteArrayList<String>();

        readFile.beginObject();
        while (readFile.hasNext()) {
            String name = readFile.nextName();
            if (name.equals(key) && readFile.peek() != JsonToken.NULL) {
                dialog = readDialog(readFile);
            } else {
                readFile.skipValue();
            }
        }
        readFile.endObject();

        return dialog;
    }


    public List<String> readDialog(JsonReader reader) throws IOException{

         List<String> dialog = new CopyOnWriteArrayList<String>();

        reader.beginArray();
        while (reader.hasNext()){
            dialog.add(reader.nextString());
        }
        reader.endArray();
        return dialog;
    }

    public LatLng getLalng (JsonReader reader) throws IOException {

        double latitude,longitude;

        reader.beginArray();
        reader.hasNext();
        latitude = reader.nextDouble();
        reader.hasNext();
        longitude = reader.nextDouble();
        reader.endArray();

        return new LatLng(latitude,longitude);
    }

    public LatLng getCoordinate(String key) throws IOException {

        LatLng coord = null;

        readFile.beginObject();
        while (readFile.hasNext()) {
            String name = readFile.nextName();
             if (name.equals(key) && readFile.peek() != JsonToken.NULL) {
                coord = getLalng(readFile);
            } else {
                readFile.skipValue();
            }
        }
        readFile.endObject();

        return coord;
    }

    public int getInt(String key) throws IOException {

        int intValue = -1;

        readFile.beginObject();
        while (readFile.hasNext()) {
            String name = readFile.nextName();
            if (name.equals(key)) {
                intValue = readFile.nextInt();
            } else {
                readFile.skipValue();
            }
        }
        readFile.endObject();

        return intValue;
    }

    public String getString(String key) throws IOException {

        String stringValue = null;

        readFile.beginObject();
        while (readFile.hasNext()) {
            String name = readFile.nextName();
            if (name.equals(key)) {
                stringValue = readFile.nextString();
            } else {
                readFile.skipValue();
            }
        }
        readFile.endObject();

        return stringValue;
    }

    public void close(){};

}
