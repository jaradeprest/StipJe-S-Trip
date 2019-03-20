package chiel.jara.stipjestrip.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Comic {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name, author, year, imageName;
    private double coordinateLONG, coordinateLAT;

    public Comic() {
    }

    @Ignore
    public Comic(String name, String author, String year, String imageName, double coordinateLONG, double coordinateLAT) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.imageName = imageName;
        this.coordinateLONG = coordinateLONG;
        this.coordinateLAT = coordinateLAT;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Comic{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", year='" + year + '\'' +
                ", imageName='" + imageName + '\'' +
                ", coordinateLONG=" + coordinateLONG +
                ", coordinateLAT=" + coordinateLAT +
                '}';
    }
}
