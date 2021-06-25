package com.anilson.chesshealthexam.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anilson.chesshealthexam.R;
import com.anilson.chesshealthexam.databinding.FragmentDetailsBinding;
import com.anilson.chesshealthexam.viewmodels.PersonListViewModel;
import com.anilson.chesshealthexam.util.Constants;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;

public class DetailsFragment extends Fragment {

    private static final String FLAG_API = "https://www.countryflags.io/";
    private static final String FLAG_ENDPOINT = "/flat/64.png";

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
                NumberFormat percentFormat = NumberFormat.getPercentInstance();
                percentFormat.setMinimumFractionDigits(2);
                binding.ageTextView.setText(String.valueOf(person.age));
                if (Constants.UNKNOWN_COUNTRY.equals(person.countryCode)) {
                    binding.countryCodeTextview.setText(R.string.detail_unknown);
                } else {
                    binding.countryCodeTextview.setText(getString(R.string.detail_format, person.countryCode, percentFormat.format(person.countryProbability)));
                }
                binding.genderTextView.setText(getString(R.string.detail_format, person.gender, percentFormat.format(person.genderProbability)));
                String flagUrl = FLAG_API + person.countryCode + FLAG_ENDPOINT;
                Glide.with(this).load(flagUrl).into(binding.flagImageView);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}