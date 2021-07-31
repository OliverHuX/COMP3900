import React, { useState } from 'react'
import './index.css'
import { Layout, Modal, Row, Col, Card, Carousel, Button, Input, Form, Upload, Select, message } from 'antd';
import { SaveOutlined, PrinterOutlined, StarFilled, StarOutlined, ClockCircleFilled, CheckCircleFilled, ToolFilled } from '@ant-design/icons';
import FoodList from '../../components/FoodList';
import StyledHeader from '../../components/StyledHeader'
import ChineseFood from '../../components/ChineseFood'
import { Switch, Route } from 'react-router-dom';
import Main from '../Main';
import axios from 'axios';
const FormData = require('form-data')

const { Content } = Layout;
const { Option } = Select;
const { TextArea } = Input;
const { Meta } = Card;
const RecipeDetail = () => {
    const contentStyle = {
        height: '400px',
        color: '#fff',
        lineHeight: '160px',
        textAlign: 'center',
        background: '#364d79',
    };
    const [rate, setRate] = useState(0)



    const data = [
        {img:'/assets/img/recipe1.png'},
        {img:'/assets/img/recipe2.png'},
        {img:'/assets/img/recipe3.png'},
    ]



    return <div className='recipedetail'>
        <div className='imgshow'>
            <div className='imgbox'>
                <Carousel autoplay effect="fade" arrows={true}>{
                
                    data.map((food)=>(
                    <div>       
                        <img src= {food.img} alt="" />
                    </div>                          
                    ))       
                }

                </Carousel>
            </div>
            <div>
                <Button className='save' style={ { color: '#fff', backgroundColor: '#be2a77', marginRight: 20, } } icon={ <SaveOutlined /> }>
                    Save Recipe
                </Button>
                <Button className='save' style={ { color: '#be2a77', borderColor: '#be2a77', } } icon={ <PrinterOutlined /> }>
                    Print
                </Button>
            </div>
        </div>
        <div className="recipeDec">
            <h2>Chorizo XXX AAA BBB</h2>
            <div>by <span className='author'>Marinane Turen</span></div>
            <div className='rate'>
                <span>Rating: 3 </span>
                <span>
                    <span style={ { marginRight: 10 } }>Rate:</span>
                    {
                        (new Array(5)).fill('').map((val, i) => {
                            if (i < rate) {
                                return <StarFilled onMouseOver={ () => setRate(i + 1) } style={ { color: '#f4bf1f' } } />
                            } else {
                                return <StarOutlined onMouseOver={ () => setRate(i + 1) } style={ { color: '#f4bf1f' } } />
                            }
                        }
                        )
                    }
                </span>
                <Button style={ { backgroundColor: '#be2a77', color: '#fff' } } size='small'>Confirm</Button>
            </div>
            <div><span className='author'>Marinane Turenxxxxxxxx</span></div>
            <div className='icons'>
                <div>
                    <ClockCircleFilled style={ { color: '#72aeb2', fontSize: 25 } } />
                    <div>
                        <h3 className='h3'>Prep：10mins</h3>
                        <h3 className='h3'>Cook：25mins</h3>
                    </div>
                </div>
                <div>
                    <CheckCircleFilled style={ { color: '#72aeb2', fontSize: 25 } } />
                    <div>
                        <h3 className='h3'>Easy</h3>
                    </div>
                </div>
                <div>
                    <ToolFilled style={ { color: '#72aeb2', fontSize: 25 } } />
                    <div>
                        <h3 className='h3'>Serves 6</h3>
                    </div>
                </div>
            </div>
            <h3 className='h3'>
                Upgrade cheesy tomato pasta with gnocchi,chorizo and mozzarella for a comforting bake that makes an excellent midweek meal
            </h3>
            <h3 className='h3'>Nutrition:Per serving</h3>
            <div className='nut'>
                <div>
                    <div>kcal</div>
                    <div>318</div>
                </div>
                <div>
                    <div>fat</div>
                    <div>13g</div>
                </div>
                <div>
                    <div>saturates</div>
                    <div>6g</div>
                </div>
                <div>
                    <div>carbs</div>
                    <div>36g</div>
                </div>
                <div>
                    <div>sugars</div>
                    <div>8g</div>
                </div>
            </div>
        </div>

    </div>

}
export default RecipeDetail;