package no.uio.ifi.autosure.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Collections;
import java.util.List;

import no.uio.ifi.autosure.helpers.WSHelper;
import no.uio.ifi.autosure.models.ClaimItem;

/**
 * Fetches basic info about all of the claims a customer have.
 */
public class ClaimListTask extends AsyncTask<Void, Void, List<ClaimItem>> {

    private final static String TAG = "ClaimListTask";
    private int sessionId;
    private List<ClaimItem> claimItems;
    private TaskListener taskListener;

    public ClaimListTask(TaskListener taskListener, int sessionId) {
        this.taskListener = taskListener;
        this.sessionId = sessionId;
    }

    @Override
    protected List<ClaimItem> doInBackground(Void... params) {
        try {
            claimItems = WSHelper.getCustomerClaims(sessionId);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return claimItems;
    }

    @Override
    protected void onPostExecute(List<ClaimItem> claimItems) {
        super.onPostExecute(claimItems);
        if (this.taskListener != null) {
            Collections.sort(claimItems);
            this.taskListener.onFinished(claimItems);
        }
    }

}
