import React, { Component } from 'react'



export default class Home_search extends Component {
    render() {
        return (
        <div>
            
            <div className="container" id="search-1" style={{marginTop: '70px',marginBottom: '67px',marginRight: '0px',marginLeft: '1230px',}}>
                <input type="search" style={{width: '500px',height: '40px',}} />
                <button className="btn btn-primary" type="button" style={{height: '40px', width: '78.9px', marginTop: '-3px', background: 'rgb(102, 215, 215)'}}>
                    <i className="fa fa-search" style={{height: '0px', fontSize: '25px'}}>
                    </i>
                </button>
            </div>
        </div>                       
        )
    }
}
