package com.labplatform.laboratorio008.view.fragments;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.labplatform.laboratorio008.R;
import com.labplatform.laboratorio008.databinding.FragmentDashboardBinding;
import com.labplatform.laboratorio008.databinding.FragmentNotificationsBinding;

import java.text.DecimalFormat;


public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    private SensorManager sensorManager;
    private Sensor sensorAcelerometer;
    private SensorEventListener sensorEventListener;
    private DecimalFormat decimalFormat;

    private LineGraphSeries <DataPoint> accSerie;

    private float index = 0;
    private int max_points = 1000;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensorAcelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        binding.graphA.setVisibility(View.VISIBLE);
        this.accSerie = new LineGraphSeries<DataPoint>(new DataPoint[]{});

        this.accSerie.setColor(Color.RED);

        binding.graphA.addSeries(accSerie);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                NotificationsFragment.this.get_data(event.values[0], event.values[1], event.values[2]);
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

        double accTotal = Math.sqrt( Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(y, 2)  );
        this.accSerie.appendData(new DataPoint(this.index, accTotal), false, this.max_points);
        binding.graphA.getViewport().setMaxX(this.index);
        binding.graphA.getViewport().setMinX(Math.max(this.index - max_points, 0));
        binding.graphA.getViewport().setXAxisBoundsManual(true);



        this.index++;
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