package com.example.weatherproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    private boolean binded=false;
    private WeatherService weatherService;
    private TextView weatherText;
    private Button weather;
    private EditText locationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weather=findViewById(R.id.button_weather);
        weatherText=findViewById(R.id.weather);
        locationText=findViewById(R.id.location);
        weather.setOnClickListener(this);
    }
    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, WeatherService.class);

        this.bindService(intent, weatherServiceConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (binded) {
            this.unbindService(weatherServiceConnection);
            binded = false;
        }
    }
    ServiceConnection weatherServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WeatherService.LocalWeatherBinder binder = (WeatherService.LocalWeatherBinder) service;
            weatherService = binder.getService();
            binded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binded = false;
        }
    };

    @Override
    public void onClick(View view) {

        String location = locationText.getText().toString();

        String weather= this.weatherService.getWeatherToday(location);

        weatherText.setText(weather);

    }
}
