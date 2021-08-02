import React from 'react';
import PropTypes from 'prop-types';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';

export function TextPopup ({ open = false, setOpen, title = 'Notification', msg, newButton, newButtonMsg, newButtonFun }) {
  const handleClose = () => setOpen(false);

  return (
      <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title" fullWidth>
        <DialogTitle id="form-dialog-title">{title}</DialogTitle>
        <DialogContent>
          {msg}
        </DialogContent>
        <DialogActions>
          {newButton && <Button onClick={() => newButtonFun} color="primary">
            {newButtonMsg}
          </Button>}
          <Button onClick={() => handleClose()} color="primary">
            Close
          </Button>
        </DialogActions>
      </Dialog>
  );
}
TextPopup.propTypes = {
  open: PropTypes.bool,
  newButton: PropTypes.bool,
  setOpen: PropTypes.func,
  title: PropTypes.string,
  msg: PropTypes.string,
  newButtonMsg: PropTypes.string,
  newButtonFun: PropTypes.func,
};
