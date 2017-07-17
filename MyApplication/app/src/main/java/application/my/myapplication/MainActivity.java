package application.my.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.text.TextUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button bLogin,bLRegister;
    private EditText eUsername,ePassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();


        eUsername = (EditText)findViewById(R.id.userNameEdit);
        ePassword = (EditText)findViewById(R.id.userPasswordEdit);

        bLogin = (Button) findViewById(R.id.loginButton);
        bLRegister = (Button) findViewById(R.id.createButton);
        progressDialog = new ProgressDialog(this);


        bLogin.setOnClickListener(this);
        bLRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                userLogin();
                break;
            case R.id.createButton: // done
                startActivity(new Intent(this,Register.class));
                break;
        }
    }

    private void userLogin(){
        String email = eUsername.getText().toString().trim();
        String password  = ePassword.getText().toString().trim();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), PlayMenu.class));
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Wrong Email or Password",Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}
