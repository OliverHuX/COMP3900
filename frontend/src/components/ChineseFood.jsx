import React from 'react'
import FoodList from './FoodList'

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
