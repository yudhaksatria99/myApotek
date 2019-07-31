package com.yudha.myapotek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SigninActivity extends AppCompatActivity {
    TextView create_new_acc;
    EditText xusername,xpassword;
    Button btn_sign_in;

    DatabaseReference reference;

    String USERNAME_KEY="usernamekey";
    String username_key="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        create_new_acc=findViewById(R.id.create_new_acc);
        xusername=findViewById(R.id.xusername);
        xpassword=findViewById(R.id.xpassword);
        btn_sign_in=findViewById(R.id.btn_sign_in);


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ubah state menjadi loading
                btn_sign_in.setEnabled(false);
                btn_sign_in.setText("Loading...");


                final String username=xusername.getText().toString();
                final String password=xpassword.getText().toString();

                if (username.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Username Kosong",Toast.LENGTH_SHORT).show();
                    //ubah state menjadi loading
                    btn_sign_in.setEnabled(true);
                    btn_sign_in.setText("SIGN IN");
                }
                else {
                    if (password.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Password Kosong",Toast.LENGTH_SHORT).show();
                        //ubah state menjadi loading
                        btn_sign_in.setEnabled(true);
                        btn_sign_in.setText("SIGN IN");
                    }
                    else {
                        reference= FirebaseDatabase.getInstance().getReference().child("Users").child(username);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){

                                    //ambil data password dari firebase
                                    String passwordFromFirebase= dataSnapshot.child("password").getValue().toString();

                                    //validsai password dengan password firebase
                                    if(password.equals(passwordFromFirebase)){

                                        //simpan username(key) kepada local
                                        //Menyimpan data kepada local/ (Handphone)
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, xusername.getText().toString());
                                        editor.apply();

                                        Intent gotodashboard=new Intent(SigninActivity.this,DashboardActivity.class);
                                        startActivity(gotodashboard);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Password Salah!",Toast.LENGTH_SHORT).show();
                                        //ubah state menjadi loading
                                        btn_sign_in.setEnabled(true);
                                        btn_sign_in.setText("SIGN IN");
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Username Tidak Ada !",Toast.LENGTH_SHORT).show();
                                    //ubah state menjadi loading
                                    btn_sign_in.setEnabled(true);
                                    btn_sign_in.setText("SIGN IN");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),"Database Eror!",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }


            }
        });

        create_new_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gonewacc=new Intent(SigninActivity.this,NewaccActivity.class);
                startActivity(gonewacc);
            }
        });
    }


}
