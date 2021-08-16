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

public class Login extends AppCompatActivity {

  private TextInputEditText textInputEditTextUsername,textInputEditTextPassword;
  private Button buttonLogin;
  private TextView textViewSignup;
  private ProgressDialog pDialog;
  private OkHttpClient client;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    client = new OkHttpClient();

    textInputEditTextUsername = findViewById(R.id.username);
    textInputEditTextPassword = findViewById(R.id.password);
    textViewSignup = findViewById(R.id.signUpText);

    //progressBar = findViewById(R.id.progress);

    buttonLogin = findViewById(R.id.buttonLogin);

    textViewSignup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
        finish();
      }
    });

    buttonLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        final String username,password;

        username = String.valueOf(textInputEditTextUsername.getText());
        password = String.valueOf(textInputEditTextPassword.getText());

        if(username.length()==0)
        {

          String message = "You Forgot to Enter Your Username";

          Toast.makeText(getApplicationContext(),message,
              Toast.LENGTH_SHORT).show();

        } else if(password.length()==0){

          String message = "You Forgot to Enter Your Password";

          Toast.makeText(getApplicationContext(),message,
              Toast.LENGTH_SHORT).show();
        }
        else{

          if(NetworkCheck.isNetworkThere(getApplicationContext()))
          {
            new AuthenticateUser(username,password).execute();

          }
          else
          {

            String message = "No Internet Connection";

            Toast.makeText(getApplicationContext(),message,
                Toast.LENGTH_SHORT).show();
          }

        }

      }

    });

  }

  @SuppressLint("StaticFieldLeak")
  private class AuthenticateUser extends AsyncTask<String, String,String>
  {
    private final String username,password;

    private AuthenticateUser(String username,String password) {
      // TODO Auto-generated constructor stub

      this.username = username;
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

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

          }
          else if (w.toString().matches("NO")){

            String message = "Unable to Login";

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

        String message = "An error occurred while attempting to login";

        Toast.makeText(getApplicationContext(),message,
            Toast.LENGTH_SHORT).show();

      }

    }

    @Override protected void onPreExecute ()
    {
      super.onPreExecute();

      pDialog = new ProgressDialog(Login.this);
      pDialog.setMessage("Login Attempt...");
      pDialog.setIndeterminate(false);
      pDialog.setCancelable(false);
      pDialog.show();

    }

    @Override
    protected String doInBackground(String... args) {
      // TODO Auto-generated method stub

      String result = null;

      RequestBody formBody = new FormBody.Builder()
          .add("username",username)
          .add("password",password)
          .build();

      Request request = new Request.Builder()
          .url("https://projects.appoutdoorz.com/crimeSystem/login.php")
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