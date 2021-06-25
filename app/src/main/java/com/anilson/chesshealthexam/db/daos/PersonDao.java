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

    @Query("SELECT * FROM person WHERE age BETWEEN :low AND :high ORDER BY name ASC")
    Single<List<Person>> searchPeopleBetweenAges(int low, int high);

    @Query("SELECT * FROM person WHERE "
            + "(name LIKE :name) AND "
            + "(countryCode LIKE :countryCode) AND "
            + "(age BETWEEN :low and :high) "
            + "ORDER BY name ASC")
    Single<List<Person>> filterPeople(String name, String countryCode, int low, int high);

    @Query("SELECT * FROM person WHERE "
            + "(countryCode LIKE :countryCode) AND "
            + "(age BETWEEN :low and :high) "
            + "ORDER BY name ASC")
    Single<List<Person>> filterPeople(String countryCode, int low, int high);

    @Query("SELECT * FROM person WHERE "
            + "(name LIKE :name) AND "
            + "(age BETWEEN :low and :high) "
            + "ORDER BY name ASC")
    Single<List<Person>> filterPeople(int low, int high, String name);
}
