package com.example.rodrigoespinoza.fragmentos.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rodrigoespinoza.fragmentos.R;
import com.example.rodrigoespinoza.fragmentos.Utils;
import com.example.rodrigoespinoza.fragmentos.model.Person;
import com.example.rodrigoespinoza.fragmentos.model.SqlConecttion;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditPersonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditPersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPersonFragment extends Fragment {

    SqlConecttion conn;

    EditText txtFragEditPersonName, txtFragEditPersonLastName;
    RadioGroup rgFragEditPerson;
    RadioButton rbFragEditPersonMasculino, rbFragEditPersonFemenino;
    Spinner spFragEditPersonLocalidad;
    Button btnFragEditPersonEdit;

    ListView listViewLocations;
    View view;

    String selectLocation;

    private OnFragmentInteractionListener mListener;

    public EditPersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditPersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditPersonFragment newInstance(String param1, String param2) {
        EditPersonFragment fragment = new EditPersonFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_edit_person, container, false);

        this.btnFragEditPersonEdit = this.view.findViewById(R.id.btnFragEditPersonEdit);

        this.listViewLocations = this.view.findViewById(R.id.spFragEditPersonLocalidad);
        getLocalidades();
        return this.view;
    }

    private void getLocalidades() {
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_expandable_list_item_1, Utils.getLocations());
        this.listViewLocations.setAdapter(arrayAdapter);
        this.listViewLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectLocation = listViewLocations.getOnItemSelectedListener().toString();
                Toast.makeText(getContext(),selectLocation, Toast.LENGTH_SHORT).show();
            }
        });
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
