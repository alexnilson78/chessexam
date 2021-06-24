package com.anilson.chesshealthexam.di;

import com.anilson.chesshealthexam.db.daos.PersonDao;
import com.anilson.chesshealthexam.db.database.PersistenceRoomDatabase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DaoModule {
    @Provides
    public PersonDao providePersonDao(PersistenceRoomDatabase db) {
        return db.personDao();
    }
}

