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
    // Format the date and time based on the clicked slot
    const dateClicked = arg.date;
    const hasTime = arg.view.type.includes('timeGrid');
  
    // Convert to local date-time string and extract the date and time parts
    const localDateTime = new Date(dateClicked - (dateClicked.getTimezoneOffset() * 60000)).toISOString().split('T');
    const startDate = localDateTime[0]; // 'YYYY-MM-DD'
    const startTime = hasTime ? localDateTime[1].substring(0, 5) : ''; // 'HH:MM' if time is included, else empty
  
    this.setState({ 
      showModal: true, 
      eventStartDate: startDate,
      eventStartTime: startTime, // Set the time only if the current view includes time slots
      selectedDate: dateClicked 
    });
  }

  handleModalClose = () => {
    this.setState({ showModal: false });
  }

  handleEventSubmit = () => {
    let newEvent;
    const hasStartTime = this.state.eventStartTime && this.state.eventStartTime.trim() !== '';
    const hasEndTime = this.state.eventEndTime && this.state.eventEndTime.trim() !== '';
  
    if (hasStartTime && hasEndTime) {
      // If start and end times are provided, use them
      newEvent = {
        id: this.state.isEditing ? this.state.eventIdToEdit : Date.now(),
        title: this.state.eventTitle,
        start: `${this.state.eventStartDate}T${this.state.eventStartTime}`,
        end: `${this.state.eventEndDate}T${this.state.eventEndTime}`,
        allDay: false,
      };
    } else {
      // If no time is provided, treat it as an all-day event
      // Adjust the end date for all-day events
      const adjustedEndDate = new Date(this.state.eventEndDate);
      adjustedEndDate.setDate(adjustedEndDate.getDate() + 1);
  
      newEvent = {
        id: this.state.isEditing ? this.state.eventIdToEdit : Date.now(),
        title: this.state.eventTitle,
        start: this.state.eventStartDate,
        // Set the end date to the day after the event ends
        end: adjustedEndDate.toISOString().split('T')[0],
        allDay: true,
      };
    }
  
    if (this.state.isEditing) {
      // Update the events array with the new event details
      const updatedEvents = this.state.events.map(event =>
        event.id === this.state.eventIdToEdit ? newEvent : event
      );
  
      this.setState({
        events: updatedEvents,
        showModal: false,
        isEditing: false,
        eventIdToEdit: null, // Clear the eventIdToEdit after updating
      });
  
      // Send update request to backend
      axios.put(`backendurl/events/${this.state.eventIdToEdit}`, newEvent)
        .then(response => {
          // Handle success...
        })
        .catch(error => {
          // Handle error...
        });
    } else {
      // Add the new event to the events array
      this.setState(prevState => ({
        events: [...prevState.events, newEvent],
        showModal: false,
      }));
  
      // Optionally, send the newEvent to your backend to create it
      // axios.post(`backendurl/events`, newEvent)
      // ... handle response
    }
  };
  
  

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
    // Ensure that eventDetails has valid start and end dates before attempting to edit
    if (this.state.eventDetails && this.state.eventDetails.start && this.state.eventDetails.end) {
      const start = new Date(this.state.eventDetails.start);
      const end = new Date(this.state.eventDetails.end);
  
      this.setState({
        showModal: true,
        showEventModal: false,
        eventTitle: this.state.eventDetails.title,
        eventStartDate: start.toISOString().split('T')[0],
        eventStartTime: start.toTimeString().slice(0, 5),
        eventEndDate: end.toISOString().split('T')[0],
        eventEndTime: end.toTimeString().slice(0, 5),
        isEditing: true
      });
    } else {
      // Handle the error - perhaps show a message to the user or log the issue
      console.error('Cannot edit event: start or end date is missing');
    }
  };
  
  
  
  handleCancelEvent = () => {
    axios.delete(`backendurl/events/${this.state.eventIdToEdit}`)
      .then(response => {
        const events = this.state.events.filter(event => event.id !== this.state.eventIdToEdit);
        this.setState({
          events: events,
          showEventModal: false,
          eventIdToEdit: null,
        });
        // Handle success...
      })
      .catch(error => {
        // Handle error...
      });
  };
  
  
  handleEventModalClose = () => {
    this.setState({ showEventModal: false });
  }

  render() {

    // Check if eventDetails.start and eventDetails.end are not null before rendering them
    const startString = this.state.eventDetails && this.state.eventDetails.start ? this.state.eventDetails.start.toLocaleString() : 'N/A';
    const endString = this.state.eventDetails && this.state.eventDetails.end ? this.state.eventDetails.end.toLocaleString() : 'N/A';
    
    return (
      <div className='calendar-component'>
        <FullCalendar
          key={this.state.events.length}
          plugins={[dayGridPlugin, timeGridPlugin, listPlugin, interactionPlugin]}
          initialView="dayGridWeek"
          selectable={true}
          allDaySlot={false}
          slotMinTime={"06:00:00"}
          slotMaxTime={"18:00:00"}
          events={this.state.events}
        //   dateClick={this.handleDateClick}
        //   eventClick={this.handleEventClick}
          headerToolbar={{
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek'
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
              <p><strong>Title:</strong> {this.state.eventDetails ? this.state.eventDetails.title : 'N/A'}</p>
              <p><strong>Start:</strong> {startString}</p>
              <p><strong>End:</strong> {endString}</p>
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