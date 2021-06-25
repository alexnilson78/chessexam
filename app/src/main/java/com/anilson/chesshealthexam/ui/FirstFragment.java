package com.anilson.chesshealthexam.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

        setUpViewModel();
        setUpSwipeHandler();
        setUpFilterButton();
        setUpSortIndicator();
        setFilterTextWatchers();
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
            viewModel.getPeople().observe(getViewLifecycleOwner(), people -> {
                if(viewModel.getIsReversed()) {
                    Collections.reverse(people);
                }
                binding.recyclerView.swapAdapter(new PeopleAdapter(people, FirstFragment.this), false);
            });
        }
    }

    private void setUpFilterButton() {
        binding.filterButton.setOnClickListener(v -> {
            toggleFilterVisibility();
        });
    }

    private void toggleFilterVisibility() {
        if (binding.countryCodeFilter.getVisibility() == View.VISIBLE) {
            binding.countryCodeFilter.setVisibility(View.GONE);
            binding.filterMinAge.setVisibility(View.GONE);
            binding.filterMaxAge.setVisibility(View.GONE);
        } else {
            binding.countryCodeFilter.setVisibility(View.VISIBLE);
            binding.filterMinAge.setVisibility(View.VISIBLE);
            binding.filterMaxAge.setVisibility(View.VISIBLE);
        }
        binding.countryCodeFilter.setText("");
        binding.filterMinAge.setText("");
        binding.filterMaxAge.setText("");
        //TODO clear filtering
    }

    private void setFilterTextWatchers() {
        binding.countryCodeFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //NO OP
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO
                Log.d("Alex", "county code: " + s);
                viewModel.setCountryCodeFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //NO OP
            }
        });

        binding.filterMinAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //NO OP
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO
                Log.d("Alex", "min age: " + s);
                viewModel.setMinAgeFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //NO OP
            }
        });

        binding.filterMaxAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //NO OP
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO
                Log.d("Alex", "max age: " + s);
                viewModel.setMaxAgeFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //NO OP
            }
        });
    }

    private void setUpSortIndicator() {
        binding.sortIndicator.setOnClickListener(v -> {
            viewModel.reverseSortOrder();
            viewModel.loadPeople();
            setSortIndicatorRotation();
        });
        setSortIndicatorRotation();
    }

    private void  setSortIndicatorRotation() {
        float rotation = 0;
        if (viewModel.getIsReversed()) {
            rotation = 180;
        }
        binding.sortIndicator.setRotation(rotation);
    }

    @Override
    public void onListItemClick(Person person) {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.argument_title), person.name);
        viewModel.selectPerson(person);
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
    }
}