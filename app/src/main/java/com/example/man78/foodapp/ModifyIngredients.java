package com.example.man78.foodapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static java.sql.DriverManager.getConnection;

public class ModifyIngredients extends AppCompatActivity implements View.OnClickListener{

    private static final String connection_url= "jdbc:mysql://frankencluster.com:3306/mdevteam7";
    private static final String user = "mdevteam7admin";
    private static final String pass = "jwH2ny7b.8s";

    ListView pantryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_ingredients);
        findViewById(R.id.AddRecipie).setOnClickListener(this);
        findViewById(R.id.RemoveRecipie).setOnClickListener(this);
        findViewById(R.id.HomeButton).setOnClickListener(this);
        findViewById(R.id.Cancel).setOnClickListener(this);

        pantryList = findViewById(R.id.pantryListView);

        String queryResult;

        try{
            queryResult = "DB CONN SUCCESS";
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = getConnection(connection_url,user, pass);
            String queryString = "select password from users where email=";
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            final ResultSet result = state.executeQuery(queryString);
            result.last();

            String corr_pass = result.getString("password");


            conn.close();
        }catch (Exception e){
            e.printStackTrace();
            queryResult = e.toString();
        }
        //statusMessage.setText(queryResult);
        //return null;
    }


    @Override
    public void onClick (View view){
        switch(view.getId()){
            case R.id.AddRecipie:
                startActivity(new Intent(this, ItemScan.class ));
                break;
            case R.id.RemoveRecipie:
                break;
            case R.id.HomeButton:
                startActivity(new Intent(this, Home.class ));
                break;
            case R.id.Cancel:
                startActivity(new Intent(this, Home.class ));
                break;
        }
    }
}
