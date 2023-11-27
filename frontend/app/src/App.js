import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import AppointmentForm from './AppointmentForm';
import HomePage from './HomePage';
import theme from './theme';
import { Box, Container, ThemeProvider } from '@mui/material';
import ListAppointmentComponent from './Components/ListAppointmentComponent';
import UpdateAppointmentWrapper from './Components/UpdateAppointment';
import ViewAppointmentWrapper from './Components/ViewAppointmentComponent';
// import SearchAppBar from './SearchAppBar';



function App() {
  return (
    <Router>
      <div className="container">
        <ThemeProvider theme={theme}>
          {/* <SearchAppBar /> */}
          <Box sx={{ display : 'flex'}}>
            <Box component='main' sx={{}}>
              
              <Container maxWidth="lg">
              <HomePage />
                <Routes>
                  <Route path="/home" element={<ListAppointmentComponent />} />
                  <Route path="/create-appointment" element={<AppointmentForm />} />
                  <Route path="/update-appointment/:id" element={<UpdateAppointmentWrapper />} />
                  <Route path="/view-appointment/:id" element={<ViewAppointmentWrapper />} />
                </Routes>
              </Container>
            </Box>
          </Box>
        </ThemeProvider>
      </div>

      <div className='container'>
        
      </div>
    </Router>
  );
}

export default App;