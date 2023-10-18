import React, { Component } from 'react';
import { Calendar, momentLocalizer } from 'react-big-calendar';
import moment from 'moment';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import axios from 'axios';
import './styles.css';


// localilizer for using moment for date functionalities
const localizer = momentLocalizer(moment);

// main class that extends Component to fetch html
class CalendarComponent extends Component {
  constructor(props) {
    super(props);
    this.state = {      // initialize component state
      events: [],  // fetched events from backend stored here
    };
  }


  // fetch events when components mount
  componentDidMount() {
    axios.get('backendurl/events')
    .then(response => {
      this.setState({ events: response.data });   // updates state with fetched event
    })
    .catch(error => {
      console.error("Error fetching events:", error);   // log errors
    });
  }


  // Method that handles form submission for new events
  handleSubmit = (event) => {
    event.preventDefault();

    // new event object
    const newEvent = {
      title: this.state.title, // assuming you've bound an input field to this state variable
      start: new Date(this.state.startDate), // assuming you've bound a date input field
      end: new Date(this.state.endDate), // same
      // other fields
    };

    // POST request made to backend to save new event 
    axios.post('backendurl/events', newEvent)
      .then(response => {
        this.setState(prevState => ({
          events: [...prevState.events, response.data]     // handle successful ceation by adding the new event to your state

        }));
      })
      .catch(error => {
        console.error("Error creating event:", error);  // log errors
      });
  }


  // Component display
  render() {
    return (
      <div>
        {/* Add Event Form */}
        <form onSubmit={this.handleSubmit}>
          <input type="text" value={this.state.title} onChange={e => this.setState({ title: e.target.value })} placeholder="Event Title" />
          <input type="date" value={this.state.startDate} onChange={e => this.setState({ startDate: e.target.value })} />
          <input type="date" value={this.state.endDate} onChange={e => this.setState({ endDate: e.target.value })} />
          <button type="submit">Add Event</button>
        </form>


        {/* Calendar Display */}
        <Calendar
            localizer={localizer}
            events={this.state.events}
            startAccessor="start"
            endAccessor="end"
            views={['month', 'week', 'day', 'agenda']}
            defaultView='month'
            style={{ height: "600px" }}  // adjust this to ensure month grid is visible
        />
      </div>
    );
  }
}

// exports for use in other parts of app
export default CalendarComponent;
