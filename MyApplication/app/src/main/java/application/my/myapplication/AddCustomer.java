package application.my.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;


public class AddCustomer extends AppCompatActivity implements View.OnClickListener {
    private Button bAdd, bBAck;
    private EditText eCustomerName,ePhoneNum,eLastDate,eCustomerWants;
    DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference customerRef = RootRef.child("customer");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        eCustomerName = (EditText) findViewById(R.id.createCustomerName);
        ePhoneNum = (EditText) findViewById(R.id.createCustomerPhoneNum);
        eLastDate = (EditText) findViewById(R.id.customerBackDate);
        eCustomerWants = (EditText) findViewById(R.id.customerWants);

        bAdd = (Button) findViewById(R.id.addcustomerButton);
        bBAck = (Button) findViewById(R.id.ABack);

        bBAck.setOnClickListener(this);
        bAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addcustomerButton:
                createCustomer();
                startActivity(new Intent(this,PlayMenu.class));
                break;
            case R.id.ABack:
                startActivity(new Intent(this,PlayMenu.class));
                break;
        }
    }

    private void createCustomer(){
            Customer customer = new Customer();
            customer.Name = eCustomerName.getText().toString();
            customer.Phone = ePhoneNum.getText().toString();
            customer.LastModifiedDate = (new Date()).toString();
            customer.Wants = eCustomerWants.getText().toString();
            customerRef.push().setValue(customer);
    }
}

