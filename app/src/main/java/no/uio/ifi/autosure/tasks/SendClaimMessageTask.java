package no.uio.ifi.autosure.tasks;

import android.os.AsyncTask;
import android.util.Log;

import no.uio.ifi.autosure.helpers.WSHelper;

/**
 * Sends a message about a claim
 */
public class SendClaimMessageTask extends AsyncTask<Void, Void, Boolean> {

    private final static String TAG = "SendClaimMessageTask";
    private int sessionId;
    private int claimId;
    private String message;
    private boolean sendSuccessful;
    private TaskListener taskListener;

    public SendClaimMessageTask(TaskListener taskListener, int sessionId, int claimId, String message) {
        this.taskListener = taskListener;
        this.sessionId = sessionId;
        this.message = message;
        this.claimId = claimId;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            sendSuccessful = WSHelper.sendClaimMessage(sessionId, claimId, message);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return sendSuccessful;
    }

    @Override
    protected void onPostExecute(Boolean sendSuccessful) {
        super.onPostExecute(sendSuccessful);
        if (this.taskListener != null) {
            this.taskListener.onFinished(sendSuccessful);
        }
    }

}
