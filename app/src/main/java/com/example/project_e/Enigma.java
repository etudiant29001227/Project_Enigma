package com.example.project_e;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Enigma {

    private int nbChapter,currentChapter,chapterFinish,radius,currentDialog,currentHelp;
    private List<String> chapterDialog,help;
    private LatLng target;
    private InputStream file;
    

    public Enigma(InputStream file){

        this.file = file;

        if(file == null)System.out.println("enigma is null");

        currentChapter = 1;
        chapterFinish = 0;
        currentDialog = 0;
        currentHelp = 0;
        try {
            JSONParser jsonFile = new JSONParser(file);
            nbChapter = jsonFile.getInt("nbChapter");
            System.out.println("apres getInt");
            chapterDialog = jsonFile.getDialog("Dialog_Chapter_"+currentChapter);
            System.out.println("apres getFialog 1");
            help = jsonFile.getDialog("Help_Chapter_"+currentChapter);
            System.out.println("apres getFialog 2");
            target = jsonFile.getCoordinate("Target_Chapter_"+currentChapter);
            System.out.println("apres getCoordinate");

        } catch (IOException e) {
            System.out.println("class enigma");
            e.printStackTrace();
        }
    }

    public int getNbChapter() {
        return nbChapter;
    }

    public int getCurrentChapter() {
        return currentChapter;
    }


    public String getCurrentChapterDialog() {

        String currentDialogString = chapterDialog.get(currentDialog);

        if(currentDialog<chapterDialog.size()-1){
            currentDialog++;
        }else{
            currentDialog = 0;
        }

        return currentDialogString;
    }

    public String getHelp() {

        String currentHelpString="";
        System.out.println(help);
//
//        if(currentHelp<help.size()-1){
//            currentHelpString = help.get(currentHelp);
//            currentHelp++;
//        }else{
//            currentHelp = 0;
//        }

        return currentHelpString;
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
