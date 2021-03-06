package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.app.AppConfig;
import com.example.myapplication.app.SessionManager;
import com.example.myapplication.fragments.BlankFragment;
import com.example.myapplication.fragments.ChatFragment;
import com.example.myapplication.fragments.MainFragment;
import com.example.myapplication.fragments.ProfileFragment;
import com.example.myapplication.fragments.SearchFragment;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = null;
    Class fragmentClass;
    CharSequence title=null;
    private SearchView searchView;
    private ImageView imageView;
    private TextView name;
    private SessionManager session;
    private Button moveMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new SessionManager(getApplicationContext());

        fragmentClass = MainFragment.class;
        title="Home";
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContent, fragment)
                .commit();
        setTitle(title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {}
            @Override public void onDrawerOpened(View drawerView) {}
            @Override public void onDrawerStateChanged(int newState) {}

            @Override
            public void onDrawerClosed(View drawerView) {
                if (fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flContent, fragment)
                            .commit();
                    setTitle(title);
                    fragment = null;
                }
            }
        });

        View headerLayout=navigationView.getHeaderView(0);
        imageView=headerLayout.findViewById(R.id.imageView);
        Picasso.with(getApplicationContext()).load(AppConfig.URL_IMAGE+session.getImage()).into(imageView);
        name=headerLayout.findViewById(R.id.name);
        name.setText("Welcome "+session.getName());
        moveMe = findViewById(R.id.moveMe);
        if(session.getMarket_Open().equals("all")){
            moveMe.setText("Open now");
        }
        else {
            moveMe.setText("Show all");
        }

        moveMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(session.getMarket_Open().equals("all")){
                    session.setMarket_Open("open");
                }
                else {
                    session.setMarket_Open("all");
                }
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("query",query);

                fragmentClass = SearchFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragment.setArguments(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flContent, fragment)
                            .commit();
                    setTitle(query);
                    fragment = null;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            fragmentClass = MainFragment.class;
        }
        else if (id == R.id.nav_profile) {
            fragmentClass = ProfileFragment.class;
        }
        else if (id == R.id.nav_chat) {
            fragmentClass = ChatFragment.class;
        }
        else if (id == R.id.nav_sales) {
            fragmentClass = BlankFragment.class;
        }
        else if (id == R.id.nav_public) {
            fragmentClass = BlankFragment.class;
        }
        else if (id == R.id.nav_business) {
            fragmentClass = BlankFragment.class;
        }
        else if (id == R.id.nav_about) {
            fragmentClass = BlankFragment.class;
        }
        else if (id == R.id.nav_contact) {
            fragmentClass = BlankFragment.class;
        }
        else if (id == R.id.nav_log_out) {
            fragmentClass = BlankFragment.class;
            session.setLogin(false);
            session.setImage("no_pix.png");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        item.setChecked(true);

        title=item.getTitle();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
