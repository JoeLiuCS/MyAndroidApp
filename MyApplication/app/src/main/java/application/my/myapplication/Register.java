package application.my.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class Register extends AppCompatActivity implements View.OnClickListener {

    private Button bCreate, bBack;
    private EditText eUserNCreate,ePasswordCreate,ePasswordConfirm;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance().getInstance();

        eUserNCreate = (EditText) findViewById(R.id.creatAcc);
        ePasswordCreate = (EditText) findViewById(R.id.createPass);
        ePasswordConfirm = (EditText) findViewById(R.id.confirmPass);
        bCreate = (Button) findViewById(R.id.createAccButton);
        bBack = (Button) findViewById(R.id.Back);
        progressDialog = new ProgressDialog(this);

        bCreate.setOnClickListener(this);
        bBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.createAccButton:
                String userString = eUserNCreate.getText().toString();
                String passwordString = ePasswordCreate.getText().toString();
                String passwordConFirmSring = ePasswordConfirm.getText().toString();

                if(TextUtils.isEmpty(userString)){
                    Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(passwordString)){
                    Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
                    return;
                }
                if(passwordString.length() < 8){
                    Toast.makeText(this,"Password length must be greater or equal to 8.",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(passwordConFirmSring)){
                    Toast.makeText(this,"Please enter confirm password",Toast.LENGTH_LONG).show();
                    return;
                }

                if(!passwordString.equals(passwordConFirmSring)){
                    Toast.makeText(this,"Passwords don't match!",Toast.LENGTH_LONG).show();
                    return;
                }

                progressDialog.setMessage("Registering Please Wait...");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(userString, passwordConFirmSring)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Register.this,"Successfully registered",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(Register.this,"Registration Error",Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            }
                        });

                startActivity(new Intent(this,MainActivity.class));
                break;

            case R.id.Back :
                startActivity(new Intent(this,MainActivity.class));
                break;

        }
    }



}

