package com.labplatform.laboratorio008.view.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.labplatform.laboratorio008.databinding.FragmentHomeBinding;

import java.text.DecimalFormat;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private SensorManager sensorManager;
    private Sensor sensorAcelerometer;
    private SensorEventListener sensorEventListener;
    private DecimalFormat decimalFormat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensorAcelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                HomeFragment.this.get_data(event.values[0], event.values[1], event.values[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(
                sensorEventListener,
                sensorAcelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    protected void get_data(float x, float y, float z){
        decimalFormat = new DecimalFormat("0.########");
        binding.sensorX.setText(decimalFormat.format(x));
        binding.sensorY.setText(decimalFormat.format(y));
        binding.sensorZ.setText(decimalFormat.format(z));
        double accTotal = Math.sqrt( Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(y, 2)  );

        binding.accelerationTotal.setText(decimalFormat.format(accTotal));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sensorManager.unregisterListener(
                sensorEventListener,
                sensorAcelerometer
        );
        binding = null;
    }
}