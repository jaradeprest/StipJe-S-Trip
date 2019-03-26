package chiel.jara.stipjestrip.model.bar_model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

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
