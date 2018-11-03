package com.example.rodrigoespinoza.fragmentos.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.rodrigoespinoza.fragmentos.model.SqlConecttion;
import com.example.rodrigoespinoza.fragmentos.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    View view;
    Intent intent;
    Button btnFragLogin;

    EditText txUserFragmentLogin, txPassWordLogin;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    StringRequest stringRequest;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_login, container, false);

        this.txUserFragmentLogin = this.view.findViewById(R.id.txUserFragmentLogin);
        this.txPassWordLogin = this.view.findViewById(R.id.txPasswordFragmentLogin);

        this.btnFragLogin = this.view.findViewById(R.id.btnFragLogin);
        this.btnFragLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(txUserFragmentLogin.getText().toString(), txPassWordLogin.getText().toString());
                autenticaUsuario(user);
            }
        });

        this.requestQueue = Volley.newRequestQueue(getContext());
        return this.view;
    }

    private void autenticaUsuario(final User user) {
        this.progressDialog = new ProgressDialog(getContext());
        this.progressDialog.setMessage("Cargando... ");
        this.progressDialog.show();

        try {
            String url = "https://androidsandbox.site/wsAndroid/wsConsultarUsuario.php";
            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.optJSONArray("id_usuario");

                        JSONObject json = jsonArray.getJSONObject(0);

                        Integer id = json.optInt("id");

                        intent = new Intent(getActivity(), MenuActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    } catch (Exception ex) {
                        Toast.makeText(getContext(), "Usuario o Contraseña Invalida", Toast.LENGTH_LONG).show();
                    }
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
                    params.put("email", user.getEmail());
                    params.put("pass", user.getPass());
                    return params;
                }
            };
            if(stringRequest != null){
            requestQueue.add(stringRequest);
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
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
