package application.my.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
class Customer{
    public String Name;
    public String Phone;
    public String LastModifiedDate;
    public String Wants;

}
class RCustomer{
    public String Id;
    public String Name;
    public String Phone;
    public String LastModifiedDate;
    public String Wants;

}
public class PlayMenu extends AppCompatActivity implements View.OnClickListener {


    private Button bLogout,bToAdd;
    private ListView LcustomerInfo;
    private ArrayList<String> data = new ArrayList<String>();
    private FirebaseAuth firebaseAuth;
    DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference customerRef = RootRef.child("customer");
    private FirebaseAuth.AuthStateListener mAuthListener;
    private  String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_menu);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();

        bToAdd = (Button) findViewById(R.id.addCusButton);
        bLogout = (Button) findViewById(R.id.logoutButton);

        LcustomerInfo = (ListView) findViewById(R.id.cusListView);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bToAdd.setOnClickListener(this);
        bLogout.setOnClickListener(this);
    }

    private void showData(DataSnapshot dataSnapshot){
        HashMap<String, HashMap<String,String>> td = (HashMap<String, HashMap<String,String>>) dataSnapshot.child("customer").getValue();
        ArrayList<RCustomer> array  =  new ArrayList<RCustomer>();
        Iterator it = td.entrySet().iterator();
        while (it.hasNext()) {
            RCustomer customer = new RCustomer();
            Map.Entry<String, HashMap<String,String>> pair = (Map.Entry)it.next();
            HashMap<String,String> c = pair.getValue();
            customer.Name = c.get("Name");
            customer.Phone = (c.get("Phone"));
            customer.Id = pair.getKey();
            String time =c.get("LastModifiedDate");
            customer.LastModifiedDate = ( time);

            customer.Wants = c.get("Wants");
            array.add(customer);
        }



        MyListAdapter adapter = new MyListAdapter(this,R.layout.list_item,array);
        LcustomerInfo.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, phone, date, wants;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.listItemCName);
            phone = (TextView) view.findViewById(R.id.listItemCPhone);
            date = (TextView) view.findViewById(R.id.listItemCLastDate);
            wants = (TextView) view.findViewById(R.id.listItemCWants);

            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {

        }
    }
    private class MyListAdapter extends ArrayAdapter<RCustomer>{
        private int layout;
        private List<RCustomer> mObjects;
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference customerRef = RootRef.child("customer");
        private MyListAdapter(Context context,int resource,List<RCustomer> objects){
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if(convertView==null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                 viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.listItemCName);
                viewHolder.phone = (TextView) convertView.findViewById(R.id.listItemCPhone);

                viewHolder.date = (TextView) convertView.findViewById(R.id.listItemCLastDate);

                viewHolder.wants = (TextView) convertView.findViewById(R.id.listItemCWants);

                viewHolder.button = (Button) convertView.findViewById(R.id.CalledButton);
                convertView.setTag(viewHolder);
            }

                viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customerRef.child(getItem(position).Id).child("LastModifiedDate").setValue((new Date().toString()));
                    Toast.makeText(getContext(), "Button was clicked for list item " + mObjects.get(position).Id, Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.name.setText(getItem(position).Name);
            viewHolder.phone.setText(getItem(position).Phone);
            viewHolder.date.setText(getItem(position).LastModifiedDate);
            viewHolder.wants.setText(getItem(position).Wants);

            return convertView;
        }
        public class ViewHolder{
            TextView name, phone, date, wants;
            Button button;
        }

    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addCusButton:
                startActivity(new Intent(this,AddCustomer.class));
                break;
            case R.id.logoutButton:
                firebaseAuth.signOut();
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }

}

