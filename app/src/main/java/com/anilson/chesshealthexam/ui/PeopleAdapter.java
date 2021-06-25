package com.anilson.chesshealthexam.ui;

import com.anilson.chesshealthexam.R;
import com.anilson.chesshealthexam.databinding.ItemPersonBinding;
import com.anilson.chesshealthexam.db.entities.Person;

import org.jetbrains.annotations.NotNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.NumberFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    private final List<Person> dataSet;
    private final Callback callback;

    public PeopleAdapter(List<Person> dataSet, Callback callback) {
        this.dataSet = dataSet;
        this.callback = callback;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemPersonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Person person = dataSet.get(position);
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMinimumFractionDigits(2);
        holder.binding.nameTextView.setText(person.name);
        holder.binding.ageTextView.setText(getString(holder, R.string.age_format, person.age));
        holder.binding.genderTextView.setText(getString(holder, R.string.gender_format, person.gender, percentFormat.format(person.genderProbability)));
        holder.binding.nationalityTextView.setText(getString(holder, R.string.country_code_format, person.countryCode, percentFormat.format(person.countryProbability)));
        holder.binding.getRoot().setOnClickListener(v -> {
            callback.onListItemClick(person);
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemPersonBinding binding;

        public ViewHolder(ItemPersonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public Person getItemAt(int position) {
        return dataSet.get(position);
    }

    private String getString(ViewHolder holder, @StringRes int res, Object... args) {
        return holder.binding.getRoot().getContext().getString(res, args);
    }

    public interface Callback {
        void onListItemClick(Person person);
    }
}
