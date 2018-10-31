package com.example.vishal.e_voting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
// Login function
public class Login extends AppCompatActivity {

    TextView textView;
    Button login_button;
    EditText aadhaarid,password;
    String AadhaarId,Password;
    String log_url="http://192.168.43.79/projects/login.php";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView=(TextView)findViewById(R.id.reg_text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });
        builder=new AlertDialog.Builder(Login.this);
        login_button=(Button)findViewById(R.id.login);
        aadhaarid=(EditText)findViewById(R.id.logAadhaarId);
        password=(EditText)findViewById(R.id.Logpass);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AadhaarId=aadhaarid.getText().toString();
                Password=password.getText().toString();
                if(AadhaarId.equals("")){
                   aadhaarid.setError("Please enter your Aadhaar ID");
                }
               else if(Password.equals("")){
                    password.setError("Please enter the password");
                }
                else{
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, log_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray=new JSONArray(response);
                                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                                        String code=jsonObject.getString("code");
                                        if(code.equals("login_failed")){
                                            builder.setTitle("Login Error..");
                                            displayAlert(jsonObject.getString("message"));
                                        }
                                        else {
                                            Intent intent=new Intent(Login.this,Login_success.class);
                                            Bundle bundle=new Bundle();
                                            bundle.putString("name",jsonObject.getString("name"));
                                            bundle.putString("aadhaar_id",jsonObject.getString("aadhaar_id"));
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Login.this,"Error",Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params= new HashMap<String, String>();
                            params.put("aadhaar_id",AadhaarId);
                            params.put("password",Password);
                            return params;
                        }
                    };
                    MySingleton.getInstances(Login.this).addToRequestQue(stringRequest);

                }
            }
        });

    }
    public void displayAlert(String message){
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                aadhaarid.setText("");
                password.setText("");
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
      Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}

