package com.anilson.chesshealthexam.db.daos;

import com.anilson.chesshealthexam.db.entities.Person;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(Person person);

    @Delete
    Single<Integer> delete(Person person);

    @Query("SELECT * FROM person ORDER BY name ASC")
    Single<List<Person>> getPeople();

    @Query("SELECT * FROM person WHERE name LIKE :name")
    Single<List<Person>> searchPeopleByName(String name);

    @Query("SELECT * FROM person WHERE countryCode LIKE :countryCode")
    Single<List<Person>> searchPeopleByCountryCode(String countryCode);

    @Query("SELECT * FROM person WHERE age BETWEEN :low AND :high")
    Single<List<Person>> searchPeopleBetweenAges(int low, int high);
}
