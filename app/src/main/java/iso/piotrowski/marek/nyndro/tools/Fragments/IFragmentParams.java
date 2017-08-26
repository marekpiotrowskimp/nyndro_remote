package iso.piotrowski.marek.nyndro.tools.Fragments;

/**
 * Created by marek.piotrowski on 22/08/2017.
 */

public interface IFragmentParams {
    String getFragmentName();
    FragmentsFactory.TypeOfFragment getTypeOf();
    boolean isButtonVisible();
    void refreshData();
}
