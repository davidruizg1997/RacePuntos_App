package com.example.davidruiz.preliminar;

public class ListModelHistorical {
    private String nameService;
    private int points;

    public ListModelHistorical (String nameServiceConstructor,int pointsConstructor){
        this.nameService=nameServiceConstructor;
        this.points=pointsConstructor;
    }

    public String getNameService(){
        return this.nameService;
    }

    public int getPoints(){
        return this.points;
    }
}
