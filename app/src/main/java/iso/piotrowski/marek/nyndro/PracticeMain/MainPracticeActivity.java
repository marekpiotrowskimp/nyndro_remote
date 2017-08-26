package iso.piotrowski.marek.nyndro.PracticeMain;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.RemainderService.RemainderService;
import iso.piotrowski.marek.nyndro.plans.AddNewPlans.AddRemainderFragment;
import iso.piotrowski.marek.nyndro.plans.PlansFragment;
import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;
import iso.piotrowski.marek.nyndro.practice.ListOfPractice.PracticeListFragment;
import iso.piotrowski.marek.nyndro.practice.PracticeMainFragment;
import iso.piotrowski.marek.nyndro.tools.DrawableMapper;
import iso.piotrowski.marek.nyndro.tools.Fragments.FragmentsFactory;
import iso.piotrowski.marek.nyndro.tools.Fragments.IActivityDelegate;
import iso.piotrowski.marek.nyndro.tools.Fragments.IFragmentParams;
import iso.piotrowski.marek.nyndro.tools.Fragments.INavigator;
import iso.piotrowski.marek.nyndro.tools.Fragments.Navigator;
import iso.piotrowski.marek.nyndro.tools.Fragments.NyndroFragment;
import iso.piotrowski.marek.nyndro.tools.UITool;


public class MainPracticeActivity extends AppCompatActivity implements PracticeMainContract.IViewer, IActivityDelegate {

    private PracticeMainContract.IPresenter presenter;
    private INavigator navigator;

    @BindView(R.id.bottom_bar_main_practice) BottomBar bottomBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.boom_menu_button) BoomMenuButton boomButton;

    @Override
    public void setPresenter(PracticeMainContract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PracticeMainPresenter(this, DataSource.getInstance());
        Navigator.initialization(new WeakReference<>(this));
        navigator = Navigator.getInstance();
        setContentView(R.layout.activity_main_practics);
        ButterKnife.bind(this);
        setBoomButton();
        setUpBottomBar();
        setSupportActionBar(toolbar);
        restoreApplicationInstance(savedInstanceState);
        startRemainderService();
    }

    private void setBoomButton() {
        Practice practices[] = Practice.practices;
        for (int i = 0; i < boomButton.getButtonPlaceEnum().buttonNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(DrawableMapper.getDrawableId(DrawableMapper.TypeOfImage.values()[practices[i].getImageResourcesId()]))
                    .normalText(practices[i].getName()).rotateImage(true).rotateText(true).shadowEffect(true).shadowOffsetX(30).shadowOffsetY(30)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            NyndroFragment currentFragment = navigator.getCurrentFragment();
                            if (currentFragment instanceof PracticeMainFragment) {
                                presenter.insertPractice(practices[index]);
                                currentFragment.refreshData();
                                Snackbar.make(bottomBar, "Praktyka dodana: " + practices[index].getName(), Snackbar.LENGTH_LONG).show();
                            }
                            if (currentFragment instanceof PlansFragment) {
                                navigator.changeFragmentInContainer(AddRemainderFragment.getInstance(null), true);
                            }
                        }
                    });
            boomButton.addBuilder(builder);
        }
    }

    private void restoreApplicationInstance(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            navigator.changeFragmentInContainer(PracticeMainFragment.getInstance());
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("fab_visible")) {
                startAnimationForFloatingButton(UITool.TypeOfButtonAnimation.Emerge);
            }
        }
    }

    private void setUpBottomBar() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                navigator.changeFragmentInContainer(FragmentsFactory.getFragment(FragmentsFactory.getFragmentForTap(tabId)));
            }
        });
    }

    private void startAnimationForFloatingButton(final UITool.TypeOfButtonAnimation typeOfAnimation) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UITool.lastTypeOfAnimation != typeOfAnimation) {
                    UITool.animateButton(boomButton, typeOfAnimation);
                }
            }
        }, 250);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.practice_menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigator.goBack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fab_visible", boomButton.getVisibility() == View.INVISIBLE);
    }

    private void setApplicationLabel(Fragment currentFragment) {
        IFragmentParams fragmentName = (IFragmentParams)currentFragment;
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) supportActionBar.setTitle(fragmentName != null ? fragmentName.getFragmentName() : getResources().getString(R.string.app_name));
    }

    private void startRemainderService() {
        Intent intent2 = new Intent(this, RemainderService.class);
        startService(intent2);
    }


    @Override
    public void onBackPressed() {
        if (navigator.goBack()) {
            setBottomBar((IFragmentParams) navigator.getCurrentFragment());
            return;
        }
        super.onBackPressed();
    }

    private void setBottomBar (IFragmentParams fragmentParams){
        if (fragmentParams != null) {
            int tapIdForFragment = FragmentsFactory.getTapIdForFragment(fragmentParams.getTypeOf());
            if ((tapIdForFragment != 0) && (bottomBar.getCurrentTabId() != tapIdForFragment)) {
                bottomBar.selectTabWithId(tapIdForFragment);
            }
        }
    }

    @Override
    public AppCompatActivity getMainActivity() {
        return this;
    }

    @Override
    public void onFragmentChange(Fragment currentFragment) {
        IFragmentParams fragmentParams = (IFragmentParams) currentFragment;
        if (fragmentParams != null) {
            startAnimationForFloatingButton(fragmentParams.isButtonVisible() ? UITool.TypeOfButtonAnimation.Emerge : UITool.TypeOfButtonAnimation.Disappear);
        }
        setApplicationLabel(currentFragment);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) supportActionBar.setDisplayHomeAsUpEnabled(navigator.countOfFragments() > 0);
    }
    
}
