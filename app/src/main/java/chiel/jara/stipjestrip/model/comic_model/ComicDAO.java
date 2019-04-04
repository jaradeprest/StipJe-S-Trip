package chiel.jara.stipjestrip.model.comic_model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import chiel.jara.stipjestrip.model.bar_model.Bar;

/**
 * Created By Chiel&Jara 03/2019
 */

@Dao
public interface ComicDAO {

    //Data toevoegen
    @Insert
    void addComic(Comic comic);

    @Insert
    void addBar(Bar bar);

    @Delete
    void removeComic(Comic comic);

    //Alle data opvragen
    @Query("select * from Comic")
    List<Comic> getAllComics();

    @Query("select * from Comic where isFavorite = 1")
    List<Comic> getFavComics();

    @Query("select * from Bar")
    List<Bar> getAllBars();

    //date updaten
    @Update
    void updateComic (Comic... comic);

    @Update
    void updateBar(Bar... bar);
}
