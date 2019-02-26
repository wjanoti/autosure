package no.uio.ifi.autosure;

public interface TaskListener<T> {

    /**
     * Callback passed to tasks to return values to the calling activity.
     */
    void onFinished(T value);

}
