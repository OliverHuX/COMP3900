import React, { useState } from 'react'
import FoodList from './FoodList'
import FetchFunc from './fetchFunc';
import SelectInput from '@material-ui/core/Select/SelectInput';



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

const data1 = [
  { recipePhotos:['/assets/img/recipe1.png','/assets/img/recipe2.png'],isLiked:0,likes:10, title: 'AAA', introduction: 'AAAsimple decoration', timeDuration: '11', rateScore: 2 },
  { recipePhotos: ['/assets/img/recipe2.png','/assets/img/recipe1.png'],isLiked:1,likes:20, title: 'BBB', introduction: 'BBBsimple decoration', timeDuration: '20', rateScore: 3 },
  { recipePhotos: ['/assets/img/recipe3.png','/assets/img/recipe1.png'],isLiked:0,likes:100, title: 'CCC', introduction: 'CCCsimple decoration', timeDuration: '25', rateScore: 5 }, 
  { recipePhotos: ['/assets/img/recipe1.png','/assets/img/recipe3.png'],isLiked:0,likes:10, title: 'AAA', introduction: 'AAAsimple decoration', timeDuration: '15', rateScore: 2 },
  { recipePhotos:['/assets/img/recipe2.png'],isLiked:1,likes:20, title: 'BBB', introduction: 'BBBsimple decoration', timeDuration: '20', rateScore: 3 },
  { recipePhotos: ['/assets/img/recipe3.png'],isLiked:0,likes:100, title: 'CCC', introduction: 'CCCsimple decoration', timeDuration: '25', rateScore: 5 },
]
const JanpneseFood = () => {
  const [recipelist,setData] = useState([])
  const token = localStorage.getItem('token');
  React.useEffect(()=>{ 
    getInfo(token,setData)
  },[])
//   // console.log(data1[0])
  
  console.log('recipelist is  ',recipelist)
  console.log('hhhhh', recipelist['0'])
  const like = (i)=>{
      let d = [...recipelist];
      if(d[i].isLiked){
          d[i].isLiked = 0;
          d[i].likes--;
      }else{
          d[i].isLiked = 1;
          d[i].likes++;
      }
      
      React.useEffect(setData(d), [])
  }
  


   return (

        
        <h1>
          
            <h2 className='subtitle'>Japanese Food Recipe</h2>
            <p style={ { textAlign: 'center',fontSize:20 } }>simple decorationsimple decorationsimple decoration</p>
            <FoodList data={recipelist} like={like} />
        </h1>
    )
}

export default JanpneseFood
