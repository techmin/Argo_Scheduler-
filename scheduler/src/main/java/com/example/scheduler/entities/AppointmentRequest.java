package com.example.scheduler.entities;

public class AppointmentRequest {
    private Appointments appointment;
    private RecurrenceProperty recurrence;

    public AppointmentRequest(Appointments appointment, RecurrenceProperty recurrence){
        this.appointment = appointment;
        this.recurrence = recurrence;
    }


    public Appointments getAppointment() {
        return this.appointment;
    }

    public void setAppointment(Appointments appointment) {
        this.appointment = appointment;
    }

    public RecurrenceProperty getRecurrence() {
        return this.recurrence;
    }

    public void setRecurrence(RecurrenceProperty recurrence) {
        this.recurrence = recurrence;
    }

}
