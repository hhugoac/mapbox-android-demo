package com.mapbox.mapboxandroiddemo.examples.styles;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatActivity;


import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import timber.log.Timber;


public class MissingIconActivity extends AppCompatActivity {



  private MapView mapView;
  private Style style;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Mapbox access token is configured here. This needs to be called either in your application
    // object or in the same activity which contains the mapview.
    Mapbox.getInstance(this, getString(R.string.access_token));

    // This contains the MapView in XML and needs to be called after the access token is configured.
    setContentView(R.layout.activity_styles_missing_icon);

    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.addOnStyleImageMissingListener(new MapView.OnStyleImageMissingListener() {
      @Override
      public void onStyleImageMissing(@NonNull String id) {
        style.addImageAsync(id, BitmapUtils.getBitmapFromDrawable(
          getResources().getDrawable(R.drawable.map_marker_push_pin_pink)));
      }
    });

    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        try {
          // Switch the map to a style that has no background
          mapboxMap.setStyle(new Style.Builder()
                          .fromJson(readRawResource(MissingIconActivity.this, R.raw.missing_icon)),
            new Style.OnStyleLoaded() {

              @Override
              public void onStyleLoaded(@NonNull Style style) {
                MissingIconActivity.this.style = style;
              }
            });
        } catch (IOException exception) {
          Timber.e(exception);
        }
      }
    });
  }


  // Add the mapView lifecycle to the activity's lifecycle methods
  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  /**
   * Get the map style JSON from the raw file in the app's raw folder
   */
  public static String readRawResource(Context context, @RawRes int rawResource) throws IOException {
    String json = "";
    if (context != null) {
      Writer writer = new StringWriter();
      char[] buffer = new char[1024];
      try (InputStream is = context.getResources().openRawResource(rawResource)) {
        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        int numRead;
        while ((numRead = reader.read(buffer)) != -1) {
          writer.write(buffer, 0, numRead);
        }
      }
      json = writer.toString();
    }
    return json;
  }
}
