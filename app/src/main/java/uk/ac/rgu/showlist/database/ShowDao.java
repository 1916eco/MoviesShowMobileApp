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

    @Insert
    public void insert(Show show);

    @Insert
    public void insertShows(Show... show);

    @Query("SELECT * from show ORDER BY name ASC ")
    public List<Show> getAllShows();

    @Query("SELECT * from show WHERE name like :searchForName ORDER BY name ASC ")
    public List<Show> findShowByName(String searchForName);

    @Delete
    public void delete(Show show);

    @Query("DELETE FROM show")
    public void deleteAll();
}
