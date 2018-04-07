package pelican.pelican;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;

import static java.lang.System.exit;

public class SplashScreenActivity extends AppCompatActivity{

    final int PERMISSIONS_REQUEST = 0;

    private static final String TAG = "SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // show splash screen for a moment
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e){
            Log.d(TAG, e.toString());
        }

        // check for permissions. if not granted, ask for them
        if (ContextCompat.checkSelfPermission(SplashScreenActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(SplashScreenActivity.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashScreenActivity.this,
                    new String[]{android.Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    PERMISSIONS_REQUEST);
        } else {
            Login();
        }
    }

    protected void Login (){
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

    public boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (hasAllPermissionsGranted(grantResults)) {
            // permissions granted, proceed
            Login();
        } else {
            // no permissions --> exit
            exit(0);
        }
    }
}
