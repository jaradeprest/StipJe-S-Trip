package chiel.jara.stipjestrip.model.comic_model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created By Chiel&Jara 03/2019
 */

@Database(entities = {Comic.class}, version = 1, exportSchema = false)
public abstract class ComicDatabase extends RoomDatabase {

    private static ComicDatabase instance;

    public static ComicDatabase getInstance(Context context) {
        //Wanneer de instantie nog niet bestaat wordt er een nieuwe database gecreëerd (File)
        if (instance == null) {
            instance = createDatabase(context);
        }
        //anders wordt het opgeslagen bij de bestaande database
        return instance;
    }

    //Maken van een nieuwe database
    private static ComicDatabase createDatabase(Context context) {
        return Room.databaseBuilder(context,ComicDatabase.class,"Comics.db").allowMainThreadQueries().build();
    }

    //Abstracte klasse
    //Van hier uit kan je de methodes aan spreken
    public abstract ComicDAO getMethodsComic();
}
