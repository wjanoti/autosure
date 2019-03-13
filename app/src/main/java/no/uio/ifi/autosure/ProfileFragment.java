package no.uio.ifi.autosure;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import no.uio.ifi.autosure.models.Customer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String CUSTOMER = "customer";
    private OnFragmentInteractionListener mListener;
    private Customer customer;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        customer = (Customer) bundle.getSerializable(CUSTOMER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView txtCustomerName = this.getActivity().findViewById(R.id.txtCustomerName);
        TextView txtfiscalNumberTxt =  this.getActivity().findViewById(R.id.txtFiscalNumber);
        TextView txtaddress =  this.getActivity().findViewById(R.id.txtAddress);
        TextView txtdateOfBirth =  this.getActivity().findViewById(R.id.txtDateOfBirth);
        TextView txtinsurancePolicyNumber =  this.getActivity().findViewById(R.id.txtInsurancePolicyNumber);
        txtCustomerName.setText(customer.getName());
        txtaddress.setText(customer.getAddress());
        txtdateOfBirth.setText(customer.getDateOfBirth());
        txtinsurancePolicyNumber.setText(Integer.toString(customer.getPolicyNumber()));
        txtfiscalNumberTxt.setText(Integer.toString(customer.getFiscalNumber()));
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
