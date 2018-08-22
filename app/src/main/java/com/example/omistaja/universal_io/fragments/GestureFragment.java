package com.example.omistaja.universal_io.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.omistaja.universal_io.R;

public class GestureFragment extends Fragment implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private final static String TAG = "GestureFragment";
    Context _context;
    private GestureDetectorCompat gestureDetectorCompat;
    TextView testView;

    public GestureFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_gesture, container, false);


        testView = rootView.findViewById(R.id.testview);
        gestureDetectorCompat = new GestureDetectorCompat(_context, this);
        gestureDetectorCompat.setOnDoubleTapListener(this);

        rootView.setOnTouchListener((view, motionEvent) -> {
            gestureDetectorCompat.onTouchEvent(motionEvent);
            return true;
        });

        return rootView;
    }





    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        testView.setText("onSingleTapConfirmed");
        Log.d(TAG, "Singletap press");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        testView.setText("onDoubleTap" + motionEvent.toString());
        Log.d(TAG, "Doubletap press");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        testView.setText("onDoubleTapEvent" + motionEvent.toString());
        Log.d(TAG, "Doubletap event press");
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        testView.setText("onDown" + motionEvent.toString());
        Log.d(TAG, "onDown press");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        testView.setText("onShowPress" + motionEvent.toString());
        Log.d(TAG, "onShowPress press");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        testView.setText("onSingleTapUp" + motionEvent.toString());
        Log.d(TAG, "onSingleTapUp press");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        testView.setText("onScroll" + motionEvent.toString() + motionEvent1.toString() + v + v1);
        Log.d(TAG, "OnScroll press");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        testView.setText("onLongPress" + motionEvent.toString());
        Log.d(TAG, "OnLong press");
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        testView.setText("onFling" + motionEvent.toString() + motionEvent1.toString() + v + v1);
        Log.d(TAG, "OnFling press");
        return false;
    }
}
