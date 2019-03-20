package no.uio.ifi.autosure.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Collections;
import java.util.List;

import no.uio.ifi.autosure.helpers.WSHelper;
import no.uio.ifi.autosure.models.ClaimItem;
import no.uio.ifi.autosure.models.ClaimMessage;

/**
 * Fetches basic info about all of the claims a customer have.
 */
public class ListClaimMessagesTask extends AsyncTask<Void, Void, List<ClaimMessage>> {

    private final static String TAG = "ListClaimMessagesTask";
    private int sessionId;
    private int claimId;
    private List<ClaimMessage> claimMessages;
    private TaskListener taskListener;

    public ListClaimMessagesTask(TaskListener taskListener, int sessionId, int claimId) {
        this.taskListener = taskListener;
        this.sessionId = sessionId;
        this.claimId = claimId;
    }

    @Override
    protected List<ClaimMessage> doInBackground(Void... params) {
        try {
            claimMessages = WSHelper.getClaimMessages(sessionId, claimId);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return claimMessages;
    }

    @Override
    protected void onPostExecute(List<ClaimMessage> claimMessages) {
        super.onPostExecute(claimMessages);
        if (this.taskListener != null) {
            this.taskListener.onFinished(claimMessages);
        }
    }

}
