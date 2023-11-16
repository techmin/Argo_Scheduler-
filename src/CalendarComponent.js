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
      eventTitle: '',
      eventStartDate: null,
      eventEndDate: null,
      eventStartTime: null,
      eventEndTime: null,
      showModal: false,
      selectedDate: null,
      showEventModal: false,
      eventDetails: null,
      isEditing: false,
      eventIdToEdit: null,
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

  handleInputChange = (event) => {
    const { name, value } = event.target;
    this.setState({ [name]: value });
  }   

  handleDateClick = (arg) => {
    // Extract the date and time from the clicked argument
    const date = arg.date;
    const dateStr = arg.dateStr; // Contains both date and time if available
    const allDay = arg.allDay; // Boolean indicating if the click was on an all-day slot
  
    // If it's an all-day slot, we don't set the time, otherwise, we extract the time
    const startDate = allDay ? date.toISOString().substring(0, 10) : dateStr; // 'YYYY-MM-DD'
    const startTime = allDay ? '' : date.toISOString().substring(11, 16); // 'HH:MM'
  
    this.setState({ 
      showModal: true, 
      eventStartDate: startDate,
      eventStartTime: startTime, // Will be an empty string if it's an all-day slot
      selectedDate: date 
    });
  }

  handleModalClose = () => {
    this.setState({ showModal: false });
  }

  handleEventSubmit = () => {
    const newEvent = {
      id: this.state.isEditing ? this.state.eventIdToEdit : Date.now(), // provide a unique id if it's a new event
      title: this.state.eventTitle,
      start: `${this.state.eventStartDate}T${this.state.eventStartTime}`,
      end: `${this.state.eventEndDate}T${this.state.eventEndTime}`,
    };
  
    if (this.state.isEditing) {
      const updatedEvents = this.state.events.map(event => 
        event.id === this.state.eventIdToEdit ? newEvent : event
      );
      this.setState({
        events: updatedEvents,
        showModal: false,
        isEditing: false
      });
    } else {
      this.setState(prevState => ({
        events: [...prevState.events, newEvent],
        showModal: false
      }));
    }
  
    // Optionally, update the event on your backend.
  }
  

  handleEventClick = (info) => {
    this.setState({
      showEventModal: true,
      eventDetails: {
        title: info.event.title,
        start: info.event.start,
        end: info.event.end
      },
      eventIdToEdit: info.event.id
    });
  }

  handleEditEvent = () => {
    // Populate form fields with event details
    const { title, start, end } = this.state.eventDetails;
    this.setState({
      showModal: true,
      showEventModal: false,
      eventTitle: title,
      eventStartDate: start.toISOString().split('T')[0],
      eventStartTime: start.toTimeString().slice(0, 5),
      eventEndDate: end.toISOString().split('T')[0],
      eventEndTime: end.toTimeString().slice(0, 5),
      isEditing: true
    });
  }
  
  handleCancelEvent = () => {
    const events = this.state.events.filter(event => event.id !== this.state.eventIdToEdit);
    this.setState({
      events: events,
      showEventModal: false
    });
    // Optionally, remove the event from your backend.
  }
  
  handleEventModalClose = () => {
    this.setState({ showEventModal: false });
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
          eventClick={this.handleEventClick}
          headerToolbar={{
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
          }}
        />
        {this.state.showModal && (
          <div className="modal">
            <div className="modal-content">
              <span className="close" onClick={this.handleModalClose}>&times;</span>
              <h4>Create Event for {this.state.selectedDate?.toLocaleDateString()}</h4>
              <label>Title: 
                <input 
                  type="text" 
                  name="eventTitle" 
                  value={this.state.eventTitle} 
                  onChange={this.handleInputChange} 
                  placeholder="Event Title"
                  />
              </label><br />
              <label>Start Date: 
                <input 
                  type="date" 
                  name="eventStartDate"
                  value={this.state.eventStartDate || ''} // Set the value from state
                  onChange={this.handleInputChange} 
                />
              </label><br />
              <label>End Date: 
                <input 
                  type="date" 
                  name="eventEndDate"
                  onChange={this.handleInputChange} 
                />
              </label><br />
              <label>Start Time: 
                <input 
                  type="time" 
                  name="eventStartTime"
                  value={this.state.eventStartTime || ''} // Set the value from state
                  onChange={this.handleInputChange} 
                />
              </label><br />
              <label>End Time: 
                <input 
                  type="time" 
                  name="eventEndTime"
                  onChange={this.handleInputChange} 
                />
              </label><br />
              <label>Duration (minutes): <input type="number" /></label><br />
              <label>Recurrence Pattern: 
                <select>
                  <option value="daily">Daily</option>
                  <option value="weekly">Weekly</option>
                  <option value="monthly">Monthly</option>
                  <option value="yearly">Yearly</option>
                </select>
              </label><br />
              <label>Weekdays Only: <input type="checkbox" /></label><br />
              <div>
                <label>Specific Days:</label><br />
                <label><input type="checkbox" /> Sunday</label>
                <label><input type="checkbox" /> Monday</label>
                <label><input type="checkbox" /> Tuesday</label>
                <label><input type="checkbox" /> Wednesday</label>
                <label><input type="checkbox" /> Thursday</label>
                <label><input type="checkbox" /> Friday</label>
                <label><input type="checkbox" /> Saturday</label>
              </div>
              <label>Ends After 
                <input type="number" min="1" placeholder="number of occurrences" />
                occurrences
              </label><br />

              <button onClick={this.handleEventSubmit}>Add Event</button>
            </div>
          </div>
        )}
        {/* Event details modal */}
        {this.state.showEventModal && (
          <div className="modal">
            <div className="modal-content">
              <span className="close" onClick={this.handleEventModalClose}>&times;</span>
              <h4>Event Details</h4>
              <p><strong>Title:</strong> {this.state.eventDetails.title}</p>
              <p><strong>Start:</strong> {this.state.eventDetails.start.toLocaleString()}</p>
              <p><strong>End:</strong> {this.state.eventDetails.end.toLocaleString()}</p>
              <button onClick={this.handleEditEvent}>Edit Event</button>
              <button onClick={this.handleCancelEvent}>Cancel Event</button>
            </div>
          </div>
        )}
        </div>
    );
  }
}

export default CalendarComponent;

