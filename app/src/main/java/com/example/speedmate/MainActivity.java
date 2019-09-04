package com.example.speedmate;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.speedmate.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up fragments with the tabs
        ViewPager viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(this,getSupportFragmentManager());
        adapter.addFragment(new Home(),"Home");
        adapter.addFragment(new Settings(),"Settings");
        viewPager.setAdapter(adapter);
    }
}