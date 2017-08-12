package iso.piotrowski.marek.nyndro.practice;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.R;

/**
 * Created by Marek on 25.07.2016.
 * Adapter to practices
 */

public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.ViewPracticeHolder> {

    public static final int _ID = 0;
    public static final int NAME_ID = 1;
    public static final int DESCRIPTION_ID = 2;
    public static final int PRACTICE_IMAGE_ID_ID = 3;
    public static final int PROGRESS_ID = 4;
    public static final int MAX_REPETITION_ID = 5;
    public static final int REPETITION_ID = 6;
    public static final int ACTIVE_ID = 7;

    public static final int STANDARD_TYPE = 1;
    public static final int END_TYPE = 2;

    private List<PracticeModel> practices;
    private ICardViewListener cardViewListener = null;
    private IImageCardViewListener imageButtonListener = null;
    private INextAndLastDateOfPractice nextAndLastDateOfPractice = null;

    interface INextAndLastDateOfPractice {
        long getNextPractice (long practiceId);
        long getLastPractice (long practiceId);
    }

    interface ICardViewListener {
        void onClickToShowPracticeDetails (View view, int position);
    }

    interface IImageCardViewListener {
        void onClick(View view, PracticeModel practice, int multiple);
    }

    PracticeAdapter(List<PracticeModel> practices) {
        this.practices = practices;
    }

    public void setNextAndLastDateOfPractice(INextAndLastDateOfPractice nextAndLastDateOfPractice) {
        this.nextAndLastDateOfPractice = nextAndLastDateOfPractice;
    }

    void setCardViewListener(ICardViewListener cardViewListener) {
        this.cardViewListener = cardViewListener;
    }

    void setImageButtonListener(IImageCardViewListener imageButtonListener) {
        this.imageButtonListener = imageButtonListener;
    }

    @Override
    public int getItemCount() {
        return getCountWithFooter();
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (practices.size() > position) {
            type = STANDARD_TYPE;
        } else {
            type = END_TYPE;
        }
        return type;
    }

    @Override
    public ViewPracticeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv;
        switch (viewType) {
            case STANDARD_TYPE:
                cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_practice, parent, false);
                break;
            case END_TYPE:
                cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_practice_blank, parent, false);
                break;
            default:
                cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_practice, parent, false);
        }
        return new ViewPracticeHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewPracticeHolder holder, final int position) {
        if (getItemViewType(position) == STANDARD_TYPE) {
            bindPracticeViewHolderWithData(holder, position);
        }
    }

    private void bindPracticeViewHolderWithData(ViewPracticeHolder holder, int position) {
        holder.multiplePracticeSeekBar.setProgress(1);
        holder.practiceRepetitionMultiple.setText(String.valueOf(holder.multiplePracticeSeekBar.getProgress()));
        holder.multiplePracticeSeekBar.setOnSeekBarChangeListener(changeProgress(holder));
        PracticeModel practice = practices.get(position);
        if (practice != null) {
            holder.practiceImage.setContentDescription(practice.getName());
            holder.practiceImage.setImageDrawable(holder.cardView.getResources().getDrawable(practice.getPracticeImageId()));
            holder.practiceName.setText(practice.getName().replace("\n", " "));
            holder.practiceProgress.setMax(practice.getMaxRepetition());
            holder.practiceProgress.setProgress(practice.getProgress());
            holder.practiceStatus.setText(String.valueOf(practice.getProgress()) + " / " + String.valueOf(practice.getMaxRepetition()));
            holder.practiceRepetition.setText(String.valueOf(practice.getRepetition()));
            holder.practiceDescription.setText(practice.getDescription().replace("\n"," "));
            setNextAndLastDateOfPractice(holder, practice);
            holder.cardView.setOnClickListener(onClickToShowPracticeDetails(position));
            holder.practiceRapetitionAdd.setOnClickListener(onClickToAddRepetition(holder, position));
        }
    }

    private void setNextAndLastDateOfPractice(ViewPracticeHolder holder, PracticeModel practice) {
        if (nextAndLastDateOfPractice != null) {
            long lastDateOfPractice = nextAndLastDateOfPractice.getLastPractice(practice.getID());
            long nextDateOfPractice = nextAndLastDateOfPractice.getNextPractice(practice.getID());
            Calendar calendar = Calendar.getInstance();
            if (lastDateOfPractice == -1) {
                holder.practiceDateLast.setText(String.format("%s %s", holder.cardView.getResources().getString(R.string.last_practice_date),
                        holder.cardView.getResources().getString(R.string.NoDateToShow)));
            } else {
                calendar.setTimeInMillis(lastDateOfPractice);
                holder.practiceDateLast.setText(String.format("%s %tD", holder.cardView.getResources().getString(R.string.last_practice_date),
                        calendar));
            }
            if (nextDateOfPractice == -1) {
                holder.practiceDateNext.setText(String.format("%s %s", holder.cardView.getResources().getString(R.string.next_practice_date),
                        holder.cardView.getResources().getString(R.string.NoDateToShow)));
            } else{
                calendar.setTimeInMillis(nextDateOfPractice);  //cursorPractices.getLong(NEXT_PRACTICE_DATE_ID));
                holder.practiceDateNext.setText(String.format("%s %tD", holder.cardView.getResources().getString(R.string.next_practice_date),
                        calendar));
            }
        }
    }

    @NonNull
    private View.OnClickListener onClickToAddRepetition(final ViewPracticeHolder holder, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageButtonListener != null) {
                    imageButtonListener.onClick(view, practices.get(position), holder.multiplePracticeSeekBar.getProgress());
                }

            }
        };
    }

    @NonNull
    private View.OnClickListener onClickToShowPracticeDetails(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardViewListener != null) {
                    cardViewListener.onClickToShowPracticeDetails(view, position);
                }
            }
        };
    }

    @NonNull
    private SeekBar.OnSeekBarChangeListener changeProgress(final ViewPracticeHolder holder) {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                holder.practiceRepetitionMultiple.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };
    }

    private int getCountWithFooter() {
        return practices.size() + 1;
    }

    static class ViewPracticeHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        @BindView(R.id.practice_image) @Nullable ImageView practiceImage;
        @BindView(R.id.practice_name) @Nullable TextView practiceName;
        @BindView(R.id.practice_status) @Nullable TextView practiceStatus;
        @BindView(R.id.practice_repetition) @Nullable TextView practiceRepetition;
        @BindView(R.id.practice_repetition_multiple) @Nullable TextView practiceRepetitionMultiple;
        @BindView(R.id.practice_date_last) @Nullable TextView practiceDateLast;
        @BindView(R.id.practice_date_next) @Nullable TextView practiceDateNext;
        @BindView(R.id.practice_description) @Nullable TextView practiceDescription;
        @BindView(R.id.multiple_seek_bar) @Nullable SeekBar multiplePracticeSeekBar;
        @BindView(R.id.practice_repetition_add) @Nullable ImageButton practiceRapetitionAdd;
        @BindView(R.id.practice_progress) @Nullable ProgressBar practiceProgress;

        ViewPracticeHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
            ButterKnife.bind(this, this.cardView);
        }
    }
}
