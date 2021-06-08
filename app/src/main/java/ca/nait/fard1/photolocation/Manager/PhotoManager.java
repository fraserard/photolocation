package ca.nait.fard1.photolocation.Manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.IOException;

import ca.nait.fard1.photolocation.Entity.Photo;
import ca.nait.fard1.photolocation.R;
import ca.nait.fard1.photolocation.Service.MemoryCache;

/**
 *  Manages anything to do with photos.
 */
public class PhotoManager{

    static final String TAG = "PhotoManager";

    static SQLiteDatabase db;
    static DbManager manager;


    //Gets the image adapter for displaying photos in GridView
    public ImageAdapter getImageAdapter(int locationId, Context context){
        manager = DbManager.getInstance(context);
        db = manager.getReadableDatabase();
        String query = "SELECT * FROM " + DbManager.T_PHOTO + " WHERE " + DbManager.C_LOCATION_ID + " = " + locationId;
        Cursor cursor = db.rawQuery(query, null);
        ImageAdapter imageAdapter = new ImageAdapter(context, R.layout.photos_row, cursor, new String[]{DbManager.C_ID, DbManager.C_URI}, new int[]{R.id.tvPhotoIdRow, R.id.ivImageRow});

        return imageAdapter;
    }

    //Get photo details from Db into Photo object
    public Photo getPhotoDetails(int photoId, Context context){
        Photo photo = null;
        manager = DbManager.getInstance(context);
        db = manager.getReadableDatabase();
        String query = "SELECT * FROM " + DbManager.T_PHOTO + " WHERE " + DbManager.C_ID + " = " + photoId;
        Cursor c = db.rawQuery(query, null);
        try {
            if (c.moveToFirst()) { 
                photo = new Photo(
                        c.getInt(c.getColumnIndex(DbManager.C_ID)),
                        c.getInt(c.getColumnIndex(DbManager.C_LOCATION_ID)),
                        c.getString(c.getColumnIndex(DbManager.C_URI)),
                        c.getDouble(c.getColumnIndex(DbManager.C_LAT)),
                        c.getDouble(c.getColumnIndex(DbManager.C_LON)),
                        c.getString(c.getColumnIndex(DbManager.C_FSTOP)),
                        c.getString(c.getColumnIndex(DbManager.C_EXPOSURE_TIME)),
                        c.getDouble(c.getColumnIndex(DbManager.C_ISO)),
                        c.getString(c.getColumnIndex(DbManager.C_FOCAL_LENGTH)),
                        c.getString(c.getColumnIndex(DbManager.C_MAKE)),
                        c.getString(c.getColumnIndex(DbManager.C_MODEL)),
                        c.getString(c.getColumnIndex(DbManager.C_DATE_TAKEN))
                );
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return photo;
    }

    //Add new photo to Db
    public void addPhoto(Photo newPhoto, Context context){
        if (newPhoto == null){
            Toast.makeText(context, "Error: Your photo cannot be empty.", Toast.LENGTH_LONG).show();
        }else {
            manager = DbManager.getInstance(context);
            db = manager.getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put(DbManager.C_LOCATION_ID, newPhoto.getLocationId());
                values.put(DbManager.C_URI, newPhoto.getImageUri());
                values.put(DbManager.C_LAT, newPhoto.getLatitude());
                values.put(DbManager.C_LON, newPhoto.getLongitude());
                values.put(DbManager.C_FSTOP, newPhoto.getFstop());
                values.put(DbManager.C_EXPOSURE_TIME, newPhoto.getExposureTime());
                values.put(DbManager.C_ISO, newPhoto.getIso());
                values.put(DbManager.C_FOCAL_LENGTH, newPhoto.getFocalLength());
                values.put(DbManager.C_MAKE, newPhoto.getMake());
                values.put(DbManager.C_MODEL, newPhoto.getModel());
                values.put(DbManager.C_DATE_TAKEN, newPhoto.getDateTaken());

                db.insertOrThrow(DbManager.T_PHOTO, null, values);
                Toast.makeText(context, "Added new photo!", Toast.LENGTH_SHORT).show();
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
            } finally {
                db.endTransaction();
            }
        }
    }

    //Update photo in Db
    public void updatePhoto(Photo newPhoto, Context context){
        if (newPhoto == null){
            Toast.makeText(context, "Error: Your photo cannot be empty.", Toast.LENGTH_LONG).show();
        }else {
            manager = DbManager.getInstance(context);
            db = manager.getWritableDatabase();

            try {
                ContentValues values = new ContentValues();
                values.put(DbManager.C_LOCATION_ID, newPhoto.getLocationId());
                values.put(DbManager.C_LAT, newPhoto.getLatitude());
                values.put(DbManager.C_LON, newPhoto.getLongitude());
                values.put(DbManager.C_FSTOP, newPhoto.getFstop());
                values.put(DbManager.C_EXPOSURE_TIME, newPhoto.getExposureTime());
                values.put(DbManager.C_ISO, newPhoto.getIso());
                values.put(DbManager.C_FOCAL_LENGTH, newPhoto.getFocalLength());
                values.put(DbManager.C_MAKE, newPhoto.getMake());
                values.put(DbManager.C_MODEL, newPhoto.getModel());
                values.put(DbManager.C_DATE_TAKEN, newPhoto.getDateTaken());
                String id = String.valueOf(newPhoto.getId());
                db.update(DbManager.T_PHOTO, values,"_id = ?", new String[]{id});
                Toast.makeText(context, "Updated photo!", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
            } finally {
                db.close();
            }
        }
    }

    //Delete photo in Db
    public void deletePhoto(int photoId, Context context){
        manager = DbManager.getInstance(context);
        db = manager.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(DbManager.T_PHOTO,DbManager.C_ID + "=" + photoId,null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e, Toast.LENGTH_LONG).show();
        } finally {
            db.endTransaction();
            Toast.makeText(context, "Removed photo successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    //Remove photo from memory cache
    public void releasePhotoFromMemory(String uri){
        MemoryCache m = MemoryCache.getInstance();
        m.removeBitmapFromMemCache(uri);
    }

    //ImageAdapter is a cursor to get images into the GridView in MainActivity
    public class ImageAdapter extends SimpleCursorAdapter {
        MemoryCache memoryCache = MemoryCache.getInstance();
        public ImageAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
            super(context, layout, c, from, to);

        }

        //Scale bitmaps to 20% size
        public Bitmap scaleBitmap(Uri uri, Context context){
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.2), (int)(bitmap.getHeight()*0.2), true);
            cacheImage(String.valueOf(uri), bitmap);
            return bitmap;
        }

        //Add image to cache
        public void cacheImage(String uri, Bitmap bmp){
            memoryCache.addBitmapToMemoryCache(uri, bmp);
        }

        @Override
        public void bindView(View row, Context context, Cursor cursor){
            super.bindView(row, context, cursor);

            try {
                ImageView iv = row.findViewById(R.id.ivImageRow);
                String uriString = cursor.getString(cursor.getColumnIndex(DbManager.C_URI));
                Uri uri = Uri.parse(uriString);

                //Check if image in cache or not
                if(memoryCache.getBitmapFromMemCache(uriString) == null){
                    iv.setImageBitmap(scaleBitmap(uri, context));
                }
                else
                    iv.setImageBitmap(memoryCache.getBitmapFromMemCache(uriString));

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
