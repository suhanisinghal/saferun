package com.teamkernel.saferun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FacilitatorExitRunConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilitator_exit_run_confirmation);
    }

    public void goBackFromExit(View view) {
        Intent intent = new Intent(this, OrganizeNewRun.class);
        //intent.putExtra(EXTRA_USERNAME, sUsername);
        startActivity(intent);
    }
    public void endRunFromExit(View view) {
        //dummy method to end the app
    }

}

