import React, { Component } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import listPlugin from '@fullcalendar/list';
import interactionPlugin from '@fullcalendar/interaction'; 
import axios from 'axios';
import './styles.css';

class CalendarComponent extends Component {
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
        <FullCalendar
          plugins={[dayGridPlugin, timeGridPlugin, listPlugin, interactionPlugin]}
          initialView="dayGridMonth"
          events={this.state.events}
          selectable={true}
          dateClick={this.handleDateClick}
          headerToolbar={{
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
          }}
        />
      </div>
    );
  }
}

export default CalendarComponent;
