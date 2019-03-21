package chiel.jara.stipjestrip.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Comic {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    private String imgID;
    private String name, author, year, imageName;
    private double coordinateLONG, coordinateLAT;
    private String URLimg;

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
        this.URLimg = URLimg;
    }


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

    @Override
    public String toString() {
        return "Comic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", year='" + year + '\'' +
                ", imageName='" + imageName + '\'' +
                ", coordinateLONG=" + coordinateLONG +
                ", coordinateLAT=" + coordinateLAT +
                ", URLimg='" + URLimg + '\'' +
                '}';
    }
}
