package com.example.scheduler.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "recurrence_properties")
public class RecurrenceProperty {
    public enum Frequency{
        DAILY, WEEKLY, MONTHLY, YEARLY;
    }

    public enum daysOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Frequency frequency;
    
    private int intervalAmount;


    // @ElementCollection(targetClass = daysOfWeek.class)
    @CollectionTable(name = "recurrence_days_of_week", joinColumns = @JoinColumn(name = "recurrence_id"))
    private List<daysOfWeek> daysOfWeek;


    private int dayOfMonth;
    private String weekOfMonth;



    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Frequency getFrequency() {
        return this.frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public int getInterval() {
        return this.intervalAmount;
    }

    public void setInterval(int interval) {
        this.intervalAmount = interval;
    }

    public List<daysOfWeek> getDaysOfWeek() {
        return this.daysOfWeek;
    }

    public void setDaysOfWeek(List<daysOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public int getDayOfMonth() {
        return this.dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getWeekOfMonth() {
        return this.weekOfMonth;
    }

    public void setWeekOfMonth(String weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }
    
}

