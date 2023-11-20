import logo from './logo.svg';
import React from 'react';
import './App.css';
import AppointmentForm from './AppointmentForm';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h2>Create Appointment</h2>
        <AppointmentForm />
      </header>
    </div>
  );
}

export default App;
