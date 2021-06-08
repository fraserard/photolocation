package ca.nait.fard1.photolocation.Entity;

/**
 *  Photo entity.
 */
public class Photo {
    int id;
    int locationId;
    String imageUri;
    Double latitude;
    Double longitude;
    String fstop;
    String exposureTime;
    Double iso;
    String focalLength;
    String make;
    String model;
    String dateTaken;

    public Photo(int id, int locationId, String imageUri, Double latitude, Double longitude, String fstop, String exposureTime, Double iso, String focalLength, String make, String model, String dateTaken) {
        this.id = id;
        this.locationId = locationId;
        this.imageUri = imageUri;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fstop = fstop;
        this.exposureTime = exposureTime;
        this.iso = iso;
        this.focalLength = focalLength;
        this.make = make;
        this.model = model;
        this.dateTaken = dateTaken;
    }

    public Photo() {
    }

    public Photo(int photoId, String imageUri) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getFstop() {
        return fstop;
    }

    public void setFstop(String fstop) {
        this.fstop = fstop;
    }

    public String getExposureTime() {
        return exposureTime;
    }

    public void setExposureTime(String exposureTime) {
        this.exposureTime = exposureTime;
    }

    public Double getIso() {
        return iso;
    }

    public void setIso(Double iso) {
        this.iso = iso;
    }

    public String getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }
}
