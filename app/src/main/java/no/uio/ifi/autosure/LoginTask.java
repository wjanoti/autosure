package no.uio.ifi.autosure;

import android.os.AsyncTask;
import android.util.Log;

public class LoginTask extends AsyncTask<Void, Void, Integer> {

    private final static String TAG = "LoginTask";
    private String username;
    private String password;
    private TaskListener taskListener;

    LoginTask(TaskListener taskListener, String userName, String password) {
        this.taskListener = taskListener;
        this.username = userName;
        this.password = password;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        int sessionId = 0;

        try{
            sessionId = WSHelper.login(username,password);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return sessionId;
    }

    @Override
    protected void onPostExecute(Integer sessionId) {
        super.onPostExecute(sessionId);
        if(this.taskListener != null) {
            this.taskListener.onFinished(sessionId);
        }
    }

}
