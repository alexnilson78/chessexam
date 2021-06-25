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

    private final MutableLiveData<Person> selectedPerson = new MutableLiveData<>();
    private LiveData<List<Person>> people;
    private boolean isReversed = false;
    private int minAgeFilter = 0;
    private int maxAgeFilter = Integer.MAX_VALUE;
    private String countryCodeFilter = "";
    private String searchName = "";

    DataRepository dataRepository;

    @Inject
    public PersonListViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
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

    public void loadPeople() {
        dataRepository.getFilteredPeople(searchName, countryCodeFilter, minAgeFilter, maxAgeFilter);
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
        countryCodeFilter = code;
    }

    public void setMinAgeFilter(String age) {
        if (age != null && !age.isEmpty()) {
            this.minAgeFilter = Integer.parseInt(age);
        } else {
            this.minAgeFilter = 0;
        }
    }

    public void setMaxAgeFilter(String age) {
        if (age != null && !age.isEmpty()) {
            this.maxAgeFilter = Integer.parseInt(age);
        } else {
            this.maxAgeFilter = Integer.MAX_VALUE;
        }
    }

    public void setSearchName(String searchName) {
        if (searchName != null && !searchName.isEmpty()) {
            searchName = "%"+searchName+"%";
        }
        this.searchName = searchName;
    }
}
