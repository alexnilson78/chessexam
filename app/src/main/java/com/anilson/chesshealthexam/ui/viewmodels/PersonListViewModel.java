package com.anilson.chesshealthexam.ui.viewmodels;

import com.anilson.chesshealthexam.db.entities.Person;
import com.anilson.chesshealthexam.repositories.DataRepository;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PersonListViewModel extends ViewModel {

    DataRepository dataRepository;

    @Inject
    public PersonListViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    private LiveData<List<Person>> people;
    public LiveData<List<Person>> getPeople() {
        if (people == null) {
            loadPeople();
        }
        return people;
    }

    private void loadPeople() {
        people = dataRepository.getListOfPeople();
    }

    public void getPerson(String name) {
        //TODO error handling
        dataRepository.getPerson(name)
            .subscribe(person -> {
                dataRepository.savePerson(person);
            }, Throwable::printStackTrace);
    }
}
