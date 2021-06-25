package com.anilson.chesshealthexam.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import dagger.hilt.android.AndroidEntryPoint;

import com.anilson.chesshealthexam.R;
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
            NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_addPersonDialog);
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getActivity() != null) {
            viewModel = new ViewModelProvider(getActivity()).get(PersonListViewModel.class);
            viewModel.getPeople().observe(getViewLifecycleOwner(), people -> {
                binding.recyclerView.swapAdapter(new PeopleAdapter(people), false);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}