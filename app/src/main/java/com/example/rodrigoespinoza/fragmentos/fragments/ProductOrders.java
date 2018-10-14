 package com.example.rodrigoespinoza.fragmentos.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.rodrigoespinoza.fragmentos.R;
import com.example.rodrigoespinoza.fragmentos.model.Order;
import com.example.rodrigoespinoza.fragmentos.model.SqlConecttion;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

 /**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductOrders.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductOrders extends Fragment {
    private OnFragmentInteractionListener mListener;
    SqlConecttion conn;
    Button btnOpenAddOrder;
    ArrayList<Order> ProductOrderList;
    ArrayList<String> detailList;
    ListView listViewProductOrder;
    View view;
    // añadir fragmento agregar nuevar orden
    public ProductOrders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProductOrders.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductOrders newInstance(String param1, String param2) {
        ProductOrders fragment = new ProductOrders();
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
                R.layout.fragment_product_orders, container, false);
        this.listViewProductOrder = this.view.findViewById(R.id.listProductOrder);
        getProductOrder();
        return this.view;
    }

     private void getProductOrder() {
        conn = new SqlConecttion(getContext(), "bd_gestor_pedidos", null, 1);
         SQLiteDatabase db = conn.getReadableDatabase();
         Order order;
         Cursor cursor = db.rawQuery("SELECT * FROM pedido", null);
         this.ProductOrderList = new ArrayList<>();
         while (cursor.moveToNext()){
             order = new Order();
             order.setId_order(cursor.getInt(0));
             String dateTime = cursor.getString(1);
             DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             Date date = null;
             try {
                 date = iso8601Format.parse(dateTime);
             } catch (ParseException e) {
                 Log.e(String.valueOf(Log.WARN), "Parsing ISO8601 datetime failed", e);
             }
             order.setFecha(date);
             this.ProductOrderList.add(order);
             setDataToList();
             db.close();
             conn.close();

         }
     }

     private void setDataToList() {
        this.detailList = new ArrayList<>();
        for(int i=0; i < this.ProductOrderList.size(); i++){
            this.detailList.add(
                    "Ordern : "
                    + this.ProductOrderList.get(i).getId_order().toString()
                            + " Fecha:" + this.ProductOrderList.get(i).getFecha());
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
