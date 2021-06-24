package com.anilson.chesshealthexam.db.database;

import com.anilson.chesshealthexam.db.daos.PersonDao;

public interface PersistenceDatabase {
    public PersonDao personDao();
}
