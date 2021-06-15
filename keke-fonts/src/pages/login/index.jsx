import React, { Component } from 'react'



export default class Login extends Component {
    render() {
        return (
            
            <section className="login-clean" style={{background: 'url("assets/img/backgroun.jpg") center / auto no-repeat, #ffffff', height: '1000px', paddingLeft: '0px', boxShadow: '0px 0px', transform: 'translate(0px)', opacity: 1}}>
                    <form method="post" style={{marginTop: '100px', opacity: 1, backgroundColor: 'rgba(255,255,255,0.75)'}}>
                        <h2 className="visually-hidden">Login Form</h2>
                        <div className="illustration"><i className="fas fa-birthday-cake"></i></div>
                        <div className="mb-3"><input type="email" className="form-control" name="email" placeholder="Email" /></div>
                        <div className="mb-3"><input type="password" className="form-control" name="password" placeholder="Password" /></div>
                        <div className="mb-3"><button className="btn btn-primary d-block w-100" type="submit" style={{background: 'rgb(102, 215, 215)'}}>Log In</button></div><a className="forgot" href="###">Forgot your email or password?</a>
                    </form>
            </section> 
              
        )
    }
}
