package com.anilson.chesshealthexam.db.daos;

import com.anilson.chesshealthexam.db.entities.Person;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Person... people);

    @Update
    void update(Person... people);

    @Delete
    void delete(Person person);

    @Query("SELECT * FROM person ORDER BY name ASC")
    Single<List<Person>> getPeople();

    @Query("SELECT * FROM person WHERE name LIKE :name")
    Single<List<Person>> searchPeople(String name);
}
