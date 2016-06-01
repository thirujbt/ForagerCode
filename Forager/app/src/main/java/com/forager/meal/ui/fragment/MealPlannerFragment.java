package com.forager.meal.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forager.meal.ui.R;


public class MealPlannerFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_meal_planner, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {

    }

}
