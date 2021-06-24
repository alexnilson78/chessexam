package com.anilson.chesshealthexam.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Person {
    @PrimaryKey
    public int uid;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public int age;

    @ColumnInfo
    public String countryCode;

    @ColumnInfo
    public double countryProbability;

    @ColumnInfo
    public String gender;

    @ColumnInfo
    public double genderProbability;
}
