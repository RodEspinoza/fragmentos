package com.example.rodrigoespinoza.fragmentos.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rodrigoespinoza.fragmentos.MenuActivity;
import com.example.rodrigoespinoza.fragmentos.R;
import com.example.rodrigoespinoza.fragmentos.Utils;
import com.example.rodrigoespinoza.fragmentos.model.Person;
import com.example.rodrigoespinoza.fragmentos.model.SqlConecttion;
import com.example.rodrigoespinoza.fragmentos.model.User;
import com.facebook.share.Share;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistroFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    View view;
    User user;
    Person person;

    EditText txtFragRegistroEmail, txtFragRegistroPass, txtFragRegistroRePass;
    EditText txtFragRegistroRut, txtFragRegistroNombre, txtFragRegistroApellido;

    String sexoSeleccionado, localidad;
    RadioGroup rgFragRegistroSexo;
    Spinner spFragRegistroLocalidad;
    Button btnFragRegistroRegistrar;

    //Componente de progreso
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    StringRequest stringRequest;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnFragmentInteractionListener mListener;

    public RegistroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroFragment newInstance(String param1, String param2) {
        RegistroFragment fragment = new RegistroFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_registro, container, false);
        this.user = new User();
        this.person = new Person();
        this.txtFragRegistroEmail =  this.view.findViewById(R.id.txtFragRegistroEmail);
        this.txtFragRegistroPass = this.view.findViewById(R.id.txtFragRegistroPass);
        this.txtFragRegistroRePass = this.view.findViewById(R.id.txtFragRegistroRePass);
        this.txtFragRegistroRut = this.view.findViewById(R.id.txtFragRegistroRut);
        this.txtFragRegistroNombre = this.view.findViewById(R.id.txtFragRegistroNombre);
        this.txtFragRegistroApellido = this.view.findViewById(R.id.txtFragRegistroApellido);
        this.rgFragRegistroSexo = this.view.findViewById(R.id.rgFragRegistroSexo);

        this.spFragRegistroLocalidad = this.view.findViewById(R.id.spFragRegistroLocalidad);
        this.rgFragRegistroSexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rbFragRegistroHombre){
                    sexoSeleccionado = "Masculino";
                } else{
                    sexoSeleccionado = "Femenino";
                }
            }
        });
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, Utils.getLocations());
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        this.spFragRegistroLocalidad.setAdapter(arrayAdapter);

        this.spFragRegistroLocalidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                localidad = spFragRegistroLocalidad.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.btnFragRegistroRegistrar = (Button) this.view.findViewById(R.id.btnFragRegistroRegistrar);
        this.btnFragRegistroRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaPassword(txtFragRegistroPass.getText().toString(), txtFragRegistroRePass.getText().toString())) {
                    if (validaRut(txtFragRegistroRut.getText().toString())) {

                        registrarUsuario(txtFragRegistroEmail.getText().toString(), txtFragRegistroPass.getText().toString());

                    } else {
                        Toast.makeText(getContext(),"Rut Invalido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),"Contraseñas Incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.requestQueue = Volley.newRequestQueue(getContext());

        return this.view;
    }

    private void setLoginFragment() {
        LoginFragment nextFrag = new LoginFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFragment, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
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
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

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

    private void registrarPersona(final String rut, final String nombre, final String apellido, final String sexo, final String localidad, final String idUser) {
        this.progressDialog = new ProgressDialog(getContext());

        this.progressDialog.setMessage("Procesando valores del perfil... ");
        this.progressDialog.show();
        Map<String, String>  params = new HashMap<>();
        params.put("rut", rut);
        params.put("nombre", nombre);
        params.put("last_name", apellido);
        params.put("sexo", sexo);
        params.put("location", localidad);
        params.put("id_user", idUser);
       db.collection("person")
                .add(params).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.hide();
                        String person_id = documentReference.getId();
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("person_id", person_id);
                        editor.commit();
                        Intent intent = new Intent(getContext(), MenuActivity.class);
                        intent.putExtra("person_id", person_id);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               progressDialog.hide();
               Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT);
           }
       });



    }
    private void registrarUsuario(final String email, final String pass) {
        this.progressDialog = new ProgressDialog(getContext());
        this.progressDialog.setMessage("Comprobando datos de usuario ");
        this.progressDialog.show();
        final Map<String, String>  params = new HashMap<>();
        params.put("email", email);
        params.put("pass", pass);


        db.collection("user")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot document = task.getResult();
                            if(document.getDocuments().size()==0){

                                progressDialog.hide();
                                db.collection("user")
                                        .add(params)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                progressDialog.hide();
                                                registrarPersona(txtFragRegistroRut.getText().toString(), txtFragRegistroNombre.getText().toString(),
                                                        txtFragRegistroApellido.getText().toString(), sexoSeleccionado, localidad, documentReference.getId());

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.hide();
                                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT);
                                    }
                                });

                            }else{
                                progressDialog.hide();
                                Toast.makeText(getContext(), "Correo ya registrado.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    private boolean validaPassword(String password, String rePassword) {
        if(password.equals(rePassword)){
            return true;
        } else {
            return false;
        }
    }
    private boolean validaRut(String rut) {
        boolean valida = false;
        rut = rut.replace(".","");
        rut = rut.replace("-","");
        try {
            Integer rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));
            char dv = rut.charAt(rut.length() - 1);
            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10)
            {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                valida = true;
            }
            return valida;
        } catch (Exception ex){
            return valida;
        }
    }
}
