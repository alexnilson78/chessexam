package com.anilson.chesshealthexam.ui;

import com.anilson.chesshealthexam.databinding.ItemPersonBinding;
import com.anilson.chesshealthexam.db.entities.Person;

import org.jetbrains.annotations.NotNull;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.text.NumberFormat;
import java.util.List;

import androidx.annotation.NonNull;
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
        ViewHolder viewHolder = new ViewHolder(ItemPersonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        viewHolder.binding.getRoot().setOnClickListener(v -> {
            Person person = dataSet.get(viewHolder.getAdapterPosition());
            callback.onListItemClick(person);
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Person person = dataSet.get(position);
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMinimumFractionDigits(2);
        holder.binding.nameTextView.setText(person.name);
        holder.binding.ageTextView.setText(String.valueOf(person.age));
        holder.binding.genderTextView.setText(person.gender);
        holder.binding.genderProbabilityTextView.setText(percentFormat.format(person.genderProbability));
        holder.binding.nationalityTextView.setText(person.countryCode);
        holder.binding.nationalityProbability.setText(percentFormat.format(person.countryProbability));
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

    public interface Callback {
        void onListItemClick(Person person);
    }
}
