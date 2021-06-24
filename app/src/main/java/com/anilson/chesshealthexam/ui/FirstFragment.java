package com.anilson.chesshealthexam.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;

import com.anilson.chesshealthexam.databinding.FragmentListBinding;
import com.anilson.chesshealthexam.ui.viewmodels.PersonListViewModel;

@AndroidEntryPoint
public class FirstFragment extends Fragment {

    private PersonListViewModel viewModel;
    private FragmentListBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addButton.setOnClickListener(v -> {
            //TODO wire up adding new people
        });

        if (getActivity() != null) {
            viewModel = new ViewModelProvider(getActivity()).get(PersonListViewModel.class);
            viewModel.getPeople().observe(getViewLifecycleOwner(), people -> {
                //TODO
                Log.d("Test", "Loaded people");
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}