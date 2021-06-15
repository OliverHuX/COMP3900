import React, { Component } from 'react'
import {Switch, Route} from "react-router-dom"

import Navigation from './components/navigation'
import Home from './pages/Home'
import Footer from './components/footer'
import Login from './pages/login'

export default class App extends Component {
    render() {
        return (
        <div>
            {/*compinent in home pages*/}
            <Navigation/>   

            <Switch>
            <Route path = "/"  exact component = {Home}/> 
            
            <Route path = "/login"  component = {Login}/>

            </Switch>
        
             
            <Footer/>
           
        </div>                       
        )
    }
}
