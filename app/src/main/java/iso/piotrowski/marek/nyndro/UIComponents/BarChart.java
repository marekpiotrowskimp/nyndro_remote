package iso.piotrowski.marek.nyndro.UIComponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import iso.piotrowski.marek.nyndro.R;

/**
 * Created by Marek on 08.08.2016.
 */
public class BarChart extends View {

    private Context context;
    private int desiredWidth = 100;
    private int desiredHeight = 100;
    private int maxCount;
    private int columnCount;
    private int color;
    private int barSize;
    private int barMargin;
    private int textSize;
    private List<DataObj> dataChart;
    private Paint[] paints;
    private Paint textPaint;
    private Paint background;
    private int countMaximum;
    private int lenght;

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        invalidate();
        requestLayout();
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        invalidate();
        requestLayout();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
        requestLayout();
    }

    public List<DataObj> getDataChart() {
        return dataChart;
    }

    public void setDataChart(List<DataObj> dataChart) {
        this.dataChart = dataChart;
        invalidate();
        requestLayout();
        Init();
    }

    public int getBarSize() {
        return barSize;
    }

    public void setBarSize(int barSize) {
        this.barSize = barSize;
        invalidate();
        requestLayout();
    }

    public int getBarMargin() {
        return barMargin;
    }

    public void setBarMargin(int barMargin) {
        this.barMargin = barMargin;
        invalidate();
        requestLayout();
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        invalidate();
        requestLayout();
    }

    public static class DataObj {
        private String columnName;
        private int progress;
        private int color;

        public DataObj(String columnName, int progress, int color) {
            this.setColumnName(columnName);
            this.setProgress(progress);
            this.setColor(color);
        }

        @Override
        public String toString() {
            return getColumnName();
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

    public BarChart(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.BarChart,
                0, 0);

        try {
            setMaxCount(a.getInteger(R.styleable.BarChart_max_count, 100));
            setColumnCount(a.getInteger(R.styleable.BarChart_column_count, 7));
            setColor(a.getColor(R.styleable.BarChart_color_bar_chart, 0x202020));
            setBarSize(a.getDimensionPixelSize(R.styleable.BarChart_bar_size, 20));
            setBarMargin(a.getDimensionPixelOffset(R.styleable.BarChart_bar_margin, 4));
            setTextSize(a.getDimensionPixelSize(R.styleable.BarChart_text_size, 10));
        } finally {
            a.recycle();
        }
    }

    private void Init() {
        columnCount = columnCount < dataChart.size() ? dataChart.size() : columnCount;
        paints = new Paint[columnCount];
        Iterator<DataObj> iterator = dataChart.iterator();
        int ind = 0;
        countMaximum = 0;
        while (iterator.hasNext()) {
            DataObj dataObj = iterator.next();
            paints[ind] = new Paint();
            paints[ind].setColor(dataObj.getColor());
            paints[ind].setAlpha(255);
            if (countMaximum < dataObj.getProgress()) {
                countMaximum = dataObj.getProgress();
            }
            ind++;
        }
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textSize);
        background = new Paint();
        background.setColor(color);
        background.setAlpha(255);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        lenght = (w) / countMaximum;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //desire dimensions
        desiredWidth = desiredWidth < widthSize ? widthSize : desiredWidth;
        desiredHeight = columnCount * (getBarSize() + getBarMargin());
        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), background);

        if ((dataChart != null) && (!dataChart.isEmpty())) {
            Iterator<DataObj> iterator = dataChart.iterator();
            int ind = 0;
            int marginInWidth = 0;
            int marginInHeight = getBarSize() + getBarMargin();
            while (iterator.hasNext()) {
                DataObj dataObj = iterator.next();
                canvas.drawRect(marginInWidth, ind * marginInHeight,
                        lenght * dataObj.getProgress(), ind * marginInHeight + getBarSize(), paints[ind]);
                canvas.drawText(dataObj.getColumnName(), marginInWidth + getBarMargin(),
                        ind * marginInHeight + getTextSize(), textPaint);
                ind++;
            }
        }
    }
}
