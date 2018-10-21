package com.example.rodrigoespinoza.fragmentos.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.rodrigoespinoza.fragmentos.R;
import com.example.rodrigoespinoza.fragmentos.Utils;
import com.example.rodrigoespinoza.fragmentos.model.Person;
import com.example.rodrigoespinoza.fragmentos.model.SqlConecttion;
import com.example.rodrigoespinoza.fragmentos.model.User;

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
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    //Segunda forma
    StringRequest stringRequest;

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
                if (validaPassword(txtFragRegistroPass.getText().toString(), txtFragRegistroRePass.getText().toString())){
                    if (validaRut(txtFragRegistroRut.getText().toString())){

                        user.setEmail(txtFragRegistroEmail.getText().toString());
                        user.setPass(txtFragRegistroPass.getText().toString());
                        person.setRut(txtFragRegistroRut.getText().toString());
                        person.setName(txtFragRegistroNombre.getText().toString());
                        person.setLast_name(txtFragRegistroApellido.getText().toString());
                        person.setSexo(sexoSeleccionado);
                        person.setLocation(localidad);
                        registrarUsuario(user);
                        //person.setId_user(registrarUsuario(user));

                        /*if (registrarPersona(person) != 0) {
                            Toast.makeText(getContext(), "Registrado", Toast.LENGTH_SHORT).show();
                            setLoginFragment();
                        } else {
                            Toast.makeText(getContext(), "Ha ocurrido un problema, intentelo mas tarde", Toast.LENGTH_SHORT).show();
                        }*/
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

    private void registrarPersona(final Integer id) {
        this.progressDialog = new ProgressDialog(getContext());
        this.progressDialog.setMessage("Cargando... ");
        this.progressDialog.show();
        try {
            String url = "https://androidsandbox.site/wsAndroid/wsIngresarPersona.php";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(response);
                        JSONArray json = jsonObject.optJSONArray("id_persona");

                        JSONObject jo = null;
                        jo = json.getJSONObject(0);

                        Integer id =  jo.optInt("id");

                    } catch (Exception ex) {
                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.hide();
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.hide();
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT);
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<>();

                    params.put("rut", person.getRut());
                    params.put("nombre", person.getName());
                    params.put("last_name", person.getLast_name());
                    params.put("sexo", person.getSexo());
                    params.put("location", person.getLocation());
                    params.put("id_user",id.toString());
                    return params;
                }
            };
            //Toast.makeText(getContext(), stringRequest.toString(), Toast.LENGTH_LONG).show();
            requestQueue.add(stringRequest);
        } catch (Exception ex){

        }

        /*SqlConecttion conexion = new SqlConecttion(getContext(), "bd_gestor_pedidos", null, 1);
        SQLiteDatabase dataBase = conexion.getWritableDatabase();
        try {
            ContentValues nuevaPersona = new ContentValues();
            nuevaPersona.put("rut", persona.getRut());
            nuevaPersona.put("name", persona.getName());
            nuevaPersona.put("last_name", persona.getLast_name());
            nuevaPersona.put("sexo", persona.getSexo());
            nuevaPersona.put("location", persona.getLocation());
            nuevaPersona.put("id_user", persona.getId_user());
            Long id = dataBase.insert("person", "id", nuevaPersona);
            //Toast.makeText(getContext(), id.toString(), Toast.LENGTH_SHORT).show();
            dataBase.close();
            conexion.close();

        } catch (Exception ex) {
            dataBase.close();
            conexion.close();
            //Toast.makeText(getContext(),"No pude registrar, " + ex.getMessage().toString(), Toast.LENGTH_SHORT).show();

        } finally {
            dataBase.close();
        }*/
    }
    private void registrarUsuario(User usuario) {
        this.progressDialog = new ProgressDialog(getContext());
        this.progressDialog.setMessage("Cargando... ");
        this.progressDialog.show();
        try {
            String url = "https://androidsandbox.site/wsAndroid/wsIngresarUsuario.php";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = null;
                        jsonObject = new JSONObject(response);
                        JSONArray json = jsonObject.optJSONArray("id_usuario");

                        JSONObject jo = null;
                        jo = json.getJSONObject(0);

                        Integer id = jo.optInt("id");

                        registrarPersona(id);
                    } catch (Exception ex) {
                        Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.hide();
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.hide();
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT);
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<>();
                    params.put("email", user.getEmail());
                    params.put("pass", user.getPass());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    params.put("fecha", dateFormat.format(date));
                    return params;
                }
            };
            //Toast.makeText(getContext(), stringRequest.toString(), Toast.LENGTH_LONG).show();
            requestQueue.add(stringRequest);
        } catch (Exception ex) {
            Toast.makeText(getContext(), "Error" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        /*SqlConecttion conexion = new SqlConecttion(getContext(), "bd_gestor_pedidos", null, 1);
        SQLiteDatabase dataBase = conexion.getWritableDatabase();
        try {
            ContentValues nuevoUsuario = new ContentValues();
            nuevoUsuario.put("email", usuario.getEmail());
            nuevoUsuario.put("pass", usuario.getPass());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            nuevoUsuario.put("fecha", dateFormat.format(date));
            Long id = dataBase.insert("user", "id",nuevoUsuario);
            Toast.makeText(getContext(), id.toString(), Toast.LENGTH_SHORT).show();
            dataBase.close();
            conexion.close();
            return Integer.parseInt(id.toString());
        } catch (Exception ex) {
            dataBase.close();
            conexion.close();
            Toast.makeText(getContext(),"No pude registrar, " + ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
            return 0;
        } finally {
            dataBase.close();
        }*/
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
            char dv = rut.charAt(Integer.parseInt(rut.substring(rut.length() - 1, rut.length())));
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
