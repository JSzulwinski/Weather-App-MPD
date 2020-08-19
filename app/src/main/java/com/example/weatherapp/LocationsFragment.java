//Jakub Szulwinski S1627131

package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LocationsFragment extends Fragment {

    ListView listView;


    public LocationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations, container, false);

        ArrayList<String> locations = new ArrayList<>(Arrays.asList(
                "Glasgow",
                "London",
                "New York",
                "Oman",
                "Mauritius",
                "Bangladesh"
        ));

        listView = view.findViewById(R.id.location_list_list_view);
        InfiniteScrollArrayAdapter<String> arrayAdapter = new InfiniteScrollArrayAdapter(getContext(), R.layout.location_list_item, R.id.location_list_item_text_view, locations.toArray());
        listView.setAdapter(arrayAdapter);
        listView.setSelectionFromTop(arrayAdapter.mid, 0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), WeatherInfoActivity.class);
                TextView location = view.findViewById(R.id.location_list_item_text_view);
                String locationName = location.getText().toString();
                intent.putExtra("locationName", locationName);
                startActivity(intent);
            }
        });


        return view;
    }
}