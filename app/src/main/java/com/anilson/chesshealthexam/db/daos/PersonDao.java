package com.anilson.chesshealthexam.db.daos;

import com.anilson.chesshealthexam.db.entities.Person;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface PersonDao {
    @Insert
    void insertAll(Person... people);

    @Update
    void update(Person... people);

    @Delete
    void delete(Person person);
}