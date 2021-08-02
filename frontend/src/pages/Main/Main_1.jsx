import React, { useState } from 'react'
import { Row, Col, Card,Alert, Button, Space  } from 'antd';
import { NavLink as Link } from 'react-router-dom'
import { FieldTimeOutlined, HeartOutlined, HeartFilled,StarFilled } from '@ant-design/icons';
import FoodList from '../../components/FoodList';
import FetchFunc from '../../components/fetchFunc';
import { useHistory } from 'react-router-dom';
const { Meta } = Card;

const cur_recipeId = '53702903163a4556b664ef0cd9947662'
function getInfo(token,setData,setData1,setData2) {

    // post the request
    console.log('token now is ', token);
    const result = FetchFunc(`visitor/recipe_list`, 'GET', null, null);
    console.log(result)
    result.then((data) => {
      console.log(data);
      if (data.status === 200) {
        data.json().then(res => {
          // console.log('res content', res);
          // console.log('res.top_likes_list content', res.top_likes_list);
          // console.log('res.top_likes_list.list content', res.top_likes_list.list);
          // console.log('res.top_likes_list content', res.top_likes_list.list.recipeId);
          setData(data => [...data, res.top_rates_list.list])
          console.log('xxxxxxxxxxxxxxxxxxxxx', data);
        

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
const Main_1 = () => {
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
        
        <h2 className='subtitle'>Top rate recipe</h2>
            <FoodList data={ data } like={like} />
    </div>
    )
}

export default Main_1
