import React, { Component } from 'react';
import AppointmentForm from '../AppointmentForm';
import AppointmentService from "../Services/AppointmentService";
import { useParams } from 'react-router';
import dayjs from 'dayjs';

function UpdateAppointmentWrapper(){
    const { id } = useParams();
    return <UpdateAppointment id={id} />;
}

class UpdateAppointment extends Component {
    constructor(props){
        super(props)

        this.state = {
            // id: this.props.id,
            appointmentData: null,
        }
    }

    componentDidMount(){
        const { id } = this.props;
        AppointmentService.getAppointmentById(id).then( (res) => {
            let appointment = res.data;
            console.log(appointment);
            appointment.startDate = dayjs(appointment.startDate);
            if (appointment.endDate) {
                appointment.endDate = dayjs(appointment.endDate);
            }
            if (appointment.endTime) {
                appointment.endTime = dayjs(appointment.endTime);
            }
            this.setState({ 
                appointmentData: appointment,
            });
        });
    }
    render() {
        return (
            <div>
                {this.state.appointmentData && <AppointmentForm initialData={this.state.appointmentData} />}
            </div>
        );
    }
}

export default UpdateAppointmentWrapper;