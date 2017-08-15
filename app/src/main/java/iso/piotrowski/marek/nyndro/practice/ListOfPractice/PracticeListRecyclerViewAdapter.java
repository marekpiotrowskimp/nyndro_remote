package iso.piotrowski.marek.nyndro.practice.ListOfPractice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;
import iso.piotrowski.marek.nyndro.R;


public class PracticeListRecyclerViewAdapter extends RecyclerView.Adapter<PracticeListRecyclerViewAdapter.ViewHolder> {

    private final Practice[] mPractices;
    private final PracticeListFragment.OnListFragmentInteractionListener mListener;

    public PracticeListRecyclerViewAdapter(Practice[] mPractices, PracticeListFragment.OnListFragmentInteractionListener listener) {
        this.mPractices = mPractices;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.practice_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mPractices[position];
        holder.mNameView.setText(mPractices[position].getName());
        holder.mDescriptionView.setText(mPractices[position].getDescription());
        holder.mImageView.setImageDrawable(holder.mView.getResources().getDrawable(mPractices[position].getImageResourcesId()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPractices.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @BindView(R.id.new_practice_image) ImageView mImageView;
        @BindView(R.id.new_practice_name) TextView mNameView;
        @BindView(R.id.new_practice_description) TextView mDescriptionView;
        public Practice mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
