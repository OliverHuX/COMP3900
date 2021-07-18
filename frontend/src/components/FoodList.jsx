import React from 'react'
import {  Card } from 'antd';
import {Link} from 'react-router-dom'
import { FieldTimeOutlined, HeartOutlined, StarOutlined } from '@ant-design/icons';
const { Meta } = Card;

const FoodList = () => {
    return (
        <div >
            <h2 style={ { textAlign: 'center' } }><Link to='/home/foodlist' className='gomore'>Easy Dinners</Link></h2>
            <div className="foodlist">
                <Card
                    hoverable
                    style={ { width: '30%' } }
                    cover={ <img style={ { height: 298 } } alt="example" src="/assets/img/recipe1.png" /> }
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
                    style={ { width: '30%' } }
                    cover={ <img style={ { height: 298 } } alt="example" src="/assets/img/recipe1.png" /> }
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
                    style={ { width: '30%' } }
                    cover={ <img style={ { height: 298 } } alt="example" src="/assets/img/recipe1.png" /> }
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

            </div>
        </div>
    )
}

export default FoodList
