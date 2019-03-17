package no.uio.ifi.autosure;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import no.uio.ifi.autosure.models.ClaimItem;

public class ClaimItemAdapter extends RecyclerView.Adapter<ClaimItemAdapter.ViewHolder> {

    private List<ClaimItem> claimItemsList;

    // Reference to the view of each data item in the recycler view.
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView txtClaimItemTitle;
        TextView txtClaimItemId;

        ViewHolder(View v) {
            super(v);
            txtClaimItemTitle = v.findViewById(R.id.txtClaimItemTitle);
            txtClaimItemId = v.findViewById(R.id.txtClaimItemId);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // loads the claim details fragment
                    int sessionId = ((MainActivity)v.getContext()).getSessionManager().getSessionId();
                    ((MainActivity)v.getContext()).loadFragment(
                        ClaimDetailsFragment.newInstance(
                                sessionId,
                                Integer.parseInt(txtClaimItemId.getText().toString())
                        ), true
                    );
                }
            });
        }
    }

    ClaimItemAdapter(List<ClaimItem> claimItemsList) {
        this.claimItemsList = claimItemsList;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ClaimItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                          int viewType) {
        View claimItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.claim_item_view, parent, false);

        return new ViewHolder(claimItemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ClaimItem claimItem = claimItemsList.get(position);
        holder.txtClaimItemTitle.setText(claimItem.getTitle());
        holder.txtClaimItemId.setText(Integer.toString(claimItem.getId()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (claimItemsList != null) {
            return claimItemsList.size();
        }
        return 0;
    }

    void setClaimItemsList(List<ClaimItem> claimItemsList) {
        this.claimItemsList = claimItemsList;
    }
}