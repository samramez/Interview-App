package com.apppartner.androidprogrammertest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.apppartner.androidprogrammertest.models.CustomHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class LoginActivity extends ActionBarActivity
{

    private EditText loginEditText;
    private EditText passEditText;

    private TextView resultTextView;

    // String for receiving the JSON response from the server
    String response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Enabling Back Button in the ActionBar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.btn_back);


        // Initializing Views
        loginEditText = (EditText) findViewById(R.id.loginEditText);
        passEditText = (EditText) findViewById(R.id.passEditText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

    }


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    public void onLoginClick(View view) {

        // Getting user and pass from EditText
        String username = loginEditText.getText().toString();
        String password = passEditText.getText().toString();

        // Just fot testing purposes:
//        username = "SuperBoise";
//        password = "qwerty";

        //String[] login = {username,password};

        // Running the AsyncTask function and check if user and pass is right
        AuthUserPass authUserPass = new AuthUserPass();
        authUserPass.execute(username, password);
    }



    public class AuthUserPass extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            // Counting the performance time
            long startTime = System.nanoTime();

            ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("username", params[0] ));
            postParameters.add(new BasicNameValuePair("password", params[1] ));
            String res = null;

            try{

                String url = "http://dev.apppartner.com/AppPartnerProgrammerTest/scripts/login.php";

                response = CustomHttpClient.executeHttpPost(url , postParameters);

                res = response;
                //res = res.replaceAll("\\s+","");

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Stopping the stopwatch
            long stopTime = System.nanoTime();
            long timePeriod =  (stopTime - startTime);

            String apiTime = "API call time : " + Long.toString(timePeriod) + " miliseconds" + "\n" ;



            return apiTime + "\n" + res;
        }

        @Override
        protected void onPostExecute(String result) {

            if(result != null){


                if(result.contains("Success")){

                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login Successful!")
                            .setMessage(result)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } else {

                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login Failed!")
                            .setMessage(result)
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }


                // Showing the return from the server
                //resultTextView.setText(result);
                //resultTextView.setVisibility(View.VISIBLE);
            }
        }


    }


    public void onClickUsernameEditText(View view) {
        loginEditText.setText("");
    }

    public void onClickPassEditText(View view) {
        passEditText.setText("");
        passEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
}
