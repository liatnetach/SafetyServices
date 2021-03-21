package com.first.safetyservices.view.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.first.safetyservices.R;
import com.first.safetyservices.model.UpdateUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class UserValidation extends AppCompatActivity implements Observer,View.OnClickListener{
    private static final int ResultLoadImage=1;
    private static final int ResultTakePhoto=2;
    private Dialog myDialog;
    private FirebaseAuth mAuth;
    private ImageView idImage, selfieImage;
    private Button chooseId, takeSelfie, saveDocs,sendSMS;
    private EditText phoneToVerify;
    private CountryCodePicker ccp;
    private TextView uid;
    private CheckBox babysitter,dogsitter,dogwalking;
    private String combinedPhoneNum,selected_country_code,testingPhone="+16505553434",testingVerificationId="123456",verificationId,userVerifCode;
    private List<String> userServices;
    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private UpdateUser updateuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_validation);
        myDialog = new Dialog(this);
        idImage=(ImageView)findViewById(R.id.idProf);
        selfieImage=(ImageView)findViewById(R.id.selfieProof);
        mAuth=FirebaseAuth.getInstance();
        chooseId=(Button)findViewById(R.id.uploadID);
        takeSelfie=(Button)findViewById(R.id.uploadSelfie);
        saveDocs=(Button)findViewById(R.id.saveDocuments);
        saveDocs.setEnabled(false);
        saveDocs.setOnClickListener(this);
        chooseId.setOnClickListener(this);
        takeSelfie.setOnClickListener(this);
        ccp = findViewById(R.id.ccp);
        uid=(TextView)findViewById(R.id.userId);
        uid.setText(getIntent().getStringExtra("uid"));
        updateuser=new UpdateUser(uid.getText().toString());
        updateuser.addObserver(this);
        babysitter=(CheckBox)findViewById(R.id.babysitterCbox);
        dogsitter=(CheckBox)findViewById(R.id.dogsitterCbox);
        dogwalking=(CheckBox)findViewById(R.id.dogwalkingCbox);
        userServices=new ArrayList<String>();
//phone verification arguments
        phoneToVerify=(EditText)findViewById(R.id.phoneNumberTxt);
        sendSMS=(Button)findViewById(R.id.callRecaptchaBtn);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() { //mechanism for the sms code sending
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
            }
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Intent intent=new Intent(UserValidation.this,Login.class);
                UserValidation.this.verificationId=verificationId;
            }
        };
    }
//manage the buttons in our activity- upload ID from gallery, capture selfie and save all
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.uploadSelfie://press on the uploadselfie btn
                selfieImage.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, ResultTakePhoto);
                } catch (ActivityNotFoundException e) {
                    System.out.println("Something went wrong");
                }
                break;
            case R.id.uploadID://press on the uploadID btn
                idImage.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                Intent gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,ResultLoadImage);
                break;
            case R.id.saveDocuments://checking that every field filled with data before continue
                if(idImage.getDrawable()!=null&&selfieImage.getDrawable()!=null) {
                    if(babysitter.isChecked()) userServices.add("Babysitter");
                    if(dogsitter.isChecked()) userServices.add("Dogsitter");
                    if(dogwalking.isChecked()) userServices.add("Dogwalking");
                    if(!userServices.isEmpty()) {
                        Toast.makeText(UserValidation.this, "validation succeeded.",
                                Toast.LENGTH_LONG).show();
                        updateuser.updateProviderPhone(combinedPhoneNum);
                        updateuser.updateProviderServices(userServices);
                        updateuser.updateProviderValidate(true);
                        Intent goToProfile = new Intent(UserValidation.this, Login.class);
                        startActivity(goToProfile);
                    }
                    else{
                        babysitter.setTextColor(getResources().getColor(R.color.red));
                        dogsitter.setTextColor(getResources().getColor(R.color.red));
                        dogwalking.setTextColor(getResources().getColor(R.color.red));
                    }
                }
                else {
                    idImage.setBackgroundColor(getResources().getColor(R.color.red));
                    selfieImage.setBackgroundColor(getResources().getColor(R.color.red));
                }
                break;
        }
    }
//saving the pictures from camera/gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ResultLoadImage && resultCode==RESULT_OK && data!=null) {//save the uploaded ud image
            Uri selectedImage = data.getData();
            idImage.setImageURI(selectedImage);
            updateuser.updateIDFile(selectedImage);
        }
        else if(requestCode==ResultTakePhoto && resultCode==RESULT_OK && data!=null){//save the picture that was taken
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            selfieImage.setImageBitmap(imageBitmap);
            updateuser.updateSelfieFile(imageBitmap);
        }
    }
//opening new dialog for the user phone verification
    public void openPhoneVerificationPopup(View view) {
        TextView txtclose,errorMsg;
        Button verifyCode;
        myDialog.setContentView(R.layout.phone_verification_popup);
        txtclose=(TextView) myDialog.findViewById(R.id.txtclosing);;
        txtclose.setText("X");
        errorMsg=(TextView)myDialog.findViewById(R.id.errorMsg);
        errorMsg.setText("");
        EditText userCode;
        userCode=(EditText)myDialog.findViewById(R.id.popupEditTxt);
        verifyCode=(Button)myDialog.findViewById(R.id.sendBtn);
        String phoneNum=phoneToVerify.getText().toString();
        selected_country_code = ccp.getSelectedCountryCodeWithPlus();
         combinedPhoneNum= selected_country_code+phoneNum;
        if(selected_country_code.isEmpty()||phoneNum.isEmpty())
        {
            phoneToVerify.setHintTextColor(getResources().getColor(R.color.red));
            ccp.setContentColor(getResources().getColor(R.color.red));
        }
        else {
            sendSMS.setEnabled(false);
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(combinedPhoneNum).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallbacks).build();
            PhoneAuthProvider.verifyPhoneNumber(options);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendSMS.setEnabled(true);
                    myDialog.dismiss();
                }
            });
            verifyCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userVerifCode=userCode.getText().toString();
                    if(userVerifCode.equals(testingVerificationId)&&combinedPhoneNum.equals(testingPhone)) {
                        saveDocs.setEnabled(true);
                        phoneToVerify.setEnabled(false);
                        myDialog.dismiss();
                    }
                    else
                        errorMsg.setText("The code is wrong");
                    //the next lines of code will work only for real Android device - in Emulator the Recaptcha will activate automatically
                    //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, userVerifCode);
                    //signInWithPhoneAuthCredential(credential);
                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        }
        }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {    // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                        } else {// Sign in failed
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                System.out.println("The verification code invalid"); // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}