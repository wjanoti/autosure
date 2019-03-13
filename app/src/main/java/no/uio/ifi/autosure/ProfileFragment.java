package no.uio.ifi.autosure;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import no.uio.ifi.autosure.models.Customer;

public class ProfileFragment extends Fragment {

    private static final String CUSTOMER = "customer";
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

}
