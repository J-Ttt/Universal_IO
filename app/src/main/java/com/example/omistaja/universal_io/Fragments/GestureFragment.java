package com.example.omistaja.universal_io.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.omistaja.universal_io.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GestureFragment extends Fragment implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    Context _context;
    private GestureDetectorCompat gestureDetectorCompat;
    TextView testView;

    public GestureFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_gesture, container, false);

        testView = rootView.findViewById(R.id.testview);
        gestureDetectorCompat = new GestureDetectorCompat(_context, this);
        gestureDetectorCompat.setOnDoubleTapListener(this);

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetectorCompat.onTouchEvent(motionEvent);
                return false;
            }
        });

        return rootView;
    }



    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        testView.setText("onSingleTapConfirmed");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        testView.setText("onDoubleTap" + motionEvent.toString());
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        testView.setText("onDoubleTapEvent" + motionEvent.toString());
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        testView.setText("onDown" + motionEvent.toString());
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        testView.setText("onShowPress" + motionEvent.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        testView.setText("onSingleTapUp" + motionEvent.toString());
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        testView.setText("onScroll" + motionEvent.toString() + motionEvent1.toString() + v + v1);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        testView.setText("onLongPress" + motionEvent.toString());
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        testView.setText("onFling" + motionEvent.toString() + motionEvent1.toString() + v + v1);
        return false;
    }
}
