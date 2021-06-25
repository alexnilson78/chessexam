package com.anilson.chesshealthexam.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.hilt.android.AndroidEntryPoint;

import com.anilson.chesshealthexam.R;
import com.anilson.chesshealthexam.databinding.FragmentListBinding;
import com.anilson.chesshealthexam.db.entities.Person;
import com.anilson.chesshealthexam.ui.viewmodels.PersonListViewModel;

import java.util.Collections;

@AndroidEntryPoint
public class FirstFragment extends Fragment implements PeopleAdapter.Callback {

    private PersonListViewModel viewModel;
    private FragmentListBinding binding;
    private boolean isListingReversed = false;

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
            NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_addPersonDialog);
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setUpSwipeHandler();
        setUpViewModel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setUpSwipeHandler() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Person person = ((PeopleAdapter) binding.recyclerView.getAdapter()).getItemAt(viewHolder.getAdapterPosition());
                viewModel.removePerson(person);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
    }

    private void setUpViewModel() {
        if (getActivity() != null) {
            viewModel = new ViewModelProvider(getActivity()).get(PersonListViewModel.class);
            viewModel.getIsReversed().observe(getViewLifecycleOwner(), isReversed -> {
                isListingReversed = isReversed;
                //TODO handle as an event and reload list
            });
            viewModel.getPeople().observe(getViewLifecycleOwner(), people -> {
                if(isListingReversed) {
                    Collections.reverse(people);
                }
                binding.recyclerView.swapAdapter(new PeopleAdapter(people, FirstFragment.this), false);
            });
        }
    }

    @Override
    public void onListItemClick(Person person) {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.argument_title), person.name);
        viewModel.selectPerson(person);
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
    }
}