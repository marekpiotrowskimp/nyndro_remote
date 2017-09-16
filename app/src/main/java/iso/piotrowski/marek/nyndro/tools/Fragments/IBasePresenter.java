package iso.piotrowski.marek.nyndro.tools.Fragments;

import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;

/**
 * Created by marek.piotrowski on 26/08/2017.
 */

public interface IBasePresenter {
    void loadData();
    void refreshData();
    void selectedPractice (int position);
}
