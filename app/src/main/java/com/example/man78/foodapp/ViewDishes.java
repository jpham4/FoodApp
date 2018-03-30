package com.example.man78.foodapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ViewDishes extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dishes);
        findViewById(R.id.UpdayeInfoButton).setOnClickListener(this);
        findViewById(R.id.IngredientButton).setOnClickListener(this);
        findViewById(R.id.RecipiesButton).setOnClickListener(this);
    }

    @Override
    public void onClick (View view){
        switch(view.getId()){
            case R.id.UpdayeInfoButton:
                startActivity(new Intent(this, UpdateInfo.class ));
                break;
            case R.id.IngredientButton:
                startActivity(new Intent(this, ModifyIngredients.class));
                break;
            case R.id.RecipiesButton:
                startActivity(new Intent(this, ViewRecipie.class));
                break;
        }
    }
}
