package iso.piotrowski.marek.nyndro.FragmentsFactory;

/**
 * Created by marek.piotrowski on 26/08/2017.
 */

public interface IBaseViewer {
    IBasePresenter getBasePresenter();
    void showMessage(String message);
}
