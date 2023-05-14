package com.lichao.mnistapplicantion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteArea extends View {
    private Paint border;
    private Paint point;
    private int width;
    private int height;
    private int interval;
    private int x=0,y=0;
    private List<data> traceData;
    private data mdata;
    private boolean isclear=false;
    public WriteArea(Context context) {
        super(context);
    }

    public WriteArea(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
         traceData = new ArrayList<data>();

        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.WriteArea);
        if(typedArray!=null){
            width= (int) typedArray.getDimension(R.styleable.WriteArea_width,0);
            height= (int) typedArray.getDimension(R.styleable.WriteArea_height,0);
        }
    }

    public WriteArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WriteArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        border=new Paint();
        border.setStyle(Paint.Style.STROKE);
        border.setColor(Color.BLACK);
        border.setStrokeWidth(2);

        interval= (int) (10*3.1);

        point=new Paint();
        point.setColor(Color.BLACK);
        point.setStrokeWidth(interval);
        point.setStrokeCap(Paint.Cap.SQUARE);




        canvas.drawRect(0,0,width,height,border);
        for(int i=1;i<28;i++){
            canvas.drawLine(interval*i,0,interval*i,height,border);
        }
        for(int i=1;i<28;i++){
            canvas.drawLine(0,interval*i,width,interval*i,border);
        }
        if(!isclear) {
            for (int i = 0; i < traceData.size(); i++) {
                canvas.drawPoint(traceData.get(i).getX() * interval - interval / 2, traceData.get(i).getY() * interval - interval / 2, point);
            }
        }

    }

    //需要传入的x,y就是touch事件里手指的坐标
    public void PaintPoint(int x,int y){
        this.x=x;
        this.y=y;
        mdata=new data((int)Math.round(x/interval),(int)Math.round(y/interval));
        traceData.add(mdata);
        postInvalidate();

    }
    public void Clear(){
        isclear=true;
        traceData.clear();
        postInvalidate();
        isclear=false;
    }

    public  List<data> dataBack(){
        return traceData;
    }

}
