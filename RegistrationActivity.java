package pelican.pelican;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private Button mRegister;
    private EditText mName, mEmail, mPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // if the FirebaseAuth state is changed (e.g. someone has logged in/out),
        // this listener calls onAuthStateChanged()
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // if user is logged in
                if (user!=null){

                    // create intent to skip login/registration and go to MainActivity
                    Intent intent = new Intent(getApplication(), MainActivity.class);

                    // erase everything on top of this activity
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // go to MainActivity
                    startActivity(intent);

                    // close current activity
                    finish();
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();
        mRegister = findViewById(R.id.register);
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        // if login button is clicked
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                // grab user name from name field
                final String name = mName.getText().toString();

                // grab email from email field
                final String email = mEmail.getText().toString();

                // grab passwd from passwd field
                final String password = mPassword.getText().toString();

                // if at least one of the fields are empty, display error '*'
                if (email.equals("") || password.equals("")){
                    TextView requireEmail = findViewById(R.id.requireEmail);
                    TextView requirePassword = findViewById(R.id.requirePassword);
                    TextView requireMessage = findViewById(R.id.requireMessage);
                    requireEmail.setVisibility((email.equals("")) ? View.VISIBLE : View.INVISIBLE);
                    requirePassword.setVisibility((password.equals("")) ? View.VISIBLE : View.INVISIBLE);
                    requireMessage.setVisibility(View.VISIBLE);

                    // send email and password to Firebase server
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( RegistrationActivity.this, new OnCompleteListener<AuthResult>() {

                        // if login is not successful, show error popup
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Registration Failed: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                TextView errorText = findViewById(R.id.errormsg);
                                errorText.append(task.getException().toString());
                            } else {
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currUserDb = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                                Map userInfo = new HashMap<>();
                                userInfo.put("email", email);
                                userInfo.put("name", name);
                                userInfo.put("profileImageUrl", "default");
                                currUserDb.updateChildren(userInfo);

                                // create intent to go to MainActivity
                                Intent intent = new Intent(getApplication(), MainActivity.class);

                                // erase everything on top of this activity
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                // go to MainActivity
                                startActivity(intent);

                                // close current activity
                                finish();

                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
