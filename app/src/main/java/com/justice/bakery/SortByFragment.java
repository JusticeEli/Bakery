package com.justice.bakery;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;


public class SortByFragment extends BottomSheetDialogFragment {


    private TextInputLayout fromInput;
    private TextInputLayout toInput;
    private Button searchBtn;

    private SetupInterface setupInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sort_by, container, false);

        fromInput = view.findViewById(R.id.fromInput);
        toInput = view.findViewById(R.id.toInput);
        searchBtn = view.findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBtnTapped();
            }
        });

        return view;
    }

    private void searchBtnTapped() {
        if (fromInput.getEditText().length() == 0 || toInput.getEditText().length() == 0) {
            Toast.makeText(getActivity(), "Fill All Fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int from = Integer.parseInt(fromInput.getEditText().getText().toString());
        int to = Integer.parseInt(fromInput.getEditText().getText().toString());
        setupInterface.searchFor(from, to);


    }

    public interface SetupInterface {
        void searchFor(int from, int to);
    }

    public void sortByInterface(SetupInterface setupInterface){
        this.setupInterface=setupInterface;
    }

}
