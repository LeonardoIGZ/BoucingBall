package com.ligz.boucingball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    //Sensors stuff
    private SensorManager sensorManager;
    private Sensor accelerometer;

    //Axis
    private static int x, y;

    //Display size
    private int height, width;

    //To drawing
    ShapeDrawable drawable;

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
            x -= (int) sensorEvent.values[0];
            y += (int) sensorEvent.values[1];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public class Ball extends View{
        static final int width = 100;
        static final int height = 100;

        public Ball(Context context) {
            super(context);
            drawable = new ShapeDrawable(new OvalShape());
            drawable.getPaint().setColor(0xff74AC23);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            RectF oval = new RectF(MainActivity.x, MainActivity.y, MainActivity.x + width, MainActivity.y
                    + height); // set bounds of rectangle
            Paint p = new Paint(); // set some paint options
            p.setColor(Color.BLUE);
            canvas.drawOval(oval, p);
            invalidate();
        }
    }
}