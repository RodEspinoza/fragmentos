package com.example.rodrigoespinoza.fragmentos.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rodrigoespinoza.fragmentos.R;
import com.example.rodrigoespinoza.fragmentos.model.Order;
import com.example.rodrigoespinoza.fragmentos.model.Person;
import com.example.rodrigoespinoza.fragmentos.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNewOrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNewOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewOrderFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    View view;
    Spinner spProductos;
    String selected_product;
    private ArrayList<String> productList;
    Order order;
    Product product;
    Person person;
    RadioGroup rStatus;
    String status;
    TextView txTotal;
    Button btnSubmitNewOrder;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog progressDialog;


    private OnFragmentInteractionListener mListener;

    public AddNewOrderFragment() {
        // Required empty public constructor
    }


    public static AddNewOrderFragment newInstance() {
        AddNewOrderFragment fragment = new AddNewOrderFragment();
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
        Bundle menuBundle = this.getArguments();
        if (!menuBundle.isEmpty()) {
            person = new Person();
            person.setId(Integer.parseInt(menuBundle.get("person_id").toString()));
        }
        this.view = inflater.inflate(R.layout.fragment_add_new_order, container, false);
        this.spProductos = this.view.findViewById(R.id.spProducts);
        this.productList = new ArrayList<>();
        this.requestQueue = Volley.newRequestQueue(getContext());
        this.txTotal = this.view.findViewById(R.id.txTotalNewOrder);
        this.rStatus = this.view.findViewById(R.id.rdStatusNewOrder);
        this.btnSubmitNewOrder = this.view.findViewById(R.id.btnSubmitNewOrder);
        this.btnSubmitNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProduct(selected_product);
                if(product.getId() == null){
                    Toast.makeText(getContext(), "Producto no seleccionado", Toast.LENGTH_SHORT);
                }
                order = new Order();
                order.setPerson(person);
                order.setProduct(product);
                order.setTotal(Integer.parseInt(txTotal.getText().toString()));
                Date date = new Date();
                order.setFecha(date);
                order.setState(status.toString());
                saveOrder(order);
            }
        });
        getProducts();


      /*  if(saveOrder(this.order)>0){
            Toast.makeText(getContext(), "Orden almacenada", Toast.LENGTH_SHORT).show();
        }
        */
        return this.view;
    }

    private void saveOrder(final Order order) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        String url ="https://androidsandbox.site/wsAndroid/wsAddOrder.php";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT);
                ProductFragment nextFrag = new ProductFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.containerFragmentMenu, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();
                Map<String, String>  params = new HashMap<>();
                params.put("fecha", dateFormat.format(date));
                params.put("state", order.getState());
                params.put("total", order.getTotal().toString());
                params.put("id_person", order.getPerson().getId().toString());
                params.put("id_product", order.getProduct().getId().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private void getProducts(){
        String url ="https://androidsandbox.site/wsAndroid/wsGetAllProducts.php";
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Product product = new Product();
                        product.setStock(Integer.parseInt(jsonObject.getString("stock")));
                        product.setName(jsonObject.getString("name"));
                        product.setId(Integer.parseInt(jsonObject.getString("id")));

                        productList.add(product.getName());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, productList);
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spProductos.setAdapter(arrayAdapter);
                spProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selected_product = spProductos.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    public void getProduct(String productName){
        String url = "https://androidsandbox.site/wsAndroid/wsGetProduct.php/?name="+productName;
        Toast.makeText(getContext(), url, Toast.LENGTH_SHORT);
        final Product product = new Product();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        requestQueue.add(jsonObjectRequest);
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
    public void onResponse(JSONObject response) {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando consulta...");
        progressDialog.show();
        try{
            product = new Product();
            JSONArray json = response.optJSONArray("producto");
            JSONObject jsonObject = null;
            jsonObject =json.getJSONObject(0);
            product.setId(jsonObject.getInt("id"));
            product.setName(jsonObject.getString("name"));
            product.setStock(jsonObject.getInt("stock"));
            progressDialog.hide();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

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
