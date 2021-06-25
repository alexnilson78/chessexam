package com.anilson.chesshealthexam.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "name", unique = true)})
public class Person {
    @PrimaryKey(autoGenerate = true)
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

    public Person(int uid, String name, int age, String countryCode, double countryProbability, String gender, double genderProbability) {
        this.uid = uid;
        this.name = name;
        this.age = age;
        this.countryCode = countryCode;
        this.countryProbability = countryProbability;
        this.gender = gender;
        this.genderProbability = genderProbability;
    }
}
