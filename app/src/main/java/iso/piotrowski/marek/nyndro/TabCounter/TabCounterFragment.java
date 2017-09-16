package iso.piotrowski.marek.nyndro.TabCounter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.PracticeMain.PracticeMainContract;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.FragmentsFactory.FragmentsFactory;
import iso.piotrowski.marek.nyndro.FragmentsFactory.NyndroFragment;

/**
 * Created by marek.piotrowski on 26/08/2017.
 */

public class TabCounterFragment extends NyndroFragment implements View.OnClickListener, TabCounterContract.IViewer {

    private TextView counter;
    private boolean soundOn;

    public static TabCounterFragment getInstance(){
        return new TabCounterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = getLayout(getActivity());
        setHasOptionsMenu(true);
        new TapCounterPresenter(this, DataSource.getInstance());
        soundOn = getPresenter().getSoundPreference();
        return layout;
    }

    private LinearLayout getLayout(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setBackgroundResource(R.drawable.amitabha_large);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.getBackground().setAlpha(30);
        linearLayout.setOnClickListener(this);

        counter = new TextView(context);
        counter.setText(getCount(0));
        counter.setTextSize(120);
        counter.setAlpha(1f);
        counter.setGravity(Gravity.CENTER);
        linearLayout.addView(counter);
        return linearLayout;
    }

    private String getCount(int count) {
        return String.format("%04d", count);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tap_counter_menu, menu);
        setSoundIcon(menu.findItem(R.id.sound_repeat));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sound_repeat:
                soundOn = !soundOn;
                getPresenter().setSoundPreference(soundOn);
                setSoundIcon(item);
                break;
            case R.id.reset_repeats:
                getPresenter().setCounter(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSoundIcon(MenuItem item) {
        item.setIcon(soundOn ? R.mipmap.ic_sound_on : R.mipmap.ic_sound_off);
    }

    @Override
    public FragmentsFactory.TypeOfFragment getTypeOf() {
        return FragmentsFactory.TypeOfFragment.TapCounter;
    }

    @Override
    public String getFragmentName() {
        return NyndroApp.getContext().getResources().getString(R.string.tap_counter_name);
    }

    @Override
    public void onClick(View view) {
        getPresenter().setCounter(getPresenter().getCounter() + 1);
        if (soundOn) view.playSoundEffect(SoundEffectConstants.CLICK);
    }

    private TabCounterContract.IPresenter getPresenter(){
        return (TabCounterContract.IPresenter) presenter;
    }

    @Override
    public void setPresenter(TabCounterContract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void refreshCounter(int counter) {
        this.counter.setText(getCount(counter));
    }

    @Override
    public boolean isButtonToolBarVisible() {
        return true;
    }

    @Override
    public PracticeMainContract.TypeOfBoomButton getTypeOfBoomButton() {
        return PracticeMainContract.TypeOfBoomButton.AddedPractice;
    }
}
