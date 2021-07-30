import React, { useState } from 'react'
import FoodList from './FoodList'
import FetchFunc from './fetchFunc';


function getInfo() {
    const result = FetchFunc('recipe/recipe_list?pageNum=1&pageSize=9', 'GET', null, null);
    console.log(result)
    result.then((data) => {
      console.log(data);
      if (data.status === 200) {
        data.json().then(res => {
                    
        })
      }
      // if (data.code === 200) {
      //   data.json().then(res => {
      //     console.log(res)
      //     console.log(res.data)
      //     // console.log(res.err)
      //     if (res.code === 0) {
      //       history.push('./home')
      //     }
      //   })
      // }
    })
    .catch(err => console.error('Caught error: ', err))
}

const data1 = [
  { img: '/assets/img/recipe1.png',isLiked:0,likes:10, name: 'AAA', dec: 'AAAsimple decoration', time: '15', rate: 2 },
  { img: '/assets/img/recipe2.png',isLiked:1,likes:20, name: 'BBB', dec: 'BBBsimple decoration', time: '20', rate: 3 },
  { img: '/assets/img/recipe3.png',isLiked:0,likes:100, name: 'CCC', dec: 'CCCsimple decoration', time: '25', rate: 5 }, 
  { img: '/assets/img/recipe1.png',isLiked:0,likes:10, name: 'AAA', dec: 'AAAsimple decoration', time: '15', rate: 2 },
  { img: '/assets/img/recipe2.png',isLiked:1,likes:20, name: 'BBB', dec: 'BBBsimple decoration', time: '20', rate: 3 },
  { img: '/assets/img/recipe3.png',isLiked:0,likes:100, name: 'CCC', dec: 'CCCsimple decoration', time: '25', rate: 5 },
]
const ChineseFood = () => {

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

   getInfo()


   return (
        
        <h1>
          
            <h2 className='subtitle'>Chinese Food Recipe</h2>
            <p style={ { textAlign: 'center',fontSize:20 } }>simple decorationsimple decorationsimple decoration</p>
            <FoodList data={data} like={like} />
        </h1>
    )
}

export default ChineseFood
