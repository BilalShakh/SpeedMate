package com.example.speedmate;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CheckBox mSpeedVTimecheckBox;
    private CheckBox mSvEcheckBox;
    private CheckBox mEvTcheckBox;
    private SeekBar mSpeedValSeekBar;
    private TextView mSITextView;
    private SeekBar mElevationValSeekBar;
    private TextView mElTextView;
    private Button msaveBtn;
    private SharedPreferences mPrefrences;
    private SharedPreferences.Editor mEditor;
    private String graphType="Speed-Time";
    private int SpeedInterval=1;
    private int ElevationInterval=10;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
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
        View view= inflater.inflate(R.layout.fragment_settings, container, false);

        mSpeedVTimecheckBox = view.findViewById(R.id.SpeedVTimecheckBox);
        mSpeedVTimecheckBox.setChecked(true);
        mSvEcheckBox = view.findViewById(R.id.SvECheckBox);
        mEvTcheckBox = view.findViewById(R.id.EvTCheckBox);
        mSpeedValSeekBar = view.findViewById(R.id.SpeedValSeekBar);
        mSITextView = view.findViewById(R.id.SITextView);
        mElevationValSeekBar = view.findViewById(R.id.ElevationValSeekBar);
        mElTextView = view.findViewById(R.id.EltextView);
        msaveBtn = view.findViewById(R.id.saveBtn);
        mPrefrences = getContext().getSharedPreferences("SpeedApp.Settings",Context.MODE_PRIVATE);
        mEditor = mPrefrences.edit();

        mSpeedVTimecheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBoxes(mSpeedVTimecheckBox,mSvEcheckBox,mEvTcheckBox);
                graphType = (String) mSpeedVTimecheckBox.getText();
            }
        });

        mSvEcheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBoxes(mSvEcheckBox,mSpeedVTimecheckBox,mEvTcheckBox);
                graphType = (String) mSvEcheckBox.getText();
            }
        });

        mEvTcheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBoxes(mEvTcheckBox,mSpeedVTimecheckBox,mSvEcheckBox);
                graphType = (String) mEvTcheckBox.getText();
            }
        });

        mSpeedValSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mSITextView.setText(i+"s");
                SpeedInterval = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mElevationValSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mElTextView.setText(i+"s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        msaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditor.putString(getString(R.string.GraphTypeKey),graphType);
                mEditor.apply();

                mEditor.putInt("SpeedInterval",SpeedInterval);
                mEditor.apply();

                mEditor.putInt("ElevationInterval",ElevationInterval);
                mEditor.apply();
            }
        });

        return view;
    }

    private void CheckBoxes(CheckBox selectCheckBox,CheckBox checkBox2,CheckBox checkBox3) {
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        selectCheckBox.setChecked(true);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

/*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
