package com.example.rodrigoespinoza.fragmentos.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.rodrigoespinoza.fragmentos.R;
import com.example.rodrigoespinoza.fragmentos.model.Person;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AzulFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AzulFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements View.OnClickListener {

    View vista;
    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;
    private FusedLocationProviderClient mFuseLocationProvider;
    Person person;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public MapsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle menuBundle = this.getArguments();
        Toast.makeText(getContext(), "faack", Toast.LENGTH_SHORT);
        if(!menuBundle.isEmpty()){
            this.person = new Person(menuBundle.get("person_id").toString());
        }
        mFuseLocationProvider = LocationServices.getFusedLocationProviderClient(getContext());
        obtenerSubirLatLonFirebase();

    }

    private void obtenerSubirLatLonFirebase() {
        if (ActivityCompat.checkSelfPermission(
                getContext() , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFuseLocationProvider.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Toast.makeText(getContext(), location.getLatitude()+"", Toast.LENGTH_SHORT);
                            final HashMap<String ,String > params = new HashMap<>();
                            params.put("person_id", person.getId());
                            params.put("lat", location.getLatitude()+"");
                            params.put("lon", location.getLongitude()+"");

                            db.collection("location")
                                    .whereEqualTo("person_id", person.getId())
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        if(task.getResult().getDocuments().size()==1){
                                            String document = task.getResult().getDocuments().get(0).getId();
                                            db.collection("location").document(document).set(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getContext(), "UPDATE", Toast.LENGTH_SHORT);
                                                }
                                            });
                                        }else{
                                            db.collection("location").add(params).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(getContext(), "ADD" + documentReference.getId(), Toast.LENGTH_SHORT);
                                                }
                                            });
                                        }
                                    }
                                }
                            });

                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.vista = inflater.inflate(R.layout.fragment_azul, container, false);

        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
