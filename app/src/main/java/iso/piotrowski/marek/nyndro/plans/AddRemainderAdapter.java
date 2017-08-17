package iso.piotrowski.marek.nyndro.plans;

import android.database.Cursor;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.practice.PracticeAdapter;

/**
 * Created by Marek on 12.08.2016.
 */
public class AddRemainderAdapter extends RecyclerView.Adapter<AddRemainderAdapter.AddRemainderHolder> {

    private Cursor cursorPractice;
    private int selectedPosition = 0;
    private boolean firstTime = true;

    public void setCursorPractice(Cursor cursorPractice) {
        this.cursorPractice = cursorPractice;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public class AddRemainderHolder extends RecyclerView.ViewHolder {
        CardView cv;

        AddRemainderHolder(CardView cv) {
            super(cv);
            this.cv = cv;
        }
    }

    @Override
    public AddRemainderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv;

        cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.add_remainder_cardview, parent, false);

        return new AddRemainderHolder(cv);
    }

    @Override
    public void onBindViewHolder(AddRemainderHolder holder, final int position) {
        ImageView practiceImage = (ImageView) holder.cv.findViewById(R.id.add_remainder_image_practice);
        TextView practiceName = (TextView) holder.cv.findViewById(R.id.add_remainder_name_practice);
        if (cursorPractice != null) {
            if (cursorPractice.moveToPosition(position)) {
//                practiceImage.setImageDrawable(holder.cv.getResources().getDrawable(cursorPractice.getInt(PracticeAdapter.PRACTICE_IMAGE_ID_ID)));
//                practiceName.setText(cursorPractice.getString(PracticeAdapter.NAME_ID));
            }
        }

        if ((position == selectedPosition)) {
            addAnimation(holder.cv.findViewById(R.id.add_remainder_image_practice), position);
           // addAnimation(holder.cv.findViewById(R.id.add_remainder_name_practice), position);
        }

        holder.cv.clearAnimation();

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                addAnimation(view.findViewById(R.id.add_remainder_image_practice), position);
                //addAnimation(view.findViewById(R.id.add_remainder_name_practice), position);
            }
        });
    }

    private void addAnimation(final View view, final int position) {
        setSelectedPosition(position);
        Animation animation;
        animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.cardview_selected_animation);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if ((selectedPosition == position)) {
                    view.startAnimation(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.clearAnimation();
        view.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return cursorPractice.getCount();
    }
}
