package com.erebsindia.foodle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.erebsindia.foodle.adaptors.MainCategoryAdaptor;
import com.erebsindia.foodle.libraries.JRequest;
import com.erebsindia.foodle.models.MainCategoryModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.aviran.cookiebar2.CookieBar;
import org.imaginativeworld.whynotimagecarousel.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.CarouselType;
import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    ProgressBar progressLoc;
    protected LocationManager mLocationManager;
    protected Context context;
    int LOCATION_REFRESH_TIME = 10000; // 15 seconds to update
    int LOCATION_REFRESH_DISTANCE = 10; // 500 meters to update
    TextView locationText;
    SharedPreferences sharedPreferences;
    double lan,log;

    List<MainCategoryModel> mainCategoryModels = new ArrayList<>();
    private RecyclerView MainCatRecycleView;
    private MainCategoryAdaptor mainCategoryAdaptor;

    List<CarouselItem> carouselImage1 = new ArrayList<>();
    ImageCarousel carousel1;
    List<CarouselItem> carouselImage2 = new ArrayList<>();
    ImageCarousel carousel2;
    ShimmerFrameLayout locationShimmer;
    GoogleMap googleMap;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        locationText = findViewById(R.id.locationText);
        progressLoc = findViewById(R.id.progressLoc);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        sharedPreferences = getSharedPreferences("NISC", MODE_PRIVATE);
        locationShimmer = findViewById(R.id.locationShimmer);



//        progressLoc.setVisibility(View.VISIBLE);
        if(checkLocationPermission())
        {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
        }

        MainCatRecycleView = findViewById(R.id.MainCatRecycleView);
        this.mainCategoryAdaptor = new MainCategoryAdaptor(this);
        MainCatRecycleView.setAdapter(mainCategoryAdaptor);
        GridLayoutManager mGridFP = new GridLayoutManager(getApplicationContext(), 4);
        MainCatRecycleView.setLayoutManager(mGridFP);

        //ImageCarousel1
        carousel1  = findViewById(R.id.carousel1);
        carousel1.setCarouselType(CarouselType.SHOWCASE);
        carousel1.setShowNavigationButtons(false);
        carousel1.setScaleOnScroll(false);
        carousel1.setItemLayout(R.layout.custom_fixed_size_item_layout);
        carousel1.setImageViewId(R.id.image_view);

        //ImageCarousel2
        carousel2  = findViewById(R.id.carousel2);
        carousel2.setCarouselType(CarouselType.SHOWCASE);
        carousel2.setShowNavigationButtons(false);
        carousel2.setScaleOnScroll(false);
        carousel2.setItemLayout(R.layout.custom_fixed_size_item_layout);
        carousel2.setImageViewId(R.id.image_view);

        HomeApi(sharedPreferences.getString("pincode", ""));
        locationText.setText(sharedPreferences.getString("address", ""));


    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here

            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                locationText.setText(address);
                locationShimmer.showShimmer(false);
                if(postalCode.equalsIgnoreCase(sharedPreferences.getString("pincode", "")))
                {}else{
                HomeApi(postalCode);}

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("pincode", postalCode);
                editor.putString("address", address);
                editor.apply();

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Test")
                        .setMessage("Tessst")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME,
                                LOCATION_REFRESH_DISTANCE, mLocationListener);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    public void goBack(View view)
    {
        super.onBackPressed();
    }

    public void goSearch(View view)
    {
        Intent intent;
        intent = new Intent(getApplicationContext(),SearchActivity.class);
        startActivity(intent);
    }

    public void HomeApi(String Pincode)
    {
        JRequest jRequest;
        findViewById(R.id.mainLoader).setVisibility(View.VISIBLE);
        try {
            JSONObject RequestJson = new JSONObject();
            RequestJson.put("user_id", sharedPreferences.getString("id", ""));
            RequestJson.put("limit", "10");
            RequestJson.put("pincode", Pincode);
            //RequestJson.put("access_token", sharedPreferences.getString("token", ""));

            jRequest = new JRequest(RequestJson, "home", true, this, new JRequest.TaskCompleteListener(){
                @Override
                public void onTaskComplete(String result) throws JSONException
                {

                    try
                    {
                        JSONObject Res = new JSONObject(result);
                        String sts = Res.getString("sts");
                        String msg = Res.getString("msg");

                        if (sts.equalsIgnoreCase("01"))
                        {
//                            String restaurantsObjs = Res.getString("restaurants");
//                            JSONObject restaurantsObj = new JSONObject(restaurantsObjs);
                            findViewById(R.id.mainLoader).setVisibility(View.GONE);

                            //Categories
                            String categoriesObjs = Res.getString("categories");
                            JSONArray categoriesObj = new JSONArray(categoriesObjs);

                            mainCategoryModels.clear();
                            int disCnt = categoriesObj.length()>=8?8:categoriesObj.length();
                            for (int i = 0; i < disCnt; i++)
                            {
                                String categoriesArray = categoriesObj.getString(i);
                                JSONObject categoriesObject = new JSONObject(categoriesArray);
                                MainCategoryModel mainCategoryModel = new MainCategoryModel();
                                mainCategoryModel.setId(categoriesObject.getString("id"));
                                mainCategoryModel.setName(categoriesObject.getString("name"));
                                mainCategoryModel.setImage(getString(R.string.site_url)+categoriesObject.getString("image"));
                                mainCategoryModel.setType(categoriesObject.getString("type"));
                                mainCategoryModels.add(mainCategoryModel);
                            }
                            mainCategoryAdaptor.renewItems(mainCategoryModels);

                            //Sliders1
                            String slidersArrays1 = Res.getString("fbanners");
                            JSONArray slidersArray1 = new JSONArray(slidersArrays1);

                            carouselImage1.clear();
                            for (int i = 0; i < slidersArray1.length(); i++)
                            {
                                String SliderObjectString1 = slidersArray1.getString(i);
                                JSONObject SliderObject1 = new JSONObject(SliderObjectString1);
                                String sliderImageUrl1 = getString(R.string.site_url) + SliderObject1.getString("image");
                                carouselImage1.add(new CarouselItem(sliderImageUrl1));
                            }
                            carousel1.addData(carouselImage1);
                            carousel1.setAutoPlay(true);

                            //Sliders2
                            String slidersArrays2 = Res.getString("sbanners");
                            JSONArray slidersArray2 = new JSONArray(slidersArrays2);

                            carouselImage2.clear();
                            for (int i = 0; i < slidersArray2.length(); i++)
                            {
                                String SliderObjectString2 = slidersArray2.getString(i);
                                JSONObject SliderObject2 = new JSONObject(SliderObjectString2);
                                String sliderImageUrl2 = getString(R.string.site_url) + SliderObject2.getString("image");
                                carouselImage2.add(new CarouselItem(sliderImageUrl2));
                            }
                            carousel2.addData(carouselImage2);
                            carousel2.setAutoPlay(true);


                        }

                        else
                        {
                            //showMsg(msg);
                        }


                    }
                    catch (Exception e)
                    { Log.e("catcherror", e + "d"); }
                }
            });
            jRequest.execute();
        }
        catch (JSONException e)
        { e.printStackTrace(); }
    }

    public void showMsg(String msg)
    {
        CookieBar.build(this)
                .setMessage(msg)
                .setCustomView(R.layout.ebs_toast).setCookiePosition(CookieBar.BOTTOM)
                .setDuration(5000).show();
    }

    public void OpenLocView(View view)
    {
        findViewById(R.id.locView).setVisibility(View.VISIBLE);
    }
    public void CloseLocView(View view)
    {
        findViewById(R.id.locView).setVisibility(View.GONE);
    }




}