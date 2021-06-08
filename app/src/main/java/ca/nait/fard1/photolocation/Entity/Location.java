package ca.nait.fard1.photolocation.Entity;

/**
 *  Location entity.
 */
public class Location {
    int id;
    String locationName;

    public Location(int id, String locationName) {
        this.id = id;
        this.locationName = locationName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    //Override toString() to return locationName instead of Object
    @Override
    public String toString() {
        return locationName;
    }

    public int toInt(){return id;}
}
