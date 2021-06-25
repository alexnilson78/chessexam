package com.anilson.chesshealthexam.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.anilson.chesshealthexam.util.PreferencesUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import javax.inject.Inject;

@AndroidEntryPoint
public class ListFragment extends Fragment implements PeopleAdapter.Callback {

    private PersonListViewModel viewModel;
    private FragmentListBinding binding;

    @Inject
    PreferencesUtil preferencesUtil;

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
            NavHostFragment.findNavController(ListFragment.this).navigate(R.id.action_ListFragment_to_addPersonDialog);
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setUpViewModel();
        setUpSwipeHandler();
        setUpFilterButton();
        setUpSortIndicator();
        setFilterTextWatchers();
        seedDatabase();
        addScollListener();
        setUpSeedObserver();
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
                binding.recyclerView.swapAdapter(new PeopleAdapter(people, ListFragment.this), false);
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
            binding.filterBackground.setVisibility(View.GONE);
        } else {
            binding.countryCodeFilter.setVisibility(View.VISIBLE);
            binding.filterMinAge.setVisibility(View.VISIBLE);
            binding.filterMaxAge.setVisibility(View.VISIBLE);
            binding.filterBackground.setVisibility(View.VISIBLE);
        }
        binding.countryCodeFilter.setText("");
        binding.filterMinAge.setText("");
        binding.filterMaxAge.setText("");
    }

    private void setFilterTextWatchers() {
        binding.countryCodeFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //NO OP
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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

    private void seedDatabase() {
        if (preferencesUtil.isFirstLaunch()) {
            String[] names = getResources().getStringArray(R.array.seed_names);
            for(int i = 0; i < names.length; i++) {
                viewModel.addPerson(names[i]);
            }
            preferencesUtil.setFirstLaunch(false);
        }
    }

    private void setUpSeedObserver() {
        viewModel.getSeedDatabase().observe(getViewLifecycleOwner(), booleanEvent -> {
            if(booleanEvent.getContentIfNotHandledOrReturnNull()) {
                preferencesUtil.setFirstLaunch(true);
                seedDatabase();
            }
        });
    }

    private void addScollListener() {
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && binding.addButton.getVisibility() == View.VISIBLE) {
                    binding.addButton.hide();
                } else if (dy < 0 && binding.addButton.getVisibility() != View.VISIBLE) {
                    binding.addButton.show();
                }
            }
        });
    }

    @Override
    public void onListItemClick(Person person) {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.argument_title), person.name);
        viewModel.selectPerson(person);
        NavHostFragment.findNavController(ListFragment.this).navigate(R.id.action_ListFragment_to_DetailsFragment, bundle);
    }
}