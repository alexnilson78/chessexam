package com.anilson.chesshealthexam.di;

import com.anilson.chesshealthexam.db.database.PersistenceDatabase;
import com.anilson.chesshealthexam.db.database.PersistenceRoomDatabase;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
abstract class DatabaseModuleBinds {
    @Binds
    abstract PersistenceDatabase bindPersistenceDatabase(PersistenceRoomDatabase db);
}
