package iso.piotrowski.marek.nyndro.PracticeMain;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.Application.NyndroApp;
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
import iso.piotrowski.marek.nyndro.tools.Fragments.FragmentsFactory;
import iso.piotrowski.marek.nyndro.tools.Fragments.IFragmentParams;
import iso.piotrowski.marek.nyndro.tools.Fragments.FragmentTool;
import iso.piotrowski.marek.nyndro.tools.UITool;


public class MainPracticeActivity extends AppCompatActivity implements PracticeMainContract.IViewer {

    public static Resources resourcesApp;
    private boolean wasRunning;
    private PracticeMainContract.IPresenter presenter;
    private UITool.TypeOfButtonAnimation buttonAnimation = UITool.TypeOfButtonAnimation.Emerge;

    @BindView(R.id.bottom_bar_main_practice) BottomBar bottomBar;
    @BindView(R.id.fab_add_new_practice) FloatingActionButton floatingActionButton;
    @BindView(R.id.toolbar) Toolbar toolbar;

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
        setSupportActionBar(toolbar);
        restoreApplicationInstance(savedInstanceState);
        setFragmentManagerListener();
        startRemainderService();
    }

    private void restoreApplicationInstance(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentTool.changeFragmentInContainer(this, PracticeMainFragment.getInstance(), false);
        }

        if ((savedInstanceState != null) && (savedInstanceState.getBoolean("was_running"))) {
            if (savedInstanceState.getBoolean("fab_visible")) {
                startAnimationForFloatingButton(UITool.TypeOfButtonAnimation.Emerge);
            }
        }
    }

    private void setUpBottomBar() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTool.goToMainFragment(MainPracticeActivity.this);
                FragmentTool.changeFragmentInContainer(MainPracticeActivity.this,
                        FragmentsFactory.getFragment(FragmentsFactory.getFragmentForTap(tabId)));
            }
        });
    }

    private void setFragmentManagerListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                IFragmentParams fragmentParams = (IFragmentParams) getFragmentFromManager();
                if (fragmentParams != null) {
                    if (fragmentParams.isButtonVisible()) {
                        buttonAnimation = UITool.TypeOfButtonAnimation.Emerge;
                    } else {
                        buttonAnimation = UITool.TypeOfButtonAnimation.Disappear;
                    }
                    startAnimationForFloatingButton(UITool.TypeOfButtonAnimation.Disappear);
                }
                setApplicationLabel();
            }
        });
    }

    private void startAnimationForFloatingButton(final UITool.TypeOfButtonAnimation typeOfAnimation) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UITool.lastTypeOfAnimation != buttonAnimation) {
                    UITool.animateButton(floatingActionButton, buttonAnimation);
                }
            }
        }, 250);
    }

    private void setFabConfiguration() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getFragmentFromManager();
                if (fragment instanceof PracticeMainFragment) {
                    PracticeListFragment.OnListFragmentInteractionListener onListFragmentInteractionListener = new PracticeListFragment.OnListFragmentInteractionListener() {
                        @Override
                        public void onListFragmentInteraction(Practice practice) {
                            presenter.insertPractice(practice);
                            Snackbar.make(view, "Praktyka dodana: " + practice.getName(), Snackbar.LENGTH_LONG).show();
                            FragmentTool.goToPreviousFragment(MainPracticeActivity.this);
                        }
                    };
                    FragmentTool.changeFragmentInContainer(MainPracticeActivity.this, PracticeListFragment.getInstance(onListFragmentInteractionListener));
                }
                if (fragment instanceof PlansFragment) {
                    FragmentTool.changeFragmentInContainer(MainPracticeActivity.this, AddRemainderFragment.getInstance(null));
                }
            }
        });

    }

    private Fragment getFragmentFromManager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentByTag("visible_tag");
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
        outState.putBoolean("fab_visible", floatingActionButton.getVisibility() == View.INVISIBLE);
    }

    private void setApplicationLabel() {
        IFragmentParams fragmentName = (IFragmentParams)getFragmentFromManager();
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) supportActionBar.setTitle(fragmentName != null ? fragmentName.getFragmentName() : getResources().getString(R.string.app_name));
    }

    private void startRemainderService() {
        Intent intent2 = new Intent(this, RemainderService.class);
        startService(intent2);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setBottomBar((IFragmentParams) getFragmentFromManager());
    }

    private void setBottomBar (IFragmentParams fragmentParams){
        if (fragmentParams != null) {
            int tapIdForFragment = FragmentsFactory.getTapIdForFragment(fragmentParams.getTypeOf());
            if ((tapIdForFragment != 0) && (bottomBar.getCurrentTabId() != tapIdForFragment)) {
                bottomBar.selectTabWithId(tapIdForFragment);
            }
        }
    }

}
