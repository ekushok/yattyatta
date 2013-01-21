package my.ekushok.grief;

import org.afree.chart.AFreeChart;
import org.afree.graphics.geom.RectShape;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class ChartView extends View {

	private AFreeChart chart;

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectShape chartArea = new RectShape(0.0,0.0,getWidth(),getHeight());
        this.chart.draw(canvas, chartArea);

    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 引数の情報から画面の横方向の描画領域のサイズを取得する
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //int height = MeasureSpec.getSize(heightMeasureSpec);
        // Viewの描画サイズを横方向を画面端まで使う指定
        setMeasuredDimension(width,height);
    }
	
	public void setChart(AFreeChart chart) {
        this.chart = chart;
    }

}
