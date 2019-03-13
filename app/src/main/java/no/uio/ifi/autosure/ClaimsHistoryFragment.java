package no.uio.ifi.autosure;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import no.uio.ifi.autosure.models.ClaimItem;
import no.uio.ifi.autosure.tasks.ClaimsTask;
import no.uio.ifi.autosure.tasks.TaskListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClaimsHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClaimsHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
     *
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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_claims_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerViewClaimHistory = this.getActivity().findViewById(R.id.recViewClaimsHistory);
        recyclerViewClaimHistory.setLayoutManager(mLayoutManager);
        recyclerViewClaimHistory.setHasFixedSize(true);
        claimItemAdapter = new ClaimItemAdapter(claimItems, getContext());
        recyclerViewClaimHistory.setAdapter(claimItemAdapter);
        pbClaimsHistory = this.getActivity().findViewById(R.id.pbClaimsHistory);
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
            }
        };
        new ClaimsTask(fetchCustomerClaimsCallback, sessionId).execute();
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
