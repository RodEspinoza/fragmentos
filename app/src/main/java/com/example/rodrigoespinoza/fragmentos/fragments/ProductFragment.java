package com.example.rodrigoespinoza.fragmentos.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ListView;

import com.example.rodrigoespinoza.fragmentos.MenuActivity;
import com.example.rodrigoespinoza.fragmentos.R;
import com.example.rodrigoespinoza.fragmentos.model.Product;
import com.example.rodrigoespinoza.fragmentos.model.SqlConecttion;

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
    ArrayList<Product> productArrayList;
    ArrayList<String> detailList;
    ListView listViewProducts;
    View view;
    AddProductFragment ourFragment;
    private OnFragmentInteractionListener mListener;

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
        this.view = inflater.inflate(R.layout.fragment_product, container, false);
        this.btnOpenAddProduct = this.view.findViewById(R.id.btnOpenProduct);
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
        this.listViewProducts = this.view.findViewById(R.id.listProduct);
        getProducts();
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                getContext(), android.R.layout.simple_expandable_list_item_1, detailList);
        this.listViewProducts.setAdapter(arrayAdapter);
        this.listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String info ="Stock " + productArrayList.get(position).getStock();
                Product product = new Product();
                /**Intent intent = new Intent(, Activity_edit_product.class);
                intent.putExtra("product_id", productList.get(position).getId());
                intent.putExtra("product_name", productList.get(position).getName());
                intent.putExtra("product_stock", productList.get(position).getStock());
                startActivity(intent);**/
            }
        });

        return this.view;
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

    private void getProducts(){
        conn = new SqlConecttion(getContext(), "bd_gestor_pedidos", null,1);
        SQLiteDatabase db = conn.getReadableDatabase();
        Product product;
        this.productArrayList = new ArrayList<Product>();
        Cursor cursor = db.rawQuery("SELECT * FROM product", null);
        while(cursor.moveToNext()){
            product = new Product();
            product.setId(cursor.getInt(0));
            product.setName(cursor.getString(1));
            product.setStock(cursor.getInt(2));

            this.productArrayList.add(product);
        }
        setDataToList();
    }

    private void setDataToList()
    {
        this.detailList =  new ArrayList<String>();
        for(int i=0; i < this.productArrayList.size(); i++){
            this.detailList.add(
                    this.productArrayList.get(i).getId().toString() +
                            " :" + this.productArrayList.get(i).getName());
        }
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
