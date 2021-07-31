import React, { useState, createElement } from 'react'
import './index.css'
import { Layout, Modal, Row, Col, Card, Carousel, Button, Input, Form, Upload, Select, message, Comment, Avatar, Tooltip, List } from 'antd';
import { SaveOutlined, PrinterOutlined, StarFilled, StarOutlined, ClockCircleFilled, CheckCircleFilled, ToolFilled } from '@ant-design/icons';
import FoodList from '../../components/FoodList';
import StyledHeader from '../../components/StyledHeader'
import ChineseFood from '../../components/ChineseFood'
import { Switch, Route } from 'react-router-dom';
import FetchFunc from '../../components/fetchFunc';
import moment from 'moment';
import Main from '../Main';
import axios from 'axios';
import Comments from '../../components/Comments';

const FormData = require('form-data')

const { Content } = Layout;
const { Option } = Select;
const { TextArea } = Input;
const { Meta } = Card;

function addComments(token) {
    const result = FetchFunc('recipe/comment', 'POST', token, )
} 
// function getRecipeDetail(){
//     const result = fetchFunc(`recipe/recipe_list?pageNum=1&pageSize=9&search=${props.}`)
// }



function getDetial(token,cur_recipeId,setPhotoList,setTitle, setrateScore,settimeDuration) {


      const result = FetchFunc(`recipe/recipe_list?recipeId=${cur_recipeId}`, 'GET', token,null);
      result.then((data) => {
        console.log('response is ',data);

        if (data.status === 200) {          
            
            data.json().then(res => {

                setrateScore(res.recipe_lists[0].rateScore)
                setPhotoList( res.recipe_lists[0].recipePhotos)
                settimeDuration(res.recipe_lists[0].timeDuration)
                setTitle(res.recipe_lists[0].title)
                console.log('I got the recipe ditails',res.recipe_lists)
                

            
            // console.log('res content', res);

            // console.log('res.recipe_lists  ',res.recipe_lists)
          })
          
        }
      })
}



const RecipeDetail = () => {
    const contentStyle = {
        height: '400px',
        color: '#fff',
        lineHeight: '160px',
        textAlign: 'center',
        background: '#364d79',
    };





    const url = window.location.href.split('/')
    const cur_recipeId = url[url.length - 1]
    const [rate, setRate] = useState(0)
    const [comments, setComments] = useState('');
    const [photolist, setPhotoList] = useState([]);
    const [title, setTitle] = useState('');
    const [rateScore, setrateScore] = useState('');
    const [timeDuration, settimeDuration] = useState('');
    // const [timeDuration, settimeDuration] = useState('');

    const data = [
        '/assets/img/recipe1.png',
        '/assets/img/recipe2.png',
        '/assets/img/recipe3.png',
    ]
     


    const token = localStorage.getItem('token');

    React.useEffect(()=>{ 
        getDetial(token,cur_recipeId,setPhotoList,setTitle,setrateScore,settimeDuration)
      },[])
      
    //   let d = [...photolist];
    //   console.log('sssssssssssssssssssss',photolist)
      

    const handleOnchange = (e) => {
        setComments(e.target.value)
    }

    const handleSubmitting = () => {
        setComments('')
    }


    return (
        <div>
            <div className='recipedetail'>
                <div className='imgshow'>
                    <div className='imgbox'>
                        <Carousel autoplay effect="fade" arrows={true}>{
                        
                        photolist.map((i)=>(
                            <div>       
                                <img src= {i} alt="" />
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
                    <h2>{title}</h2>
                    <div>by <span className='author'>Marinane Turen</span></div>
                    <div className='rate'>
                        <span>Rating:   {rateScore} </span>
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

                    <div className='icons'>
                        <div>
                            <ClockCircleFilled style={ { color: '#72aeb2', fontSize: 25 } } />
                            <div>
                                <h3 className='h3'>Time use：{timeDuration}</h3>
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
            <div>
                {/* {comments.length > 0 && <CommentList comments={comments} />} */}
                <Comment
                    avatar={
                        <Avatar
                        src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
                        alt="Han Solo"
                        />
                    }
                    content={
                        <div>
                            <Form.Item>
                                <TextArea rows={4} onChange={e => handleOnchange(e)} value={comments} />
                                </Form.Item>
                                <Form.Item>
                                <Button htmlType="submit" onClick={() => handleSubmitting()} type="primary">
                                    Add Comment
                                </Button>
                            </Form.Item>
                        </div>
                    }
                />
            </div>
            <Comments />
        </div>
    )
}

export default RecipeDetail;