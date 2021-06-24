package com.anilson.chesshealthexam.repositories;

import com.anilson.chesshealthexam.db.database.PersistenceDatabase;
import com.anilson.chesshealthexam.db.entities.Person;
import com.anilson.chesshealthexam.networking.apis.AgeService;
import com.anilson.chesshealthexam.networking.apis.GenderService;
import com.anilson.chesshealthexam.networking.apis.NationalityService;
import com.anilson.chesshealthexam.networking.models.AgeResponse;
import com.anilson.chesshealthexam.networking.models.GenderResponse;
import com.anilson.chesshealthexam.networking.models.NationalityResponse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;

@Singleton
public class DataRepository {

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

    public LiveData<List<Person>> getListOfPeople() {
        return persistenceDatabase.personDao().getPeople();
    }

    public void savePerson(Person person) {
        persistenceDatabase.personDao().insertAll(person);
    }

    public Observable<Person> getPerson(String name) {
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
                    ); //TODO error handling, db insert
                    persistenceDatabase.personDao().insertAll(person);
                    return person;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
