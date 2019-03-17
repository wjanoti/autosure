package no.uio.ifi.autosure;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import no.uio.ifi.autosure.models.Claim;
import no.uio.ifi.autosure.tasks.ClaimTask;
import no.uio.ifi.autosure.tasks.TaskListener;

public class ClaimDetailsFragment extends Fragment {

    private ProgressBar pbClaimDetails;
    private Claim claim;

    public ClaimDetailsFragment() {
        // Required empty public constructor
    }

    public static ClaimDetailsFragment newInstance(int sessionId, int claimId) {
        ClaimDetailsFragment fragment = new ClaimDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("sessionId", sessionId);
        bundle.putInt("claimId", claimId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchClaimDetails(getArguments().getInt("sessionId"), getArguments().getInt("claimId"));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Claim Details");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_claim, container, false);

        pbClaimDetails = view.findViewById(R.id.pbClaimDetails);

        return view;
    }

    /**
     * Populate the UI with Claim data
     *
     * @param claim Claim object
     */
    public void bindData(Claim claim) {
        TextView txtClaimTitle = this.getView().findViewById(R.id.txtClaimTitle);
        TextView txtClaimId = this.getView().findViewById(R.id.txtClaimId);
        TextView txtClaimPlate = this.getView().findViewById(R.id.txtClaimPlate);
        TextView txtClaimIssuingDate = this.getView().findViewById(R.id.txtClaimIssuingDate);
        TextView txtClaimStatus = this.getView().findViewById(R.id.txtClaimStatus);
        TextView txtClaimDescription = this.getView().findViewById(R.id.txtClaimDescription);

        txtClaimTitle.setText(claim.getTitle());
        txtClaimId.setText(Integer.toString(this.claim.getId()));
        txtClaimPlate.setText(claim.getPlate());
        txtClaimIssuingDate.setText(claim.getSubmissionDate());
        switch (claim.getStatus()) {
            case ACCEPTED:
                txtClaimStatus.setText("Accepted");
                txtClaimStatus.setTextColor(getResources().getColor(R.color.approved));
                break;
            case DENIED:
                txtClaimStatus.setText("Denied");
                txtClaimStatus.setTextColor(getResources().getColor(R.color.denied));
                break;
            case PENDING:
                txtClaimStatus.setText("Pending");
                txtClaimStatus.setTextColor(getResources().getColor(R.color.pending));
                break;
        }
        txtClaimDescription.setText(claim.getDescription());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void fetchClaimDetails(int sessionId, int claimId) {

        TaskListener fetchClaimDetailsCallback = new TaskListener<Claim>() {
            @Override
            public void onFinished(Claim result) {
                    claim = result;
                pbClaimDetails.setVisibility(View.INVISIBLE);

                bindData(claim);

            }
        };
        new ClaimTask(fetchClaimDetailsCallback, sessionId, claimId).execute();
    }

}

