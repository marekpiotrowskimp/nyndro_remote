package iso.piotrowski.marek.nyndro.practice.Details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.UIComponents.BuddaProgressBar;
import iso.piotrowski.marek.nyndro.tools.DrawableMapper;
import iso.piotrowski.marek.nyndro.tools.Fragments.FragmentsFactory;
import iso.piotrowski.marek.nyndro.tools.Fragments.Navigator;
import iso.piotrowski.marek.nyndro.tools.Fragments.NyndroFragment;
import iso.piotrowski.marek.nyndro.tools.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class PracticeDetailFragment extends NyndroFragment implements PracticeDetailContract.IViewer {

    @BindView(R.id.practice_image_detail) ImageView practiceImage;
    @BindView(R.id.practice_name_detail) TextView practiceName;
    @BindView(R.id.practice_status_progress_detail) TextView practiceStatusProgress;
    @BindView(R.id.practice_status_maxrepetition_detail) TextView practiceStatusMaxRepetiton;
    @BindView(R.id.practice_repetition_detail) TextView practiceRepetition;
    @BindView(R.id.practice_date_last_detail) TextView practiceDateLast;
    @BindView(R.id.practice_date_next_detail) TextView practiceDateNext;
    @BindView(R.id.practice_description_detail) TextView practiceDescription;
    @BindView(R.id.practice_progress_detail) BuddaProgressBar practiceProgress;
    @BindView(R.id.practice_name_edit) EditText practiceNameEdit;
    @BindView(R.id.practice_status_progress_edit) EditText practiceStatusProgressEdit;
    @BindView(R.id.practice_status_maxrepetition_edit) EditText practiceStatusMaxRepetitonEdit;
    @BindView(R.id.practice_repetition_edit) EditText practiceRepetitionEdit;
    @BindView(R.id.practice_description_edit) EditText practiceDescriptionEdit;

    private static String PRACTICE_ID = "practice_id";
    private TypeOfEditMode editMode = TypeOfEditMode.Standard;
    private PracticeModel practice;
    private long practiceId;

    private PracticeDetailContract.IPresenter getPresenter(){
        return (PracticeDetailContract.IPresenter) presenter;
    }

    @Override
    public String getFragmentName() {
        return practice.getName();
    }

    @Override
    public FragmentsFactory.TypeOfFragment getTypeOf() {
        return FragmentsFactory.TypeOfFragment.PracticeDetail;
    }

    @Override
    public boolean isButtonVisible() {
        return false;
    }

    public enum TypeOfEditMode {
        Standard,
        Edit
    }

    public PracticeDetailFragment() {
    }

    public static PracticeDetailFragment getInstance(PracticeModel practice){
        PracticeDetailFragment practiceDetailFragment = new PracticeDetailFragment();
        practiceDetailFragment.setPractice(practice);
        return practiceDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new PracticeDetailPresenter(this, DataSource.getInstance());
        View practiceDetailView =inflater.inflate(R.layout.fragment_practice_detail, container, false);
        if (savedInstanceState!=null)
        {
            practiceId  = savedInstanceState.getLong(PRACTICE_ID);
            getPresenter().loadPracticeData(practiceId);
        }
        setHasOptionsMenu(true);
        ButterKnife.bind(this, practiceDetailView);
        return practiceDetailView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.practice_menu_detail,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_toolbar:
                if (getEditMode() == TypeOfEditMode.Edit) {
                    Navigator.getInstance().goBack();
                } else {
                    item.setIcon(getResources().getDrawable(R.mipmap.ic_edit_black_48dp));
                }
                setEdit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        getPresenter().loadPracticeData(practiceId);
    }

    private void viewPractice (TypeOfEditMode typeViewPractice)
    {
        practiceImage.setContentDescription(practice.getName());
        practiceImage.setImageDrawable(NyndroApp.getContect().getResources().getDrawable(
                DrawableMapper.getDrawableLargeId(DrawableMapper.TypeOfImage.values()[practice.getRawPracticeImageId()])));
        practiceProgress.setMaxProgress(practice.getMaxRepetition());
        practiceProgress.setProgress(practice.getProgress());

        Utility.setUpPracticeDate(practiceDateLast, getPresenter().getLastHistoryOfPractice(practice.getID()), R.string.last_practice_date);
        Utility.setUpPracticeDate(practiceDateNext, getPresenter().getNextPlanedOfPractice(practice.getID()), R.string.last_practice_date);

        setEditVisibility(typeViewPractice==TypeOfEditMode.Standard ? View.INVISIBLE : View.VISIBLE);
        setUpPracticeTextAndEdit(practice, typeViewPractice==TypeOfEditMode.Standard);
    }

    private void setUpPracticeTextAndEdit(PracticeModel practice, boolean textOrEdit) {
        practiceName.setText(getFormatTextWithPracticeData(R.string.name_practice, textOrEdit ? practice.getName(): " "));
        practiceStatusProgress.setText(getFormatTextWithPracticeData(R.string.progress_name, textOrEdit ? String.valueOf(practice.getProgress()): " "));
        practiceStatusMaxRepetiton.setText(getFormatTextWithPracticeData(R.string.max_repetition_name, textOrEdit ? String.valueOf(practice.getMaxRepetition()) : " "));
        practiceRepetition.setText(getFormatTextWithPracticeData(R.string.repetiton_name, textOrEdit ? String.valueOf(practice.getRepetition()) : " "));
        practiceDescription.setText(getFormatTextWithPracticeData(R.string.description_name, textOrEdit ? practice.getDescription() : " "));

        practiceNameEdit.setText(textOrEdit ? "" : practice.getName());
        practiceStatusProgressEdit.setText(textOrEdit ? "" : String.valueOf(practice.getProgress()));
        practiceStatusMaxRepetitonEdit.setText(textOrEdit ? "" : String.valueOf(practice.getMaxRepetition()));
        practiceRepetitionEdit.setText(textOrEdit ? "" : String.valueOf(practice.getRepetition()));
        practiceDescriptionEdit.setText(textOrEdit ? "" : practice.getDescription());
    }

    private String getFormatTextWithPracticeData(int resourcesId, String practiceData) {
        return String.format("%s%s", Utility.getFormatEditFromResources(resourcesId),
                practiceData);
    }

    private void setEditVisibility(int visible) {
        practiceNameEdit.setVisibility(visible);
        practiceStatusProgressEdit.setVisibility(visible);
        practiceStatusMaxRepetitonEdit.setVisibility(visible);
        practiceRepetitionEdit.setVisibility(visible);
        practiceDescriptionEdit.setVisibility(visible);
    }

    @Override
    public void setPractice(PracticeModel practice) {
        this.practice = practice;
        this.practiceId = practice.getID();
    }

    @Override
    public void showPractice() {
        viewPractice(getEditMode());
    }

    public void addRepetition (boolean typeAddSubtrack)
    {
        int repetition = practice.getProgress() * (typeAddSubtrack ? 1 : -1);
        getPresenter().addProgressToPractice(practice, repetition);
        getPresenter().addHistoryForPractice(practice, repetition);
        viewPractice(getEditMode());
    }

    void editionEnd ()
    {
        int oldProgress = practice.getProgress();
        getPresenter().updatePractice(practice.setName(practiceNameEdit.getText().toString())
                .setDescription(practiceDescriptionEdit.getText().toString())
                .setProgress(getIntegerValue(practiceStatusProgressEdit, practice.getProgress()))
                .setMaxRepetition(getIntegerValue(practiceStatusMaxRepetitonEdit, practice.getMaxRepetition()))
                .setRepetition(getIntegerValue(practiceRepetitionEdit, practice.getRepetition())));
        int repetition = practice.getProgress() - oldProgress;
        getPresenter().addHistoryForPractice(practice, repetition);
    }

    private Integer getIntegerValue(EditText fromEdit, Integer oldValue) {
        String textValue = fromEdit.getText().toString();
        return textValue.isEmpty() ? oldValue : Integer.valueOf(textValue);
    }

    public void setEdit()
    {
        if (getEditMode() == TypeOfEditMode.Standard) {
            editMode = TypeOfEditMode.Edit;
        } else {
            editMode=TypeOfEditMode.Standard;
            editionEnd();
        }
        getPresenter().loadPracticeData(practiceId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(PRACTICE_ID, practiceId);
    }

    public TypeOfEditMode getEditMode() {
        return editMode;
    }

    public String getPracticeName(){
        return practice.getName();
    }

    @Override
    public void setPresenter(PracticeDetailContract.IPresenter presenter) {
        this.presenter = presenter;
    }
}
