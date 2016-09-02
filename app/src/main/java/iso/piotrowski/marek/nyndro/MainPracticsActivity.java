package iso.piotrowski.marek.nyndro;

import android.app.backup.BackupManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.ShareActionProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Date;

import iso.piotrowski.marek.nyndro.RemainderService.RemainderService;
import iso.piotrowski.marek.nyndro.plans.AddRemainderFragment;
import iso.piotrowski.marek.nyndro.plans.PlansFragment;
import iso.piotrowski.marek.nyndro.practice.Practice;
import iso.piotrowski.marek.nyndro.practice.PracticeDetailFragment;
import iso.piotrowski.marek.nyndro.practice.PracticeListFragment;
import iso.piotrowski.marek.nyndro.practice.PracticeMainFragment;
import iso.piotrowski.marek.nyndro.statistics.StatsPagerFragment;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;


public class MainPracticsActivity extends AppCompatActivity {
    public static Resources resourcesApp;
    private ShareActionProvider sendMeditationInfoActionProvider;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private boolean wasRunning;
    private int lastTypeOfAnimation = 2;

    public void goToWabSiteOnClick(View view) {
        String url = "http://bombydgoszcz.blogspot.com/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

//    private RemainderService remainderService;
//    private boolean bound = false;
//    private ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            RemainderService.RemainderBinder remainderBinder = (RemainderService.RemainderBinder) iBinder;
//            remainderService = remainderBinder.getRemainderService();
//            bound=true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            bound = false;
//        }
//    };

    private class DrawerItemListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (l == 0) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                drawerLayout.closeDrawer(findViewById(R.id.left_layout_of_drawer));
            }
            if (l == 1) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                StatsPagerFragment statsPagerFragment = new StatsPagerFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_fragment_container, statsPagerFragment, "visible_tag");
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                drawerLayout.closeDrawer(findViewById(R.id.left_layout_of_drawer));
            }
            if (l == 2) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                PlansFragment plansFragment = new PlansFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_fragment_container, plansFragment, "visible_tag");
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                drawerLayout.closeDrawer(findViewById(R.id.left_layout_of_drawer));
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resourcesApp = getResources();
        setContentView(R.layout.activity_main_practics);

        setFabConfiguration();

        //Drawer
        ((ListView) findViewById(R.id.left_drawer_list)).setOnItemClickListener(new DrawerItemListener());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            PracticeMainFragment practiceMainFragment = new PracticeMainFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment_container, practiceMainFragment, "visible_tag");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }

        if ((savedInstanceState != null) && (savedInstanceState.getBoolean("was_running"))) {
            if (savedInstanceState.getBoolean("fab_visible")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateButton(findViewById(R.id.fab_add_new_practice), 1);
                    }
                }, 250);
            }
        }

        setFragmentManagerListener();

        //Start remainder service
        Intent intent2 = new Intent(this, RemainderService.class);
        startService(intent2);
//        Intent intent = new Intent(this, RemainderService.class);
//        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void setFragmentManagerListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("visible_tag");
                if ((fragment instanceof PracticeDetailFragment) || (fragment instanceof PracticeMainFragment) || (fragment instanceof PlansFragment)) {
                    if (lastTypeOfAnimation == 1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animateButton(findViewById(R.id.fab_add_new_practice), 2);
                            }
                        }, 250);
                    }
                }
                if ((fragment instanceof PracticeListFragment) || (fragment instanceof StatsPagerFragment) || (fragment instanceof AddRemainderFragment)) {
                    if (lastTypeOfAnimation == 2) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animateButton(findViewById(R.id.fab_add_new_practice), 1);
                            }
                        }, 250);
                    }
                }
                setApplicationLabel(fragment instanceof PracticeDetailFragment ? ((PracticeDetailFragment) fragment).getPracticeName() : "");
            }
        });
    }

    private void setFabConfiguration() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_new_practice);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("visible_tag");
                if (fragment instanceof PracticeDetailFragment) {
                    Snackbar.make(view, R.string.add_fab_snackbar, Snackbar.LENGTH_LONG)
                            .setAction(R.string.action_snackbar, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    Fragment fragment = fragmentManager.findFragmentByTag("visible_tag");
                                    if (fragment instanceof PracticeDetailFragment) {
                                        ((PracticeDetailFragment) fragment).addRepetition(false);
                                    }
                                }
                            }).show();
                    ((PracticeDetailFragment) fragment).addRepetition(true);
                }

                if (fragment instanceof PracticeMainFragment) {

                    PracticeListFragment.OnListFragmentInteractionListener onListFragmentInteractionListener = new PracticeListFragment.OnListFragmentInteractionListener() {
                        @Override
                        public void onListFragmentInteraction(Practice practice) {
                            try {
                                SQLHelper.insertPractice(practice, getApplicationContext());
                                Snackbar.make(getCurrentFocus(), "Praktyka dodana: " + practice.getName(), Snackbar.LENGTH_LONG).show();
                                getSupportFragmentManager().popBackStack();
                            } catch (SQLiteException e) {
                            }
                        }
                    };

                    PracticeListFragment practiceListFragment = new PracticeListFragment();
                    practiceListFragment.setmListener(onListFragmentInteractionListener);

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_fragment_container, practiceListFragment, "visible_tag");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
                if (fragment instanceof PlansFragment) {
                    AddRemainderFragment addRemainderFragment = new AddRemainderFragment();

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_fragment_container, addRemainderFragment, "visible_tag");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.practice_menu_main, menu);
//        MenuItem menuItem = menu.findItem(R.id.action_share);
//        sendMeditationInfoActionProvider=(ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//        setMassageToSend("Nyndro application"); //TODO ustawić w odpowiednich miejscach text do wysłania.
        Log.v("onCreateOptionsMenu", "Menu created.");
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMassageToSend(String text) {
        //TODO Dostosować do aplikacji Nyndro: Wysłać konkretne statystyki do znajomych!
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        sendMeditationInfoActionProvider.setShareIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        wasRunning = true;
        outState.putBoolean("was_running", wasRunning);
        outState.putBoolean("fab_visible", ((FloatingActionButton) findViewById(R.id.fab_add_new_practice)).getVisibility() == View.INVISIBLE);
    }


    private void animateButton(final View mFloatingView, final int typeOfAnimation) {
        Animation animation = null;
        switch (typeOfAnimation) {
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.floating_button_disappear);
                break;
            case 2:
                animation = AnimationUtils.loadAnimation(this, R.anim.floating_button_emerge);
                break;

        }

        if (animation != null) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    switch (typeOfAnimation) {
                        case 1:
                            mFloatingView.setVisibility(View.INVISIBLE);
                            lastTypeOfAnimation = 1;
                            break;
                        case 2:
                            mFloatingView.setVisibility(View.VISIBLE);
                            lastTypeOfAnimation = 2;
                            break;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mFloatingView.startAnimation(animation);
        }
    }


    private void setApplicationLabel(String practiceName) {
        String label = getResources().getString(R.string.app_name);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("visible_tag");
        if (fragment instanceof PracticeDetailFragment) {
            label = practiceName;
        }
        if (fragment instanceof PracticeMainFragment) {
            label = getResources().getString(R.string.app_name);
        }
        if (fragment instanceof PlansFragment) {
            label = getResources().getString(R.string.app_label_plans);
        }
        if (fragment instanceof PracticeListFragment) {
            label = getResources().getString(R.string.app_label_practice);
        }
        if (fragment instanceof StatsPagerFragment) {
            label = getResources().getString(R.string.app_label_stats);
        }
        if (fragment instanceof AddRemainderFragment) {
            label = getResources().getString(R.string.app_label_add_renainder);
        }
        getSupportActionBar().setTitle(label);
        requestBackup();
    }


    @Override
    protected void onDestroy() {
//        if (bound)
//        {
//  //         unbindService(connection);
//            bound=false;
//        }
        super.onDestroy();
    }

    public void requestBackup() {
        if (SQLHelper.isUpdateDatabase) {
            try {
                SQLHelper.isUpdateDatabase = false;
                BackupManager backupManager = new BackupManager(this);
                backupManager.dataChanged();
            } catch (Exception e) {
            }
        }
    }

}
