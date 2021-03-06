package chiel.jara.stipjestrip.model.comic_model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created By Chiel&Jara 03/2019
 */

@Entity
public class Comic implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    private String imgID;
    private String name, author, year, imageName;
    private double coordinateLONG, coordinateLAT;
    private String URLimg;
    private boolean isFavorite=false;
    private boolean visited=false;

    public Comic() {
    }

    @Ignore
    public Comic(String name, String author, String year, String imageName, String imgID, double coordinateLONG, double coordinateLAT) {
        this.imgID = imgID;
        this.name = name;
        this.author = author;
        this.year = year;
        this.imageName = imageName;
        this.coordinateLONG = coordinateLONG;
        this.coordinateLAT = coordinateLAT;
    }

    //method to sort by a-z
    public static final Comparator<Comic> BY_NAME_ALPHABETICAL = new Comparator<Comic>() {
        @Override
        public int compare(Comic comic1, Comic comic2) {
            return comic1.name.compareTo(comic2.name);
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public double getCoordinateLONG() {
        return coordinateLONG;
    }

    public void setCoordinateLONG(double coordinateLONG) {
        this.coordinateLONG = coordinateLONG;
    }

    public double getCoordinateLAT() {
        return coordinateLAT;
    }

    public void setCoordinateLAT(double coordinateLAT) {
        this.coordinateLAT = coordinateLAT;
    }

    public String getURLimg() {
        return URLimg;
    }

    public void setURLimg(String URLimg) {
        this.URLimg = URLimg;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public String toString() {
        return "Comic{" +
                "id=" + id +
                ", imgID='" + imgID + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", year='" + year + '\'' +
                ", imageName='" + imageName + '\'' +
                ", coordinateLONG=" + coordinateLONG +
                ", coordinateLAT=" + coordinateLAT +
                ", URLimg='" + URLimg + '\'' +
                ", isFavorite=" + isFavorite +
                ", visited=" + visited +
                '}';
    }
}

//DOCUMENTATION: All comics : https://bruxellesdata.opendatasoft.com/explore/dataset/comic-book-route/api/?q=bar
//DOCUMENTATION: How to save files on device's storage : https://developer.android.com/training/data-storage/files
