import React, { useState } from 'react'
import {  Row, Col, Card } from 'antd';
import {Link} from 'react-router-dom'
import {  FieldTimeOutlined, HeartOutlined, StarOutlined } from '@ant-design/icons';
import FoodList from '../../components/FoodList';
const { Meta } = Card;
const Main = () => {
    return (<div>
            <Row>
                <Col span={ 13 } className='rec'>
                    <img style={ { width: '100%' } } src='/assets/img/recipe1.png' alt="" />
                    <div className='deco'>
                        <h2>Recipe Name</h2>
                        <p>simple decoration simple decoration</p>
                        <a className='gomore'>Get The Recipe</a>
                    </div>
                </Col>
                <Col span={ 10 } offset={1}>
                    <h2 style={ { textAlign: 'center' } }><Link to='/home/foodlist' className='gomore'>Easy Dinners</Link></h2>
                    <div className="dinnerList">
                        <Card
                            hoverable
                            style={ { width: '47%' } }
                            cover={ <img style={ { height: 200 } } alt="example" src="/assets/img/recipe1.png" /> }
                        >
                            <Meta title="AAA" description="simple decoration" />
                            <div className='ope'>
                                <span style={ { display: 'flex', alignItems: 'center' } }><FieldTimeOutlined />20mins</span>
                                <HeartOutlined />
                                <span>
                                    <StarOutlined /><StarOutlined /><StarOutlined /><StarOutlined />
                                </span>
                            </div>
                        </Card>
                        <Card
                            hoverable
                            style={ { width: '45%' } }
                            cover={ <img style={ { height: 200 } } alt="example" src="/assets/img/recipe1.png" /> }
                        >
                            <Meta title="BBB" description="simple decoration" />
                        </Card>
                        <Card
                            hoverable
                            style={ { width: '47%' } }
                            cover={ <img style={ { height: 200 } } alt="example" src="/assets/img/recipe1.png" /> }
                        >
                            <Meta title="AAA" description="simple decoration" />
                            <div className='ope'>
                                <span style={ { display: 'flex', alignItems: 'center' } }><FieldTimeOutlined />20mins</span>
                                <HeartOutlined />
                                <span>
                                    <StarOutlined /><StarOutlined /><StarOutlined /><StarOutlined />
                                </span>
                            </div>
                        </Card>
                        <Card
                            hoverable
                            style={ { width: '45%' } }
                            cover={ <img style={ { height: 200 } } alt="example" src="/assets/img/recipe1.png" /> }
                        >
                            <Meta title="BBB" description="simple decoration" />
                        </Card>
                        <Card
                            hoverable
                            style={ { width: '47%' } }
                            cover={ <img style={ { height: 200 } } alt="example" src="/assets/img/recipe1.png" /> }
                        >
                            <Meta title="AAA" description="simple decoration" />
                            <div className='ope'>
                                <span style={ { display: 'flex', alignItems: 'center' } }><FieldTimeOutlined />20mins</span>
                                <HeartOutlined />
                                <span>
                                    <StarOutlined /><StarOutlined /><StarOutlined /><StarOutlined />
                                </span>
                            </div>
                        </Card>
                        <Card
                            hoverable
                            style={ { width: '45%' } }
                            cover={ <img style={ { height: 200 } } alt="example" src="/assets/img/recipe1.png" /> }
                        >
                            <Meta title="BBB" description="simple decoration" />
                        </Card>
                    </div>
                </Col>
            </Row>
            <FoodList />
            <FoodList />
        </div>
    )
}

export default Main
