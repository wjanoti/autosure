package no.uio.ifi.autosure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    SessionManager sessionManager;
    EditText editTextUsername;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        editTextUsername = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);

        checkLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    public void login(View view) {
        String userName = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (userName.isEmpty()) {
            editTextUsername.setError("Username is required!");
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            return;
        }

        TaskListener loginCallback = new TaskListener() {
            @Override
            public void onFinished(Integer sessionId) {
                if (sessionId != 0) {
                    sessionManager.setSessionId(sessionId);
                    navigateToClaimsHistory();
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        };

        new LoginTask(loginCallback, userName, password).execute();
    }

    private void checkLogin() {
        if (sessionManager.isLoggedIn()) {
            navigateToClaimsHistory();
        }
    }

    private void navigateToClaimsHistory() {
        Intent intent = new Intent(LoginActivity.this, ClaimsHistoryActivity.class);
        startActivity(intent);
    }

}

