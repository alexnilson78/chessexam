package com.anilson.chesshealthexam.repositories;

import com.anilson.chesshealthexam.db.database.PersistenceDatabase;
import com.anilson.chesshealthexam.db.entities.Person;
import com.anilson.chesshealthexam.networking.apis.AgeService;
import com.anilson.chesshealthexam.networking.apis.GenderService;
import com.anilson.chesshealthexam.networking.apis.NationalityService;
import com.anilson.chesshealthexam.networking.models.AgeResponse;
import com.anilson.chesshealthexam.networking.models.GenderResponse;
import com.anilson.chesshealthexam.networking.models.NationalityResponse;
import com.anilson.chesshealthexam.ui.viewmodels.PersonListViewModel;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
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

    public void getAllPeople() {
        persistenceDatabase.personDao().getPeople()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(peopleUpdate -> {
                    people.postValue(peopleUpdate);
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    public Observable<Person> addPerson(String name) {
        return Observable.zip(ageService.getAge(name),
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
                    persistenceDatabase.personDao().insertAll(person);
                    return person;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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

    public void searchForPerson(String name) {
        persistenceDatabase.personDao().searchPeople("%"+name+"%")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(peopleUpdate -> {
                    people.postValue(peopleUpdate);
                }, throwable -> {
                    //TODO
                });
    }
}
