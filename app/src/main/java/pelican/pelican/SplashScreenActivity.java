package pelican.pelican;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e){
            Log.d("Splash Screen Error", e.toString());
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // if user is already logged in
        if (mAuth.getCurrentUser() != null) {

            // create intent to skip login/registration and go to MainActivity
            Intent intent = new Intent(getApplication(), MainActivity.class);

            // erase everything on top of this activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // go to MainActivity
            startActivity(intent);

            // close current activity
            finish();

        // user is not logged in
        } else {

            // create intent to go to login/registration
            Intent intent = new Intent(getApplication(), ChooseLoginRegistrationActivity.class);

            // erase everything on top of this activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // go to ChooseLoginRegistrationActivity
            startActivity(intent);

            // close current activity
            finish();
        }
    }
}
