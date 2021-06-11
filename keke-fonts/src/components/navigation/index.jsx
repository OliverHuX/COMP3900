import React, { Component } from 'react'


export default class Navigation extends Component {
    render() {
        return (
            <nav className="navbar navbar-light navbar-expand-md navigation-clean-search" id="navigation "style={{backgroundImage: 'url('+"https://cdn.bootstrapstudio.io/placeholders/1400x800.png"+')'}} > 
                <div className="container">
                    <a className="navbar-brand" href="#">YYDS&nbsp;<strong>Recipes</strong><br /></a>
                    <button data-bs-toggle="collapse" className="navbar-toggler" data-bs-target="#navcol-1"><span className="visually-hidden">Toggle navigation</span><span className="navbar-toggler-icon"></span></button> 
                    <div className="collapse navbar-collapse" id="navcol-1"> 
                        <ul className="navbar-nav"> 
                        <li className="nav-item"></li> 
                        <li className="nav-item"><a className="nav-link" href="#">Contact us</a></li> 
                        <li className="nav-item"></li> 
                        </ul> 
                        <form className="me-auto search-form" target="_self"> 
                            <div className="d-flex align-items-center">
                             <label className="form-label d-flex mb-0" for="search-field"></label>
                            </div> 
                        </form>
                        <a href="#" style={{height: '24px', fontSize: '14px',marginTop: '16px',marginBottom: '0px'}} >join now</a>    
                        <a className="btn btn-light action-button" role="button" href="login.html">Login</a> 
                    </div> 
                </div> 
            </nav>
                           
        )
    }
}
