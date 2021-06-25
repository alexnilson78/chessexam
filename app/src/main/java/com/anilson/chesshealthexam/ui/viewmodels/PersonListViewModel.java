package com.anilson.chesshealthexam.ui.viewmodels;

import com.anilson.chesshealthexam.db.entities.Person;
import com.anilson.chesshealthexam.repositories.DataRepository;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PersonListViewModel extends ViewModel {

    private static final String TAG = PersonListViewModel.class.getSimpleName();

    DataRepository dataRepository;

    @Inject
    public PersonListViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    private final MutableLiveData<Person> selectedPerson = new MutableLiveData<>();

    private LiveData<List<Person>> people;

    public LiveData<List<Person>> getPeople() {
        if (people == null) {
            people = dataRepository.getPeople();
            loadPeople();
        }
        return people;
    }

    public void selectPerson(Person person) {
        selectedPerson.setValue(person);
    }

    public LiveData<Person> getSelectedPerson() {
        return selectedPerson;
    }

    public void loadPeople() {
        dataRepository.getAllPeople();
    }

    public void addPerson(String name) {
        //TODO error handling
        dataRepository.addPerson(name)
                .subscribe(person -> {
                    Log.d(TAG, "Finished request");
                    loadPeople();
                }, Throwable::printStackTrace);
    }

    public void removePerson(Person person) {
        dataRepository.removePerson(person)
                .subscribe(person1 -> {
                    Log.d(TAG, "Removed " + person1.name);
                    loadPeople();
                }, Throwable::printStackTrace);
    }
}
