package ca.nait.fard1.photolocation.Manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import ca.nait.fard1.photolocation.Entity.Location;

/**
 *  Manages anything to do with locations.
 */
public class LocationManager {
    static SQLiteDatabase db;
    static DbManager manager;

    //Get locations from Db into Location object
    public ArrayList<Location> getLocations(Context context){
        ArrayList<Location> locations = new ArrayList<>();
        manager = DbManager.getInstance(context);
        db = manager.getReadableDatabase();

        String query = "SELECT * FROM " + DbManager.T_LOCATION + " ORDER BY " + DbManager.C_ID + " desc";
        Cursor cursor = db.rawQuery(query, null);

        int locId;
        String locName;
        try {
            if (cursor.moveToFirst()) {
                do {
                    locId = cursor.getInt(cursor.getColumnIndex(DbManager.C_ID));
                    locName = cursor.getString(cursor.getColumnIndex(DbManager.C_LOCATION_NAME));
                    Location locTemp = new Location(locId, locName);
                    locations.add(locTemp);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return locations;
    }

    //Add location to Db
    public void addLocation(String newLocation, Context context){
        if (newLocation.isEmpty()){
            Toast.makeText(context, "Error: Your location name cannot be empty.", Toast.LENGTH_LONG).show();
        }else {
            manager = DbManager.getInstance(context);
            db = manager.getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put(DbManager.C_LOCATION_NAME, newLocation);
                db.insertOrThrow(DbManager.T_LOCATION, null, values);
                Toast.makeText(context, "Added location: " + newLocation + ".", Toast.LENGTH_SHORT).show();
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
            } finally {
                db.endTransaction();
            }
        }

    }

    //Remove location from Db
    public void removeLocation(Location loc, Context context){
        manager = DbManager.getInstance(context);
        db = manager.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(DbManager.T_LOCATION,DbManager.C_ID + "=" + loc.getId(),null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
        } finally {
            db.endTransaction();
            Toast.makeText(context, "Removed location: " + loc.getLocationName() + ".", Toast.LENGTH_SHORT).show();
        }
    }


// TODO IMPLEMENT GetLocationRunnable

//    public void setSpinner(ArrayList<>){
//        manager = DbManager.getInstance(context);
//        db = manager.getReadableDatabase();
//
//        locations = getLocations(this);
//
//        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
//        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spLocations.setAdapter(locationAdapter);
//    }

}
