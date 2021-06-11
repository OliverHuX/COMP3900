import React, { Component } from 'react'
import Navigation from './components/navigation'
import Home_search from './components/home_search'
import Popular_bar from './components/popular_bar'
import Footer from './components/footer'


export default class App extends Component {
    render() {
        return (
        <div>
            {/*compinent in home pages*/}
            <Navigation/>   
            <Home_search/>
            <Popular_bar/>
            <Footer/>
        </div>                       
        )
    }
}
