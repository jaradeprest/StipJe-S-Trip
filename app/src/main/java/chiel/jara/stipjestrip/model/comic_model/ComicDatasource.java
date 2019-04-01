package chiel.jara.stipjestrip.model.comic_model;

import java.util.ArrayList;

import chiel.jara.stipjestrip.model.bar_model.Bar;

/**
 * Created By Chiel&Jara 03/2019
 */

public class ComicDatasource {
    private static final ComicDatasource ourInstance = new ComicDatasource();

    public static ComicDatasource getInstance() {
        return ourInstance;
    }

    private ComicDatasource() {
    }

    private ArrayList<Comic> comics = new ArrayList<>();

    public ArrayList<Comic> getComics() {
        return comics;
    }

    public void addComic(Comic comic){
        comics.add(comic);
    }

    private ArrayList<Bar> bars = new ArrayList<>();

    public ArrayList<Bar> getBars() {return bars;}

    public void addBar(Bar bar){bars.add(bar);}

}
