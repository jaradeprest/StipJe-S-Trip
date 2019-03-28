package chiel.jara.stipjestrip.model.bar_model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
/**
 * Created By Chiel&Jara 03/2019
 */
@Database(entities = {Bar.class}, version = 1, exportSchema = false)
public abstract class BarDatabase extends RoomDatabase {

    private static BarDatabase instance;

    public static BarDatabase getInstance(Context context){
        if (instance == null){
            instance=createDatabase(context);
        }
        return instance;
    }

    private static BarDatabase createDatabase(Context context) {
        return Room.databaseBuilder(context, BarDatabase.class, "Bars.db").allowMainThreadQueries().build();
    }

    public abstract BarDAO getMethodsBar();
}

//TODO SAMENVOEGEN IN 1 DATABASE / 1 DAO (meerdere entities in 1 database)
