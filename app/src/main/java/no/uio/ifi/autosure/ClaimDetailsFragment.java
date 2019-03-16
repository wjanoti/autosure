package no.uio.ifi.autosure;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import no.uio.ifi.autosure.models.Claim;
import no.uio.ifi.autosure.tasks.ClaimTask;
import no.uio.ifi.autosure.tasks.TaskListener;

public class ClaimDetailsFragment extends Fragment {

    private TextView txtClaimTitle;
    private TextView txtClaimId;
    private TextView txtClaimPlate;
    private TextView txtClaimIssuingDate;
    private TextView txtClaimStatus;
    private TextView txtClaimDescription;
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_claim, container, false);

        pbClaimDetails = view.findViewById(R.id.pbClaimDetails);

        return view;
    }

    public void bindData(Claim claim) {
        txtClaimTitle = this.getView().findViewById(R.id.txtClaimTitle);
        txtClaimId = this.getView().findViewById(R.id.txtClaimId);
        txtClaimPlate = this.getView().findViewById(R.id.txtClaimPlate);
        txtClaimIssuingDate = this.getView().findViewById(R.id.txtClaimIssuingDate);
        txtClaimStatus = this.getView().findViewById(R.id.txtClaimStatus);
        txtClaimDescription = this.getView().findViewById(R.id.txtClaimDescription);

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
        this.txtClaimDescription.setText(claim.getDescription());
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

