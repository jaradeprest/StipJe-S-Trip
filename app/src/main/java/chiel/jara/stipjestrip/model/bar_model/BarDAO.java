package chiel.jara.stipjestrip.model.bar_model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
/**
 * Created By Chiel&Jara 03/2019
 */
@Dao
public interface BarDAO {

    @Insert
    void addBar(Bar bar);
    @Query("select * from Bar")
    List<Bar> getAllBars();
}
