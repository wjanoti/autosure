package no.uio.ifi.autosure;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import no.uio.ifi.autosure.models.ClaimItem;

public class ClaimItemAdapter extends RecyclerView.Adapter<ClaimItemAdapter.ViewHolder> {

    private List<ClaimItem> claimItemsList;
    private Context context;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtClaimItemTitle;
        public TextView txtClaimItemId;

        public ViewHolder(View v) {
            super(v);
            txtClaimItemTitle = v.findViewById(R.id.txtClaimItemTitle);
            txtClaimItemId = v.findViewById(R.id.txtClaimItemId);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ClaimItemAdapter(List<ClaimItem> claimItemsList, Context context) {
        this.claimItemsList = claimItemsList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ClaimItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View claimItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.claim_item_view, parent, false);

        return new ViewHolder(claimItemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ClaimItem claimItem = claimItemsList.get(position);
        holder.txtClaimItemTitle.setText(claimItem.getTitle());
        holder.txtClaimItemId.setText(claimItem.getId());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (claimItemsList != null) {
            return claimItemsList.size();
        }
        return 0;
    }

    public void setClaimItemsList(List<ClaimItem> claimItemsList) {
        this.claimItemsList = claimItemsList;
    }
}