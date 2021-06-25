package com.anilson.chesshealthexam.ui;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.anilson.chesshealthexam.databinding.DialogAddPersonBinding;
import com.anilson.chesshealthexam.ui.viewmodels.PersonListViewModel;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

public class AddPersonDialog extends BottomSheetDialogFragment {

    private PersonListViewModel viewModel;
    private DialogAddPersonBinding binding;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = DialogAddPersonBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            viewModel = new ViewModelProvider(getActivity()).get(PersonListViewModel.class);
        }

        binding.nameEntryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //NO OP
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.errorIndicator.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //NO OP
            }
        });

        binding.nameEntrySubmitButton.setOnClickListener(v -> {
            String name = binding.nameEntryEditText.getText().toString();
            if (name.isEmpty()) {
                binding.errorIndicator.setVisibility(View.VISIBLE);
            } else {
                viewModel.addPerson(name);
                dismiss();
            }
        });
    }
}
