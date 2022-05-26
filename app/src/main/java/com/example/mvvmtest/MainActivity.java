package com.example.mvvmtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mvvmtest.activities.AudioFocusActivity;
import com.example.mvvmtest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModel updates the Model
        // after observing changes in the View

        // model will also update the view
        // via the ViewModel
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


    }

    void start(Class<?> cls){
        startActivity(new Intent(this, cls));
    }


    public void mvvm(View view) {
        start(MVVMActivity.class);
    }

    public void audio_focus(View view) {
        start(AudioFocusActivity.class);
    }




}