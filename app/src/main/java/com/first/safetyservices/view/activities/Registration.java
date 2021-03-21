package com.first.safetyservices.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.first.safetyservices.R;
import com.first.safetyservices.model.GiveServiceUser;
import com.first.safetyservices.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private String email,password,address,name, uid;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private  FirebaseDatabase database;
    private boolean isRegUser;
    private User newUser;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isRegUser=getIntent().getBooleanExtra("is_reg_user",false);//getting the user type- true=regular user, false=give service user
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Registration.this, MainActivity.class);
                intent.putExtra("action","moveToRegister");
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void register(View view) {
        EditText userText=findViewById(R.id.emailReg);
        email = userText.getText().toString();
        EditText passText=findViewById(R.id.passwordReg);
        password = passText.getText().toString();
        EditText nameText=findViewById(R.id.fullNameReg);
        name = nameText.getText().toString();
        RadioGroup rg=findViewById(R.id.radioAreaGroupFilter);
        if (rg.getCheckedRadioButtonId() == -1||email.isEmpty()||password.isEmpty()||name.isEmpty())
        {
            TextView warning=findViewById(R.id.warningTxt);
            warning.setText("*There are missing detailes- all fields required!");
            warning.setTextColor(getResources().getColor(R.color.red));
        }//make the user to fill all the fields!
        else {
            int selectedRbId=rg.getCheckedRadioButtonId();
           RadioButton radioButton = (RadioButton) rg.findViewById(selectedRbId);
            address = (String) radioButton.getText();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Registration.this, "Register succeeded.",
                                        Toast.LENGTH_SHORT).show();
                                user = mAuth.getCurrentUser();
                                uid = user.getUid();
                                database = FirebaseDatabase.getInstance();
                                if(isRegUser){
                                    myRef = database.getReference("Regular Users").child(uid);//define the path in our database
                                    intent=new Intent(Registration.this, Login.class);
                                    newUser = new User(name,email,address,uid);//create new user with the current data
                                }
                                else{
                                    myRef = database.getReference("Service Provider Users").child(uid);//define the path in our database
                                    intent=new Intent(Registration.this, UserValidation.class);
                                    intent.putExtra("uid",uid);
                                    newUser = new GiveServiceUser(name,email,address,uid);//create new Service Provider user with the current data
                                }
                                myRef.setValue(newUser);
                                startActivity(intent);//reg user move to login screen, service provider move to validation screen
                            } else {
                                Toast.makeText(Registration.this, "Register failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


}