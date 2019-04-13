package no.uio.ifi.autosure;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
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
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        customer = (Customer) bundle.getSerializable(CUSTOMER);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView txtCustomerName = view.findViewById(R.id.txtCustomerName);
        TextView txtfiscalNumberTxt = view.findViewById(R.id.txtFiscalNumber);
        TextView txtaddress = view.findViewById(R.id.txtAddress);
        TextView txtdateOfBirth = view.findViewById(R.id.txtDateOfBirth);
        TextView txtinsurancePolicyNumber = view.findViewById(R.id.txtInsurancePolicyNumber);
        if (customer != null) {
            txtCustomerName.setText(customer.getName());
            txtaddress.setText(customer.getAddress());
            txtdateOfBirth.setText(customer.getDateOfBirth());
            txtinsurancePolicyNumber.setText(Integer.toString(customer.getPolicyNumber()));
            txtfiscalNumberTxt.setText(Integer.toString(customer.getFiscalNumber()));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Profile");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
