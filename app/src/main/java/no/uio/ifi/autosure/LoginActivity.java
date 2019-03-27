package no.uio.ifi.autosure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import no.uio.ifi.autosure.models.Customer;
import no.uio.ifi.autosure.tasks.CustomerInfoTask;
import no.uio.ifi.autosure.tasks.LoginTask;
import no.uio.ifi.autosure.tasks.TaskListener;

public class LoginActivity extends AppCompatActivity {

    SessionManager sessionManager;
    EditText editTextUsername;
    EditText editTextPassword;
    ProgressBar progressBarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        editTextUsername = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBarLogin = findViewById(R.id.progressBarLogin);

        checkLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    public void login(final View view) {
        String userName = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        final Button loginButton = (Button) view;

        if (userName.isEmpty()) {
            editTextUsername.setError("Username is required!");
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            return;
        }

        progressBarLogin.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        TaskListener loginCallback = new TaskListener<Integer>() {
            @Override
            public void onFinished(Integer sessionId) {
                progressBarLogin.setVisibility(View.INVISIBLE);
                if (sessionId != 0) {
                    sessionManager.setSessionId(sessionId);
                    navigateToClaimsHistory();
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    sessionManager.clearSession();
                }
                loginButton.setEnabled(true);
            }
        };
        new LoginTask(loginCallback, userName, password).execute();
    }

    /**
     * Checks if there's an active session and navigates to claims history activity.
     */
    private void checkLogin() {
        if (sessionManager.isLoggedIn()) {
            TaskListener fetchCustomerInfoCallback = new TaskListener<Customer>() {
                @Override
                public void onFinished(Customer customerResult) {
                    if (customerResult != null) {
                        navigateToClaimsHistory();
                    } else {
                        sessionManager.clearSession();
                    }
                }
            };
            new CustomerInfoTask(fetchCustomerInfoCallback, sessionManager.getSessionId()).execute();
        }
    }

    private void navigateToClaimsHistory() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

