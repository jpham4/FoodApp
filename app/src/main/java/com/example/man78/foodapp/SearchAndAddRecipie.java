package com.example.man78.foodapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SearchAndAddRecipie extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_add_recipie);
        findViewById(R.id.HomeButton).setOnClickListener(this);
        findViewById(R.id.Cancel).setOnClickListener(this);
        findViewById(R.id.RemoveRecipie).setOnClickListener(this);
        findViewById(R.id.AddRecipie).setOnClickListener(this);
    }
    @Override
    public void onClick (View view) {
        switch (view.getId()) {
            case R.id.HomeButton:
                startActivity(new Intent(this, Home.class ));
                break;
            case R.id.Cancel:
                startActivity(new Intent(this, ItemScan.class));
                break;
            case R.id.RemoveRecipie:
                startActivity(new Intent(this, AddRecipie.class));
                break;
            case R.id.AddRecipie:
                startActivity(new Intent(this, ViewAndModifyRecipie.class));
                break;
        }
    }
}
