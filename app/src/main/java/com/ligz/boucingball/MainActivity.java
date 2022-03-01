package com.ligz.boucingball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    //Sensors stuff
    private SensorManager sensorManager;
    private Sensor accelerometer;

    //Axis
    private static int x, y;

    //Display size
    private static int height, width;

    //To drawing
    ShapeDrawable drawable;

    //Score
    private static int a = 0, b = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a reference to a SensorManager and Acelerometer sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Get the bounds of display
        Display display = getWindowManager().getDefaultDisplay();
        height = display.getHeight();
        width = display.getWidth();
        //Log.d("Tamaño de la pantalla", "x "+ width +"y "+height);

        //To draw in the view
        Ball ball = new Ball(this);
        setContentView(ball);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this,
                    accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //When the values of 'x' and 'y' axis change I update global variables
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            //Bounds for X axis
            if(x + 160 - (int) sensorEvent.values[0] >= width){
                x = width - 160;
            }else if(x - (int) sensorEvent.values[0] <= 1){
                x = 0;
            }else{
                x -= (int) sensorEvent.values[0];
            }

            //Bounds for Y axis
            if(y + 160 + (int) sensorEvent.values[1] >= height){
                if(x + 160 >= ((width / 2) - 130) &&  x +160 <= ((width / 2) + 130)){
                    y = height/2;
                    x = width/2;
                    a++;
                }
                else{
                    y = height - 160;
                }

            }else if(y + (int) sensorEvent.values[1] <= 1){
                if(x + 160 >= ((width / 2) - 130) &&  x +160 <= ((width / 2) + 130)){
                    y = height/2;
                    x = width/2;
                    b++;
                }else{
                    y = 0;
                }

            }else{
                y += (int) sensorEvent.values[1];
            }

            //Log.d("Valores de los ejes", "x "+ x +", y "+y);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public class Ball extends View{
        static final int width = 160;
        static final int height = 160;

        public Ball(Context context) {
            super(context);
            drawable = new ShapeDrawable(new OvalShape());
            drawable.getPaint().setColor(0xff74AC23);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Paint p = new Paint(); // set some paint options
            p.setColor(Color.rgb(0, 95, 115));

            RectF oval = new RectF(MainActivity.x, MainActivity.y, MainActivity.x + width, MainActivity.y
                    + height); // set bounds of rectangle

            canvas.drawColor(Color.rgb(42, 157, 143));
            //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.field),0,0,null); <--- Draw a background

            //Porterias
            RectF p1 = new RectF((MainActivity.width / 2) - 130, 0, (MainActivity.width / 2) + 130, 160);
            canvas.drawRect(p1, p);
            RectF p2 = new RectF((MainActivity.width / 2) - 130, MainActivity.height-160, (MainActivity.width / 2) + 130, MainActivity.height-160+160);
            canvas.drawRect(p2, p);

            //Field lines
            p.setColor(Color.rgb(0, 95, 115));
            RectF center = new RectF(0 , (MainActivity.height/2)-5, MainActivity.width , (MainActivity.height/2) + 5);
            canvas.drawRect(center, p);
            canvas.drawCircle(MainActivity.width/2, MainActivity.height/2, 150, p);

            //Ball
            p.setColor(Color.rgb(38, 70, 83));
            canvas.drawOval(oval, p);

            //Score
            Paint paint = new Paint();

            paint.setColor(Color.WHITE);
            paint.setTextSize(60);
            paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
            canvas.drawText(MainActivity.a + " - " + MainActivity.b, 16, 60, paint);

            invalidate();
        }
    }
}