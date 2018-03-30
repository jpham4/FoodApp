package com.example.man78.foodapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ViewAndModifyRecipie extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_and_modify_recipie);
        findViewById(R.id.HomeButton).setOnClickListener(this);
        findViewById(R.id.Cancel).setOnClickListener(this);
        findViewById(R.id.AddRecipie).setOnClickListener(this);
        findViewById(R.id.RemoveRecipie).setOnClickListener(this);
    }
    @Override
    public void onClick (View view){
        switch(view.getId()){
            case R.id.HomeButton:
                startActivity(new Intent(this, Home.class ));
                break;
            case R.id.Cancel:
                startActivity(new Intent(this, Home.class));
                break;
            case R.id.AddRecipie:

                break;
            case R.id.RemoveRecipie:

                break;
        }
    }
}
