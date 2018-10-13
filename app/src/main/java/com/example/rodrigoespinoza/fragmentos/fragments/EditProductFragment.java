package com.example.rodrigoespinoza.fragmentos.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.rodrigoespinoza.fragmentos.R;
import com.example.rodrigoespinoza.fragmentos.model.Product;

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
    Product product;
    View view;



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
        //this.btnDeleteProduct.setOnClickListener();
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
