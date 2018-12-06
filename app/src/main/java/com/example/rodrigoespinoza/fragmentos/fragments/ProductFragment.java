package com.example.rodrigoespinoza.fragmentos.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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

import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rodrigoespinoza.fragmentos.R;
import com.example.rodrigoespinoza.fragmentos.model.Product;
import com.example.rodrigoespinoza.fragmentos.model.SqlConecttion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {
    SqlConecttion conn;
    Button btnOpenAddProduct;

    ArrayList<String> detailList;

    View view;
    ListView listView;
    ArrayList<Product> productList;

    //Componente de Progreso
    ProgressDialog progressDialog;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance() {
        ProductFragment fragment = new ProductFragment();
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
        this.view = inflater.inflate(
                R.layout.fragment_product, container, false);
        this.btnOpenAddProduct = this.view.findViewById(R.id.btnOpenProduct);
        productList = new ArrayList<>();
        detailList = new ArrayList<>();
        this.btnOpenAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductFragment nextFrag = new AddProductFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.containerFragmentMenu, nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        listView = this.view.findViewById(R.id.listProduct);

        getProducts();




        return this.view;
    }



    private void getProducts() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = new Product();
                            product.setId(document.getId());
                            product.setStock(Integer.parseInt(document.get("stock").toString()));
                            product.setName((document.get("name").toString()));
                            productList.add(product);
                        }
                        setDataToList();
                        }
                        else{
                            Log.w("Products", "Error getting documents.", task.getException());
                            progressDialog.dismiss();
                        }
                    }
                });

        progressDialog.dismiss();

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
    private void setDataToList()
    {
        this.detailList =  new ArrayList<String>();
        for(int i=0; i < this.productList.size();i++){
            this.detailList.add(

                    this.productList.get(i).getName() + ". Stock:" + this.productList.get(i).getStock());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_expandable_list_item_1, detailList);

        this.listView.setAdapter(arrayAdapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditProductFragment nextFrag = new EditProductFragment();
                Bundle bundle = new Bundle();
                bundle.putString("product_id", productList.get(position).getId());
                bundle.putInt("product_stock", productList.get(position).getStock());
                bundle.putString("product_name", productList.get(position).getName());
                nextFrag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(
                        R.id.containerFragmentMenu, nextFrag, "findThisFrag"
                ).addToBackStack(null).commit();
            }
        });


    }
}
