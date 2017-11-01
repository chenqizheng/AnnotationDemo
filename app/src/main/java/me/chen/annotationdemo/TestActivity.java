package me.chen.annotationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.chen.annotation.TalkingData;

@TalkingData(name = "Test")
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
