package iso.piotrowski.marek.nyndro.practice;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;

/**
 * Created by marek.piotrowski on 13/08/2017.
 */

public class SimpleCallbackForTouches extends ItemTouchHelper.SimpleCallback {

    private OnSwipedListener swipedListener;

    public interface OnSwipedListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);
    }

    public SimpleCallbackForTouches(int dragDirs, int swipeDirs, OnSwipedListener swipedListener) {
        super(dragDirs, swipeDirs);
        this.swipedListener = swipedListener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (swipedListener != null) {
            swipedListener.onSwiped(viewHolder, direction);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder.getItemViewType() == PracticeAdapter.TypeOfCardView.Standard.getValue()) {
            if (isCurrentlyActive) {
                Paint paint = new Paint();
                paint.setARGB(200, 255, 255, 255);
                drawDeleteInformation(c, viewHolder, paint, false);
            }
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder.getItemViewType() == PracticeAdapter.TypeOfCardView.Standard.getValue()) {
            if (isCurrentlyActive) {
                Paint paint = new Paint();
                paint.setARGB(255, 255, 255, 255);
                drawDeleteInformation(c, viewHolder, paint, true);
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    private void drawDeleteInformation(Canvas c, RecyclerView.ViewHolder viewHolder, Paint paint, boolean withBackground) {
        int margin = 10;
        int iconSize = 128;
        View itemView = viewHolder.itemView;
        int x, y, width, height;
        x = itemView.getLeft() + margin * 2;
        y = itemView.getTop() + margin * 3;
        width = itemView.getWidth() - margin * 2;
        height = itemView.getHeight() - margin * 2;
        int leftBoundary = x + width - margin;
        int middleOfChild = y + height / 2;

        if (withBackground) {
            Paint backgroundPaint = new Paint();
            backgroundPaint.setARGB(255, 223, 33, 33);
            c.drawRect(new Rect(x, y, width, height), backgroundPaint);
        }
        c.drawBitmap(BitmapFactory.decodeResource(NyndroApp.getContect().getResources(),
                android.R.drawable.ic_menu_delete), null, new Rect(leftBoundary - iconSize, middleOfChild - iconSize / 2, leftBoundary, middleOfChild + iconSize / 2), paint);
    }
}
