package ca.nait.fard1.photolocation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ca.nait.fard1.photolocation.Entity.Location;
import ca.nait.fard1.photolocation.Entity.Photo;
import ca.nait.fard1.photolocation.Manager.LocationManager;
import ca.nait.fard1.photolocation.Manager.PhotoManager;

/**
 * Activity to display details of a single photo selected from MainActivity.
 */
public class PhotoDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    Photo photo;
    ArrayList<Location> locations;
    ArrayAdapter<Location> locationAdapter;
    LocationManager lm;
    PhotoManager pm;

    Spinner spLocations;
    ImageButton bRefreshSpinner;
    ImageView ivImage;
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
    Button updatePhoto;
    Button deletePhoto;

    int id;
    int currentLocationIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        //If no photo passed in go back to MainActivity
        Intent intent = getIntent();
        id = intent.getIntExtra("PhotoId", -1);
        if(id == -1){
            Intent i = new Intent(this, MainActivity.class);
            this.startActivity(i);
        }

        lm = new LocationManager();
        pm = new PhotoManager();

        photo = pm.getPhotoDetails(id, this);

        ivImage = findViewById(R.id.ivNewImage);

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

        updatePhoto = findViewById(R.id.bUpdatePhoto);
        updatePhoto.setOnClickListener(this);
        deletePhoto = findViewById(R.id.bDeletePhoto);
        deletePhoto.setOnClickListener(this);

        setSpinner();
        setFields();
    }

    //Set photo details
    private void setFields(){
        photo = pm.getPhotoDetails(id, this);
        ivImage.setImageURI(Uri.parse(photo.getImageUri()));
        etLatitude.setText(photo.getLatitude().toString());
        etLongitude.setText(photo.getLongitude().toString());
        etDateTaken.setText(photo.getDateTaken());
        etFstop.setText(photo.getFstop());
        etFocalLength.setText(photo.getFocalLength());
        etExposureTime.setText(photo.getExposureTime());
        if(photo.getIso().toString() == null)
            etIso.setText("");
        else
            etIso.setText(photo.getIso().toString());
        etMake.setText(photo.getMake());
        etModel.setText(photo.getModel());

        for(int i = 0; i < spLocations.getCount(); i++){
            Location loc = (Location) spLocations.getItemAtPosition(i);
            if(loc.getId() == photo.getLocationId()){
                spLocations.setSelection(i);
                break;
            }
        }
    }

    //Open coordinates in Google Maps
    private void openInMaps(){
        Double lat = Double.parseDouble(etLatitude.getText().toString());
        Double lon = Double.parseDouble(etLongitude.getText().toString());
        if(lat == null || lon == null){
            Toast.makeText(this, "Error: Latitude and Longitude must be set.", Toast.LENGTH_SHORT).show();
        }else{
            Uri intentUri = Uri.parse("geo:0,0?q="+lat+","+lon);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        }
    }

    //Set locations spinner
    public void setSpinner(){
        locations = lm.getLocations(this);

        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLocations.setAdapter(locationAdapter);
    }

    //Update photo
    public void update(){
        try{
            Photo photo = new Photo();
            photo.setId(id);
            photo.setLocationId(locations.get(currentLocationIndex).getId());
            photo.setLatitude(Double.valueOf(etLatitude.getText().toString()));
            photo.setLongitude(Double.valueOf(etLongitude.getText().toString()));
            photo.setFstop(etFstop.getText().toString());
            photo.setExposureTime(etExposureTime.getText().toString());
            photo.setIso(Double.valueOf(etIso.getText().toString()));
            photo.setFocalLength(etFocalLength.getText().toString());
            photo.setMake(etMake.getText().toString());
            photo.setModel(etModel.getText().toString());
            photo.setDateTaken(etDateTaken.getText().toString());

            pm.updatePhoto(photo, this);
            setSpinner();
            setFields();
        }catch(Exception e){
            Toast.makeText(this, "Update Error: " + e, Toast.LENGTH_LONG).show();
        }
    }

    //Delete photo
    public void delete(){
        try{
            AlertDialog alert = new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to delete this photo?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        pm.deletePhoto(id, this);
                        pm.releasePhotoFromMemory(photo.getImageUri());
                        Intent in = new Intent(this, MainActivity.class);
                        this.startActivity(in);
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> {
                    })
                    .show();

        }catch (Exception e){
            Toast.makeText(this, "Delete Error: " + e, Toast.LENGTH_LONG).show();
        }

    }

    //Button clicks
    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if(vid == R.id.bOpenInMapsAdd){
            openInMaps();
        }else if(vid == R.id.refreshSpinner){
            setSpinner();
        }else if(vid == R.id.bUpdatePhoto){
            update();
        }else if(vid == R.id.bDeletePhoto){
            delete();
        }
    }

}