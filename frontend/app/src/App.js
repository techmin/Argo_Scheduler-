import React, { useState } from 'react';
import './App.css';
import Calendar from './Calendar';
import AppointmentForm from './AppointmentForm';

function App() {
  const [view, setView] = useState('calendar');

  const handleDateSelect = () => {
    setView('appointmentForm');
  };

  return (
    <div className="App">
      <h1>ARGO</h1>
      {view === 'calendar' && <Calendar />}
      {view === 'appointmentForm' && (
        <>
          <h2>Create Appointment</h2>
          <AppointmentForm />
        </>
      )}
    </div>
  );
}

export default App;