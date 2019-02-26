package no.uio.ifi.autosure;

import android.os.AsyncTask;
import android.util.Log;

public class LogoutTask extends AsyncTask<Void, Void, Boolean> {

    private final static String TAG = "LoginTask";
    private int sessionId;
    private boolean logoutSuccessful;
    private TaskListener taskListener;

    LogoutTask(TaskListener taskListener, int sessionId) {
        this.taskListener = taskListener;
        this.sessionId = sessionId;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try{
            logoutSuccessful = WSHelper.logout(sessionId);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return logoutSuccessful;
    }

    @Override
    protected void onPostExecute(Boolean logoutSuccessful) {
        super.onPostExecute(logoutSuccessful);
        if(this.taskListener != null) {
            this.taskListener.onFinished(logoutSuccessful);
        }
    }

}
