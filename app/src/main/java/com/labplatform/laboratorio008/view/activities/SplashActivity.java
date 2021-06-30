package com.labplatform.laboratorio008.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.labplatform.laboratorio008.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}