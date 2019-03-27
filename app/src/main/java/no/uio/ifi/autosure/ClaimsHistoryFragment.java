package no.uio.ifi.autosure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import no.uio.ifi.autosure.models.ClaimItem;
import no.uio.ifi.autosure.tasks.ClaimListTask;
import no.uio.ifi.autosure.tasks.TaskListener;

public class ClaimsHistoryFragment extends Fragment {

    private List<ClaimItem> claimItems;
    private ProgressBar pbClaimsHistory;
    private ClaimItemAdapter claimItemAdapter;
    private SwipeRefreshLayout swipeContainer;
    private TextView txtNoClaimsMessage;

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

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchCustomerClaimItems(getArguments().getInt("sessionId"));
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorAccent);

        pbClaimsHistory = view.findViewById(R.id.pbClaimsHistory);

        txtNoClaimsMessage = view.findViewById(R.id.txtNoClaimsMessage);
        claimItemAdapter = new ClaimItemAdapter(claimItems);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerViewClaimHistory = view.findViewById(R.id.recViewClaimsHistory);
        recyclerViewClaimHistory.setLayoutManager(mLayoutManager);
        recyclerViewClaimHistory.setHasFixedSize(true);
        recyclerViewClaimHistory.setAdapter(claimItemAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchCustomerClaimItems(getArguments().getInt("sessionId"));
        pbClaimsHistory.setVisibility(View.INVISIBLE);
        ((MainActivity) getActivity()).setActionBarTitle("Claims History");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void fetchCustomerClaimItems(int sessionId) {
        TaskListener fetchCustomerClaimsCallback = new TaskListener<List<ClaimItem>>() {
            @Override
            public void onFinished(List<ClaimItem> result) {
                if (result != null) {
                    claimItems = result;
                    claimItemAdapter.setClaimItemsList(claimItems);
                    txtNoClaimsMessage.setVisibility(
                            claimItemAdapter.getItemCount() > 0 ? View.INVISIBLE : View.VISIBLE
                    );
                    claimItemAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Could not fetch claims from server", Toast.LENGTH_SHORT).show();
                }
                swipeContainer.setRefreshing(false);
                pbClaimsHistory.setVisibility(View.INVISIBLE);
            }
        };
        new ClaimListTask(fetchCustomerClaimsCallback, sessionId).execute();
    }

}
