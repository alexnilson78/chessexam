package com.anilson.chesshealthexam.db.daos;

import com.anilson.chesshealthexam.db.entities.Person;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Person... people);

    @Update
    void update(Person... people);

    @Delete
    void delete(Person person);

    @Query("SELECT * FROM person ORDER BY name ASC")
    LiveData<List<Person>> getPeople();
}
