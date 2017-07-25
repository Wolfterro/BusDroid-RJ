/*
The MIT License (MIT)

Copyright (c) 2017 Wolfgang Almeida

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package io.github.wolfterro.busdroidrj;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // Propriedades da Activity
    // ========================
    private GoogleMap mMap;

    private String busLine = "";
    private ArrayList<String> results = new ArrayList<String>();
    private LatLng busOrderLocation = null;
    private String route = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent i = getIntent();
        busLine = i.getStringExtra("BUSLINE");
        results = i.getStringArrayListExtra("RESULTS");
        route = i.getStringExtra("ROUTE");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Determinando a localização do usuário se houver permissão
        // ---------------------------------------------------------
        if(ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        // Adicionando marcadores customizados no mapa
        // -------------------------------------------
        for(int x = 0; x < results.size(); x++) {
            Random random = new Random();   // So much random...

            String[] info = results.get(x).split(" -//- ");
            String ll = info[0];
            String order = info[1];
            String lastUpdate = info[2];
            String speed = info[3];

            double lat = Double.parseDouble(ll.split(",")[0]);
            double lng = Double.parseDouble(ll.split(",")[1]);

            LatLng loc = new LatLng(lat, lng);

            int selectedColor = random.nextInt(360);

            MarkerOptions options = new MarkerOptions();
            options.position(loc);
            options.title(String.format("%s - %s", busLine, order));
            options.snippet(String.format("%s: %s\n%s: %s\n%s: %s %s",
                    getString(R.string.route),
                    route,
                    getString(R.string.lastUpdate),
                    lastUpdate,
                    getString(R.string.speed),
                    speed,
                    getString(R.string.kmh)));
            options.icon(BitmapDescriptorFactory.defaultMarker(selectedColor));

            Marker marker = mMap.addMarker(options);

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View contentView = getLayoutInflater()
                            .inflate(R.layout.custom_markerinfo, null);

                    TextView tvTitle = (TextView)contentView.findViewById(R.id.titleInfoWindow);
                    TextView tvSnippet = (TextView)contentView.findViewById(R.id.snippetInfoWindow);

                    tvTitle.setText(marker.getTitle());
                    tvSnippet.setText(marker.getSnippet());

                    return contentView;
                }
            });

            busOrderLocation = loc;
        }

        if(results.size() == 1) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busOrderLocation, 16.0f));
        }
        else {
            LatLng rio = new LatLng(-22.95, -43.40);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rio, 10.0f));
        }
    }
}
