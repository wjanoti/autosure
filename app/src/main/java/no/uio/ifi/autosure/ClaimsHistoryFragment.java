package no.uio.ifi.autosure;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import no.uio.ifi.autosure.models.ClaimItem;
import no.uio.ifi.autosure.tasks.ClaimsTask;
import no.uio.ifi.autosure.tasks.TaskListener;

public class ClaimsHistoryFragment extends Fragment {

    private String TAG = "ClaimsHistoryFragment";

    private List<ClaimItem> claimItems;
    private RecyclerView recyclerViewClaimHistory;
    private ProgressBar pbClaimsHistory;
    private ClaimItemAdapter claimItemAdapter;
    private OnFragmentInteractionListener mListener;

    public ClaimsHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment ClaimsHistoryFragment.
     */
    public static ClaimsHistoryFragment newInstance(int sessionId) {
        ClaimsHistoryFragment fragment = new ClaimsHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("sessionId", sessionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchCustomerClaimItems(getArguments().getInt("sessionId"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_claims_history, container, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        claimItemAdapter = new ClaimItemAdapter(claimItems);

        recyclerViewClaimHistory = view.findViewById(R.id.recViewClaimsHistory);
        recyclerViewClaimHistory.setLayoutManager(mLayoutManager);
        recyclerViewClaimHistory.setHasFixedSize(true);
        recyclerViewClaimHistory.setAdapter(claimItemAdapter);
        pbClaimsHistory = view.findViewById(R.id.pbClaimsHistory);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    private void fetchCustomerClaimItems(int sessionId) {
        TaskListener fetchCustomerClaimsCallback = new TaskListener<List<ClaimItem>>() {
            @Override
            public void onFinished(List<ClaimItem> result) {
                if (result.size() > 0) {
                    claimItems = result;
                } else {
                    Toast.makeText(getActivity(), "Error fetching claims", Toast.LENGTH_SHORT).show();
                }
                ClaimsHistoryFragment.this.pbClaimsHistory.setVisibility(View.INVISIBLE);
                ClaimsHistoryFragment.this.claimItemAdapter.setClaimItemsList(claimItems);
                ClaimsHistoryFragment.this.claimItemAdapter.notifyDataSetChanged();
            }
        };
        new ClaimsTask(fetchCustomerClaimsCallback, sessionId).execute();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
