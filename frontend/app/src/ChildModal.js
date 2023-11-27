import React from 'react';
import { Modal, Box, Button } from '@mui/material';

const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 2,
};

const ChildModal = ({ open, handleClose }) => {
    return (
      <Modal open={open} onClose={handleClose}>
        <Box sx={style}>
          {/* Modal content */}
          <h2>Child Modal Title</h2>
          <p>Child modal content goes here...</p>
          <Button onClick={handleClose}>Close</Button>
        </Box>
      </Modal>
    );
};
  
export default ChildModal;