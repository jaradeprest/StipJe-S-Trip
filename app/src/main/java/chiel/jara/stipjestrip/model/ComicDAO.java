package chiel.jara.stipjestrip.model;

import java.util.ArrayList;

//ToDo Klassenaam aanpassen naar ComicDatasource!
public class ComicDAO {
    private static final ComicDAO ourInstance = new ComicDAO();

    public static ComicDAO getInstance() {
        return ourInstance;
    }

    private ComicDAO() {
    }

    private ArrayList<Comic> comics = new ArrayList<>();

    public ArrayList<Comic> getComics() {
        return comics;
    }

    public void addComic(Comic comic){comics.add(comic);}
}
