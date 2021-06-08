package ca.nait.fard1.photolocation;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import ca.nait.fard1.photolocation.Entity.Location;
import ca.nait.fard1.photolocation.Manager.PhotoManager;
import ca.nait.fard1.photolocation.Manager.LocationManager;
import ca.nait.fard1.photolocation.Service.GetImagesRunnable;

/**
 * Activity to display images by location.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Spinner spLocations;
    GridView lvPhotos;
    ArrayList<Location> locations;

    ArrayAdapter<Location> locationAdapter;
    LocationManager lm;

    int currentLocationIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lm = new LocationManager();

        lvPhotos = findViewById(R.id.lvPhotos);
        lvPhotos.setOnItemClickListener(this);
        spLocations = findViewById(R.id.spLocationsMain);
        spLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentLocationIndex = position;
                setPhotos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setSpinner();
        setPhotos();

    }

    //Puts photos into GridView on background thread
    public void setPhotos(){
        GetImagesRunnable getImagesRunnable = new GetImagesRunnable();
        getImagesRunnable.setContext(this);
        getImagesRunnable.setLocationId(locations.get(currentLocationIndex).getId());
        getImagesRunnable.setLvPhotos(lvPhotos);
        Thread thread = new Thread(getImagesRunnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //When photo is clicked
    @Override
    public void onItemClick(AdapterView<?> parent, View row, int position, long id) {
        TextView tempId = row.findViewById(R.id.tvPhotoIdRow);
        int photoId = Integer.parseInt(tempId.getText().toString());
        Intent i = new Intent(MainActivity.this, PhotoDetailsActivity.class).putExtra("PhotoId", photoId);
        this.startActivity(i);
    }

    //Set locations spinner
    public void setSpinner(){
        locations = lm.getLocations(this);

        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLocations.setAdapter(locationAdapter);
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_photo) {
            Intent i = new Intent(MainActivity.this, AddPhotosActivity.class);
            this.startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}