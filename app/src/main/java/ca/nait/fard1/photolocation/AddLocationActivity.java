package ca.nait.fard1.photolocation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import ca.nait.fard1.photolocation.Entity.Location;
import ca.nait.fard1.photolocation.Manager.LocationManager;

/**
 *  Activity to add/delete locations.
 */
public class AddLocationActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etAddLocation;
    Button bAddLocation;
    Spinner spLocations;
    Button bRemoveLocation;

    ArrayList<Location> locations;

    ArrayAdapter<Location> locationAdapter;
    LocationManager lm;
    int currentLocationIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        etAddLocation = findViewById(R.id.etAddLocation);
        bAddLocation = findViewById(R.id.bAddLocation);
        spLocations = findViewById(R.id.spLocationsRemove);
        bRemoveLocation = findViewById(R.id.bRemoveLocation);

        bAddLocation.setOnClickListener(this);
        bRemoveLocation.setOnClickListener(this);

        lm = new LocationManager();

        spLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentLocationIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setSpinner();

    }

    //Set locations spinner
    public void setSpinner(){
        locations = lm.getLocations(this);

        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLocations.setAdapter(locationAdapter);
    }

    //Add or delete locations
    @Override
    public void onClick(View v) {

        int id = v.getId();
        if(id == R.id.bAddLocation){
            lm.addLocation(etAddLocation.getText().toString(), this);
            etAddLocation.setText("");
            setSpinner();
        }else if(id == R.id.bRemoveLocation){
            AlertDialog alert = new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to delete location: " + locations.get(currentLocationIndex).getLocationName() + "? " +
                            "This will also delete all photos associated with the location.") //(It doesn't)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        lm.removeLocation(locations.get(currentLocationIndex), this);
                        setSpinner();
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> {
                    })
                    .show();
        }
        else{

        }
    }


}