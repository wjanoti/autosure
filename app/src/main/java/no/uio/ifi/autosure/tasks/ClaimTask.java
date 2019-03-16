package no.uio.ifi.autosure.tasks;

import android.os.AsyncTask;
import android.util.Log;

import no.uio.ifi.autosure.WSHelper;
import no.uio.ifi.autosure.models.Claim;

/**
 * Fetches detailed info about one claim.
 */
public class ClaimTask extends AsyncTask<Void, Void, Claim> {

    private final static String TAG = "ClaimTask";
    private int sessionId;
    private int claimId;
    private Claim claim;
    private TaskListener taskListener;

    public ClaimTask(TaskListener taskListener, int sessionId, int claimId) {
        this.taskListener = taskListener;
        this.sessionId = sessionId;
        this.claimId = claimId;
    }

    @Override
    protected Claim doInBackground(Void... params) {
        try{
            claim = WSHelper.getClaimInfo(sessionId, claimId);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return claim;
    }

    @Override
    protected void onPostExecute(Claim claim) {
        super.onPostExecute(claim);
        if(this.taskListener != null) {
            this.taskListener.onFinished(claim);
        }
    }

}
