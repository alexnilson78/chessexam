package com.anilson.chesshealthexam.ui;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.anilson.chesshealthexam.databinding.DialogAddPersonBinding;
import com.anilson.chesshealthexam.viewmodels.PersonListViewModel;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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

        setTextWatcherForClearingError();
        setImeActionHandler();
        focusTextEntryAndShowKeyboard();
        setSubmitClickListener();
    }

    private void setSubmitClickListener() {
        binding.nameEntrySubmitButton.setOnClickListener(v -> {
            String name = binding.nameEntryEditText.getText().toString();
            if (name.isEmpty()) {
                binding.errorIndicator.setVisibility(View.VISIBLE);
            } else {
                viewModel.addPerson(name);
                closeKeyboard();
                dismiss();
            }
        });
    }

    private void setImeActionHandler() {
        binding.nameEntryEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.nameEntrySubmitButton.performClick();
                return true;
            }
            return false;
        });
    }

    private void setTextWatcherForClearingError() {
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
    }

    private void focusTextEntryAndShowKeyboard() {
        if (getContext() != null) {
            InputMethodManager inputMethodManager = ContextCompat.getSystemService(getContext(), InputMethodManager.class);
            if (inputMethodManager!= null) {
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                binding.nameEntryEditText.requestFocus();
            }
        }
    }

    private void closeKeyboard() {
        if (getContext() != null) {
            InputMethodManager inputMethodManager = ContextCompat.getSystemService(getContext(), InputMethodManager.class);
            if (inputMethodManager != null) {
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        }
    }
}
