import React, { Component } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction'; // for selectable
import axios from 'axios';
import './styles.css';

class CalenderFC extends Component {
  constructor(props) {
    super(props);
    this.state = {
      events: [],
    };
  }

  componentDidMount() {
    axios.get('backendurl/events')
    .then(response => {
      this.setState({ events: response.data });
    })
    .catch(error => {
      console.error("Error fetching events:", error);
    });
  }

  handleDateClick = (arg) => {
    // Example: add an event upon date click
    const newEvent = {
      title: "New Event",
      start: arg.date,
      allDay: arg.allDay
    };
    this.setState({ events: [...this.state.events, newEvent] });
  }

  render() {
    return (
      <div>
        {/* You can retain your Add Event Form here */}
        {/* ... */}

        <FullCalendar
          plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
          initialView="dayGridMonth"
          events={this.state.events}
          selectable={true}
          dateClick={this.handleDateClick}
        />
      </div>
    );
  }
}

export default CalenderFC;