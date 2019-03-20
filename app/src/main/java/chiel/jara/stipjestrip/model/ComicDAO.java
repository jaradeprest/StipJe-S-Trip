package chiel.jara.stipjestrip.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ComicDAO {

    //Data toevoegen
    @Insert
    void addComic(Comic comic);

    //Alle data opvragen
    @Query("select * from Comic")
    List<Comic> getAllComics();
}
