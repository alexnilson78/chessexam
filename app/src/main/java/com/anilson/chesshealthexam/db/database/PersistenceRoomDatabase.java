package com.anilson.chesshealthexam.db.database;

import com.anilson.chesshealthexam.db.daos.PersonDao;
import com.anilson.chesshealthexam.db.entities.Person;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                Person.class
        }, version = 1
)
abstract public class PersistenceRoomDatabase extends RoomDatabase implements PersistenceDatabase {

}
