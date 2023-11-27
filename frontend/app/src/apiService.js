import axios from 'axios';

const API_BASE_URL = 'http://localhost:3000'; 

export const createAppointment = (appointmentData) => {
    return axios.post(API_BASE_URL, appointmentData);
};

export const getAllAppointments = () => {
    return axios.get(API_BASE_URL);
};

export const getAppointmentById = (id) => {
    return axios.get(`${API_BASE_URL}/${id}`);
};

export const updateAppointment = (id, appointmentData) => {
    return axios.put(`${API_BASE_URL}/${id}`, appointmentData);
};

export const deleteAppointment = (id) => {
    return axios.delete(`${API_BASE_URL}/${id}`);
};