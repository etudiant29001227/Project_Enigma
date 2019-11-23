package com.example.project_e;


import android.util.JsonReader;
import android.util.JsonToken;
import com.google.android.gms.maps.model.LatLng;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class JSONParser {

    InputStream readFile;

    public JSONParser(InputStream file){

        //String fpath = "../res/enigma-res/" + file + ".json";
        //InputStream inputStreamFile = new FileInputStream(fpath);
        readFile = new PushbackInputStream(file);
        readFile.mark(1024*1024);

    }

    private JsonReader openJson() throws IOException{
        readFile.reset();
        return new JsonReader(new InputStreamReader(readFile,"UTF-8"));

    }


    public List<String> getDialog(String key) throws IOException {

        JsonReader reader = openJson();
        System.out.println("apres openfile");

        List<String> dialog = new CopyOnWriteArrayList<>();
        if(reader != null)System.out.println("avant begin");
        reader.beginObject();
        System.out.println("apres  begin");
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(key) && reader.peek() != JsonToken.NULL) {
                dialog = readDialog(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        System.out.println("apres end");
        reader.close();
        return dialog;
    }


    public List<String> readDialog(JsonReader reader) throws IOException{

         List<String> dialog = new CopyOnWriteArrayList<>();

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

        JsonReader reader = openJson();

        LatLng coord = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
             if (name.equals(key) && reader.peek() != JsonToken.NULL) {
                coord = getLalng(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        reader.close();
        return coord;
    }

    public int getInt(String key) throws IOException {

        JsonReader reader = openJson();
        int intValue = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(key)) {
                intValue = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        reader.close();
        return intValue;
    }

    public String getString(String key) throws IOException {

        JsonReader reader = openJson();
        String stringValue = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(key)) {
                stringValue = reader.nextString();
            } else {
                reader.skipValue();
            }
}
        reader.endObject();
        reader.close();
        return stringValue;
    }

}
