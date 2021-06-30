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

import java.text.DecimalFormat;


public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private SensorManager sensorManager;
    private Sensor sensorAcelerometer;
    private SensorEventListener sensorEventListener;
    private DecimalFormat decimalFormat;

    private LineGraphSeries <DataPoint> x_serie;
    private LineGraphSeries <DataPoint> y_serie;
    private LineGraphSeries <DataPoint> z_serie;

    private float index = 0;
    private int max_points = 1000;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensorAcelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        binding.graphXYZ.setVisibility(View.VISIBLE);
        this.x_serie = new LineGraphSeries<DataPoint>(new DataPoint[]{});
        this.y_serie = new LineGraphSeries<DataPoint>(new DataPoint[]{});
        this.z_serie = new LineGraphSeries<DataPoint>(new DataPoint[]{});

        this.x_serie.setColor(Color.RED);
        this.y_serie.setColor(Color.GREEN);
        this.z_serie.setColor(Color.BLUE);

        binding.graphXYZ.addSeries(x_serie);
        binding.graphXYZ.addSeries(y_serie);
        binding.graphXYZ.addSeries(z_serie);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                DashboardFragment.this.get_data(event.values[0], event.values[1], event.values[2]);
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
        this.x_serie.appendData(new DataPoint(this.index, x), false, this.max_points);
        this.y_serie.appendData(new DataPoint(this.index, y), false, this.max_points);
        this.z_serie.appendData(new DataPoint(this.index, z), false, this.max_points);
        binding.graphXYZ.getViewport().setMaxX(this.index);
        binding.graphXYZ.getViewport().setMinX(Math.max(this.index - max_points, 0));
        binding.graphXYZ.getViewport().setXAxisBoundsManual(true);



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