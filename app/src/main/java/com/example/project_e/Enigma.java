package com.example.project_e;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class Enigma {

    private int nbChapter,currentChapter,chapterFinish,radius,currentDialog;
    private List<String> chapterDialog,help;
    private LatLng target;
    private String file;
    

    public Enigma(String file){

        this.file = file;
        JSONParser jsonFile = new JSONParser(file);

        try {

            nbChapter = jsonFile.getInt("nbChapter");
            chapterDialog = jsonFile.getDialog("Dialog_Chapter_"+currentChapter);
            help = jsonFile.getDialog("Help_Chapter_"+currentChapter);
            target = jsonFile.getCoordinate("Target_Chapter_"+currentChapter);
            radius = jsonFile.getInt("Radius_Chapter_"+currentChapter);

        } catch (IOException e) {
            e.printStackTrace();
        }

        currentChapter = 1;
        chapterFinish = 0;
        currentDialog = 0;
    }

    public int getNbChapter() {
        return nbChapter;
    }

    public int getCurrentChapter() {
        return currentChapter;
    }


    public List<String> getChapterDialog() {
        return chapterDialog;
    }

    public List<String> getHelp() {
        return help;
    }


    public LatLng getTarget() {
        return target;
    }

    public int getRadius() {
        return radius;
    }

    public boolean isWin(){
        return !(chapterFinish<nbChapter);
    }

    private void nextChapter(){

        JSONParser jsonFile = new JSONParser(file);

            try {

                chapterDialog = jsonFile.getDialog("Dialog_Chapter_"+currentChapter);
                help = jsonFile.getDialog("Help_Chapter_"+currentChapter);
                target = jsonFile.getCoordinate("Target_Chapter_"+currentChapter);
                radius = jsonFile.getInt("Radius_Chapter_"+currentChapter);

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void nextStep(){

        currentChapter++;
        chapterFinish++;

        if(!isWin()){
            nextChapter();
        }
    }

    public String getAllDialog(){

        String allDialog = "";

        for(int i = 0; i< currentDialog;i++){

            allDialog+= chapterDialog.get(i)+"\n\n";

        }

        return allDialog;
    }

}
