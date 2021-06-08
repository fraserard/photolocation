# PhotoLocation App


## Notable features:
- Scrape photo exif data into database object
- View, Edit, Update photo exif data
- Compress images to 20% sized bitmap to increase GridView performance
- Cache, retrieve, and clear images into memory
- Open coordinates in Google maps


## Todo:
- SetSpinner() to move into LocationManager (low)
- Display images in RecyclerView instead of GridView (med)
- All database actions on separate thread (low)
- Change delete locations query to actually delete associated photos 
 & clear from cache (low)
- Suggest location based on coordinates (Maps API) (high)
- Make pretty 

## Using:
```
implementation 'androidx.exifinterface:exifinterface:1.3.1'
implementation 'com.github.dhaval2404:imagepicker:1.8'
```

Fraser Ard