package com.lichao.mnistapplicantion;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.lichao.mnistapplicantion.ml.Mnist;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button id_btn;
    private TextView result_text;
    private TextView probability_text;
    private WriteArea mWriteArearea;
    private Button mClear;
    private float posX=0,posY=0,curPosX = 0,curPosY=0,distance=0;
    private int[] data;
    private List<data> backdata;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventInit();

        id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //数据处理
                    backdata=mWriteArearea.dataBack();
                    data=new int[28*28];
                    Arrays.fill(data, 0);
                    for(int i=0;i<backdata.size();i++){
                        data[backdata.get(i).getY()*28+backdata.get(i).getX()]=255;
                    }
                    int[] shape=new int[]{1,28,28,1};
                    TensorBuffer newInput=TensorBuffer.createFixedSize(new int[]{1, 28, 28, 1}, DataType.FLOAT32);
                    newInput.loadArray(data,shape);



                    Mnist model = Mnist.newInstance(MainActivity.this);

                    // Runs model inference and gets result.
                    Mnist.Outputs outputs = model.process(newInput);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    for(int j=0;j<outputFeature0.getFloatArray().length;j++){
                        System.out.println(outputFeature0.getFloatArray()[j]+" ");
                    }
                    double max = outputFeature0.getFloatArray()[0];
                    int maxindex=0;
                    for (int i = 1; i < outputFeature0.getFloatArray().length; i++) {
                        if (outputFeature0.getFloatArray()[i] > max) {
                            max = outputFeature0.getFloatArray()[i];
                            maxindex=i;
                        }
                    }
                    System.out.println("Max is number "+maxindex+" : "+ max);
                    result_text.setText(""+maxindex);
                    probability_text.setText("probability:"+String.format("%.2f",max));
                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }
            }
        });

        mWriteArearea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.d("Action", "onTouch: 手指按下");
                        posX = motionEvent.getX();
                        posY = motionEvent.getY();
                        // 手指按下
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d("Action", "onTouch: 手指移动");
                        curPosX = motionEvent.getX();
                        curPosY = motionEvent.getY();
                        mWriteArearea.PaintPoint((int) curPosX, (int)curPosY);
                        // 手指移动
                      break;
                    case MotionEvent.ACTION_UP:
                        distance=curPosX-posX;
                        if((distance>0)&&(Math.abs(distance)>10)){
                            Log.d("Action", "onTouch: 向右移动了"+Math.abs(distance));
                        }
                        // 向左移动了
                        if((distance<0)&&(Math.abs(distance)>10)){
                            Log.d("Action", "onTouch: 向左移动了"+Math.abs(distance));
                        }
                        Log.d("Action","onTouch：手指离开");
                        break;
                }
                return true;
            }
        });

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWriteArearea.Clear();
            }
        });
    }
    public void EventInit(){
        id_btn=(Button) findViewById(R.id.identify);
        result_text=(TextView) findViewById(R.id.result);
        probability_text=(TextView) findViewById(R.id.probability);
        mWriteArearea=(WriteArea) findViewById(R.id.WriteArea);
        mClear=(Button) findViewById(R.id.clear);
    }
}
