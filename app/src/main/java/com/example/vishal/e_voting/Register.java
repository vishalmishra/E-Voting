package com.example.vishal.e_voting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class Register extends AppCompatActivity {
    Button reg_btn;
    EditText name,aadhaarid,age,gender,phone,password,confirmpass;
   String Name,AadhaarId,Age,Gender,Phone,Password,conpass,Party;
    AlertDialog.Builder builder;
    String party="none";
    String reg_url="http://192.168.43.79/projects/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        reg_btn=(Button)findViewById(R.id.register);
        name=(EditText)findViewById(R.id.name2);
        aadhaarid=(EditText)findViewById(R.id.aadhaarID);
        age=(EditText)findViewById(R.id.age);
        gender=(EditText)findViewById(R.id.gender2);
        phone=(EditText)findViewById(R.id.phone2);
        password=(EditText)findViewById(R.id.Password);
        confirmpass=(EditText)findViewById(R.id.confirmpass);
        builder=new AlertDialog.Builder(Register.this);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name=name.getText().toString();
                AadhaarId=aadhaarid.getText().toString();
                Age=age.getText().toString();
                Gender=gender.getText().toString();
                Phone=phone.getText().toString();
                Password=password.getText().toString();
                Party=party.toString();
                conpass=confirmpass.getText().toString();
                if(Name.equals("")){
                    name.setError("Enter Nane");

                }
               if(AadhaarId.equals("")){
                    aadhaarid.setError("Enter Aadhar ID");
                }
               if(Age.equals("")){
                    age.setError("Enter your age");
                }
                 if(Gender.equals("")){
                   gender.setError("Enter Your Gender");
                }
               if(Phone.equals("")){
                    phone.setError("Enter Phone No.");
                }
               if(Password.equals("")){
                   password.setError("Enter Password");
                }
                 if(conpass.equals("")){
                    confirmpass.setError("Re-enter Password");
                }
                else{
                    if(!Password.equals(conpass)){
                        builder.setTitle("Error");
                        builder.setMessage("Passwords do not matches..");
                        displayAlert("input_error");
                    }
                    else{
                        StringRequest stringRequest=new StringRequest(Request.Method.POST, reg_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray jsonArray=new JSONArray(response);
                                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                                            String code=jsonObject.getString("code");
                                            String message=jsonObject.getString("message");
                                            builder.setTitle("Server Response..");
                                            builder.setMessage(message);
                                            displayAlert(code);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        })
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params= new HashMap<String, String>();
                                params.put("name",Name);
                                params.put("aadhaar_id",AadhaarId);
                                params.put("age",Age);
                                params.put("gender",Gender);
                                params.put("phone",Phone);
                                params.put("password",Password);
                                params.put("party",Party);
                                return params;
                            }
                        };
                        MySingleton.getInstances(Register.this).addToRequestQue(stringRequest);
                    }
                }
            }
        });

    }
    public void displayAlert(final  String code){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(code.equals("input_error"))
                {
                    password.setText("");
                    confirmpass.setText("");
                }
                else if(code.equals("reg_success")){
                 finish();
                }
                else if(code.equals("reg_failed")){
                    name.setText("");
                    aadhaarid.setText("");
                    age.setText("");
                    gender.setText("");
                    phone.setText("");
                    password.setText("");
                    confirmpass.setText("");
                }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}
