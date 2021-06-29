import React, { Component } from 'react'
import { StyledHeader } from '../../components/StyledHeader';
import './index.css'

export default class Home extends Component {

      



    render() {
        return (
            
        <div>
            <StyledHeader/>

            <div className="container" id="most-popular">
                <div className="container" id="search-1" style={{marginTop: '70px',marginBottom: '67px',marginRight: '0px',marginLeft: '50px',}}>
                    
                    <button className="btn btn-primary" type="button" style={{height: '40px', width: '78.9px', marginTop: '-7px', background: 'rgb(102, 215, 215)'}}>
                        <i className="fa fa-search" style={{height: '0px', fontSize: '25px'}}>
                        </i>
                    </button>
                    <input type="search" style={{width: '600px',height: '40px',}} />
                </div>
                <div className="row" id="popular">
                    <div className="col-md-12" style={{marginTop: '20px', marginBottom: '39px', marginRight: '4px', marginLeft: '10px'}}>
                        <h1 style={{textAlign: 'center'}}>
                            Most popular are here!
                        </h1>
                    </div>
                </div>
            <div className="row">
                <div className="col-md-4">
                    <div className="card">

                    {/*here are only local photo uploaded*/}
                    <img src="assets/img/recipe1.png" alt="" className="card-img-top w-100 d-block" style={{height: '374px'}}  /> 
                    <div className="card-body">
                        <h4 className="card-title">
                            Chorizo &amp; mozzarella gnocchi bake
                            <br />
                        </h4>
                        <p className="card-text">
                            Upgrade cheesy tomato pasta with gnocchi, chorizo and mozzarella for a
                            comforting bake that makes an excellent midweek meal
                            <br />
                        </p>
                        <button className="btn btn-primary" type="button">
                            More details
                        </button>
                        </div>
                    </div>
             </div>
                <div className="col-md-4">
                    <div className="card">

                        <img src="assets/img/recipe2.png" alt="" className="card-img-top w-100 d-block" style={{height: '374px'}}  /> 
                        <div className="card-body">
                            <h4 className="card-title">
                                One-pan spaghetti with nduja, fennel &amp; olives
                                <br />
                            </h4>
                            <p className="card-text">
                                A spicy sausage pasta dish with a difference. Using the cooking water
                                helps the sauce cling to the pasta and gives the dish more body. A silky
                                smooth sauce, perfect pasta and one pan to wash!
                                <br />
                            </p>
                            <button className="btn btn-primary" type="button">
                                More details
                            </button>
                        </div>
                    </div>
                </div>
                <div className="col-md-4">
                    <div className="card">

                        <img src="assets/img/recipe3.png" alt="" className="card-img-top w-100 d-block" style={{height: '374px'}}  /> 
                        <div className="card-body">
                            <h4 className="card-title">
                                Easy pancakes
                                <br />
                            </h4>
                            <p className="card-text">
                                Learn a skill for life with our foolproof crêpe recipe that ensures perfect
                                pancakes every time – elaborate flip optional
                                <br />
                            </p>
                            <button className="btn btn-primary" type="button">
                                More details
                            </button>
                        </div>
                    </div>
                    <a href="###" style={{marginLeft: '0px', marginRight: '3px', fontSize: '15px', textAlign: 'left'}}>
                        Find more
                    </a>
                </div>
            </div>
                </div>
        </div>                       
        )
    }
}
