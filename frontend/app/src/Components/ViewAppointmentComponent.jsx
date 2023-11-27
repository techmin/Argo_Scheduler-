import React, { Component } from 'react';
import AppointmentService from '../Services/AppointmentService';
import { useParams } from 'react-router';



const styles = {
    card: {
      backgroundColor: '#f9f9f9',
      borderRadius: '8px',
      boxShadow: '0 4px 8px rgba(0,0,0,0.1)',
      padding: '20px',
      maxWidth: '400px',
      margin: 'auto',
    },
    header: {
      fontSize: '1.5em',
      color: '#333',
      marginBottom: '20px',
    },
    body: {
      color: '#555',
    },
    row: {
      marginBottom: '10px',
    },
    label: {
      fontWeight: 'bold',
    },
    value: {
      marginLeft: '8px',
    },
};
  
function ViewAppointmentWrapper(){
    const { id } = useParams();
    return <ViewAppointmentComponent id={id} />;
}
class ViewAppointmentComponent extends Component {
    constructor(props){
        super(props)

        this.state = {
            appointmentData: {},
        }
    }

    renderRecurrence(recurrence) {
        if (!recurrence) {
            return 'None';
        }
        // return `Every ${recurrence.frequency} days, on ${recurrence.daysOfWeek.join(', ')}`;
        return `${recurrence.repeat}`;
    }

    componentDidMount(){
        const { id } = this.props;
        AppointmentService.getAppointmentById(id).then((res) => {
            this.setState({appointmentData: res.data});
        })
    }
    render() {
        return (
            <div>
                <div className='card col-md-12 offset-md-8' style={styles.card}>
                    <h5 className='text-center' style={styles.header}> View Appointment Details</h5>
                    <div className='card-body' style={styles.body}>
                        <div className='row' style={styles.row}>
                            <strong style={styles.label}> Title: </strong>
                            <span style={styles.value}> {this.state.appointmentData.appTitle}</span>
                        </div>
                        <div className='row' style={styles.row}>
                            <strong style={styles.label}> Date: </strong>
                            <span style={styles.value}> {this.state.appointmentData.startDate}</span>
                        </div>
                        <div className='row' style={styles.row}>
                            <strong style={styles.label}> Time: </strong>
                            <div style={styles.value}> {this.state.appointmentData.startTime}</div>
                        </div>
                        <div className='row' style={styles.row}>
                            <strong style={styles.label}> Recurrence: </strong>
                            <div style={styles.value}> {this.renderRecurrence(this.state.appointmentData.recurrence)}</div>
                        </div>
                        <div className='row' style={styles.row}>
                            <strong style={styles.label}> Status: </strong>
                            <div style={styles.value}> {this.state.appointmentData.status}</div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default ViewAppointmentWrapper;