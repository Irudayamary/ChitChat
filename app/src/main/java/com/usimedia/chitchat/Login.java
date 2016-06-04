package com.usimedia.chitchat;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.usimedia.chitchat.model.LoginModel;
import com.usimedia.chitchat.model.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText emailText = (EditText) findViewById(R.id.email_id);
        final EditText passwordText = (EditText) findViewById(R.id.password);
        Button button = (Button)findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginModel model = new LoginModel();
                model.setEmail(emailText.getText().toString());
                model.setPassword(passwordText.getText().toString());
                new LoginTask().execute(model);
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoginTask extends AsyncTask<LoginModel, Void, Result> {

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            String message = result.getResponse() ? "Success" : "Failed";
            Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
            if(result.getResponse())
            {
                Intent sendIntent = new Intent(Login.this,Contacts.class);
                startActivity(sendIntent);
            }
        }

        @Override
        protected Result doInBackground(LoginModel... loginModels) {

            LoginModel loginModel = loginModels[0];

            String url="http://192.168.1.24:8000/login";


            RequestBody body = new FormBody.Builder()
                    .add("email", loginModel.getEmail())
                    .add("password", loginModel.getPassword())
                    .build();

            Result result = request(url, body);

            return result;
        }
    }

    private Result request(String url, RequestBody body)
    {
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();


        String jsonData = null;
        try {
            Response response = client.newCall(request).execute();
            jsonData = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Result result = new Result();

        try {
            if(null != jsonData) {
                JSONObject json = new JSONObject(jsonData);
                result.setResponse(json.getBoolean("response"));
                result.setName(json.getString("name"));
            }
            else {
                result.setResponse(false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;
    }



}
