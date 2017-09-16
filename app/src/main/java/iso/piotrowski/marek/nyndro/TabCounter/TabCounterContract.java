package iso.piotrowski.marek.nyndro.TabCounter;

import iso.piotrowski.marek.nyndro.tools.Fragments.IBasePresenter;
import iso.piotrowski.marek.nyndro.tools.Fragments.IBaseViewer;

/**
 * Created by marek.piotrowski on 11/09/2017.
 */

public class TabCounterContract {

    interface IViewer extends IBaseViewer {
        void setPresenter(IPresenter presenter);
        void refreshCounter(int counter);
    }

    interface IPresenter extends IBasePresenter {
        int getCounter();
        void setCounter(int counter);
        boolean getSoundPreference();
        void setSoundPreference(boolean soundOn);
    }

}
