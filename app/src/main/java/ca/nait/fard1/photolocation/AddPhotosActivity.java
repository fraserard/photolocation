package ca.nait.fard1.photolocation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ca.nait.fard1.photolocation.Entity.Location;
import ca.nait.fard1.photolocation.Entity.Photo;
import ca.nait.fard1.photolocation.Manager.LocationManager;
import ca.nait.fard1.photolocation.Manager.PhotoManager;

/**
 * Activity to add photo and photo details
 */
public class AddPhotosActivity extends AppCompatActivity implements View.OnClickListener{

    File imageFile;
    ArrayList<Location> locations;
    ArrayAdapter<Location> locationAdapter;
    LocationManager lm;
    PhotoManager pm;
    ExifInterface exif;

    Spinner spLocations;
    ImageButton bRefreshSpinner;
    ImageView ivNewImage;
    Button bSelectPhoto;
    EditText etLatitude;
    EditText etLongitude;
    Button bOpenInMaps;
    EditText etDateTaken;
    EditText etFstop;
    EditText etExposureTime;
    EditText etIso;
    EditText etFocalLength;
    EditText etMake;
    EditText etModel;
    Button savePhoto;

    String imageUri;
    Double lat;
    Double lon;
    String fstop;
    String exposureTime;
    Double parsedExposureTime;
    String iso;
    String focalLength;
    Double parsedFocalLength;
    String make;
    String model;
    String dateTaken;

    int currentLocationIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);

        lm = new LocationManager();
        pm = new PhotoManager();

        ivNewImage = findViewById(R.id.ivNewImage);
        bSelectPhoto = findViewById(R.id.bSelectPhoto);
        bSelectPhoto.setOnClickListener(this);
        bRefreshSpinner = findViewById(R.id.refreshSpinner);
        bRefreshSpinner.setOnClickListener(this);
        spLocations = findViewById(R.id.spLocationsPhoto);
        spLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentLocationIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        etLatitude = findViewById(R.id.etLatitudeAdd);
        etLongitude = findViewById(R.id.etLongitudeAdd);
        bOpenInMaps = findViewById(R.id.bOpenInMapsAdd);
        bOpenInMaps.setOnClickListener(this);
        etDateTaken = findViewById(R.id.etDateAdd);
        etFstop = findViewById(R.id.etFstopAdd);
        etIso = findViewById(R.id.etIsoAdd);
        etExposureTime = findViewById(R.id.etExposureAdd);
        etFocalLength = findViewById(R.id.etFocalLengthAdd);
        etMake = findViewById(R.id.etMakeAdd);
        etModel = findViewById(R.id.etModelAdd);

        savePhoto = findViewById(R.id.bAddPhoto);
        savePhoto.setOnClickListener(this);

        setSpinner();
    }

    //Set locations spinner
    public void setSpinner(){
        locations = lm.getLocations(this);

        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLocations.setAdapter(locationAdapter);
    }

    //Scrape exif data into variables
    private void scrapeData(){
        try {
            exif = new ExifInterface(imageFile);
        } catch (IOException e) {
            Toast.makeText(this, "Scrape Error: " + e, Toast.LENGTH_LONG).show();
        }
        double[] coordArray = exif.getLatLong();
        lat = coordArray[0];
        lon = coordArray[1];

        fstop = exif.getAttribute(ExifInterface.TAG_F_NUMBER);
        if (fstop == null)
            fstop = "";

        exposureTime = exif.getAttribute(ExifInterface.TAG_SHUTTER_SPEED_VALUE);
        try{
            String num = exposureTime.split("/")[1];
            Double pNum = Double.parseDouble(num);
            exposureTime = exposureTime.split("/")[0];
            parsedExposureTime = Double.parseDouble(exposureTime);
            parsedExposureTime = Math.pow(2, -parsedExposureTime / pNum);
            exposureTime = parsedExposureTime + "s";
        }catch (Exception e){
            exposureTime = "";
        }

        iso = exif.getAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY);
        if (iso == null)
            iso = "";

        focalLength = exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
        try{
            focalLength = focalLength.split("/")[0];
            parsedFocalLength = Double.parseDouble(focalLength);
            parsedFocalLength /= 1000;
            focalLength = parsedFocalLength +"mm";
        }catch (Exception e){
            focalLength = "";
        }

        make = exif.getAttribute(ExifInterface.TAG_MAKE);
        if (make == null)
            make = "";
        model = exif.getAttribute(ExifInterface.TAG_MODEL);
        if (model == null)
            model = "";
        dateTaken = exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
        if (dateTaken == null)
            dateTaken = "";

        setFields();
    }

    //Set text fields
    private void setFields(){
        etLatitude.setText(lat.toString());
        etLongitude.setText(lon.toString());
        etDateTaken.setText(dateTaken);
        etFstop.setText(fstop);
        etFocalLength.setText(focalLength);
        etExposureTime.setText(exposureTime);
        etIso.setText(iso);
        etMake.setText(make);
        etModel.setText(model);
    }

    //Save new image
    private void save(){
        try{
            Photo photo = new Photo();
            photo.setLocationId(locations.get(currentLocationIndex).getId());
            photo.setImageUri(imageUri);
            photo.setLatitude(Double.valueOf(etLatitude.getText().toString()));
            photo.setLongitude(Double.valueOf(etLongitude.getText().toString()));
            photo.setFstop(etFstop.getText().toString());
            photo.setExposureTime(etExposureTime.getText().toString());
            if(etIso.getText().toString().isEmpty())
                photo.setIso(null);
            else
                photo.setIso(Double.valueOf(etIso.getText().toString()));
            photo.setFocalLength(etFocalLength.getText().toString());
            photo.setMake(etMake.getText().toString());
            photo.setModel(etModel.getText().toString());
            photo.setDateTaken(etDateTaken.getText().toString());

            pm.addPhoto(photo, this);

            Intent i = new Intent(this, MainActivity.class);
            this.startActivity(i);
        }catch(Exception e){
            Toast.makeText(this, "Save Error: " + e, Toast.LENGTH_LONG).show();
        }

    }

    //Open coordinates in Google Maps
    private void openInMaps(){
        if(lat == null || lon == null){
            Toast.makeText(this, "Error: Latitude and Longitude must be set.", Toast.LENGTH_LONG).show();
        }else{
            Uri intentUri = Uri.parse("geo:0,0?q="+lat+","+lon);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        }
    }

    //Opens the image picker
    private void OpenImagePicker(){
        ImagePicker.Companion.with(this).galleryOnly().start();
    }

    //After image selected set ImageView, call ScrapeData()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            imageFile = ImagePicker.Companion.getFile(data);
            imageUri = data.toURI();
            ivNewImage.setImageURI(Uri.parse(imageUri));
            scrapeData();
        }else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, "Error: " + data, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    //Button clicks
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.bSelectPhoto){
            OpenImagePicker();
        }else if(id == R.id.bOpenInMapsAdd){
            openInMaps();
        }else if(id == R.id.refreshSpinner){
            setSpinner();
        }else if(id == R.id.bAddPhoto){
            save();
        }
    }

    //Menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_location) {
            Intent i = new Intent(this, AddLocationActivity.class);
            this.startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}