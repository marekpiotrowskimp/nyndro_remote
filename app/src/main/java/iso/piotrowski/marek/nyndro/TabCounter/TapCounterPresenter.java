package iso.piotrowski.marek.nyndro.TabCounter;

import android.content.Context;
import android.content.SharedPreferences;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.DataSource.IDataSource;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.FragmentsFactory.NyndroPresenter;

/**
 * Created by marek.piotrowski on 11/09/2017.
 */

public class TapCounterPresenter extends NyndroPresenter implements TabCounterContract.IPresenter {

    private static final String NAME_SHARED = "TabCounterPreferences";
    private static final String SOUND_KEY_SHARED = "SoundOn";
    private IDataSource dataSource;
    private TabCounterContract.IViewer viewer;
    private int counter = 0;

    public TapCounterPresenter(TabCounterContract.IViewer viewer, IDataSource dataSource) {
        this.dataSource = dataSource;
        this.viewer = viewer;
        this.viewer.setPresenter(this);
    }

    @Override
    public void selectedPractice(int position) {
        PracticeModel practice = dataSource.fetchUnfinishedPractices().get(position);
        dataSource.addProgressToPractice(practice, counter);
        dataSource.addHistoryForPractice(practice, counter);
        setCounter(0);
    }

    @Override
    public int getCounter() {
        return counter;
    }

    @Override
    public void setCounter(int counter) {
        this.counter = counter;
        viewer.refreshCounter(counter);
    }

    @Override
    public boolean getSoundPreference() {
        SharedPreferences sharedPreferences = NyndroApp.getContext().getSharedPreferences(NAME_SHARED, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SOUND_KEY_SHARED, true);
    }

    @Override
    public void setSoundPreference(boolean soundOn) {
        SharedPreferences sharedPreferences = NyndroApp.getContext().getSharedPreferences(NAME_SHARED, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SOUND_KEY_SHARED, soundOn);
        editor.apply();
    }
}
