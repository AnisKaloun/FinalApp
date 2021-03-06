/**
 * Created by SADA INFO on 10/03/2018.
 */
package com.android.pfe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pfe.R;
import com.android.pfe.fragment.ArticleFragment;
import com.android.pfe.fragment.ContactFragment;
import com.android.pfe.fragment.HomeFragment;
import com.android.pfe.fragment.NotificationsFragment;
import com.android.pfe.fragment.RechercheFragment;
import com.android.pfe.other.Notification;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    // tags pour les fragments
    private static final String TAG_HOME = "Home";
    private static final String TAG_CONTACT = "Contact";
    private static final String TAG_ARTICLE = "Article";
    private static final String TAG_RECHERCHE = "Recherche";
    private static final String TAG_NOTIFICATION = "notification";
  
        // index pour connaitre l'item selectionné
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_HOME;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgProfile;
    private TextView txtName, txtWebsite,profil;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;
    // les titres des fragments
    private String[] activityTitles;

    // pour recharger le fragment home aprés avoir appuyé sur back
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private FirebaseAuth auth;

    private DatabaseReference Database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth=FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        mHandler = new Handler();
        //database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = navHeader.findViewById(R.id.name);
        txtWebsite = navHeader.findViewById(R.id.job);

        imgProfile = navHeader.findViewById(R.id.img_profile);
        
        if(auth.getCurrentUser()!=null) {
            if(auth.getCurrentUser().getDisplayName()!=null) {
                txtName.setText(auth.getCurrentUser().getDisplayName().toString().trim());
            }
            txtWebsite.setText(auth.getCurrentUser().getEmail().toString().trim());

        }
        Database=FirebaseDatabase.getInstance().getReference("User").child(auth.getCurrentUser().getUid()).child("picUrl");

        Database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             if (dataSnapshot.exists())
             {
                     String URL= (String) dataSnapshot.getValue();
                     if(URL!=null) {
                         Log.w("URLPIC", ""+URL);

                         Glide.with(MenuActivity.this).load(URL)
                                 .into(imgProfile);

                     }

             }
             else
             {
                 Log.w("URLPIC", "doesn't exists");
             }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
             
        //je peut mettre la photos içi
        profil= navHeader.findViewById(R.id.Profil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insc = new Intent(MenuActivity.this,ProfilActivity.class);
                startActivity(insc);

            }
        });
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        ValueEventListener dotlistener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Notification notif = dataSnapshot.getValue(Notification.class);
                if (dataSnapshot.exists()) {
                    if (notif.isState() == true) {
                        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot);
                    }
                    else{
                        navigationView.getMenu().getItem(4).setActionView(null);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase=FirebaseDatabase.getInstance().getReference("User").child(auth.getCurrentUser().getUid()).child("Notification");
        mDatabase.addValueEventListener(dotlistener);

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private void loadHomeFragment() {

        selectNavMenu();

        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {

                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };


        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        drawer.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();

                return homeFragment;
            case 1:
                // Contact
                ContactFragment contactFragment = new ContactFragment();
                return contactFragment;
            case 2:
                // Article
                ArticleFragment articleFragment = new ArticleFragment();
                return articleFragment;
            case 3:
                //  recherche
                RechercheFragment rechercheFragment = new RechercheFragment();
                return rechercheFragment;

            case 4:
                // notifications fragment
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                return notificationsFragment;


            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;

                        break;
                    case R.id.nav_contact:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_CONTACT;
                        break;
                    case R.id.nav_article:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_ARTICLE;
                        break;

                    case R.id.nav_recherche:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_RECHERCHE;
                        break;
                    default:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }


        if (shouldLoadHomeFragOnBackPress) {

            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.menu, menu);
        }


        if (navItemIndex == 4) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "vous allez vous déconnecter", Toast.LENGTH_LONG).show();
            auth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            navigationView.getMenu().getItem(4).setActionView(null);
            Toast.makeText(getApplicationContext(), " Marquer comme lue", Toast.LENGTH_LONG).show();
            mDatabase=FirebaseDatabase.getInstance().getReference("User").child(auth.getCurrentUser().getUid()).child("Notification");
            Notification notification=new Notification();
            notification.setState(false);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("state",false);
            mDatabase.updateChildren(childUpdates);

        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            navigationView.getMenu().getItem(4).setActionView(null);
            Toast.makeText(getApplicationContext(), "Supprimer toute les notifications", Toast.LENGTH_LONG).show();
            mDatabase=FirebaseDatabase.getInstance().getReference("User").child(auth.getCurrentUser().getUid()).child("Notification");
            Notification notification=new Notification();
            notification.setState(false);
            mDatabase.setValue(notification);


            FragmentManager fragMgr = getSupportFragmentManager();
            FragmentTransaction fragTrans = fragMgr.beginTransaction();
            NotificationsFragment myFragment = new NotificationsFragment();
            fragTrans.replace(R.id.frame, myFragment);
            fragTrans.addToBackStack(null);
            fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragTrans.commit();


            // içi on doit recharger NotificationFragment
            //delete notification and mark as read
        }

        return super.onOptionsItemSelected(item);
    }



}
