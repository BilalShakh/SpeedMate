package com.example.speedmate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.widget.Chronometer;
import android.widget.Toast;


import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LineChart mGraph;
    private long lastUpdate = 0;
    private boolean runningChrono=true;
    private long chronoTimeElapsed=0;
    private SharedPreferences mPrefrences;
    private SharedPreferences.Editor mEditor;
    private String graphType="Speed-Time";

    private boolean StartOrStop=true;
    private ArrayList<Float> SpeedList = new ArrayList<>();
    private LocationManager locationManager;
    private ArrayList<ILineDataSet> dataSets=new ArrayList<>();
    private int runCount=0;
    private long startTime=0;
    private ArrayList<Float> timeList = new ArrayList<>();
    private HSSFWorkbook workbook = new HSSFWorkbook();
    private HSSFSheet sheet = workbook.createSheet();
    private int previLength=1;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Home() {
        // Required empty public constructor
    }

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mPrefrences = getContext().getSharedPreferences("SpeedApp.Settings",Context.MODE_PRIVATE);
        graphType = mPrefrences.getString("GraphType","Speed-Time");

        //request for permissions needed
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},12);

        //Event handler for starting the measurement process
        Button startBtn = view.findViewById(R.id.startBtn);
        final Chronometer chronometer = view.findViewById(R.id.Chronometer);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMeasurement(chronometer);
            }
        });

        //Se
        Button resetButton = view.findViewById(R.id.resetBtn);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetApp(chronometer);
            }
        });


        mGraph = view.findViewById(R.id.dataGraph);

        //graph settings
        mGraph.setDragEnabled(true);
        mGraph.setScaleEnabled(false);

        Button saveBtn = view.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSpreadSheet(workbook);
            }
        });


        return view;
    }

    private void startMeasurement(Chronometer chronometer) {

        if(StartOrStop){
            startTime = SystemClock.elapsedRealtime();
            SpeedList.clear();
            timeList.clear();
            startLocationRequests();
            StartChronometer(chronometer);
            runCount++;
        } else{
            StartChronometer(chronometer);
        }

        //Setup Sensor
        StartOrStop=reverseBool(StartOrStop);
    }

    private void StartChronometer(Chronometer chronometer) {

        if(runningChrono) {
            resetChrono(chronometer);
            chronometer.setBase(SystemClock.elapsedRealtime()-chronoTimeElapsed);
            chronometer.start();
            runningChrono = false;
        } else{
            runningChrono = true;
            chronometer.stop();
        }

    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private void resetChrono(Chronometer chronometer){
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronoTimeElapsed = 0;
        chronometer.stop();
    }

    public void resetApp(Chronometer chronometer){
        mGraph.clear();
        mGraph.invalidate();
        SpeedList.clear();
        lastUpdate = 0;
        resetChrono(chronometer);
        runningChrono=true;
        runCount = 0;
        dataSets.clear();
    }

    public boolean reverseBool(Boolean bool){
        if(bool){
            bool=false;
        }else{
            bool=true;
        }
        return bool;
    }

    public void WriteSpeadsheet(int runNum,int prevLength){
        HSSFRow firstRow = sheet.createRow(prevLength-1);
        firstRow.createCell(0).setCellValue(new HSSFRichTextString("Run " + runNum));

        for(int i=0;i<(timeList.size());i++){
            HSSFRow row = sheet.createRow(i+prevLength);
            row.createCell(0).setCellValue(timeList.get(i));
            row.createCell(1).setCellValue(SpeedList.get(i));
        }
    }

    private void saveSpreadSheet(HSSFWorkbook workbook) {
        //Setup file to save data
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"speedData.xls");
        FileOutputStream fos = null;

        //Save the data into the directory stated above.
        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(isExternalStorageWritable()) {
                if (fos != null) {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getContext(), "Excel Sheet Generated", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "not possible to write", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void updateGraphData(){

        ArrayList<Entry> SpeedEntryList = new ArrayList<>();
        Random random = new Random();

        for(int i = 0;i<SpeedList.size();i++){
            SpeedEntryList.add(new Entry(timeList.get(i),SpeedList.get(i)));
        }

        //Setup a unique data set with a random colour associated with it
        LineDataSet lineDataSet = new LineDataSet(SpeedEntryList, "Run "+runCount+" Speed(m/s)");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setColor(Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256)));


        //Creating a data set with all data
        dataSets.add(lineDataSet);


        //Updating graph
        LineData data = new LineData(dataSets);
        mGraph.setData(data);
        mGraph.notifyDataSetChanged();
        mGraph.invalidate();

        WriteSpeadsheet(runCount,previLength);
        previLength += timeList.size()+1;
        //
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        /*long actualTime = SystemClock.elapsedRealtimeNanos();
        long difference = actualTime-lastUpdate;
        long differenceLimit = SpeedInterval*1000000000L;*/
        long occurTime = (SystemClock.elapsedRealtime()-startTime)/1000L;

        if(StartOrStop){
            stopLocationRequests();
            updateGraphData();
        }else {
            SpeedList.add(location.getSpeed());
            timeList.add((float)occurTime);
        }

        /*if (difference>differenceLimit){
            SpeedList.add(location.getSpeed());
            lastUpdate = actualTime;
        }*/
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @SuppressLint("MissingPermission")
    public void startLocationRequests(){
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if(locationManager!=null){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        }
    }

    public void stopLocationRequests() {
        locationManager.removeUpdates(this);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
