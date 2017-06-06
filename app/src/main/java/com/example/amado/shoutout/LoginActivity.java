package com.example.amado.shoutout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText mPasswordView, mUserEmail;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUserEmail = (EditText) findViewById(R.id.userName);
        mPasswordView = (EditText) findViewById(R.id.userPassword);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        LoginActivity.this.startActivity(intent);

        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {


//                String uEmail = mUserEmail.getText().toString();
  //              String pwd = mPasswordView.getText().toString();
//
  //              final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
    //                    R.style.Theme_AppCompat_DayNight_Dialog);
      //          progressDialog.setIndeterminate(true);
        //        progressDialog.setMessage("Authenticating...");
          //      progressDialog.show();

//                Response.Listener<String> stringListener = new Response.Listener<String>() {
                    /**
                     * Called when a response is received.
                     *
                     * @param response
                     */
  //                  @Override
    //                public void onResponse(String response) {
      //                  Log.i("JSON Parser", "Error parsing data " + response);

        //                try {
          //                  JSONObject jResponse = new JSONObject(response);
            //                Boolean success = jResponse.getBoolean("success");
//
  //                          if(success) {

//                                String name = jResponse.getString("name");
  //                              String email = jResponse.getString("email");

//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
  //                              intent.putExtra("name", name);
    //                            intent.putExtra("email", email);


//                                LoginActivity.this.startActivity(intent);
  //                          }else {
    //                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
      //                          builder.setMessage("Register failed")
        //                                .setNegativeButton("Retry", null)
          //                              .create()
            //                            .show();
              //              }

                //        } catch (JSONException e) {
                  //          e.printStackTrace();
                    //    }
//
  //                  }
    //            };

      //          LoginHandler loginHandler = new LoginHandler(uEmail, pwd, stringListener);
        //        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
          //      requestQueue.add(loginHandler);

           }
        });


        TextView regLink = (TextView) findViewById(R.id.signUpLink);
        regLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    public boolean validate() {
        boolean valid = true;

        String uName = mUserEmail.getText().toString();
        String pwd = mPasswordView.getText().toString();

        if (uName.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(uName).matches()) {
            mUserEmail.setError("enter a valid email address");
            valid = false;
        } else {
            mUserEmail.setError(null);
        }

        if (pwd.isEmpty() || pwd.length() < 8 || pwd.length() > 15) {
            mPasswordView.setError("between 8 and 15 alphanumeric characters");
            valid = false;
        } else {
            mPasswordView.setError(null);
        }

        return valid;
    }




    private class LoginHandler extends StringRequest {

        private static final String LOGIN_REQUEST_URL = "https://amadon2g.000webhostapp.com/login.php";
        private Map<String, String> params;

        public LoginHandler(String email, String password,
                               Response.Listener<String> listener) {
            super(Method.POST, LOGIN_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("email", email);
            params.put("password", password);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
}

