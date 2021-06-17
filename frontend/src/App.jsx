import React from 'react';
// import style from './App.module.css';
import './App.css';
import {
  BrowserRouter as Router,
  Switch,
  Route,
} from 'react-router-dom';
import Register from './pages/Register';
import Login from './pages/Login';

export default function App () {
  return <>
    <Router>
      <div className='container'>
        <Switch>
          <Route path="/" exact>
            <div className='back-Img'>
              <Login />
            </div>
          </Route>
          <Route path="/register" exact>
            <div className='back-Img'>
              <Register />
            </div>
          </Route>
        </Switch>
      </div>
    </Router>
  </>;
}
