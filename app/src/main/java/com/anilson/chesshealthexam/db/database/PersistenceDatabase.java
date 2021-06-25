package com.anilson.chesshealthexam.db.database;

import com.anilson.chesshealthexam.db.daos.PersonDao;

public interface PersistenceDatabase {
    PersonDao personDao();
}
