package com.example.rodrigoespinoza.fragmentos.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.RadioButton;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

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
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        final Person person = new Person();

        final Product product = new Product();
        sharedPreferences.getAll();
        person.setId(sharedPreferences.getInt("person_id", 0));
        this.view = inflater.inflate(R.layout.fragment_add_new_order, container, false);
        this.spProductos = this.view.findViewById(R.id.spProducts);
        this.productList = new ArrayList<>();
        this.requestQueue = Volley.newRequestQueue(getContext());
        this.txTotal = this.view.findViewById(R.id.txTotalNewOrder);
        this.btnSubmitNewOrder = this.view.findViewById(R.id.btnSubmitNewOrder);
        this.rStatus = this.view.findViewById(R.id.rdStatusNewOrder);
        this.btnSubmitNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProduct(selected_product);
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
        Map<String, String>  params = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        params.put("fecha", dateFormat.format(date));
        params.put("state", order.getState());
        params.put("total", order.getTotal().toString());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            person = new Person();
            person.setId(0);
            order.setPerson(person);
        }else{
            // intenta con Facebook
        }
        params.put("id_person", order.getPerson().getId().toString());
        params.put("id_product", order.getProduct().getId().toString());
        db.collection("orders")
                .add("params")
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Orden agragada", Toast.LENGTH_SHORT).show();
                        ProductOrders nextFrag = new ProductOrders();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.containerFragmentMenu, nextFrag, "findThisFrag")
                                .addToBackStack(null)
                                .commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
  }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private void getProducts(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        db.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful()){
                   for (QueryDocumentSnapshot document : task.getResult()) {
                       Product product = new Product();
                       product.setName((document.get("name").toString()));
                       productList.add(product.getName());
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
               }else{
                   progressDialog.dismiss();
               }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });

         }

    public void getProduct(String productName){
        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        product = new Product();
        final String TAG ="orden";
        Task<QuerySnapshot> query = db.collection("products")
                .whereEqualTo("name", productName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot document = task.getResult();
                            product.setId(document.getDocuments().get(0).getId());
                            product.setStock(Integer.parseInt(document.getDocuments().get(0).get("stock").toString()));
                            Log.d(TAG, "DocumentSnapshot data: " + document.getDocuments().get(0).getId().toString());
                            if(product.getId() == null){
                                Toast.makeText(getContext(), "Producto no seleccionado", Toast.LENGTH_SHORT);
                            }else {
                                RadioButton radio = view.findViewById(view.findViewById(rStatus.getCheckedRadioButtonId()).getId());
                                String current_status = radio.getText().toString();

                                order = new Order();
                                order.setPerson(person);
                                order.setProduct(product);
                                order.setTotal(Integer.parseInt(txTotal.getText().toString()));
                                Date date = new Date();
                                order.setFecha(date);
                                order.setState(current_status);

                                saveOrder(order);
                            }

                    }
                    else {
                            Log.d(TAG, "ERROR QUERY");
                        }}
                });

        Toast.makeText(getContext(), "nani", Toast.LENGTH_SHORT);

/**
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        requestQueue.add(jsonObjectRequest);*/
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
            product.setId(jsonObject.getString("id"));
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
