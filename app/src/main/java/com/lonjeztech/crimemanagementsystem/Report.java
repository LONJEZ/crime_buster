package com.lonjeztech.crimemanagementsystem;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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


public class Report extends AppCompatActivity {

    private Spinner countySpinner,subCountySpinner;

    private TextInputEditText textInputEditTextCrimeDescription,
        textInputEditTextLocation,textInputEditTextStreet;

    private Button buttonSubmit;

    private ProgressDialog pDialog;

    private String county,sub_county,crime_description,specific_location,street_name;

    private OkHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report);

        countySpinner = findViewById(R.id.spinner_county);

        subCountySpinner = findViewById(R.id.spin_sub_county);

        subCountySpinner.setVisibility(View.GONE);

        textInputEditTextCrimeDescription = findViewById(R.id.et_crime_description);

        textInputEditTextLocation = findViewById(R.id.et_location);

        textInputEditTextStreet = findViewById(R.id.et_street_name);

        buttonSubmit = findViewById(R.id.button_submit);

        //progressBar = findViewById(R.id.progress);

        client = new OkHttpClient();

        // Create an ArrayAdapter using the string array and
        // a default spinner layout
        ArrayAdapter<CharSequence> county_list = ArrayAdapter.createFromResource(
            this, R.array.County,
                android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of
        // choices appears
        county_list.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        countySpinner.setAdapter(county_list);

        countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int position, long id) {

                if (adapterView.getSelectedItem().toString().equals("Mombasa")) {

                    subCountySpinner.setVisibility(View.VISIBLE);

                    ArrayAdapter<CharSequence> ad_name
                            = ArrayAdapter.createFromResource(
                            getApplicationContext(),
                            R.array.mombasa_subcounty,
                            android.R.layout
                                    .simple_spinner_item);

                    ad_name.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);

                    subCountySpinner.setAdapter(ad_name);

                    county = countySpinner.getSelectedItem().toString();
                }
                else if (adapterView.getSelectedItem().toString().equals("Nakuru")) {

                    subCountySpinner.setVisibility(View.VISIBLE);

                    ArrayAdapter<CharSequence> ad_name
                            = ArrayAdapter.createFromResource(
                            getApplicationContext(),
                            R.array.nakuru_subcounty,
                            android.R.layout
                                    .simple_spinner_item);

                    ad_name.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);

                    subCountySpinner.setAdapter(ad_name);

                    county = countySpinner.getSelectedItem().toString();
                }
                else if (adapterView.getSelectedItem().toString().equals("Baringo")) {

                    subCountySpinner.setVisibility(View.VISIBLE);

                    ArrayAdapter<CharSequence> ad_name
                            = ArrayAdapter.createFromResource(
                            getApplicationContext(),
                            R.array.baringo_subcounty,
                            android.R.layout
                                    .simple_spinner_item);

                    ad_name.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);

                    subCountySpinner.setAdapter(ad_name);

                    county = countySpinner.getSelectedItem().toString();
                }
                else if (adapterView.getSelectedItem().toString().equals("Nairobi")){

                    subCountySpinner.setVisibility(View.VISIBLE);

                    ArrayAdapter<CharSequence> ad_name
                            = ArrayAdapter.createFromResource(
                            getApplicationContext(),
                            R.array.nairobi_subcounty,
                            android.R.layout
                                    .simple_spinner_item);

                    ad_name.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);

                    subCountySpinner.setAdapter(ad_name);

                    county = countySpinner.getSelectedItem().toString();
                }
                else{

                    subCountySpinner.setVisibility(View.VISIBLE);

                    ArrayAdapter<CharSequence> ad_name
                            = ArrayAdapter.createFromResource(
                            getApplicationContext(),
                            R.array.kajiado_subcounty,
                            android.R.layout
                                    .simple_spinner_item);

                    ad_name.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);

                    subCountySpinner.setAdapter(ad_name);

                    county = countySpinner.getSelectedItem().toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getApplicationContext(), "Please select at least one County",
                    Toast.LENGTH_SHORT).show();
            }

        });

        subCountySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int position, long id) {

                sub_county =subCountySpinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getApplicationContext(), "Please select at least one Sub County",
                    Toast.LENGTH_SHORT).show();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                crime_description = String.valueOf(textInputEditTextCrimeDescription.getText());
                specific_location = String.valueOf(textInputEditTextLocation.getText());
                street_name = String.valueOf(textInputEditTextStreet.getText());

                if(!crime_description.equals("") && !specific_location.equals("")
                    && !county.isEmpty() && !sub_county.isEmpty() && !street_name.equals("")) {

                    if(NetworkCheck.isNetworkThere(getApplicationContext()))
                    {
                        new ReportCrime(county,sub_county,crime_description,
                            specific_location,street_name).execute();

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
    private class ReportCrime extends AsyncTask<String, String,String>
    {
        private final String county,sub_county,crime_description,specific_location,street_name;

        private ReportCrime(String county,String sub_county,String crime_description,
                            String specific_location,String street_name) {
            // TODO Auto-generated constructor stub

            this.county = county;
            this.sub_county = sub_county;
            this.specific_location = specific_location;
            this.crime_description = crime_description;
            this.street_name = street_name;

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
                        String message = "The Crime has been Reported Successfully";

                        Toast.makeText(getApplicationContext(),message,
                            Toast.LENGTH_LONG).show();

                        finish();

                    }
                    else if (w.toString().matches("NO")){

                        String message = "Unable to Report Crime";

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

                String message = "An error occurred while attempting to Report Crime";

                Toast.makeText(getApplicationContext(),message,
                    Toast.LENGTH_SHORT).show();

            }

        }

        @Override protected void onPreExecute ()
        {
            super.onPreExecute();

            pDialog = new ProgressDialog(Report.this);
            pDialog.setMessage("Crime Reporting...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            String result = null;

            RequestBody formBody = new FormBody.Builder()
                .add("crime_description",crime_description)
                .add("specific_location",specific_location)
                .add("county",county)
                .add("sub_county",sub_county)
                .add("street_name",street_name)
                .build();

            Request request = new Request.Builder()
                .url("https://projects.appoutdoorz.com/crimeSystem/report.php")
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