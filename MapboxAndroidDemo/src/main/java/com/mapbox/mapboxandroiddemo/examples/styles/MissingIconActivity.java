package com.mapbox.mapboxandroiddemo.examples.styles;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxandroiddemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import timber.log.Timber;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

/**
 * Thisan example for handling missing image in Style.
 * <p>
 * If an icon-image cannot be found in a map Style, a custom image could be provided via
 * onStyleImageMissing listener
 */
public class MissingIconActivity extends AppCompatActivity {

  private static final String ICON_SOURCE_ID = "ICON_SOURCE_ID";
  private static final String ICON_LAYER_ID = "ICON_LAYER_ID";
  private static final String PROFILE_NAME = "PROFILE_NAME";
  private static final String CARLOS = "CARLOS";
  private static final String ANTONY = "ANTONY";
  private static final String MARIA = "MARIA";
  private static final String LUCIANA = "LUCIANA";
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
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(@NonNull final MapboxMap mapboxMap) {

        Feature carlosFeature = Feature.fromGeometry(Point.fromLngLat(-7.976074218749999,
            41.27780646738183));
        carlosFeature.addStringProperty(PROFILE_NAME, CARLOS);

        Feature antonyFeature = Feature.fromGeometry(Point.fromLngLat(-8.06396484375,
            37.54457732085582));
        antonyFeature.addStringProperty(PROFILE_NAME, ANTONY);

        Feature mariaFeature = Feature.fromGeometry(Point.fromLngLat(-9.184570312499998,
            38.976492485539396));
        mariaFeature.addStringProperty(PROFILE_NAME, MARIA);

        Feature lucianaFeature = Feature.fromGeometry(Point.fromLngLat(-7.5146484375,
            40.245991504199026));
        lucianaFeature.addStringProperty(PROFILE_NAME, LUCIANA);

        mapboxMap.setStyle(
            new Style.Builder().fromUrl(Style.LIGHT)
                .withSource(new GeoJsonSource(ICON_SOURCE_ID,
                    FeatureCollection.fromFeatures(new Feature[]{
                        carlosFeature,
                        antonyFeature,
                        mariaFeature,
                        lucianaFeature}))),
            new Style.OnStyleLoaded() {
              @Override
              public void onStyleLoaded(@NonNull Style style) {
                MissingIconActivity.this.style = style;
                style.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                    iconImage(get(PROFILE_NAME)),
                    iconIgnorePlacement(true),
                    iconAllowOverlap(true)
                ));

                mapView.addOnStyleImageMissingListener(new MapView.OnStyleImageMissingListener() {
                  @Override
                  public void onStyleImageMissing(@NonNull String id) {
                    switch (id) {
                      case CARLOS:
                        addImage(id, R.drawable.carlos);
                        break;
                      case ANTONY:
                        addImage(id, R.drawable.antony);
                        break;
                      case MARIA:
                        addImage(id, R.drawable.maria);
                        break;
                      case LUCIANA:
                        addImage(id, R.drawable.luciana);
                        break;
                    }
                  }
                });
              }
            });
      }
    });
  }

  private void addImage(String id, int drawableImage) {
    style.addImageAsync(id + "_PROFILE_PHOTO", BitmapUtils.getBitmapFromDrawable(
        getResources().getDrawable(drawableImage)));
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
