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

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.Utility;

import static iso.piotrowski.marek.nyndro.practice.PracticeAdapter.TypeOfCardView.Standard;

/**
 * Created by Marek on 25.07.2016.
 * Adapter to practices
 */

public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.ViewPracticeHolder> {

    public enum TypeOfCardView {
        Standard(0),
        End(1);

        private final int value;
        TypeOfCardView(int value){
            this.value = value;
        }

        int getValue(){
            return value;
        }
    }

    private List<PracticeModel> practices;
    private ICardViewListener cardViewListener = null;
    private IImageCardViewListener imageButtonListener = null;
    private INextAndLastDateOfPractice nextAndLastDateOfPractice = null;

    interface INextAndLastDateOfPractice {
        long getNextPractice (long practiceId);
        long getLastPractice (long practiceId);
    }

    interface ICardViewListener {
        void onClickToShowPracticeDetails (View view, PracticeModel practice);
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
        if (practices.size() > position) {
            return Standard.getValue();
        } else {
            return TypeOfCardView.End.getValue();
        }
    }

    @Override
    public ViewPracticeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = null;
        switch (TypeOfCardView.values()[viewType]) {
            case Standard:
                cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_practice, parent, false);
                break;
            case End:
                cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_practice_blank, parent, false);
                break;
        }
        return new ViewPracticeHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewPracticeHolder holder, final int position) {
        if (getItemViewType(position) == TypeOfCardView.Standard.getValue()) {
            bindPracticeViewHolderWithData(holder, position);
        }
    }

    private void bindPracticeViewHolderWithData(ViewPracticeHolder holder, int position) {
        holder.multiplePracticeSeekBar.setProgress(1);
        holder.practiceRepetitionMultiple.setText(String.valueOf(holder.multiplePracticeSeekBar.getProgress()));
        holder.multiplePracticeSeekBar.setOnSeekBarChangeListener(changeProgress(holder));
        PracticeModel practice = practices.get(position);
        holder.setPractice(practice);
        if (practice != null) {
            holder.practiceImage.setContentDescription(practice.getName());
            holder.practiceImage.setImageDrawable(holder.cardView.getResources().getDrawable(practice.getPracticeImageId()));
            holder.practiceName.setText(practice.getName().replace("\n", " "));
            holder.practiceProgress.setMax(practice.getMaxRepetition());
            holder.practiceProgress.setProgress(practice.getProgress());
            holder.practiceStatus.setText(String.valueOf(practice.getProgress()) + " / " + String.valueOf(practice.getMaxRepetition()));
            holder.practiceRepetition.setText(String.valueOf(practice.getRepetition()));
            float percentage = ((float) practice.getProgress()) / ((float) practice.getMaxRepetition())*100;
            holder.progressPercentage.setText(String.format(Locale.UK, "%.2f%%", percentage));
            setNextAndLastDateOfPractice(holder, practice);
            holder.cardView.setOnClickListener(onClickToShowPracticeDetails(position));
            holder.practiceRepetitionAdd.setOnClickListener(onClickToAddRepetition(holder, position));
        }
    }

    private void setNextAndLastDateOfPractice(ViewPracticeHolder holder, PracticeModel practice) {
        if (nextAndLastDateOfPractice != null) {
            long lastDateOfPractice = nextAndLastDateOfPractice.getLastPractice(practice.getID());
            long nextDateOfPractice = nextAndLastDateOfPractice.getNextPractice(practice.getID());
            Utility.setUpPracticeDate(holder.practiceDateLast, lastDateOfPractice, R.string.last_practice_date);
            Utility.setUpPracticeDate(holder.practiceDateNext, nextDateOfPractice, R.string.next_practice_date);
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
                    cardViewListener.onClickToShowPracticeDetails(view, practices.get(position));
                }
            }
        };
    }

    @NonNull
    private SeekBar.OnSeekBarChangeListener changeProgress(final ViewPracticeHolder holder) {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress<1) {
                    progress = 1;
                    holder.multiplePracticeSeekBar.setProgress(progress);
                }
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

    public class ViewPracticeHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        private PracticeModel practice;
        @BindView(R.id.practice_image) @Nullable ImageView practiceImage;
        @BindView(R.id.practice_name) @Nullable TextView practiceName;
        @BindView(R.id.practice_status) @Nullable TextView practiceStatus;
        @BindView(R.id.practice_repetition) @Nullable TextView practiceRepetition;
        @BindView(R.id.practice_repetition_multiple) @Nullable TextView practiceRepetitionMultiple;
        @BindView(R.id.practice_date_last) @Nullable TextView practiceDateLast;
        @BindView(R.id.practice_date_next) @Nullable TextView practiceDateNext;
        @BindView(R.id.practice_status_percent) @Nullable TextView progressPercentage;
        @BindView(R.id.multiple_seek_bar) @Nullable SeekBar multiplePracticeSeekBar;
        @BindView(R.id.practice_repetition_add) @Nullable ImageButton practiceRepetitionAdd;
        @BindView(R.id.practice_progress) @Nullable ProgressBar practiceProgress;

        ViewPracticeHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
            ButterKnife.bind(this, this.cardView);
        }

        public PracticeModel getPractice (){
            return practice;
        }

        public void setPractice(PracticeModel practice) {
            this.practice = practice;
        }
    }
}
