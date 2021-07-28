import React from 'react'
import FoodList from './FoodList'
import FetchFunc from './fetchFunc';



const result = FetchFunc('recipe/recipe_list?pageNum=1&pageSize=9', 'GET', null, null);
console.log(result)
result.then((data) => {
  console.log(data);
  if (data.status === 200) {
    data.json().then(res => {
      console.log(res.token);
      localStorage.setItem('token', result.token);
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




const data = [
    {img:'/assets/img/recipe1.png', name:'AAA',dec:'AAAsimple decoration',time:'15',rate:2},
    {img:'/assets/img/recipe2.png', name:'BBB',dec:'BBBsimple decoration',time:'20',rate:3},
    {img:'/assets/img/recipe3.png', name:'CCC',dec:'CCCsimple decoration',time:'25',rate:5},
    {img:'/assets/img/recipe1.png', name:'DDD',dec:'DDDsimple decoration',time:'30',rate:3},
    {img:'/assets/img/recipe2.png', name:'EEE',dec:'EEEsimple decoration',time:'35',rate:4},
]

const ChineseFood = () => {
    return (
        
        <h1>
            <h2 className='subtitle'>Chinese Food Recipe</h2>
            <p style={ { textAlign: 'center',fontSize:20 } }>simple decorationsimple decorationsimple decoration</p>
            <FoodList data={data}/>
        </h1>
    )
}

export default ChineseFood
