import React, { useState } from 'react';
import { TextField, Button, Dialog, DialogTitle, DialogContent, FormControl, InputLabel, Select, MenuItem, Grid } from '@mui/material';
import dayjs from 'dayjs';
import { DateCalendar, TimePicker } from '@mui/x-date-pickers';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { useNavigate } from 'react-router-dom';
import './AppointmentForm.css';


function AppointmentForm({ initialData }) {
  const navigate = useNavigate();

  const [appointment, setAppointment] = useState(initialData || {
    appTitle: '',
    startDate: dayjs(),
    endDate: '',
    startTime: dayjs(),
    endTime: dayjs(),
    repeat: 'Does not repeat',
    intervalAmount: 1
  });

  // const handleInputChange = (event) => {
  //   const { name, value } = event.target;
  //   setAppointment({ ...appointment, [name]: value });
  // };

  const handleCancel = () => {
    navigate('/home');
  }
  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const formattedStartDate = appointment.startDate.format("YYYY-MM-DD");
      const formattedEndDate = appointment.endDate ? appointment.endDate.format("YYYY-MM-DD") : null;
      const formattedStartTime = appointment.startTime.format("HH:mm:ss");
      const formattedEndTime = appointment.endTime ? appointment.endTime.format("HH:mm:ss") : null;

      // let recurrence = null;
      let recurrenceData = appointment.repeat !== 'Does not repeat' ? {
        repeat: appointment.repeat,
        intervalAmount: appointment.intervalAmount
      } : null;

      const endpoint = initialData ? `http://localhost:8080/api/appointment/update/${initialData.id}` : 'http://localhost:8080/api/appointment/schedule';
      const method = initialData ? 'PUT' : 'POST';

      const dataToSend = {
        appointment: {
          appTitle: appointment.appTitle,
          startTime: formattedStartTime,
          endTime: formattedEndTime,
          startDate: formattedStartDate,
          endDate: formattedEndDate,
          recurrence: recurrenceData,
        }
      }
      // console.log("Data to send:", dataToSend);
      const response = await fetch(endpoint, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(dataToSend),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      navigate('/home');
      if (response.headers.get('Content-Type').includes('application/json')) {
        const result = await response.json(); // Parse as JSON
        // console.log(result);
      } else {
        const text = await response.text(); // Read as text
        console.log('Received non-JSON response:', text);
      }
      
      // Handle success message here
      setAppointment({
        appTitle:'',
        startDate: dayjs(),
        endDate: '',
        startTime: dayjs(),
        endTime: dayjs(),
        repeat: 'Does not repeat',
        intervalAmount: 1
      })
    } catch (error) {
      console.error('There was an error!', error);
      // Handle error message here
    }
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <form onSubmit={handleSubmit}>
        <Dialog open = {true} onClose={() => {}} style={{ width: 'auto', maxWidth: '500px', margin: '0 auto' }}>
          <DialogTitle>New Appointment</DialogTitle>
          <DialogContent className='dialogContent'>
            <Grid container rowSpacing={2}>
              <Grid item>
                <DateCalendar 
                  label="Select date"
                  value={appointment.startDate}
                  onChange={(newDate) => setAppointment({ ...appointment, startDate: newDate})}
                  renderInput={(params) => <TextField {...params} fullWidth />}
                />
              </Grid>
              <Grid item>
                <TextField
                  className='textField'
                  fullWidth
                  margin='normal'
                  name='appTitle'
                  label='Appointment Title'
                  value={appointment.appTitle}
                  onChange={(e) => {setAppointment({ ...appointment, appTitle: e.target.value })}}
                  required
                />
              </Grid>
              <Grid item>
                <TimePicker 
                  className='timePicker'
                  label="Start time"
                  value={appointment.startTime}
                  onChange={(newTime) => setAppointment({...appointment, startTime: newTime })}
                  renderInput={(params) => <TextField {...params} fullWidth />}
                  required
                  // style={{marginTop: 20, marginBottom: 20}}
                />
                </Grid>
              <Grid item>
                <TimePicker
                  className='timePicker'
                  label="End time"
                  value={appointment.endTime}
                  onChange={(newTime) => setAppointment({...appointment, endTime: newTime })}
                  renderInput={(params) => <TextField {...params} fullWidth />}
                  required
                  // style={{marginTop: 20, marginBottom: 20}}
                />
                </Grid>
                <Grid item xs={12}>
                <FormControl fullWidth>
                  <InputLabel id="repeat-label">Repeat</InputLabel>
                  <Select
                    labelId='repeat-label'
                    value={appointment.repeat}
                    label="Repeat"
                    onChange={(e) => {setAppointment({...appointment, repeat: e.target.value});
                    console.log("Selected recurrence: ", e.target.value);
                    }}>
                    <MenuItem value="Does not repeat">Does not repeat</MenuItem>
                    <MenuItem value="DAILY">Daily</MenuItem>
                    <MenuItem value="WEEKLY">Weekly</MenuItem>
                    <MenuItem value="MONTHLY">Monthly</MenuItem>
                    <MenuItem value="YEARLY">Yearly</MenuItem>
                  </Select>
                  </FormControl>
                </Grid>
                <Grid item>
                  <div className='buttonGroup'>
                    <Button className="buttonSubmit" onClick={handleSubmit} variant='contained' color='primary'>Submit</Button>
                    <Button className='buttonCancel' onClick={handleCancel}>Cancel</Button>
                  </div>
                </Grid>
            </Grid>  
          </DialogContent>
        </Dialog>
    </form>
    </LocalizationProvider>
    
  );
}

export default AppointmentForm;
