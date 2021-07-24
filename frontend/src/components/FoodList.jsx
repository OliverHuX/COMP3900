import React from 'react'
import {  Card } from 'antd';
import {Link} from 'react-router-dom'
import { FieldTimeOutlined, HeartOutlined, StarFilled } from '@ant-design/icons';
const { Meta } = Card;

const FoodList = (props) => {

    return (
        <div >
            {/* <h2 style={ { textAlign: 'center' } }><Link to='/home/foodlist' className='gomore'>Easy Dinners</Link></h2> */}
            <div className="foodlist">
                {
                    props.data.map((food,idx)=>(
                        <Card
                            key={idx}
                            hoverable
                            style={ { width: '30%',margin: '10px 1.5% 0' } }
                            cover={ <img style={ { height: 298 } } alt="example" src={food.img} /> }
                        >
                            <Meta title={food.name} description={food.dec} />
                            <div className='ope'>
                                <span style={ { display: 'flex', alignItems: 'center' } }><FieldTimeOutlined style={{color: '#197574'}} />{food.time}mins</span>
                                <HeartOutlined />
                                <span>
                                    {
                                        (new Array(5)).fill('').map((val,i)=>{
                                            if(i<food.rate){
                                                return <StarFilled style={{color:'#197574'}} />
                                            }else{
                                                return <StarFilled style={{color:'#aecbd1'}} />
                                            }
                                            

                                        }
                                        )
                                    }
                                </span>
                            </div>
                        </Card>
                    ))
                }
                

            </div>
        </div>
    )
}

export default FoodList
