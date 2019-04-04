package chiel.jara.stipjestrip.model.bar_model;

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
public class Bar implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;
    private String name;
    private String street;
    private String houseNumber;
    private String postalcode;
    private String city;
    private String phone;
    private String website;
    private String description;
    private boolean rated=false;
    private int rating;
    private boolean visited=false;

    public Bar() {
    }

    @Ignore
    public Bar(String name, String street, String houseNumber, String postalcode, String city, String phone, String website, String description) {
        this.name = name;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postalcode = postalcode;
        this.city = city;
        this.phone = phone;
        this.website = website;
        this.description = description;
    }

    //method to sort by a-z
    public static final Comparator<Bar> BY_NAME_ALPHABETICAL = new Comparator<Bar>() {
        @Override
        public int compare(Bar o1, Bar o2) {
            return o1.name.compareTo(o2.name);
        }
    };

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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", postalcode='" + postalcode + '\'' +
                ", city='" + city + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", description='" + description + '\'' +
                ", rated=" + rated +
                ", rating=" + rating +
                ", visited=" + visited +
                '}';
    }
}
//DOCUMENTATION: All bars : https://opendata.visitflanders.org/tourist/reca/beer_bars.json   https://opendata.visitflanders.org/tourist/reca/beer_bars

