package no.uio.ifi.autosure.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionManager {

    private SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Stores the session id of the current session.
     *
     * @param sessionId session id received from the server
     */
    public void setSessionId(int sessionId) {
        sharedPreferences.edit().putInt("sessionId", sessionId).apply();
    }

    /**
     * Retrieves the current session id, returns -1 if there is no current session.
     *
     * @return int
     */
    public int getSessionId() {
        return sharedPreferences.getInt("sessionId", -1);
    }

    /**
     * Checks if the user is logged in.
     *
     * @return boolean value
     */
    public boolean isLoggedIn() {
        return getSessionId() != -1;
    }

    /**
     * Clear sessions from storage.
     */
    public void clearSession() {
        sharedPreferences.edit().clear().apply();
    }
}