package com.example.rodrigoespinoza.fragmentos.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rodrigoespinoza.fragmentos.MenuActivity;
import com.example.rodrigoespinoza.fragmentos.R;
import com.example.rodrigoespinoza.fragmentos.Utils;
import com.example.rodrigoespinoza.fragmentos.model.Person;
import com.example.rodrigoespinoza.fragmentos.model.SqlConecttion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    Intent intent;

    Person person;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    StringRequest stringRequest;

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
        this.rbFragEditPersonMasculino = (RadioButton) this.view.findViewById(R.id.rbFragEditPersonMasculino);
        this.rbFragEditPersonFemenino = (RadioButton) this.view.findViewById(R.id.rbFragEditPersonFemenino);
        this.rgFragEditPerson.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rbFragEditPersonMasculino){
                    sexo = "Masculino";
                } else{
                    sexo = "Femenino";
                }
            }
        });

        Bundle menuBundle = this.getArguments();
        if (!menuBundle.isEmpty()) {
            this.txtFragEditPersonName.setText(menuBundle.get("nombre").toString());
            this.txtFragEditPersonLastName.setText(menuBundle.get("last_name").toString());
            this.sexo = menuBundle.get("sexo").toString();
            this.idUser = Integer.parseInt(menuBundle.get("id").toString());
            if (sexo.equals("Masculino")){
                this.rbFragEditPersonMasculino.setChecked(true);
            } else {
                this.rbFragEditPersonFemenino.setChecked(true);
            }
        }

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

        this.btnFragEditPersonEdit = (Button) this.view.findViewById(R.id.btnFragEditPersonEdit);
        this.btnFragEditPersonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (actualizarPerson(txtFragEditPersonName.getText().toString(), txtFragEditPersonLastName.getText().toString(),
                    sexo, localidad, idUser)){
                    Toast.makeText(getContext(), "Actualizado", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), MenuActivity.class);
                    intent.putExtra("id",idUser);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(),"Error a actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.requestQueue = Volley.newRequestQueue(getContext());
        return this.view;
    }

    private boolean actualizarPerson(final String name, final String last_name, final String sexo, final String localidad, final Integer idUser) {
        this.progressDialog = new ProgressDialog(getContext());
        this.progressDialog.setMessage("Cargando... ");
        this.progressDialog.show();

        try {
            String url = "https://androidsandbox.site/wsAndroid/wsEditarPersona.php";
            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.hide();
                    Toast.makeText(getContext(), response, Toast.LENGTH_LONG);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.hide();
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", idUser.toString());
                    params.put("name", name);
                    params.put("last_name", last_name);
                    params.put("sexo", sexo);
                    params.put("location", localidad);
                    return params;
                }
            };
            if(stringRequest != null){
                requestQueue.add(stringRequest);
            }
            return true;
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }

        /*conn = new SqlConecttion(getContext(), "bd_gestor_pedidos", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        try {
            String[] buscar = {idUser.toString()};
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("last_name", last_name);
            values.put("sexo", sexo);
            values.put("location", localidad);

            db.update("person", values, "id = ?", buscar);


            conn.close();
            db.close();
            return true;
        } catch (Exception ex){

            conn.close();
            db.close();
            return false;
        }*/
    }

    private void getCampos(final Person person) {


        /*conn = new SqlConecttion(getContext(), "bd_gestor_pedidos", null, 1);
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
        }*/
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
