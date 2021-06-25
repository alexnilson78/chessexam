package com.anilson.chesshealthexam.ui.viewmodels;

import com.anilson.chesshealthexam.R;
import com.anilson.chesshealthexam.db.entities.Person;
import com.anilson.chesshealthexam.repositories.DataRepository;
import com.anilson.chesshealthexam.util.Event;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class PersonListViewModel extends ViewModel {

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

    private final MutableLiveData<Event<Boolean>> reseedDatabase = new MutableLiveData<>();
    private final MutableLiveData<Event<Integer>> errorData = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        compositeDisposable.clear();
        super.onCleared();
    }

    public LiveData<List<Person>> getPeople() {
        if (people == null) {
            people = dataRepository.getPeople();
            loadPeople();
        }
        return people;
    }

    public LiveData<Event<Integer>> getErrorData() {
        return errorData;
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
        compositeDisposable.add(dataRepository.getFilteredPeople(searchTerm.getValue(), countryCode.getValue(), minAge.getValue(), maxAge.getValue())
                .doOnError(throwable -> errorData.postValue(new Event<>(R.string.error_database)))
                .subscribe());
    }

    public void addPerson(String name) {
        compositeDisposable.add(dataRepository.retrievePerson(name)
                .subscribe(this::insertPerson,
                        throwable -> {
                    throwable.printStackTrace();
                    errorData.postValue(new Event<>(R.string.errpr_network));
                        }));
    }

    private void insertPerson(Person person) {
        compositeDisposable.add(dataRepository.insertPerson(person)
                .subscribe(aLong -> loadPeople(),
                        throwable -> errorData.postValue(new Event<>(R.string.error_insert))));
    }

    public void removePerson(Person person) {
        compositeDisposable.add(dataRepository.removePerson(person)
                .subscribe(integer -> loadPeople(),
                        throwable -> errorData.postValue(new Event<>(R.string.error_delete))));
    }

    public void removeEveryone() {
        compositeDisposable.add(dataRepository.deleteAllPeople()
                .subscribe(() -> reseedDatabase.postValue(new Event<>(true))
                        , throwable -> errorData.postValue(new Event<>(R.string.error_reset))));
    }

    public LiveData<Event<Boolean>> getSeedDatabase() {
        return reseedDatabase;
    }

    public void setCountryCodeFilter(String code) {
        if (code != null && !code.isEmpty()) {
            code = "%" + code + "%";
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
            searchName = "%" + searchName + "%";
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
