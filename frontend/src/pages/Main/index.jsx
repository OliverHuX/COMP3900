import React, { useState } from 'react'
import { Row, Col, Card } from 'antd';
import { Link } from 'react-router-dom'
import { FieldTimeOutlined, HeartOutlined, HeartFilled,StarFilled } from '@ant-design/icons';
import FoodList from '../../components/FoodList';
const { Meta } = Card;
const data1 = [
    { img: '/assets/img/recipe1.png',isLiked:0,likes:10, name: 'AAA', dec: 'AAAsimple decoration', time: '15', rate: 2 },
    { img: '/assets/img/recipe2.png',isLiked:1,likes:20, name: 'BBB', dec: 'BBBsimple decoration', time: '20', rate: 3 },
    { img: '/assets/img/recipe3.png',isLiked:0,likes:100, name: 'CCC', dec: 'CCCsimple decoration', time: '25', rate: 5 },
]
const Main = () => {
    const [data,setData] = useState(data1)
    const like = (i)=>{
        let d = [...data];
        if(d[i].isLiked){
            d[i].isLiked = 0;
            d[i].likes--;
        }else{
            d[i].isLiked = 1;
            d[i].likes++;
        }
        setData(d)
    }
    return (<div>
        <Row>
            <Col span={ 13 } className='rec'>
                <img style={ { width: '100%' } } src='/assets/img/recipe1.png' alt="" />
                <div className='deco'>
                    <h2>Recipe Name</h2>
                    <p>simple decoration simple decoration</p>
                    <Link to='/home/recipedetail' className='gomore'>Get The Recipe</Link>
                </div>
            </Col>
            <Col span={ 10 } offset={ 1 }>
                <h2 style={ { textAlign: 'center' } }><Link to='/home/foodlist' className='gomore'>Easy Dinners</Link></h2>
                <div className="dinnerList">
                    {
                        data.map((food, idx) => (
                            <Card
                                key={ idx }
                                hoverable
                                style={ { width: '47%',marginBottom:10 } }
                                cover={ <img style={ { height: 200 } } alt="example" src={ food.img } /> }
                            >
                                <Meta title={ food.name } description={ food.dec } />
                                <div className='ope'>
                                    <span style={ { display: 'flex', alignItems: 'center' } }><FieldTimeOutlined style={ { color: '#197574' } } />{ food.time }mins</span>
                                    <span onClick={()=>like(idx)} style={ { display: 'flex', alignItems: 'center' } }>{food.isLiked?<HeartFilled style={{color: '#f00',marginRight:5}}/>:<HeartOutlined style={{marginRight:5}}/>}{food.likes}</span>
                                </div>
                                    <span>
                                        {
                                            (new Array(5)).fill('').map((val, i) => {
                                                if (i < food.rate) {
                                                    return <StarFilled style={ { color: '#197574' } } />
                                                } else {
                                                    return <StarFilled style={ { color: '#aecbd1' } } />
                                                }
                                            })
                                        }
                                    </span>
                            </Card>
                        ))
                    }
                </div>
            </Col>
        </Row>
        <h2 className='subtitle'>Title One Easy Dinners</h2>
        <FoodList data={ data } like={like} />
        <h2 className='subtitle'>Title Two Easy Dinners</h2>
        <FoodList data={ data } like={like} />
    </div>
    )
}

export default Main
