package no.uio.ifi.autosure.tasks;

import android.os.AsyncTask;
import android.util.Log;

import no.uio.ifi.autosure.helpers.WSHelper;
import no.uio.ifi.autosure.models.Customer;

/**
 * Fetches info about a customer
 */
public class CustomerInfoTask extends AsyncTask<Void, Void, Customer> {

    private final static String TAG = "CustomerInfoTask";
    private int sessionId;
    private Customer customer;
    private TaskListener taskListener;

    public CustomerInfoTask(TaskListener taskListener, int sessionId) {
        this.taskListener = taskListener;
        this.sessionId = sessionId;
    }

    @Override
    protected Customer doInBackground(Void... params) {
        try {
            customer = WSHelper.getCustomerInfo(sessionId);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return customer;
    }

    @Override
    protected void onPostExecute(Customer customer) {
        super.onPostExecute(customer);
        if (this.taskListener != null) {
            this.taskListener.onFinished(customer);
        }
    }

}
