package com.anilson.chesshealthexam.ui.viewmodels;

import com.anilson.chesshealthexam.db.entities.Person;
import com.anilson.chesshealthexam.repositories.DataRepository;

import android.util.Log;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PersonListViewModel extends ViewModel {

    DataRepository dataRepository;

    @Inject
    public PersonListViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    private MutableLiveData<List<Person>> people;
    public LiveData<List<Person>> getPeople() {
        if (people == null) {
            people = new MutableLiveData<>();
            loadPeople();
        }
        return people;
    }

    private void loadPeople() {
        //TODO this is just a proof of concept for requesting a new person
        dataRepository.getPerson("Alex")
            .subscribe(person -> {
                people.postValue(Arrays.asList(person));
                Log.d("Test", "Sub result");
            }, Throwable::printStackTrace);
    }
}
