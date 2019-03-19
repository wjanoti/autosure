package no.uio.ifi.autosure.tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import no.uio.ifi.autosure.helpers.WSHelper;

public class PlatesTask extends AsyncTask<Void, Void, List<String>> {

    private final static String TAG = "PlatesTask";
    private int sessionId;
    private List<String> plates;
    private TaskListener taskListener;

    public PlatesTask(TaskListener taskListener, int sessionId) {
        this.taskListener = taskListener;
        this.sessionId = sessionId;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        try {
            plates = WSHelper.listPlates(sessionId);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return plates;
    }

    @Override
    protected void onPostExecute(List<String> plates) {
        super.onPostExecute(plates);
        if (this.taskListener != null) {
            this.taskListener.onFinished(plates);
        }
    }

}
