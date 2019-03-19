package chiel.jara.stipjestrip.model;

public class Comic {

    private String name, author, year, imageName;
    private float coordinateLONG, coordinateLAT;

    public Comic() {
    }

    public Comic(String name, String author, String year, String imageName, float coordinateLONG, float coordinateLAT) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.imageName = imageName;
        this.coordinateLONG = coordinateLONG;
        this.coordinateLAT = coordinateLAT;
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

    public float getCoordinateLONG() {
        return coordinateLONG;
    }

    public void setCoordinateLONG(float coordinateLONG) {
        this.coordinateLONG = coordinateLONG;
    }

    public float getCoordinateLAT() {
        return coordinateLAT;
    }

    public void setCoordinateLAT(float coordinateLAT) {
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
