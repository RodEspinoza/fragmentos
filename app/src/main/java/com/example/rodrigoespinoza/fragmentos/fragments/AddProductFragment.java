package com.example.rodrigoespinoza.fragmentos.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.rodrigoespinoza.fragmentos.model.Product;
import com.example.rodrigoespinoza.fragmentos.model.SqlConecttion;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    Intent intent;
    View view;
    EditText txProductName, txProductStock;
    Product product;
    Button btnSubmitNewProduct;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog progressDialog;
    StringRequest stringRequest;
    private OnFragmentInteractionListener mListener;

    public AddProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment AddProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
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
        this.view = inflater.inflate(R.layout.fragment_add_product_fracment, container, false);
        this.txProductName = this.view.findViewById(R.id.txProductName);
        this.txProductStock = this.view.findViewById(R.id.txProductStock);
        this.product = new Product();
        this.btnSubmitNewProduct = this.view.findViewById(R.id.btnSubmitNewProduct);
        this.btnSubmitNewProduct.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitProduct();

            }
        });
        this.requestQueue = Volley.newRequestQueue(getContext());

        return this.view;
    }


    private void submitProduct()
    {
        this.product.setName(this.txProductName.getText().toString());
        this.product.setStock((Integer.parseInt(this.txProductStock.getText().toString())));
        this.addNewProduct(product);

    }

    private void addNewProduct(final Product product) {
        this.progressDialog = new ProgressDialog(getContext());
        this.progressDialog.setMessage("Cargando...");
        String uri = "http://127.0.0.1:8889/wwww/wsandroid/wsAddProduct.php";
        stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();


            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String>  params = new HashMap<>();
            params.put("name", product.getName());
            params.put("stock", product.getStock());
            return params;
            }
        };
        requestQueue.add(stringRequest);
        /**
            if(TextUtils.isEmpty(this.product.getName())){

            }
            SqlConecttion conn = new SqlConecttion(
                    getContext(), "bd_gestor_pedidos", null,1);
            SQLiteDatabase db = conn.getWritableDatabase();
            try{

                ContentValues values = new ContentValues();
                values.put("name", product.getName());
                values.put("stock", product.getStock());
                Long id = db.insert("product", "id", values);
                Toast.makeText(getContext(), id.toString(), Toast.LENGTH_SHORT).show();
                db.close();
                ProductFragment nextFrag = new ProductFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.containerFragmentMenu, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();

            }catch (SQLiteException exc){
                Toast.makeText(getContext(), "500", Toast.LENGTH_SHORT).show();
                db.close();
            }
        **/
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
