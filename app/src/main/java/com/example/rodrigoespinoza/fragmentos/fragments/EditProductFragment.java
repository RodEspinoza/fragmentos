package com.example.rodrigoespinoza.fragmentos.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.rodrigoespinoza.fragmentos.R;
import com.example.rodrigoespinoza.fragmentos.model.Product;
import com.example.rodrigoespinoza.fragmentos.model.SqlConecttion;

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

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    StringRequest stringRequest;

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
        if (productBundle != null)
        {
            this.product = new Product(
                    Integer.parseInt(productBundle.get("product_id").toString()),
                    productBundle.get("product_name").toString(),
                    Integer.parseInt(productBundle.get("product_stock").toString())
            );
            this.txFragEditProductName.setText(this.product.getName());
            this.txtFragEditProductStock.setText(this.product.getStock().toString());
        }
        this.btnDeleteProduct.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v) {
                deleteProduct(product.getId());
            }
        });
        this.btnUpdateProduct.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setStock(Integer.parseInt(txtFragEditProductStock.getText().toString()));
                product.setName(txFragEditProductName.getText().toString());
                updateProduct(product);

            }
        });

        return this.view;
    }

    private void updateProduct(final Product product) {
        this.progressDialog = new ProgressDialog(getContext());
        this.progressDialog.setMessage("Cargando...");
        this.progressDialog.show();
        String url ="https://androidsandbox.site/wsAndroid/wsEditProduct.php";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String id = product.getId().toString();
                String name = product.getName();
                String stock = product.getStock().toString();
                Map<String,String> parametros = new HashMap<>();
                parametros.put("id", id);
                parametros.put("name", name);
                parametros.put("stock", stock);
                return parametros;

            }
        }
        ;

        requestQueue.add(stringRequest);

        /**
        SqlConecttion conn = new SqlConecttion(
                getContext(), "bd_gestor_pedidos", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        try{

            String params[] = {product.getId().toString()};
            ContentValues values = new ContentValues();
            values.put("stock", product.getStock());
            values.put("name", product.getName());
            db.update("product", values,"id=?", params);
            setProductFragment();
        }
        catch (Exception exp){
            db.close();
            conn.close();
            Toast.makeText(getContext(),"Wrong update.",Toast.LENGTH_SHORT).show();
        }**/
    }

    private void deleteProduct(Integer id) {
        SqlConecttion conn = new SqlConecttion(
                getContext(), "bd_gestor_pedidos", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        String params[] = {id.toString()};
        try{
            db.delete("product", "id=?", params);
            db.close();
            conn.close();
            setProductFragment();
        }catch (Exception exp){
            db.close();
            conn.close();
            Toast.makeText(getContext(),"Wrong update.",Toast.LENGTH_SHORT).show();
        }

    }

    private void setProductFragment(){
        ProductFragment nextFrag = new ProductFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFragmentMenu, nextFrag, "findThisFragment")
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
