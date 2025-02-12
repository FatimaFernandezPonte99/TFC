package com.example.bookbuddy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RequestQueue requestQueue;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        //Inicializamos la cola de solicitudes
        requestQueue = Volley.newRequestQueue(requireContext());

        //Inicializamos el proveedor de ubicación fusionada
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        //Obtenemos el fragmento del mapa y registramos el callback de la lista de espera
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Agregamos botones para acercar y alejar el mapa
        Button zoomInButton = requireView().findViewById(R.id.zoom_in);
        Button zoomOutButton = requireView().findViewById(R.id.zoom_out);

        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

        //MARCADORES

        //Marcador de San Andrés
        LatLng puestoSanAndres = new LatLng(43.37016866345176, -8.402021949408889);
        mMap.addMarker(new MarkerOptions().position(puestoSanAndres).title("Puesto San Andrés"));
        //Movemos la cámara a ese puesto con un determinado Zoom para que se vea bien
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puestoSanAndres, 13));

        //Marcador Calle Real
        LatLng puestoCalleReal = new LatLng(43.3697877752539, -8.40060283308214);
        mMap.addMarker(new MarkerOptions().position(puestoCalleReal).title("Puesto Calle Real"));

        //Marcador Los Mallos
        LatLng puestoLosMallos = new LatLng(43.355602096687505, -8.412293388163485);
        mMap.addMarker(new MarkerOptions().position(puestoLosMallos).title("Puesto Los Mallos"));

        //Marcador Elviña
        LatLng puestoElvina = new LatLng(43.33506899798543, -8.403907938536882);
        mMap.addMarker(new MarkerOptions().position(puestoElvina).title("Puesto Elviña"));

        //Marcador Monte Alto
        LatLng puestoMonteAlto = new LatLng(43.37822902582594, -8.402120918844597);
        mMap.addMarker(new MarkerOptions().position(puestoMonteAlto).title("Puesto Monte Alto"));

        //Marcador Espacio Coruña
        LatLng puestoEspacioCoruna = new LatLng(43.33661672298876, -8.410880690011624);
        mMap.addMarker(new MarkerOptions().position(puestoEspacioCoruna).title("Puesto Espacio Coruña"));

        //Marcador Matogrande
        LatLng puestoMatogrande = new LatLng(43.34084962067369, -8.404143732340769);
        mMap.addMarker(new MarkerOptions().position(puestoMatogrande).title("Puesto Matogrande"));

        //Comprobamos si se han concedido los permisos de ubicación
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }

        mMap.setMyLocationEnabled(true);

        //Obtenemos la última ubicación conocida
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        lastKnownLocation = location;
                        //Toast.makeText(requireContext(), location.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        //Agregamos un listener para los clics en los marcadores
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Verificamos si el marcador tiene un título asignado
                if (marker.getTitle() != null) {
                    //Verificamos si el título del marcador es el adecuado
                    if (marker.getTitle().equals("Puesto San Andrés")) {
                        //Mostramos la información del puesto
                        marker.setSnippet("Puesto San Andrés\n"+getAddressFromLatLng(marker.getPosition()));
                        marker.showInfoWindow();
                    } else if (marker.getTitle().equals("Puesto Calle Real")) {
                        //Mostramos la información del puesto
                        marker.setSnippet("Puesto Calle Real\n"+getAddressFromLatLng(marker.getPosition()));
                        marker.showInfoWindow();
                    } else if (marker.getTitle().equals("Puesto Los Mallos")) {
                        marker.setSnippet("Puesto Los Mallos\n"+getAddressFromLatLng(marker.getPosition()));
                        marker.showInfoWindow();
                    } else if (marker.getTitle().equals("Puesto Elviña")) {
                        marker.setSnippet("Puesto Elviña\n"+getAddressFromLatLng(marker.getPosition()));
                        marker.showInfoWindow();
                    } else if (marker.getTitle().equals("Puesto Monte Alto")) {
                        marker.setSnippet("Puesto Monte Alto\n"+getAddressFromLatLng(marker.getPosition()));
                        marker.showInfoWindow();
                    } else if (marker.getTitle().equals("Puesto Espacio Coruña")) {
                        marker.setSnippet("Puesto Espacio Coruña\n"+getAddressFromLatLng(marker.getPosition()));
                        marker.showInfoWindow();
                    } else if (marker.getTitle().equals("Puesto Matogrande")) {
                        marker.setSnippet("Puesto Matogrande\n"+getAddressFromLatLng(marker.getPosition()));
                        marker.showInfoWindow();
                    }
                }
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(@NonNull Marker marker) {
                        if (marker.getTitle() != null && marker.getTitle().equals("Puesto San Andrés")) {
                            //Le pasamos el id del puesto
                            int stand_id = 1;

                            //Creamos un Bundle para pasar los datos
                            Bundle bundle = new Bundle();
                            bundle.putInt("stand_id", stand_id);

                            
                            //Pasamos al fragment de LibrosDisponiblesPuesto
                            Fragment myFragment = new LibrosDisponiblesPuesto();
                            // Asignamos los argumentos al Fragmento de Destino
                            myFragment.setArguments(bundle);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainer, myFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else if (marker.getTitle() != null && marker.getTitle().equals("Puesto Calle Real")) {
                            //Le pasamos el id del puesto
                            int stand_id = 2;

                            // Creamos un Bundle para pasar los datos
                            Bundle bundle = new Bundle();
                            bundle.putInt("stand_id", stand_id);


                            //Pasamos al fragment de LibrosDisponiblesPuesto
                            Fragment myFragment = new LibrosDisponiblesPuesto();
                            // Asignamos los argumentos al Fragmento de Destino
                            myFragment.setArguments(bundle);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainer, myFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else if (marker.getTitle() != null && marker.getTitle().equals("Puesto Los Mallos")) {
                            //Le pasamos el id del puesto
                            int stand_id = 3;

                            // Creamos un Bundle para pasar los datos
                            Bundle bundle = new Bundle();
                            bundle.putInt("stand_id", stand_id);


                            //Pasamos al fragment de LibrosDisponiblesPuesto
                            Fragment myFragment = new LibrosDisponiblesPuesto();
                            // Asignamos los argumentos al Fragmento de Destino
                            myFragment.setArguments(bundle);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainer, myFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else if (marker.getTitle() != null && marker.getTitle().equals("Puesto Elviña")) {
                            //Le pasamos el id del puesto
                            int stand_id = 4;

                            // Creamos un Bundle para pasar los datos
                            Bundle bundle = new Bundle();
                            bundle.putInt("stand_id", stand_id);


                            //Pasamos al fragment de LibrosDisponiblesPuesto
                            Fragment myFragment = new LibrosDisponiblesPuesto();
                            // Asignamos los argumentos al Fragmento de Destino
                            myFragment.setArguments(bundle);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainer, myFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else if (marker.getTitle() != null && marker.getTitle().equals("Puesto Monte Alto")) {
                            //Le pasamos el id del puesto
                            int stand_id = 5;

                            // Creamos un Bundle para pasar los datos
                            Bundle bundle = new Bundle();
                            bundle.putInt("stand_id", stand_id);


                            //Pasamos al fragment de LibrosDisponiblesPuesto
                            Fragment myFragment = new LibrosDisponiblesPuesto();
                            // Asignamos los argumentos al Fragmento de Destino
                            myFragment.setArguments(bundle);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainer, myFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else if (marker.getTitle() != null && marker.getTitle().equals("Puesto Espacio Coruña")) {
                            //Le pasamos el id del puesto
                            int stand_id = 6;

                            // Creamos un Bundle para pasar los datos
                            Bundle bundle = new Bundle();
                            bundle.putInt("stand_id", stand_id);


                            //Pasamos al fragment de LibrosDisponiblesPuesto
                            Fragment myFragment = new LibrosDisponiblesPuesto();
                            // Asignamos los argumentos al Fragmento de Destino
                            myFragment.setArguments(bundle);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainer, myFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else if (marker.getTitle() != null && marker.getTitle().equals("Puesto Matogrande")) {
                            //Le pasamos el id del puesto
                            int stand_id = 7;

                            // Creamos un Bundle para pasar los datos
                            Bundle bundle = new Bundle();
                            bundle.putInt("stand_id", stand_id);


                            //Pasamos al fragment de LibrosDisponiblesPuesto
                            Fragment myFragment = new LibrosDisponiblesPuesto();
                            // Asignamos los argumentos al Fragmento de Destino
                            myFragment.setArguments(bundle);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragmentContainer, myFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    }
                });

                return true;
            }
        });

        //LISTENERS PARA LOS MARCADORES DE LOS PUESTOS


        //Configuramos el adaptador para el infoWindow
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null; //Devuelve null para usar la vista predeterminada del infoWindow
            }

            @Override
            public View getInfoContents(Marker marker) {
                //Creamod un layout personalizado para el infoWindow
                View infoWindowView = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                //Configuramos el texto de la dirección
                TextView snippetTextView = infoWindowView.findViewById(R.id.addressTextView);
                snippetTextView.setText(marker.getSnippet());

                return infoWindowView;
            }
        });
    }

    //Método para obtener la dirección a partir de las coordenadas de latitud y longitud
    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
