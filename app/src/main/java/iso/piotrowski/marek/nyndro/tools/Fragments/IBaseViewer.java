package iso.piotrowski.marek.nyndro.tools.Fragments;

/**
 * Created by marek.piotrowski on 26/08/2017.
 */

public interface IBaseViewer {
    IBasePresenter getBasePresenter();
    void showMessage(String message);
}
