package chiel.jara.stipjestrip.model.bar_model;

import java.util.ArrayList;

public class BarDatasource {
    private static final BarDatasource ourInstance = new BarDatasource();

    public static BarDatasource getInstance(){return ourInstance;}

    private BarDatasource(){
    }

    private ArrayList<Bar> bars = new ArrayList<>();

    public ArrayList<Bar> getBars() {return bars;}

    public void addBar(Bar bar){bars.add(bar);}
}
