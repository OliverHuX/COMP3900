import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import RestaurantMenuIcon from '@material-ui/icons/RestaurantMenu';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import FastfoodIcon from '@material-ui/icons/Fastfood';
import { makeStyles } from '@material-ui/core/styles';
import logout from './logout';
import PropTypes from 'prop-types';
import Link from '@material-ui/core/Link';

const useStyles = makeStyles((theme) => ({
  icon: {
    marginRight: theme.spacing(2),
  },
  logout: {
    position: 'relative',
    display: 'flex',
    marginLeft: 'auto',
    marginRight: 0,
    cursor: 'pointer'
  },
  icons: {
    position: 'relative',
    marginLeft: theme.spacing(2),
    marginRight: 0,
    cursor: 'pointer'
  },
  footer: {
    backgroundColor: theme.palette.background.paper,
    padding: theme.spacing(6),
  },
}));

const jumpto = () => {
  window.location.href = '/'
}
export function IconButton ({ icon, handleOnClick }) {
  const classes = useStyles();
  if (!handleOnClick) handleOnClick = (icon === 'join' ? jumpto : logout)
  if (icon === 'join') {
    return (
      <FastfoodIcon className={classes.icons} align="right" onClick={handleOnClick}>
      </FastfoodIcon>
    )
  }
  return (
    <ExitToAppIcon className={classes.icons} align="right" onClick={handleOnClick}>
    </ExitToAppIcon>
  )
}
IconButton.propTypes = {
  icon: PropTypes.string,
  handleOnClick: PropTypes.func
};
export function StyledHeader ({ handleOnClick }) {
  const classes = useStyles();
  const token = localStorage.getItem('token');
  return (
    <AppBar position="relative">
      <Toolbar>
        <RestaurantMenuIcon className={classes.icon} />
        <Link href="/home" variant="h6" color="inherit" noWrap>
                    {"MyRecipes"}
        </Link>

        <div className={classes.logout} color="inherit">
          <IconButton handleOnClick={handleOnClick} icon={'join'} />
          {token && (<IconButton handleOnClick={handleOnClick} icon={'logout'} />)}
        </div>
      </Toolbar>
    </AppBar>
  )
}
StyledHeader.propTypes = {
  handleOnClick: PropTypes.func
};
