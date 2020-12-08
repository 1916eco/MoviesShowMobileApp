package uk.ac.rgu.showlist;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "show")
public class Show {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int uid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    String name;
    String overview;
    Double voteAvg;
    @ColumnInfo(name = "ShowID")
    Integer id;
    String firstAirDate;
    String posterImage;
    String backdropImage;

    public Show(String name, String overview, Double voteAvg, Integer id, String firstAirDate, String posterImage, String backdropImage) {
        this.name = name;
        this.overview = overview;
        this.voteAvg = voteAvg;
        this.id = id;
        this.firstAirDate = firstAirDate;
        this.posterImage = posterImage;
        this.backdropImage = backdropImage;
    }

    public Show() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(Double voteAvg) {
        this.voteAvg = voteAvg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public String getBackdropImage() {
        return backdropImage;
    }

    public void setBackdropImage(String backdropImage) {
        this.backdropImage = backdropImage;
    }
}
