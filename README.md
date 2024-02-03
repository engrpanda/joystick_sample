Android Application using Android Studio

Simple Joystick you can import to your project. Just copy the JoystickView.Java to your project and make sure to copy the xml layout JoystickView to your layout



![299174283-ccd7ac96-6734-4cdb-a686-17fb137d559f](https://github.com/engrpanda/joystick_sample/assets/53995355/07f00295-ff0e-4810-bab1-7091c8edac7b)


https://github.com/engrpanda/joystick_sample/assets/53995355/21cdb381-dd26-47d1-91b7-78407671853d



# Sample code 

```
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, JoystickView.JoystickListener {
    private JoystickView joystickView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_and_receive_sei_message);

        joystickView = findViewById(R.id.joystickView);
        joystickView.setJoystickListener(new JoystickView.JoystickListener() {
            @Override
            public void onJoystickMoved(float xPercentage, float yPercentage) {
                handleJoystickMovement(xPercentage, yPercentage);
            }
        });
    }

    private void handleJoystickMovement(float xPercentage, float yPercentage) {
        if (Math.abs(xPercentage) > Math.abs(yPercentage)) {
            if (xPercentage > 0) {
                showToast("Right");
                // Add your specific motion logic for moving right
            } else if (xPercentage < 0) {
                showToast("Left");
                // Add your specific motion logic for moving left
            } else {
                showToast("Stop X");
                // Add your specific motion logic for stopping in the X-axis
            }
        } else {
            if (yPercentage > 0) {
                showToast("Down");
                // Add your specific motion logic for moving backward
            } else if (yPercentage < 0) {
                showToast("Up");
                // Add your specific motion logic for moving forward
            } else {
                showToast("Stop Y");
                // Add your specific motion logic for stopping in the Y-axis
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Implement any touch handling logic if needed
        return false;
    }
```
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}


