package com.anilson.chesshealthexam.repositories;

import com.anilson.chesshealthexam.db.database.PersistenceDatabase;
import com.anilson.chesshealthexam.db.entities.Person;
import com.anilson.chesshealthexam.networking.apis.AgeService;
import com.anilson.chesshealthexam.networking.apis.GenderService;
import com.anilson.chesshealthexam.networking.apis.NationalityService;
import com.anilson.chesshealthexam.networking.models.AgeResponse;
import com.anilson.chesshealthexam.networking.models.GenderResponse;
import com.anilson.chesshealthexam.networking.models.NationalityResponse;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;

@Singleton
public class DataRepository {

    private static final String TAG = DataRepository.class.getSimpleName();

    private final AgeService ageService;
    private final GenderService genderService;
    private final NationalityService nationalityService;
    private final PersistenceDatabase persistenceDatabase;

    @Inject
    public DataRepository(
            AgeService ageService,
            GenderService genderService,
            NationalityService nationalityService,
            PersistenceDatabase persistenceDatabase
    ) {
        this.ageService = ageService;
        this.genderService = genderService;
        this.nationalityService = nationalityService;
        this.persistenceDatabase = persistenceDatabase;
    }

    private MutableLiveData<List<Person>> people = new MutableLiveData<>();

    public LiveData<List<Person>> getPeople() {
        return people;
    }

    private void getAllPeople() {
        persistenceDatabase.personDao().getPeople()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(peopleUpdate -> {
                    people.postValue(peopleUpdate);
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    public void addPerson(String name) {
        Observable.zip(ageService.getAge(name),
                genderService.getGender(name),
                nationalityService.getNationality(name),
                (Response<AgeResponse> ageResponse, Response<GenderResponse> genderResponse, Response<NationalityResponse> nationalityResponse) -> {
                    Person person = new Person(0,
                            name,
                            ageResponse.body().getAge(),
                            nationalityResponse.body().getCountry().get(0).getCountry_id(),
                            nationalityResponse.body().getCountry().get(0).getProbability(),
                            genderResponse.body().getGender(),
                            genderResponse.body().getProbability()
                    ); //TODO error handling
                    persistenceDatabase.personDao().insert(person);
                    return person;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(person -> {
                    Log.d(TAG, "Finished network requests");
                    insertPerson(person);
                }, throwable -> {
                    //TODO
                });
    }

    private void insertPerson(Person person) {
        persistenceDatabase.personDao().insert(person)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    Log.d(TAG, "Inserted " + person.name);
                    getAllPeople();
                }, throwable -> {
                    //TODO
                });
    }

    public void removePerson(Person person) {
        persistenceDatabase.personDao().delete(person)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    Log.d(TAG, "Deleted " + person.name);
                    getAllPeople();
                }, throwable -> {
                    //TODO
                });
    }

    public void getFilteredPeople(String name, String countryCode, Integer low, Integer high) {
        determineFilterFunction(name, countryCode, low, high)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( peopleUpdate -> {
                    people.postValue(peopleUpdate);
                }, throwable -> {
                    //TODO
                });
    }

    private Single<List<Person>> determineFilterFunction(String name, String countryCode, int low, int high) {
        if (name!= null && !name.isEmpty() && countryCode != null && !countryCode.isEmpty()) {
            return persistenceDatabase.personDao().filterPeople(name, countryCode, low, high);
        } else if (name != null && !name.isEmpty()) {
            return persistenceDatabase.personDao().filterPeople(low, high, name);
        } else if (countryCode != null && !countryCode.isEmpty()) {
            return persistenceDatabase.personDao().filterPeople(countryCode, low, high);
        } else {
            return persistenceDatabase.personDao().searchPeopleBetweenAges(low, high);
        }
    }
}
