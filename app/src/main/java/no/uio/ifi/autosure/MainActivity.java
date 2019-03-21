package no.uio.ifi.autosure;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import no.uio.ifi.autosure.models.Customer;
import no.uio.ifi.autosure.tasks.CustomerInfoTask;
import no.uio.ifi.autosure.tasks.LogoutTask;
import no.uio.ifi.autosure.tasks.TaskListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static SessionManager sessionManager;
    private Customer customer;
    private TextView navUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navUserName = navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);

        // load default fragment
        loadFragment(ClaimsHistoryFragment.newInstance(sessionManager.getSessionId()), false);
        navigationView.getMenu().getItem(1).setChecked(true);

        fetchCustomerInfo(sessionManager.getSessionId());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Bundle bundle = new Bundle();

        // keeps selected option highlighted on the drawer menu
        item.setChecked(true);

        try {
            switch (item.getItemId()) {
                case R.id.nav_profile:
                    Fragment profileFragment = ProfileFragment.newInstance();
                    bundle.putSerializable("customer", this.customer);
                    profileFragment.setArguments(bundle);
                    loadFragment(profileFragment, true);
                    break;
                case R.id.nav_history:
                    Fragment claimHistoryFragment = ClaimsHistoryFragment.newInstance(sessionManager.getSessionId());
                    loadFragment(claimHistoryFragment, true);
                    break;
                case R.id.nav_logout:
                    logout();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setActionBarTitle(String title) {
        setTitle(title);
    }

    public void fetchCustomerInfo(int sessionId) {
        TaskListener fetchCustomerInfoCallback = new TaskListener<Customer>() {
            @Override
            public void onFinished(Customer customerResult) {
                customer = customerResult;
                navUserName.setText(customerResult.getName());
            }
        };
        new CustomerInfoTask(fetchCustomerInfoCallback, sessionId).execute();
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.clContent, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentManager.popBackStack();
        fragmentTransaction.commit();
    }

    private void logout() {
        TaskListener logoutCallback = new TaskListener<Boolean>() {
            @Override
            public void onFinished(Boolean logoutSuccessful) {
                if (logoutSuccessful) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    sessionManager.clearSession();
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Logout failed", Toast.LENGTH_SHORT).show();
                }
            }
        };
        new LogoutTask(logoutCallback, sessionManager.getSessionId()).execute();
    }

    public void newClaim(View view) {
        Intent intent = new Intent(MainActivity.this, NewClaimActivity.class);
        intent.putExtra("sessionId", sessionManager.getSessionId());
        startActivity(intent);
    }

    public void claimMessages(View view) {
        Intent intent = new Intent(MainActivity.this, ClaimMessagesActivity.class);
        startActivity(intent);
    }
}
