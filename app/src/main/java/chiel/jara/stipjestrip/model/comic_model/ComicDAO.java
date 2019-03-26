package chiel.jara.stipjestrip.model.comic_model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created By Chiel&Jara 03/2019
 */

@Dao
public interface ComicDAO {

    //Data toevoegen
    @Insert
    void addComic(Comic comic);

    //Alle data opvragen
    @Query("select * from Comic")
    List<Comic> getAllComics();
}
