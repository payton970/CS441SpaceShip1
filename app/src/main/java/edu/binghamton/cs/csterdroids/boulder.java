package edu.binghamton.cs.csterdroids;

import android.graphics.Canvas;
import android.graphics.Paint;

public class boulder {
    float x, y, dx, dy, fx, fy, diameter;
    float width, height;
    public boolean isSpaceShip = false;

    public void update()
    {
        if(!isSpaceShip){
            x += dx;
            y += dy;
            if (x < 0) dx = -dx;
            if (y < 0) dy = -dy;
            if (x > width) dx = -dx;
            if (y > height) dy = -dy;
        }
        else{
            dx = fx - x;
            dy = fy - y;
            if (x < 0) dx = -dx;
            if (y < 0) dy = -dy;
            if (x > width) dx = -dx;
            if (y > height) dy = -dy;
            if(Math.abs(dx) > 1 || Math.abs(dy) > 1){
                x += dx / 5.0;
                y += dy / 5.0;
            }
        }
    }

    public void draw(Canvas canvas, Paint paint)
    {
       canvas.drawCircle(x, y, diameter, paint);
    }

}
