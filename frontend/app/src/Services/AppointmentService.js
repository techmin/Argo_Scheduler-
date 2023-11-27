import axios from 'axios';

const APPOINTMENT_BASE_URL = "http://localhost:8080/api/appointment";

class AppointmentService {
    getAppointment(){
        return axios.get(APPOINTMENT_BASE_URL + '/list');
    }

    getAppointmentById(id){
        return axios.get(APPOINTMENT_BASE_URL + '/app/' + id);
    }

    updateAppointment(id, appointmentData){
        return axios.put(APPOINTMENT_BASE_URL + '/update/' + id, appointmentData);
    }

    deleteAppointment(id){
        return axios.delete(APPOINTMENT_BASE_URL + "/delete/" + id);
    }
}

export default new AppointmentService();