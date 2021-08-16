
package com.lonjeztech.crimemanagementsystem;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SignUp extends AppCompatActivity {

    private  TextInputEditText textInputEditTextFullname,textInputEditTextUsername,
        textInputEditTextPassword,textInputEditTextEmail;

    private Button buttonSignUp;
    private TextView textViewLogin;

    private ProgressDialog pDialog;
    private OkHttpClient client;

    private String fullname,username,password,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        client = new OkHttpClient();

        textInputEditTextFullname = findViewById(R.id.full_name);
        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        textInputEditTextEmail = findViewById(R.id.email);

        buttonSignUp = findViewById(R.id.buttonSignUp);

        textViewLogin = findViewById(R.id.loginText);

        //progressBar = (ProgressBar) findViewById(R.id.progress);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fullname = String.valueOf(textInputEditTextFullname.getText());
                username = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                email = String.valueOf(textInputEditTextEmail.getText());

                if(!fullname.equals("") && !username.equals("") && !password.equals("")
                    && !email.equals("")) {

                    if(NetworkCheck.isNetworkThere(getApplicationContext()))
                    {
                        new SignupUser(fullname,username,password,email).execute();

                    }
                    else
                    {

                        String message = "No Internet Connection";

                        Toast.makeText(getApplicationContext(),message,
                            Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "All fields are required",
                        Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class SignupUser extends AsyncTask<String, String,String>
    {
        private final String fullname,username,password,email;

        private SignupUser(String fullname,String username,String password,String email) {
            // TODO Auto-generated constructor stub

            this.fullname = fullname;
            this.username = username;
            this.email    = email;
            this.password = password;

        }

        @Override protected void onPostExecute(String result)
        {

            pDialog.dismiss();

            if(result != null){

                //Log.d("msg",result);

                try{

                    JSONObject json_data = new JSONObject(result);

                    CharSequence w = (CharSequence) json_data.get("status");

                    if(w.toString().matches("OK"))
                    {

                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);

                        finish();

                    }
                    else if (w.toString().matches("NO")){

                        String message = "Unable to Sign up";

                        Toast.makeText(getApplicationContext(),message,
                            Toast.LENGTH_SHORT).show();

                    }

                }
                catch(JSONException e)
                {
                    //Log.d("msg","Login: "+result+" "+e.toString());

                    String message = "An error occurred while processing info";

                    Toast.makeText(getApplicationContext(),message,
                        Toast.LENGTH_SHORT).show();

                }

            }else{

                String message = "An error occurred while attempting to Sign Up";

                Toast.makeText(getApplicationContext(),message,
                    Toast.LENGTH_SHORT).show();

            }

        }

        @Override protected void onPreExecute ()
        {
            super.onPreExecute();

            pDialog = new ProgressDialog(SignUp.this);
            pDialog.setMessage("Signup Attempt...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            String result = null;

            RequestBody formBody = new FormBody.Builder()
                .add("fullname",fullname)
                .add("email",email)
                .add("username",username)
                .add("password",password)
                .build();

            Request request = new Request.Builder()
                .url("https://projects.appoutdoorz.com/crimeSystem/signup.php")
                //.header("Accept","Application/json")
                .post(formBody)
                .build();

            try {
                Response response = client.newCall(request).execute();
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
    }


}


