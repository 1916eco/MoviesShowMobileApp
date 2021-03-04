package uk.ac.rgu.showlist.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import uk.ac.rgu.showlist.Show;

@Dao
public interface ShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Show show);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertShows(Show... show);

    @Query("SELECT * from show WHERE listName like :listNamePassed ORDER BY name ASC ")
    public List<Show> getAllWatchedShows(String listNamePassed);

    @Query("SELECT * from show WHERE (name like :searchForName) and (listName like :listNamePassed) ORDER BY name ASC ")
    public List<Show> findShowByName(String searchForName, String listNamePassed);

    @Delete
    public void delete(Show show);

    @Query("DELETE FROM show")
    public void deleteAll();

    @Query("DELETE FROM show WHERE listName like :listNamePassed")
    public void deleteAllbyList(String listNamePassed);

    @Query("DELETE FROM show WHERE (name like :searchForName) and (listName like :listNamePassed)")
    public void deleteByName(String searchForName, String listNamePassed);

    @Query("SELECT COUNT(name) FROM show WHERE listName like 'seenList'")
    public int getCountAll();
}
