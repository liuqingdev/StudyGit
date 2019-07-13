package com.lq.projectpoint.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lq.projectpoint.R;

/**
 * Created by lq on 2019/3/12.
 */

public class ChartView extends View {
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private Path mPath;
    private int yPart = 15;//y轴分为15等分
    private int xPart = 2;//x轴分为2等分
    private int offOrigin = 80;//横坐标的第一条数据离起点的距离
    private int xGap;//x轴每条数据之间的间距
    private int yGap;//y轴每条数据之间的间距

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, mHeight / 2);
        initPaint();
        drawXY(canvas, mPaint, mPath);
        drawVerticalLine(canvas, mPaint);
        drawHorizontalLine(canvas, mPaint);
        drawLine(canvas,mPaint,mPath);
    }


    private void drawLine(Canvas canvas,Paint paint,Path path){
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(6);
        path.moveTo(50 + offOrigin + 0 * xGap, (float) ((-mHeight / 2 + 50)*(1/1.5)));
        path.lineTo(50 + offOrigin + 1 * xGap,(float) ((-mHeight / 2 + 50)*(1.1/1.5)));
        path.lineTo(50 + offOrigin + 2 * xGap,(float) ((-mHeight / 2 + 50)*(1/1.5)));
        canvas.drawPath(path,paint);
        path.reset();
        canvas.drawCircle(50 + offOrigin + 0 * xGap, (float) ((-mHeight / 2 + 50)*(1/1.5)),4,paint);
        canvas.drawCircle(50 + offOrigin + 1 * xGap,(float) ((-mHeight / 2 + 50)*(1.1/1.5)),4,paint);
        canvas.drawCircle(50 + offOrigin + 2 * xGap,(float) ((-mHeight / 2 + 50)*(1/1.5)),4,paint);

        paint.setColor(0xff00B694);
        path.moveTo(50 + offOrigin + 0 * xGap, (float) ((-mHeight / 2 + 50)*(0.3/1.5)));
        path.lineTo(50 + offOrigin + 1 * xGap,(float) ((-mHeight / 2 + 50)*(0.4/1.5)));
        path.lineTo(50 + offOrigin + 2 * xGap,(float) ((-mHeight / 2 + 50)*(0.3/1.5)));
        canvas.drawPath(path,paint);
        canvas.drawCircle(50 + offOrigin + 0 * xGap, (float) ((-mHeight / 2 + 50)*(0.3/1.5)),4,paint);
        canvas.drawCircle(50 + offOrigin + 1 * xGap,(float) ((-mHeight / 2 + 50)*(0.4/1.5)),4,paint);
        canvas.drawCircle(50 + offOrigin + 2 * xGap,(float) ((-mHeight / 2 + 50)*(0.3/1.5)),4,paint);
    }


    private void drawHorizontalLine(Canvas canvas, Paint paint) {
        //y轴每条数据之间的间距
        yGap = (-mHeight / 2 + 50) / yPart;
        //水平虚线的间距设为10dp
        int divideLength = (mWidth - 50 - 50) / 10;
        paint.setTextSize(25);
        String[] yStr = {"0.1", "0.2", "0.3","0.4","0.5","0.6","0.7",
                "0.8","0.9","1.0","1.1","1.2","1.3","1.4","1.5"};
        for (int j=1;j<=yPart;j++) {
            for (int i = 0; i < divideLength; i++) {
                if (i % 2 == 0) {
                    canvas.drawLine(50+i*10,yGap*j,50+(i+1)*10,yGap*j,paint);
                }
            }
            canvas.drawText(yStr[j-1],50-40,yGap*j+10,paint);
        }
    }

    private void drawVerticalLine(Canvas canvas, Paint paint) {
        //x轴每条数据之间的间距
        xGap = (mWidth - 50 - 50 - offOrigin * 2) / xPart;
        //竖直虚线的间距设为10dp
        int divideLength = -(-mHeight / 2 + 50) / 10;
        paint.setTextSize(25);
        String[] xStr = {"2019/01/02", "2019/01/15", "2019/01/21"};
        for (int j = 0; j <= xPart; j++) {
            for (int i = 0; i < divideLength; i++) {
                if (i % 2 == 0) {
                    canvas.drawLine(50 + offOrigin + j * xGap, -i * 10,
                            50 + offOrigin + j * xGap, -(i + 1) * 10, paint);
                }
            }
            canvas.drawText(xStr[j],50+offOrigin+j*xGap-50,30,paint);

        }
    }

    private void drawXY(Canvas canvas, Paint paint, Path path) {
        //x轴,y轴
        canvas.drawLine(50, 0, mWidth - 50, 0, paint);
        canvas.drawLine(50, 0, 50, -mHeight / 2 + 50, paint);
        paint.setStyle(Paint.Style.FILL);
        //画x轴三角形
        path.moveTo(mWidth - 50, 0);
        path.lineTo(mWidth - 50, -10);
        path.lineTo(mWidth - 50 + 10, 0);
        path.lineTo(mWidth - 50, 10);
        path.close();
        canvas.drawPath(path, paint);
        //y轴三角形
        path.moveTo(50, -mHeight / 2 + 50);
        path.lineTo(50 - 10, -mHeight / 2 + 50);
        path.lineTo(50, -mHeight / 2 + 50 - 10);
        path.lineTo(50 + 10, -mHeight / 2 + 50);
        path.close();
        canvas.drawPath(path, paint);
        path.reset();//下次更改画笔不影响

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(0xffBBBBB9);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPath = new Path();
    }
}
