package ca.nait.fard1.photolocation.Service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.GridView;

import ca.nait.fard1.photolocation.Manager.PhotoManager;
import ca.nait.fard1.photolocation.Manager.PhotoManager.ImageAdapter;

/**
 * Gets and sets compressed images for MainActivity to run on background thread.
 * ex. Thread thread = new Thread(GetImagesRunnable);
 */
public class GetImagesRunnable implements Runnable {

    private PhotoManager pm = null;

    private Context context;
    private int locationId;
    private GridView lvPhotos;

    private ImageAdapter imageAdapter;

    @Override
    public void run() {
        Looper.prepare();
        pm = new PhotoManager();
        imageAdapter = pm.getImageAdapter(locationId, context);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> lvPhotos.setAdapter(imageAdapter));
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public void setLvPhotos(GridView lvPhotos){
        this.lvPhotos = lvPhotos;
    }
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
}
