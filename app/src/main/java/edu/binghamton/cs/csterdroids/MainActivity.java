package edu.binghamton.cs.csterdroids;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    AsteroidView asteroidView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        asteroidView = new AsteroidView(this);
        setContentView(asteroidView);
    }

    class AsteroidView extends SurfaceView implements Runnable {
        Thread gameThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playing;
        boolean paused = true;
        Canvas canvas;
        Paint paint;
        int y;
        int posx, posy;
        int dx, dy;
        int height, width;
        boulder[] b;
        boulder spaceShip;
        private boolean isSpaceShipTouched = false;

        private long thisTimeFrame;
        public AsteroidView(Context context) {
            super(context);

            ourHolder = getHolder();
            paint = new Paint();
        }

        @Override
        public void run() {
            Random r = new Random();
            b = new boulder[5];
            spaceShip = new boulder();
            spaceShip.isSpaceShip = true;
            posx = 50;
            posy = 50;
            dx = 20;
            dy = 45;
            for (int i = 0; i < 5; ++i) {
                b[i] = new boulder();
                b[i].x = r.nextInt(50);
                b[i].y = r.nextInt(50);
                b[i].dx = r.nextInt(30) - 15;
                b[i].dy = r.nextInt(30) - 15;
                b[i].diameter = 95;
            }

            spaceShip.x = 100;
            spaceShip.y = 100;

            spaceShip.dx = 15;
            spaceShip.dy = 15;
            spaceShip.diameter = 120;


            while (playing)
            {
                if (!paused) {
                    update();
                }
                update();
                draw();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {

                }
            }
        }
        public void update() {
            y = y + 5;
            if (y > 200)
                y = 5;

            posx += dx;
            posy += dy;
            if ((posx > width) || (posx < 0))
                dx = -dx;
            if ((posy > height) || (posy < 0))
                dy = -dy;

            for (int i = 0; i < 5; ++i)
                b[i].update();

            spaceShip.update();

        }
        public void draw() {
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();

                width = canvas.getWidth();
                height = canvas.getHeight();

                // Draw the background color
                canvas.drawColor(Color.argb(255, 26, 128, 182));

                // Choose the brush color for drawing
                paint.setColor(Color.argb(255, 255, 255, 255));
                canvas.drawLine(0, 0, 300, y, paint);


                // canvas.drawCircle(posx, posy, 30l, paint);
                for (int i = 0; i < 5; ++i) {
                    b[i].width = width;
                    b[i].height = height;
                    b[i].draw(canvas, paint);
                }

                paint.setColor(Color.argb(255, 255, 0, 0)); // Red color
                spaceShip.width = width;
                spaceShip.height = height;
                spaceShip.draw(canvas, paint);

                ourHolder.unlockCanvasAndPost(canvas);
            }
        }

        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }

        }

        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN){
                spaceShip.fx = motionEvent.getX();
                spaceShip.fy = motionEvent.getY();
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                spaceShip.x = motionEvent.getX();
                spaceShip.y = motionEvent.getY();
            }

            return true;
        }
    }


    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        asteroidView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        asteroidView.pause();
    }

}
