package no.uio.ifi.autosure.tasks;

import android.os.AsyncTask;
import android.util.Log;

import no.uio.ifi.autosure.helpers.WSHelper;
import no.uio.ifi.autosure.models.Claim;

/**
 * Fetches detailed info about one claim.
 */
public class NewClaimTask extends AsyncTask<Void, Void, Boolean> {

    private final static String TAG = "ClaimTask";
    private int sessionId;
    private String claimTitle;
    private String occurrenceDate;
    private String plate;
    private String claimDescription;
    private TaskListener taskListener;

    public NewClaimTask(TaskListener taskListener, int sessionId, String claimTitle,
                        String ocurrenceDate, String plate, String claimDescription) {
        this.taskListener = taskListener;
        this.sessionId = sessionId;
        this.claimTitle = claimTitle;
        this.occurrenceDate = ocurrenceDate;
        this.plate = plate;
        this.claimDescription = claimDescription;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean successful = false;
        try {
            successful = WSHelper.submitNewClaim(sessionId, claimTitle, occurrenceDate, plate, claimDescription);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return successful;
    }

    @Override
    protected void onPostExecute(Boolean successful) {
        super.onPostExecute(successful);
        if (this.taskListener != null) {
            this.taskListener.onFinished(successful);
        }
    }

}
