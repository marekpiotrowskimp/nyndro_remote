package iso.piotrowski.marek.nyndro.PracticeMain;

import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.RemainderService.RemainderService;
import iso.piotrowski.marek.nyndro.history.HistoryFragment;
import iso.piotrowski.marek.nyndro.plans.AddNewPlans.AddRemainderFragment;
import iso.piotrowski.marek.nyndro.plans.PlansFragment;
import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;
import iso.piotrowski.marek.nyndro.practice.Details.PracticeDetailFragment;
import iso.piotrowski.marek.nyndro.practice.ListOfPractice.PracticeListFragment;
import iso.piotrowski.marek.nyndro.practice.PracticeMainFragment;
import iso.piotrowski.marek.nyndro.statistics.StatsFragment;


public class MainPracticeActivity extends AppCompatActivity implements PracticeMainContract.IViewer {
    public static Resources resourcesApp;
    private boolean wasRunning;
    private int lastTypeOfAnimation = 2;
    private PracticeMainContract.IPresenter presenter;
    @BindView(R.id.bottom_bar_main_practice) BottomBar bottomBar;

    public void goToWabSiteOnClick(View view) {
        String url = "http://bombydgoszcz.blogspot.com/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void setPresenter(PracticeMainContract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PracticeMainPresenter(this, DataSource.getInstance());
        resourcesApp = getResources();
        setContentView(R.layout.activity_main_practics);
        ButterKnife.bind(this);
        setFabConfiguration();
        setUpBottomBar();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        Intent intent2 = new Intent(this, RemainderService.class);
        startService(intent2);
    }

    private void setUpBottomBar() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_practice) {
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                if (tabId == R.id.tab_statistic) {
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    StatsFragment statsFragment = StatsFragment.getInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_fragment_container, statsFragment, "visible_tag");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
                if (tabId == R.id.tab_history) {
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    HistoryFragment historyFragment = new HistoryFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_fragment_container, historyFragment, "visible_tag");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
                if (tabId == R.id.tab_plans) {
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    PlansFragment plansFragment = new PlansFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.main_fragment_container, plansFragment, "visible_tag");
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }

            }
        });
    }

    private void setFragmentManagerListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag("visible_tag");
                if ((fragment instanceof PracticeMainFragment) || (fragment instanceof PlansFragment)) {
                    if (lastTypeOfAnimation == 1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animateButton(findViewById(R.id.fab_add_new_practice), 2);
                            }
                        }, 250);
                    }
                }
                if ((fragment instanceof PracticeListFragment) || (fragment instanceof PracticeDetailFragment) || (fragment instanceof HistoryFragment) || (fragment instanceof StatsFragment) || (fragment instanceof AddRemainderFragment)) {
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

                if (fragment instanceof PracticeMainFragment) {

                    PracticeListFragment.OnListFragmentInteractionListener onListFragmentInteractionListener = new PracticeListFragment.OnListFragmentInteractionListener() {
                        @Override
                        public void onListFragmentInteraction(Practice practice) {
                            presenter.insertPractice(practice);
                            Snackbar.make(getCurrentFocus(), "Praktyka dodana: " + practice.getName(), Snackbar.LENGTH_LONG).show();
                            getSupportFragmentManager().popBackStack();
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
                    AddRemainderFragment addRemainderFragment = AddRemainderFragment.getInstance(null);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.practice_menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                break;
        }
        return super.onOptionsItemSelected(item);
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
        if (fragment instanceof StatsFragment) {
            label = getResources().getString(R.string.app_label_stats);
        }
        if (fragment instanceof HistoryFragment) {
            label = getResources().getString(R.string.app_label_history);
        }
        if (fragment instanceof AddRemainderFragment) {
            label = getResources().getString(R.string.app_label_add_renainder);
        }
        getSupportActionBar().setTitle(label);
    }

}
