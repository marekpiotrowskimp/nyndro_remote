package iso.piotrowski.marek.nyndro.practice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import iso.piotrowski.marek.nyndro.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link }.
 * TODO: Replace the implementation with code for your data type.
 */
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
        public final ImageView mImageView;
        public final TextView mNameView;
        public final TextView mDescriptionView;
        public Practice mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.new_practice_image);
            mNameView = (TextView) view.findViewById(R.id.new_practice_name);
            mDescriptionView = (TextView) view.findViewById(R.id.new_practice_description);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
