package com.example.rodrigoespinoza.fragmentos.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rodrigoespinoza.fragmentos.R;
import com.example.rodrigoespinoza.fragmentos.model.Product;
import com.example.rodrigoespinoza.fragmentos.model.SqlConecttion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProductFragment extends Fragment {
    Button btnUpdateProduct, btnDeleteProduct;
    EditText txtFragEditProductStock, txFragEditProductName;
    SqlConecttion sqlConecttion;
    Product product;
    View view;
    String baseUrl ="https://androidsandbox.site/wsAndroid";
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    StringRequest stringRequest;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private OnFragmentInteractionListener mListener;

    public EditProductFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment EditProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProductFragment newInstance(String param1, String param2) {
        EditProductFragment fragment = new EditProductFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_edit_product, container, false);
        this.btnUpdateProduct = this.view.findViewById(R.id.btnFragEditProduct);
        this.btnDeleteProduct = this.view.findViewById(R.id.btnFragDeleteProduct);
        this.txFragEditProductName = this.view.findViewById(R.id.txFragEditProductName);
        this.txtFragEditProductStock = this.view.findViewById(R.id.txtFragEditProductStock);
        Bundle productBundle = this.getArguments();
        if(!productBundle.isEmpty()){

            this.product = new Product(
                    (productBundle.get("product_id").toString()),
                    productBundle.get("product_name").toString(),
                    Integer.parseInt(productBundle.get("product_stock").toString())
            );
            this.txFragEditProductName.setText(this.product.getName());
            this.txtFragEditProductStock.setText(this.product.getStock().toString());
        }
        this.btnUpdateProduct.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setStock(Integer.parseInt(txtFragEditProductStock.getText().toString()));
                product.setName(txFragEditProductName.getText().toString());
                updateProduct(product);

            }
        });
        this.btnDeleteProduct.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder
                        .setMessage("¿Eliminar producto "+ product.getName() +"?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProduct(product.getId());
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

            }
        });

        this.requestQueue = Volley.newRequestQueue(getContext());
        return this.view;
    }

    private void updateProduct(final Product product) {
        callProgressDialog();
         Map<String, String> update_map = new HashMap<>();
         update_map.put("name", product.getName());
         update_map.put("stock",  product.getStock().toString());
         db.collection("products")
                 .document(product.getId())
                 .set(update_map).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 Log.d("Nani", "DocumentSnapshot successfully written!");
                 progressDialog.hide();
                Toast.makeText(getContext(), "Producto Actualizado", Toast.LENGTH_SHORT);
                ProductFragment nextFrag = new ProductFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                         .replace(R.id.containerFragmentMenu, nextFrag,"findThisFragment")
                         .addToBackStack(null)
                         .commit();
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 progressDialog.hide();
                 Toast.makeText(getContext(), "Nope", Toast.LENGTH_SHORT);
             }
         });

    }

    private void deleteProduct(final String id) {
        callProgressDialog();
        db.collection("products").document(id)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            setProductFragment();
            progressDialog.hide();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Nani", "Error deleting document", e);
                Toast.makeText(getContext(), "Algo salio mal", Toast.LENGTH_SHORT);
                progressDialog.hide();
            }
        });



    }

    private void setProductFragment(){
        ProductFragment nextFrag = new ProductFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFragmentMenu, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }
    private void callProgressDialog(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
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
