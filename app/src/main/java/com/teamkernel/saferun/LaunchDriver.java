package com.teamkernel.saferun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.core.Context;
import com.firebase.client.snapshot.StringNode;

import java.util.HashMap;
class Run {
    //attributes must match the ones stored in firebase
    public String name;
    public String runKey;
}
public class LaunchDriver extends AppCompatActivity {
    Firebase rootFirebase;
    Firebase runsFirebase;
    Firebase activeRunsFirebase;
    String name;
    HashMap<String,String> activeRunsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_driver);

        //clear any run key
        MyUtils.removeFromSharedPrefs("runKey",this);

        //root Firebase
        rootFirebase = new Firebase("https://saferun.firebaseio.com");
        runsFirebase = rootFirebase.child("Runs");
        activeRunsFirebase = rootFirebase.child("ActiveRuns");

        name = MyUtils.getValueFromSharedPrefs("name", this);

        final RadioGroup runs_available = (RadioGroup) findViewById(R.id.choices);
        final LinearLayout.LayoutParams radiogroupparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        if (runs_available != null) {
            runs_available.setOnCheckedChangeListener(new OnCheckedChangeListener()
            {
                public void onCheckedChanged(RadioGroup rGroup, int checkedId)
                {
                    // This will get the radiobutton that has changed in its check state
                    RadioButton checkedRadioButton = (RadioButton)rGroup.findViewById(checkedId);
                    // This puts the value (true/false) into the variable
                    boolean isChecked = checkedRadioButton.isChecked();
                    // If the radiobutton that has changed in check state is now checked...
                    if (isChecked)
                    {
                        // Changes the textview's text to "Checked: example radiobutton text"

                        String name = checkedRadioButton.getText().toString();
                        Log.d("ss", "Checked:" + name);

                        String runKey = activeRunsMap.get(name);
                        Log.d("ss", "Checked key:" + runKey);

                        MyUtils.putInSharedPrefs("runKey",runKey,LaunchDriver.this);

                    }
                }
            });
        }

        //for each new run added
        activeRunsFirebase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("ss", "snap: " + snapshot.getValue());

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Run myRun = postSnapshot.getValue(Run.class);

                    if(!myRun.name.trim().isEmpty()){

                        RadioButton rb = new RadioButton(LaunchDriver.this);
                        rb.setText(myRun.name);
                        runs_available.addView(rb, 0, radiogroupparams);
                        activeRunsMap.put(myRun.name, myRun.runKey);

                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError error) {

            }

        });

    }
    public void driverJoinRun(View view){

        //create driver
        boolean result = MyUtils.createNewDriver(new Driver(name),this);

        if(result){
            //start next activity
            Intent intent = new Intent(this, DriverMapView.class);
            startActivity(intent);
        }
        else {
            //toast user to choose a run first
            Toast.makeText(this, "Please choose a run first", Toast.LENGTH_SHORT).show();
        }

    }

    public void driverGoBackToMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

