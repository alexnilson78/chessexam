package com.anilson.chesshealthexam.ui.viewmodels;

import com.anilson.chesshealthexam.db.entities.Person;
import com.anilson.chesshealthexam.repositories.DataRepository;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PersonListViewModel extends ViewModel {

    private static final String TAG = PersonListViewModel.class.getSimpleName();

    private final MutableLiveData<Person> selectedPerson = new MutableLiveData<>();
    private final MutableLiveData<String> searchTerm = new MutableLiveData<>("");
    private final MutableLiveData<Integer> minAge = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> maxAge = new MutableLiveData<>(Integer.MAX_VALUE);
    private final MutableLiveData<String> countryCode = new MutableLiveData<>("");
    private LiveData<List<Person>> people;
    private boolean isReversed = false;
    private final Observer<Integer> minAgeObserver;
    private final Observer<String> searchObserver;
    private final Observer<Integer> maxAgeObserver;
    private final Observer<String> countryCodeObserver;

    DataRepository dataRepository;

    @Inject
    public PersonListViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        minAgeObserver = setUpMinAgeObserver();
        maxAgeObserver = setUpMaxAgeObserver();
        searchObserver = setUpSearchObserver();
        countryCodeObserver = setUpCountryCodeObserver();
        minAge.observeForever(minAgeObserver);
        maxAge.observeForever(maxAgeObserver);
        searchTerm.observeForever(searchObserver);
        countryCode.observeForever(countryCodeObserver);
    }

    @Override
    protected void onCleared() {
        minAge.removeObserver(minAgeObserver);
        maxAge.removeObserver(maxAgeObserver);
        searchTerm.removeObserver(searchObserver);
        countryCode.removeObserver(countryCodeObserver);
        super.onCleared();
    }

    public LiveData<List<Person>> getPeople() {
        if (people == null) {
            people = dataRepository.getPeople();
            loadPeople();
        }
        return people;
    }

    public boolean getIsReversed() {
        return isReversed;
    }

    public void reverseSortOrder() {
        isReversed = !isReversed;
    }

    public void selectPerson(Person person) {
        selectedPerson.setValue(person);
    }

    public LiveData<Person> getSelectedPerson() {
        return selectedPerson;
    }

    public LiveData<String> getSearchTerm() {
        return searchTerm;
    }

    public void loadPeople() {
        dataRepository.getFilteredPeople(searchTerm.getValue(), countryCode.getValue(), minAge.getValue(), maxAge.getValue());
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

    public void setCountryCodeFilter(String code) {
        if (code != null && !code.isEmpty()) {
            code = "%"+code+"%";
        }
        countryCode.postValue(code);
    }

    public void setMinAgeFilter(String age) {
        int value = 0;
        if (age != null && !age.isEmpty()) {
            value = Integer.parseInt(age);
        }
        minAge.postValue(value);
    }

    public void setMaxAgeFilter(String age) {
        int value = Integer.MAX_VALUE;
        if (age != null && !age.isEmpty()) {
            value = Integer.parseInt(age);
        }
        maxAge.postValue(value);
    }

    public void setSearchName(String searchName) {
        if (searchName != null && !searchName.isEmpty()) {
            searchName = "%"+searchName+"%";
        }
        searchTerm.postValue(searchName);
    }

    private Observer<Integer> setUpMinAgeObserver() {
        return integer -> loadPeople();
    }

    private Observer<String> setUpSearchObserver() {
        return string -> loadPeople();
    }

    private Observer<Integer> setUpMaxAgeObserver() {
        return integer -> loadPeople();
    }

    private Observer<String> setUpCountryCodeObserver() {
        return string -> loadPeople();
    }
}
