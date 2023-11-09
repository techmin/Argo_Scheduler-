package com.example.scheduler.entities;

import com.cronutils.model.field.value.SpecialChar;
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
    
    private int interval;


    @ElementCollection(targetClass = daysOfWeek.class)
    @CollectionTable(name = "recurrence_days_of_week", joinColumns = @JoinColumn(name = "recurrence_id"))
    private List<daysOfWeek> daysOfWeek;


    private int dayOfMonth;
    private String weekOfMonth;

    //if recurrence is every day, assign this char as *
    char dayFreq;
    private SpecialChar freq;



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
        return this.interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
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

    public SpecialChar getFreq()
    {
        return this.freq;
    }

    public void setFreq(SpecialChar freq)
    {
        this.freq = freq;
    }


    public char getDayFreq(){
        return this.dayFreq;

    }

    public void setDayFreq(char dayFreq)
    {
        this.dayFreq = dayFreq;
    }
}

