package com.anilson.chesshealthexam.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.anilson.chesshealthexam.R;
import com.anilson.chesshealthexam.databinding.FragmentDetailsBinding;
import com.anilson.chesshealthexam.ui.viewmodels.PersonListViewModel;

public class DetailsFragment extends Fragment {

    private PersonListViewModel viewModel;

    private FragmentDetailsBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            viewModel = new ViewModelProvider(getActivity()).get(PersonListViewModel.class);
            viewModel.getSelectedPerson().observe(getViewLifecycleOwner(), person -> {
                //TODO set up UI
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}