package com.example.amado.shoutout;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity {

    private EditText mFirstName, mEmail, mLocation, mPwd, mRePwd;
    private RadioGroup genderGroup;
    private RadioButton genderButton;
    private Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirstName = (EditText) findViewById(R.id.reg_name);
        mEmail = (EditText) findViewById(R.id.reg_email);
        mLocation = (EditText) findViewById(R.id.reg_location);
        mPwd = (EditText) findViewById(R.id.reg_pwd);
        mRePwd = (EditText) findViewById(R.id.reg_re_pwd);

        genderGroup = (RadioGroup) findViewById(R.id.gender);

        regButton = (Button) findViewById(R.id.reg_btn);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nm = mFirstName.getText().toString();
                final String mail = mEmail.getText().toString();
                final String loc = mLocation.getText().toString();

                int selectedId = genderGroup.getCheckedRadioButtonId();
                genderButton = (RadioButton) findViewById(selectedId);
                final String sex = genderButton.getText().toString();


                final String pwd = mPwd.getText().toString();
                final String repwd = mRePwd.getText().toString();


                Response.Listener<String> stringListener = new Response.Listener<String>() {
                    /**
                     * Called when a response is received.
                     *
                     * @param response
                     */
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jResponse = new JSONObject(response);
                            Boolean success = jResponse.getBoolean("success");

                            if(success) {
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                SignUpActivity.this.startActivity(intent);
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setMessage("Register failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                RegisterHandler registerHandler = new RegisterHandler(nm, mail, loc, sex, pwd, stringListener);
                RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
                requestQueue.add(registerHandler);

            }
        });

    }


    private class RegisterHandler extends StringRequest {

        private static final String REGISTER_REQUEST_URL = "https://amadon2g.000webhostapp.com/register.php";
        private Map<String, String> params;

        public RegisterHandler(String name, String email, String location, String gender, String password,
                               Response.Listener<String> listener) {
            super(Method.POST, REGISTER_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("name", name);
            params.put("email", email);
            params.put("location", location);
            params.put("gender", gender);
            params.put("password", password);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
}
