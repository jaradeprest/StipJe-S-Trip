package chiel.jara.stipjestrip.model.comic_model;

import java.util.ArrayList;

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
}
