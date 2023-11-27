import React, { Component } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import AppointmentService from "../Services/AppointmentService";
import { useNavigate } from "react-router";
import Button from '@mui/material/Button';
import DeleteIcon from '@mui/icons-material/Delete';
import Stack from '@mui/material/Stack';

function ListAppointmentComponentWrapper(){
    const navigate = useNavigate();
    return <ListAppointmentComponent navigate={navigate} />;
}
class ListAppointmentComponent extends Component {
    constructor(props){
        super(props)

        this.state = {
            appointments: []
        }
        this.editAppointment = this.editAppointment.bind(this);
        this.deleteAppointment = this.deleteAppointment.bind(this);
        // this.viewAppointment = this.viewAppointment.bind(this);
    }

    viewAppointment(id){
        this.props.navigate(`/view-appointment/${id}`);
    }

    deleteAppointment(id){
        AppointmentService.deleteAppointment(id).then ( (res) => {
            this.setState({ appointments: this.state.appointments.filter(appointment => appointment.id !== id)})
        })
    }

    editAppointment(id){
        this.props.navigate(`/update-appointment/${id}`);
    }

    componentDidMount(){
        AppointmentService.getAppointment().then((res) => {
            this.setState({ appointments: res.data});
        });
    }

    render(){
        console.log(this.state.appointments);
        return (
            <div className='container'>
                <h2 className="text-center">Appointments List</h2>
                <div className="row">
                    <table style={{tableLayout:"fixed"}} className="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th> Title</th>
                                <th> Time</th>
                                <th> Date</th>
                                <th> Actions</th>
                            </tr>
                        </thead>

                        <tbody>
                            {
                                this.state.appointments.map(
                                    appointment =>
                                
                                    <tr key = {appointment.id}>
                                        <td> {appointment.appTitle} </td>
                                        <td> {appointment.startTime} </td>
                                        <td> {appointment.startDate} </td>
                                        <td>
                                            <Stack direction="row" spacing={2}>
                                                <Button onClick={() => this.editAppointment(appointment.id)} className="btn btn-info" variant="contained">Edit</Button>
                                                <Button style={{marginLeft: "10px"}} onClick={() => this.deleteAppointment(appointment.id)} startIcon={<DeleteIcon />} variant="contained" color="error">Delete</Button>
                                                <Button style={{marginLeft: "10px"}} onClick={() => this.viewAppointment(appointment.id)} className="btn btn-info" variant="outlined">Details</Button>
                                            </Stack>   
                                        </td>
                                    </tr>
                                )
                            }
                        </tbody>
                    </table>

                </div>
            </div>
        )
    }
}

export { ListAppointmentComponent };

export default ListAppointmentComponentWrapper;