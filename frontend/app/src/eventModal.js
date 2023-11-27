import React, { useState } from "react";

const EventModal = ({ isOpen, onClose, onSave }) => {
    const [eventData, setEventData] = useState({
      startDate: "",
      endDate: "",
      startTime: "",
      endTime: "",
      duration: 30, // Default duration in minutes
      recurrence: "none",
      weekdaysOnly: false,
      specificDays: [],
      recurrenceEndsAfter: 1,
      noEndDate: false,
    });
  
    const handleInputChange = (e) => {
      const { name, value } = e.target;
      setEventData((prevData) => ({ ...prevData, [name]: value }));
    };
  
    return (
      <div className={`modal ${isOpen ? "open" : ""}`}>
        <div className="modal-content">
          <span className="close" onClick={onClose}>&times;</span>
          <h4>Create Event</h4>
          <label>Start Date: <input type="date" name="startDate" onChange={handleInputChange} /></label><br />
          <label>End Date: <input type="date" name="endDate" onChange={handleInputChange} /></label><br />
          <label>Start Time: <input type="time" name="startTime" onChange={handleInputChange} /></label><br />
          <label>End Time: <input type="time" name="endTime" onChange={handleInputChange} /></label><br />
          <label>Duration (minutes): <input type="number" name="duration" onChange={handleInputChange} value={eventData.duration} /></label><br />
          <label>Recurrence Pattern: 
            <select name="recurrence" onChange={handleInputChange}>
              <option value="none">None</option>
              <option value="daily">Daily</option>
              <option value="weekly">Weekly</option>
              <option value="monthly">Monthly</option>
              <option value="yearly">Yearly</option>
            </select>
          </label><br />
          <label>Weekdays Only: <input type="checkbox" name="weekdaysOnly" onChange={e => setEventData({ ...eventData, weekdaysOnly: e.target.checked })} /></label><br />
          <p>Select Specific Days:</p>
          {["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"].map(day => (
            <label key={day}>
              {day}:
              <input 
                type="checkbox" 
                name="specificDays" 
                value={day}
                onChange={e => {
                  const checked = e.target.checked;
                  setEventData(prevData => {
                    if (checked) return { ...prevData, specificDays: [...prevData.specificDays, day] };
                    return { ...prevData, specificDays: prevData.specificDays.filter(d => d !== day) };
                  });
                }}
              />
            </label>
          ))}
          <br />
          <label>Recurrence Ends After:
            <input type="number" name="recurrenceEndsAfter" onChange={handleInputChange} value={eventData.recurrenceEndsAfter} min="1" />
          </label> occurrences<br />
          <label>No End Date: <input type="checkbox" name="noEndDate" onChange={e => setEventData({ ...eventData, noEndDate: e.target.checked })} /></label><br />
          <button onClick={() => onSave(eventData)}>Save</button>
        </div>
      </div>
    );
  };