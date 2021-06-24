package com.anilson.chesshealthexam.di;

import com.anilson.chesshealthexam.db.database.PersistenceRoomDatabase;

import android.content.Context;
import android.os.Debug;

import javax.inject.Singleton;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {
    @Provides
    @Singleton
    public PersistenceRoomDatabase provideDatabase( @ApplicationContext Context context) {
        RoomDatabase.Builder<PersistenceRoomDatabase> builder = Room.databaseBuilder(context, PersistenceRoomDatabase.class, "chess_db.db");
        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries();
        }
        return builder.build();
    }
}
