package yokohama.osm;

import android.content.Context;
import android.util.AttributeSet;

public class SquareImageView extends android.support.v7.widget.AppCompatImageView {
    public SquareImageView(Context context) {
        super(context);
//        setScaleType(ScaleType.CENTER_CROP);
        setScaleType(ScaleType.CENTER);
    }
    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setScaleType(ScaleType.CENTER_CROP);
        setScaleType(ScaleType.CENTER);
    }
    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        setScaleType(ScaleType.CENTER_CROP);
        setScaleType(ScaleType.CENTER);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 常に横幅と同じ縦幅を持った矩形のサイズを要求する
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
        //setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
