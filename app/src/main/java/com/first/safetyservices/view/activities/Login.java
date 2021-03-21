package com.first.safetyservices.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.first.safetyservices.model.SearchUser;
import com.first.safetyservices.model.User;
import com.first.safetyservices.view.fragments.ChooseUserRegisterFrag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

public class Login extends AppCompatActivity implements Observer {
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private EditText emailText,passText;
    private String uid;
    private SearchUser searchUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchUser = new SearchUser();
        searchUser.addObserver(this);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("loginPrefs",MODE_PRIVATE);
        String action=getIntent().getStringExtra("action");
       if(action!=null&&action.equals("logout"))
            logout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sharedPreferences.getString("userType", null)!=null)
        {
            if(!(sharedPreferences.getString("userType",null).equals("providerNV")))
                 autoLogin(sharedPreferences.getString("keyUser", null), sharedPreferences.getString("keyPass", null));
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void autoLogin(String user, String pass)
    {
        mAuth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid=user.getUid().toString();
                            Toast.makeText(Login.this, "Auto-Login succeeded with user:"+uid,
                                    Toast.LENGTH_SHORT).show();
                            Intent mainActivity=new Intent(Login.this, MainActivity.class);
                            String type=sharedPreferences.getString("userType", null);
                            if(type!=null&&type.equals("provider"))
                            {
                                mainActivity.putExtra("userType",  "provider");
                            }
                            else if(type!=null&&type.equals("regular")){
                                mainActivity.putExtra("userType",  "regular");
                            }
                            startActivity(mainActivity);
                        } else  {                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void logout(){
    SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs",MODE_PRIVATE);
    SharedPreferences.Editor editor=sharedPreferences.edit();
    editor.clear();
    editor.commit();
}

    public void moveToUserProfile(String userType, String uid,String clientname){
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Intent mainActivity=new Intent(this, MainActivity.class);
        mainActivity.putExtra("userType",  userType);
        editor.putString("userType", userType);
        editor.putString("clientname",clientname);
        editor.apply();
        if(userType.equals("providerNV"))
            mainActivity.putExtra("uid",  uid);
        startActivity(mainActivity);
    }
   public void login(View view) {
        emailText=findViewById(R.id.emailAddressLoginText);
        String email = emailText.getText().toString();
        passText=findViewById(R.id.passwordLoginText);
        String password = passText.getText().toString();
        if (email.isEmpty()||password.isEmpty())
        {
            TextView warningMsg=findViewById(R.id.warningLoginTxt);
            warningMsg.setText("Please fill both Email and Password fields");
            warningMsg.setTextColor(getResources().getColor(R.color.red));
        }//make the user to fill all the fields!
        else
        {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(Login.this, "Login succeeded.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("keyUser", emailText.getText().toString());
                                editor.putString("keyPass", passText.getText().toString());
                                editor.apply();
                                uid=user.getUid();
                                searchUser.searchProviderUser(user.getUid().toString());

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "Login failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof SearchUser)
        {
             if(arg instanceof String[])
             {
                 String[] arr=(String[])arg;
                 if(arr[0]==null) {
                     arr[0]="regular";
                    arr[1]=uid;
                 }
                 moveToUserProfile(arr[0],arr[1],arr[2]);
             }
        }
    }

    public void moveToReg(View view) {
        Intent mainActivity=new Intent(this, MainActivity.class);
        mainActivity.putExtra("action",  "moveToRegister");
        startActivity(mainActivity);
    }
}