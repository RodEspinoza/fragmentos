package com.example.rodrigoespinoza.fragmentos.fragments;

import android.content.ContentValues;
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
    View view;
    SqlConecttion conn;

    EditText txtFragEditPersonName, txtFragEditPersonLastName;
    RadioGroup rgFragEditPerson;
    RadioButton rbFragEditPersonMasculino, rbFragEditPersonFemenino;
    Spinner spFragEditPersonLocalidad;
    Button btnFragEditPersonEdit;

    Integer idUser;
    String localidad, sexo;

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

        this.txtFragEditPersonName = (EditText) this.view.findViewById(R.id.txtFragEditPersonName);
        this.txtFragEditPersonLastName = (EditText) this.view.findViewById(R.id.txtFragEditPersonLastName);
        this.rgFragEditPerson = (RadioGroup) this.view.findViewById(R.id.rgFragEditPerson);
        this.rgFragEditPerson.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rbFragRegistroHombre){
                    sexo = "Masculino";
                } else{
                    sexo = "Femenino";
                }
            }
        });

        this.rbFragEditPersonMasculino = (RadioButton) this.view.findViewById(R.id.rbFragEditPersonMasculino);
        this.rbFragEditPersonFemenino = (RadioButton) this.view.findViewById(R.id.rbFragEditPersonFemenino);
        this.spFragEditPersonLocalidad = (Spinner) this.view.findViewById(R.id.spFragEditPersonLocalidad);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item,
                Utils.getLocations());
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        this.spFragEditPersonLocalidad.setAdapter(arrayAdapter);
        this.spFragEditPersonLocalidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                localidad = spFragEditPersonLocalidad.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Bundle menuBundle = this.getArguments();
        if (menuBundle != null) {
            idUser = Integer.parseInt(menuBundle.get("idUser").toString());
        }
        //obtenemos los valores guardados
        getCampos(idUser);

        this.btnFragEditPersonEdit = (Button) this.view.findViewById(R.id.btnFragEditPersonEdit);
        this.btnFragEditPersonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPerson(txtFragEditPersonName.getText().toString(), txtFragEditPersonLastName.getText().toString(),
                        sexo, localidad, idUser);
            }
        });
        return this.view;
    }

    private void actualizarPerson(String name, String last_name, String sexo, String localidad, Integer idUser) {
        conn = new SqlConecttion(getContext(), "bd_gestor_pedidos", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        try {
            String[] buscar = {idUser.toString()};
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("last_name", last_name);
            values.put("sexo", sexo);
            values.put("location", localidad);

            db.update("person", values, "id = ?", buscar);

            Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_SHORT).show();
            conn.close();
            db.close();
        } catch (Exception ex){
            Toast.makeText(getContext(),"Error a actualizar", Toast.LENGTH_SHORT).show();
            conn.close();
            db.close();
        }
    }

    private void getCampos(Integer idUser) {
        conn = new SqlConecttion(getContext(), "bd_gestor_pedidos", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        String[] buscar = {idUser.toString()};
        //String[] obtener = {"name", "last_name", "sexo"};

        Cursor cursor = db.rawQuery("SELECT * FROM person WHERE id = ?", buscar);
        while (cursor.moveToNext()){
            this.txtFragEditPersonName.setText(cursor.getString(2));
            this.txtFragEditPersonLastName.setText(cursor.getString(3));
            if (cursor.getString(4).equals("Masculino")){
                rbFragEditPersonMasculino.setChecked(true);
            } else {
                rbFragEditPersonFemenino.setChecked(true);
            }
        }
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
