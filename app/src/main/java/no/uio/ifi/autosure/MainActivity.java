package no.uio.ifi.autosure;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import no.uio.ifi.autosure.models.Customer;
import no.uio.ifi.autosure.tasks.CustomerInfoTask;
import no.uio.ifi.autosure.tasks.LogoutTask;
import no.uio.ifi.autosure.tasks.TaskListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ClaimsHistoryFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    private static SessionManager sessionManager;

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

        // load default fragment
        loadFragment(ClaimsHistoryFragment.newInstance());
        navigationView.getMenu().getItem(1).setChecked(true);
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

        // keeps selected option highlighted on the drawer menu
        item.setChecked(true);

        try {
            switch (item.getItemId()) {
                case R.id.nav_profile:
                    fetchCustomerInfo(sessionManager.getSessionId());
                    break;
                case R.id.nav_history:
                    loadFragment(ClaimsHistoryFragment.newInstance());
                    break;
                case R.id.nav_new_claim:
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void fetchCustomerInfo(int sessionId) {
        TaskListener fetchCustomerInfoCallback = new TaskListener<Customer>() {
            @Override
            public void onFinished(Customer customerResult) {
                Fragment profileFragment = ProfileFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putSerializable("customer", customerResult);
                profileFragment.setArguments(bundle);
                loadFragment(profileFragment);
            }
        };
        new CustomerInfoTask(fetchCustomerInfoCallback, sessionId).execute();
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


    private void loadFragment(Fragment fragment) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.clContent, fragment).commit();
    }

}
