package uk.ac.rgu.showlist.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import uk.ac.rgu.showlist.Show;

@Database(entities = {Show.class}, version = 1)
public abstract class SeenDatabase extends RoomDatabase {

    public abstract ShowDao showDao();

    public static SeenDatabase INSTANCE;

    public static SeenDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (SeenDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SeenDatabase.class,"seen_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()//NOT RECOMMENDED needs to be on different thread
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
