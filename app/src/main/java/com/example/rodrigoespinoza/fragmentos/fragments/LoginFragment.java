package com.example.rodrigoespinoza.fragmentos.fragments;

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

import com.example.rodrigoespinoza.fragmentos.MenuActivity;
import com.example.rodrigoespinoza.fragmentos.R;
import com.example.rodrigoespinoza.fragmentos.model.SqlConecttion;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    Button btnFragLogin;
    Intent intent;
    View view;
    EditText txUserFragmentLogin, txPassWordLogin;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_login, container, false);
        this.btnFragLogin = this.view.findViewById(R.id.btnFragLogin);
        this.txUserFragmentLogin = this.view.findViewById(R.id.txUserFragmentLogin);
        this.txPassWordLogin = this.view.findViewById(R.id.txPasswordFragmentLogin);
        this.btnFragLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = txUserFragmentLogin.getText().toString();
                String password = txPassWordLogin.getText().toString();
                Integer id = autenticaUsuario(user, password);
                if(id != 0) {
                    intent = new Intent(getActivity(), MenuActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    Toast.makeText(getContext(), "login ?", Toast.LENGTH_SHORT);
                }else{
                    Toast.makeText(getContext(),
                            "Usuario o contraseña no coinciden", Toast.LENGTH_SHORT);
                }


            }
        });
        return this.view;
    }

    private Integer autenticaUsuario(String user, String password) {

        SqlConecttion conn =  new SqlConecttion(
                getContext(), "bd_gestor_pedidos", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        try {
            String[] parametrosBuscar = {user, password};
            String[] camposTraer = {"id"};

            Cursor cursor = db.query(
                    "user",
                    camposTraer,
                    "email = ? AND pass = ?",
                    parametrosBuscar,
                    null, null, null);

            cursor.moveToFirst();
            Integer id = cursor.getInt(cursor.getColumnIndex("id"));
            //Toast.makeText(this, cursor.getString(0), Toast.LENGTH_SHORT).show();
            cursor.close();
            conn.close();
            return id;
        } catch (Exception ex) {
            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
            conn.close();
            return 0;
        } finally {
            conn.close();
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
