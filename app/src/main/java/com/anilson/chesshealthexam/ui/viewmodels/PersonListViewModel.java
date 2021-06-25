package com.anilson.chesshealthexam.ui.viewmodels;

import com.anilson.chesshealthexam.db.entities.Person;
import com.anilson.chesshealthexam.repositories.DataRepository;

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
    private final MutableLiveData<Boolean> isReversed = new MutableLiveData<>();

    private LiveData<List<Person>> people;

    public LiveData<List<Person>> getPeople() {
        if (people == null) {
            people = dataRepository.getPeople();
            isReversed.postValue(false);
            loadPeople();
        }
        return people;
    }

    public LiveData<Boolean> getIsReversed() {
        return isReversed;
    }

    public void reverseSortOrder() {
        isReversed.postValue(!isReversed.getValue());
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
        dataRepository.addPerson(name);
    }

    public void removePerson(Person person) {
        dataRepository.removePerson(person);
    }

    public void searchForPersonByName(String name) {
        dataRepository.searchForPersonByName(name);
    }

    public void searchForPersonByCountryCode(String countryCode) {
        dataRepository.searchForPersonByCountryCode(countryCode);
    }

    public void searchForPersonInAgeRange(int low, int high) {
        dataRepository.searchForPersonByAgeRange(low, high);
    }
}
