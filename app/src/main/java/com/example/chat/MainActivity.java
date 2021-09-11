package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * логин пользователя
     * @param view
     */
    public void button1Click(View view){
        Intent activity1 = new Intent( this, ChatActivity.class);
        startActivity(activity1);
    }
}