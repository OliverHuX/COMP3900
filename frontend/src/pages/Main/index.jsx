import React, { useState } from 'react'
import { Row, Col, Card } from 'antd';
import { NavLink as Link } from 'react-router-dom'
import { FieldTimeOutlined, HeartOutlined, HeartFilled,StarFilled } from '@ant-design/icons';
import FoodList from '../../components/FoodList';
import FetchFunc from '../../components/fetchFunc';
const { Meta } = Card;

const cur_recipeId = '53702903163a4556b664ef0cd9947662'
function getInfo(token,setData) {

    // post the request
    console.log(token);
    const result = FetchFunc(`recipe/recipe_list?pageNum=1&pageSize=9&search=Chinese`, 'GET', token, null);
    console.log(result)
    result.then((data) => {
      console.log(data);
      if (data.status === 200) {
        data.json().then(res => {
          
          setData(data => [...data, res.recipe_lists])
        
          // console.log('res content', res);

          // console.log('res.recipe_lists  ',res.recipe_lists)
        })
      }
    })
    .catch(err => console.error('Caught error: ', err))
}




// const data1 = [
//     { recipePhotos:['/assets/img/recipe1.png','/assets/img/recipe2.png'],isLiked:0,likes:10, title: 'AAA', introduction: 'AAAsimple decoration', timeDuration: '11', rateScore: 2 },
//     { recipePhotos: ['/assets/img/recipe2.png','/assets/img/recipe1.png'],isLiked:1,likes:20, title: 'BBB', introduction: 'BBBsimple decoration', timeDuration: '20', rateScore: 3 },
//     { recipePhotos: ['/assets/img/recipe3.png','/assets/img/recipe1.png'],isLiked:0,likes:100, title: 'CCC', introduction: 'CCCsimple decoration', timeDuration: '25', rateScore: 5 }, 
//     { recipePhotos: ['/assets/img/recipe1.png','/assets/img/recipe3.png'],isLiked:0,likes:10, title: 'AAA', introduction: 'AAAsimple decoration', timeDuration: '15', rateScore: 2 },
//   ]
const Main = () => {

    const [data,setData] = useState([])
    const token = localStorage.getItem('token');
    React.useEffect(()=>{ 
      getInfo(token,setData)
    },[])

    const like = (i)=>{
        let d = [...data];
        // console.log('xxxxxxxxxxx',d[0][0].likes)
    
        var recipeId = d[0][i].recipeId
        
        if(d[0][i].isLiked){
            d[0][i].isLiked = 0;
            d[0][i].likes--;
            console.log('recipe ID is :', d[0][i].recipeId)
            
            recipeId = d[0][i].recipeId
    
            const payload = JSON.stringify({
              recipeId:recipeId
            });
            const result = FetchFunc(`recipe/unlike`, 'POST', token, payload);
            console.log(result)
            result.then((data) => {
              console.log('mypost unlike data is',data);
              
              if (data.status === 200) {
                console.log('post unLike success')
              }
            })
        }else{
            d[0][i].isLiked = 1;
            d[0][i].likes++;
            console.log('recipe ID is :', d[0][i].recipeId)
            
            recipeId = d[0][i].recipeId
    
            var payload2 = JSON.stringify({
              recipeId:recipeId
            });
            const result = FetchFunc(`recipe/like`, 'POST', token, payload2);
            console.log(result)
            result.then((data) => {
              console.log('mypost data is',data);
              if (data.status === 200) {
                console.log('post Like success')
              }
            })

    
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
                    
                    <Link to={ '/home/recipedetail/' + cur_recipeId} className='gomore'>Get The Recipe
                    </Link>
                </div>
            </Col>
            <Col span={ 10 } offset={ 1 }>
                <h2 style={ { textAlign: 'center' } }><Link to='/home/foodlist' className='gomore'>Easy Dinners</Link></h2>
                <div className="dinnerList">
                    {
                        data.map((foods) => (
                            foods.map((food, idx) => (
                                        <Card
                                            key={ idx }
                                            hoverable
                                            style={ { width: '47%',marginBottom:10 } }
                                            cover={ <img style={ { height: 200 } } alt="example" src={ food.recipePhotos[0]} /> }
                                        >
                                            <Meta title={ food.title } description={ food.introduction } />
                                            <div className='ope'>
                                                <span style={ { display: 'flex', alignItems: 'center' } }><FieldTimeOutlined style={ { color: '#197574' } } />{ food.timeDuration }mins</span>
                                                <span onClick={()=>like(idx)} style={ { display: 'flex', alignItems: 'center' } }>{food.isLiked?<HeartFilled style={{color: '#f00',marginRight:5}}/>:<HeartOutlined style={{marginRight:5}}/>}{food.likes}</span>
                                            </div>
                                                <span>
                                                    {
                                                        (new Array(5)).fill('').map((val, i) => {
                                                            if (i < food.rateScore) {
                                                                return <StarFilled style={ { color: '#197574' } } />
                                                            } else {
                                                                return <StarFilled style={ { color: '#aecbd1' } } />
                                                            }
                                                        })
                                                    }
                                                </span>
                                        </Card>
                            ))
                        ))
                    }
                </div>
            </Col>
        </Row>
        <h2 className='subtitle'>Title One Easy Dinners</h2>
        <FoodList data={ data } like={like} />

        <h2 className='subtitle'>Title Two Easy Dinners</h2>
            <FoodList data={data} like={like} />
    </div>
    )
}

export default Main
