package com.forager.meal.ui.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Catherin on 11/2/2015.
 */
public class PieChart {

    public float[] calculateData(float[] data) {
        float total = 0;
        for (int i = 0; i < data.length; i++) {
            total += data[i];
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = 360 * (data[i] / total);
        }
        return data;
    }

    public static class MyGraphview extends View {
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private float[] value_degree;
        RectF rectf;
        RectF rectCircle;
        float temp = 0;

        public MyGraphview(Context context, float[] values , ArrayList<Integer> rectValues) {
            super(context);
             rectf = new RectF(rectValues.get(0),rectValues.get(0) , rectValues.get(1),rectValues.get(1));
             rectCircle = new RectF(rectValues.get(0)-2, rectValues.get(0)-2, rectValues.get(1)+2, rectValues.get(1)+2);

            this.value_degree = values;
         /*   for (int i = 0; i < values.length; i++) {
                value_degree[i] = values[i];
            }*/
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paint.setColor(Color.parseColor("#FFFFFF"));
            canvas.drawOval(rectCircle, paint);

            for (int i = 0; i < value_degree.length; i++) {
                switch (i) {

                    case 0:
                        int color = Color.parseColor("#ff99cc33");
                        paint.setColor(color);
                        canvas.drawArc(rectf, 0, value_degree[i], true, paint);
                        //canvas.drawLine(15,15,10,10,paint);
                        break;
                    case 2:
                        temp += value_degree[i - 1];
                        int color2 = Color.parseColor("#ffff9933");
                        paint.setColor(color2);
                        canvas.drawArc(rectf, temp, value_degree[i], true, paint);
                        break;
                    case 4:
                        temp += value_degree[i - 1];
                        int color4 = Color.parseColor("#fff9d17a");
                        paint.setColor(color4);
                        canvas.drawArc(rectf, temp, value_degree[i], true, paint);
                        break;

                    case 1:
                    case 3:
                    case 5:
                        temp += value_degree[i - 1];
                        int color1 = Color.parseColor("#ffffffff");
                        paint.setColor(color1);
                        canvas.drawArc(rectf, temp, value_degree[i], true, paint);

                        System.out.println("value_degree" + value_degree[i]);
                        break;
                }
            }
        }


    }
}
