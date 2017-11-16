package com.example.vishal.e_voting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login_success extends AppCompatActivity {
TextView name,email;
    Toolbar toolbar;
    FrameLayout root;
    View contentHamburger;
    RadioGroup party;
    Button vote;
    View guillotineMenu;
    RadioButton radioButton;
    AlertDialog.Builder builder;
    String reg_url="http://192.168.43.79/projects/party.php";
    String AadhaarID;
    String select;
    LinearLayout profile,feed,myevents,logout;
    private static final long RIPPLE_DURATION = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

      // name=(TextView)findViewById(R.id.textView4);
        //email=(TextView)findViewById(R.id.email2);
        Bundle bundle=getIntent().getExtras();
       // name.setText("Welcome "+bundle.getString("name"));
       //name.setText(""+bundle.getString("aadhaar_id"));
        AadhaarID=bundle.getString("aadhaar_id");
      // AadhaarID=name.getText().toString();
        root=(FrameLayout)findViewById(R.id.root);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        contentHamburger=findViewById(R.id.content_hamburger);

        profile= (LinearLayout) findViewById(R.id.profile_group);
        feed= (LinearLayout) findViewById(R.id.feed_group);
        myevents= (LinearLayout) findViewById(R.id.activity_group);
        logout= (LinearLayout) findViewById(R.id.settings_group);

        guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);

        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setActionBarViewForAnimation(toolbar)
                .setStartDelay(RIPPLE_DURATION)
                .setClosedOnStart(true).setGuillotineListener(new GuillotineListener() {
            @Override
            public void onGuillotineOpened() {
            }

            @Override
            public void onGuillotineClosed() {

            }
        })
                .build();

addListenerOnButton();
    }

    public void addListenerOnButton() {

        party = (RadioGroup) findViewById(R.id.radioParty);
        vote = (Button) findViewById(R.id.vote);

        vote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = party.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButton = (RadioButton) findViewById(selectedId);
                select=radioButton.getText().toString();

                StringRequest stringRequest=new StringRequest(Request.Method.POST, reg_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray=new JSONArray(response);
                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                    String code=jsonObject.getString("code");
                                    Intent intent=new Intent(Login_success.this,Vote.class);
                                    startActivity(intent);
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
                        params.put("party",select);
                        params.put("aadhaar_id",AadhaarID);

                        return params;
                    }
                };
                MySingleton.getInstances(Login_success.this).addToRequestQue(stringRequest);
            }

        });

    }
    public void displayAlert(String message){
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(Login_success.this,"Error..",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Login_success.this,Login.class);
        startActivity(intent);
       /* Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);*/
    }

}
