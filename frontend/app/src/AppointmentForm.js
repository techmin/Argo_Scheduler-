import React, { useState } from 'react';
import { TextField, Button, Dialog, DialogTitle, DialogContent, FormControlLabel, Switch, FormControl, InputLabel, Select, MenuItem } from '@mui/material';
import dayjs from 'dayjs';
import { DatePicker, TimePicker } from '@mui/x-date-pickers';
import { DemoContainer, DemoItem } from '@mui/x-date-pickers/internals/demo';
import { StaticDatePicker } from '@mui/x-date-pickers/StaticDatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';


function AppointmentForm() {
  const [appointment, setAppointment] = useState({
    appTitle: '',
    startDate: dayjs(),
    endDate: '',
    startTime: dayjs(),
    endTime: dayjs()
    // include other fields as necessary
  });

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setAppointment({ ...appointment, [name]: value });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const formattedStartDate = appointment.startDate.format("YYYY-MM-DD");
      const formattedEndDate = appointment.endDate ? appointment.endDate.format("YYYY-MM-DD") : null;
      const formattedStartTime = appointment.startTime.format("HH:mm:ss");
      const formattedEndTime = appointment.endDTime ? appointment.endTime.format("HH:mm:ss") : null;
      
      const dataToSend = {
        ...appointment,
        startTime: formattedStartTime,
        endTime: formattedEndTime,
        startDate: formattedStartDate,
        endDate: formattedEndDate,
      }
      const response = await fetch('http://localhost:8080/api/appointment/schedule', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(dataToSend),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      if (response.headers.get('Content-Type').includes('application/json')) {
        const result = await response.json(); // Parse as JSON
        console.log(result);
      } else {
        const text = await response.text(); // Read as text
        console.log('Received non-JSON response:', text);
      }
      
      // Handle success message here
      setAppointment({
        appTitle:'',
        startDate:'',
        endDate:'',
        startTime:'',
        endTime:''
      })
    } catch (error) {
      console.error('There was an error!', error);
      // Handle error message here
    }
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <form onSubmit={handleSubmit}>
        <Dialog open = {true} onClose={() => {} }>
          <DialogTitle>New Appointment</DialogTitle>
          <DialogContent>
            <StaticDatePicker 
              label="Select date"
              value={appointment.startDate}
              onChange={(newDate) => setAppointment({ ...appointment, startDate: newDate})}
              renderInput={(params) => <TextField {...params} fullWidth />}
            />
            <TextField 
              fullWidth
              margin='normal'
              name='appTitle'
              label='Appointment Title'
              value={appointment.appTitle}
              onChange={(e) => setAppointment({ ...appointment, appTitle: e.target.value })}
              required
            />
            <TimePicker 
              label="Start time"
              value={appointment.startTime}
              onChange={(newTime) => setAppointment({...appointment, startTime: newTime })}
              renderInput={(params) => <TextField {...params} fullWidth />}
              required
            />

            <FormControl fullWidth>
              <InputLabel id="repeat-label">Does not repeat</InputLabel>
              <Select
                labelId='repeat-label'
                value={appointment.repeat}
                label="Does not repeat"
                onChange={(e) => setAppointment({...appointment, repeat: e.target.value})}>
                <MenuItem value="Does not repeat">Does not repeat</MenuItem>
                <MenuItem value="Daily">Daily</MenuItem>
                <MenuItem value="Weekly">Weekly</MenuItem>
                <MenuItem value="Monthly">Monthly</MenuItem>
                <MenuItem value="Yearly">Yearly</MenuItem>
              </Select>
            </FormControl>
            <Button onClick={handleSubmit} variant='contained' color='primary'>Submit</Button>
            <Button onClick={() => {}}>Cancel</Button>
          </DialogContent>
        </Dialog>
      
      {/* <DemoItem label="New Appointment">
        <StaticDatePicker defaultValue={dayjs('2022-04-17')} />
      </DemoItem>
      <input
        type="text"
        name="appTitle"
        value={appointment.appTitle}
        onChange={handleInputChange}
        placeholder="Appointment Title"
        required
      /> */}
      {/* <DatePicker 
        label = "Start Date"
        value = {appointment.startDate}
        onChange = {(date) => handleInputChange('startDate', date)}
        renderInput={(props) => <TextField {...props} required fullWidth />}
      /> */}
      {/* <DatePicker 
        label = "End Date"
        value = {appointment.endDate}
        onChange = {(date) => handleInputChange('endDate', date)}
        renderInput={(props) => <TextField {...props} required fullWidth />}
      /> */}
      {/* <TimePicker 
        label = "Start Time"
        value = {appointment.startTime}
        onChange = {(time) => handleInputChange('startTime', time)}
        renderInput={(props) => <TextField {...props} required fullWidth />}
      />
      <TimePicker 
        label = "End Time"
        value = {appointment.endTime}
        onChange = {(time) => handleInputChange('endTime', time)}
        renderInput={(props) => <TextField {...props} required fullWidth />}
      /> */}

      {/* <input
        type="date"
        name="startDate"
        value={appointment.startDate}
        onChange={handleInputChange}
        required
      />
      <input
        type="date"
        name="endDate"
        value={appointment.endDate}
        onChange={handleInputChange}
        required
      />
      <input
        type="time"
        name="startTime"
        value={appointment.startTime}
        onChange={handleInputChange}
        required
      /> */}
      {/* <input
        type="time"
        name="endTime"
        value={appointment.endTime}
        onChange={handleInputChange}
        required
      /> */}
      {/* <button type="submit" variant="contained" color="primary">
        Schedule Appointment
      </button> */}
    </form>
    </LocalizationProvider>
    
  );
}

export default AppointmentForm;
