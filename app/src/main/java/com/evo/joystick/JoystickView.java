package com.evo.joystick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class JoystickView extends View {

    private static final float CENTER_THRESHOLD = 10.0f; // Adjust as needed

    private float centerX;
    private float centerY;
    private float baseRadius = 200.0f;
    private float hatRadius = 80.0f;

    private Paint basePaint;
    private Paint hatPaint;
    private PointF hatPosition;

    private TextView directionTextView;

    public JoystickView(Context context) {
        super(context);
        init();
    }

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        basePaint.setColor(Color.BLUE); // Change color here
        basePaint.setStyle(Paint.Style.FILL);

        hatPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hatPaint.setColor(Color.RED); // Change color here
        hatPaint.setStyle(Paint.Style.FILL);

        hatPosition = new PointF();

        // Create TextView for direction
        directionTextView = new TextView(getContext());
        directionTextView.setText("Stop");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        centerX = w / 2f;
        centerY = h / 2f;

        // Set the starting position of the red circle to the center
        hatPosition.set(centerX, centerY);

        // Set initial position for TextView
        directionTextView.setX(centerX + baseRadius + 16); // Adjust as needed
        directionTextView.setY(centerY - directionTextView.getHeight() / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the blue circle
        canvas.drawCircle(centerX, centerY, baseRadius, basePaint);

        // Draw the red circle within the blue circle
        canvas.drawCircle(hatPosition.x, hatPosition.y, Math.min(hatRadius, baseRadius), hatPaint);

        // Set the text color for the TextView dynamically
        directionTextView.getPaint().setColor(Color.RED); // Change color here

        // Draw the TextView on the canvas
        directionTextView.measure(0, 0); // Measure to get the correct height

        // Ensure the TextView has valid layout parameters
        if (directionTextView.getLayoutParams() == null) {
            directionTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        canvas.drawText(directionTextView.getText().toString(), directionTextView.getX(), directionTextView.getY() + directionTextView.getMeasuredHeight(), directionTextView.getPaint());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        float distance = calculateDistance(x, y, centerX, centerY);

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (distance < baseRadius - hatRadius) { // Adjusted condition
                    hatPosition.set(x, y);
                    // Update direction in TextView
                    updateDirection();
                } else {
                    float angle = calculateAngle(x, y, centerX, centerY);
                    hatPosition.set(
                            centerX + (baseRadius - hatRadius) * (float) Math.cos(angle),
                            centerY + (baseRadius - hatRadius) * (float) Math.sin(angle)
                    );
                    // Update direction in TextView
                    updateDirection();
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                hatPosition.set(centerX, centerY);
                // Reset direction in TextView
                updateDirection();
                invalidate();
                break;
        }

        return true;
    }

    private void updateDirection() {
        // Check if the joystick is at the center
        float distanceToCenter = calculateDistance(hatPosition.x, hatPosition.y, centerX, centerY);
        if (distanceToCenter < CENTER_THRESHOLD) {
            directionTextView.setText("Stop");
            return;
        }

        // Calculate direction based on hatPosition and update TextView
        // This is just a simple example; adjust as needed for your specific logic
        float angle = calculateAngle(hatPosition.x, hatPosition.y, centerX, centerY);
        if (angle >= -Math.PI / 4 && angle < Math.PI / 4) {
            directionTextView.setText("Right");
        } else if (angle >= Math.PI / 4 && angle < 3 * Math.PI / 4) {
            directionTextView.setText("Down");
        } else if (angle >= -3 * Math.PI / 4 && angle < -Math.PI / 4) {
            directionTextView.setText("Up");
        } else {
            directionTextView.setText("Left");
        }
    }

    private float calculateDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private float calculateAngle(float x1, float y1, float x2, float y2) {
        return (float) Math.atan2(y1 - y2, x1 - x2);
    }
}
