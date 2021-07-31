import React, { useState } from 'react'
import './index.css'
import { Layout, Modal, Row, Col, Card, Button, Input, Form, Upload, Select, message } from 'antd';
import { UpCircleOutlined, FieldTimeOutlined, HeartOutlined, StarOutlined, UploadOutlined } from '@ant-design/icons';
import FoodList from '../../components/FoodList';
import StyledHeader from '../../components/StyledHeader'
import ChineseFood from '../../components/ChineseFood'
import RecipeDetail from '../RecipeDetail'
import Profile from '../../components/Profile';
import Password from '../../components/Password'
import JapaneseFood from '../../components/JapaneseFood'
import FetchFunc from '../../components/fetchFunc';
import { Switch, Route } from 'react-router-dom';
import Main from '../Main';
import axios from 'axios';


const FormData = require('form-data')
const { Content } = Layout;
const { Option } = Select;
const { TextArea } = Input;
const { Meta } = Card;






export default function Home  ()  {

    
    //const tags_res = getTags()

 
    //表单数据收集
    const token = localStorage.getItem('token')
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [fileList, setFileList] = useState() 
    const [title, setTitleInputs] = React.useState('');
    const [tags_list, setTags_listInputs] = React.useState();
    const [tags_select, setTags_select] = React.useState();
    const [introduction, setIntroductionInputs] = React.useState('');
    const [ingredients, setIngredientsInputs] = React.useState('');
    const [method, setMethodInputs] = React.useState('');

    const [timeDuration, setTimeDurationInputs] = React.useState('');

    //var imagedata = document.querySelector('input[type ="file"]').files[0];

    function getTags(setTagsInputs) {
            const result = FetchFunc('tags/tag_list', 'GET', null, null);
            var children = [];
            result.then((data) => {
            if (data.status === 200) {
                data.json().then(res => {
                    
                    for (let i = 0 ; i < res.tags.length; i++) {
                        children.push(<Option key={ res.tags[i] }>{ res.tags[i]  }</Option>);
                    }
                    //console.log(children)

                    setTags_listInputs(children)
                    
                })
            }
            })
            .catch(err => console.error('Caught error: ', err))


    }

    const showModal = () => {
        setIsModalVisible(true);
        getTags(setTags_listInputs)
    };

    const handleOk = () => {
        setIsModalVisible(false);
    };

    const handleCancel = () => {
        setIsModalVisible(false);
    };
    const onFinish = async (values) => {
        setIsModalVisible(false);

    };
    // var children = [];
    // for (let i = 10; i < 15; i++) {
    //     children.push(<Option key={ i.toString(36) + i }>{ i.toString(36) + i }</Option>);
    // }

    function handleChange(value) {
        setTags_select(value);
        console.log(`selected ${value}`);
    }



    const onChange = (e) => {
        // setFileList(newFileList);
        // console.log(fileList)
        console.log(e.target.files)
        setFileList(e.target.files)
        console.log(fileList)
    };
    const handleClick = () => {
        
        var FormData = require('form-data');
        var formData = new FormData();

        // const jsonData = JSON.stringify({
        //     title: 'test title',
        //     introduction: '12131',
        //     ingredients: '1321321',
        //     method: '2321321',
        //   });
        if(fileList!=undefined){
            for(let i=0;i<fileList.length;i++){
                formData.append('uploadPhotos', fileList[i]);
            }
        }
        //formData.append('uploadPhotos', fileList[0]);
        
       
        formData.append('jsonData',new Blob ([JSON.stringify({
            title: title,
            introduction: introduction,
            ingredients: ingredients,
            method: method,
            tags:tags_select,
            timeDuration: timeDuration

          })], {type:"application/json"}));

    
        if(fileList!=undefined& title != "" & introduction !=  "" & ingredients!= "" & method!= "" & tags_select!= "" & timeDuration!="" & timeDuration < 1000){
                axios.post(
                        'http://localhost:8080/recipe/postRecipe',
                        formData,
                        {
                            headers: {
                                "token": token, //Authorization
                                "Content-Type": "multipart/form-data",
                                "type": "formData"
                            },                    
                        }
                    )
                    .then(res => {
                        console.log(`Success` + res.data);
                        alert(' Congratulations, your recipe submit successfully!')  
                        
                    })
                    .catch(err => {
                        console.log(err);
                    })
                }
                 
        }

    
    return <Layout className="layout">
        
        <StyledHeader showModal={showModal} />
        <div style={ { width: 1200, margin: '0 auto' } }>
            <Content style={ { padding: '0 50px' } }>
                <Switch>
                    <Route path='/home' exact>
                        <UpCircleOutlined className='upload' onClick={ showModal } />
                        <Main />
                    </Route>
                    <Route path='/home/foodlist' exact>
                        <UpCircleOutlined className='upload' onClick={ showModal } />   
                        <FoodList />
                    </Route>
                    <Route path='/home/chinesefood' exact>
                        <UpCircleOutlined className='upload' onClick={ showModal } />
                        <ChineseFood /> 
                    </Route>
                    <Route path='/home/janpnesefood' exact>
                        <JapaneseFood/>
                    </Route>
                    <Route path='/home/recipedetail' exact>
                        <UpCircleOutlined className='upload' onClick={ showModal } />
                        <RecipeDetail /> 
                    </Route>
                    <Route path='/home/profile' exact>
                        <Profile />
                    </Route>
                    <Route path='/home/password' exact>
                        <Password />
                    </Route> 

                </Switch>

            </Content>
            <Modal footer={ null } title="Upload Recipe" visible={ isModalVisible } onOk={ handleOk } onCancel={ handleCancel }>
                <Form
                    name="basic"
                    labelCol={ { span: 8, } }
                    wrapperCol={ { span: 14 } }
                    initialValues={ { remember: true } }
                    onFinish={ onFinish }
                >
                    <Form.Item
                        label="upload Photo/video"
                        name="Upload"
                        valuePropName="fileList"
                        rules={ [{ required: true, message: 'Need choose at least one photo!' }] }
                    >
                        {/* <Upload
                            // fileList={fileList}
                            // onChange={ onChange }
                            // name="logo"
                            // action="/upload.do"
                            // listType="picture"
                            {...props}
                        > */}
                        <div>
                            <input
                                // icon={ <UploadOutlined /> }
                                onChange = { onChange }
                                type='file'
                                multiple='True'
                            />
                                {/* Click to upload */}
                        </div>
                        {/* </Upload> */}
                    </Form.Item>
                    <Form.Item
                        label="recipe title"
                        name="title"
                        hasFeedback
                        rules={ [{ required: true, message: 'Need input recipe title!' }] }
                        onChange={ (e) => setTitleInputs(e.target.value) }
                    >
                        <Input />
                    </Form.Item>
                    <Form.Item
                        label="Select a tag!"
                        name="tag"
                        hasFeedback
                        rules={ [{ required: true, message: 'Need input tags!' }] }
                             
                    >
                        <Select mode="tags" style={ { width: '100%' } } placeholder="Tags Mode" onChange={ handleChange }> 
                            {tags_list}
                        </Select>
                    </Form.Item>
                    <Form.Item
                        label="Time use(mins)"
                        name="time"
                        hasFeedback
                        rules={ [{  required: true,
                            message: 'Need input time-use',
                          }, {
                            max: 3,
                            message: 'Must less then 1000', },
                            {
                                message:'Onlyt number accepted',
                                pattern: /^[0-9]+$/ }] }
                        onChange={ (e) => setTimeDurationInputs(e.target.value) }
                    >
                        <Input />
                    </Form.Item>
                    <Form.Item
                        label="Introduction"
                        name="Introduction"
                        hasFeedback
                        rules={ [{ required: true, message: 'Need input Introduction!' }] }
                        onChange={ (e) => setIntroductionInputs(e.target.value) }
                        
                    >
                        <TextArea ></TextArea>
                    </Form.Item>
                    <Form.Item
                        label="Ingredients"
                        name="Ingredients"
                        hasFeedback
                        rules={ [{ required: true, message: 'Need input ingredients!' }] }
                        onChange={ (e) => setIngredientsInputs(e.target.value) }
                    >
                        
                        
                        <TextArea ></TextArea>
                    </Form.Item>
                    <Form.Item
                        label="Method"
                        name="Method"
                        hasFeedback
                        rules={ [{ required: true, message: 'Need input method!' }] }
                        onChange={ (e) => setMethodInputs(e.target.value) }
                    >
                        
                        
                        <TextArea ></TextArea>
                    </Form.Item>
                    
                    <Form.Item style={ { marginTop: 20 } } wrapperCol={ { offset: 6, span: 8 } }>
                        <Button
                            type="primary"
                            htmlType="submit"
                            onClick={() => handleClick() }
                        >
                            submit
                        </Button>
                    </Form.Item>

                </Form>
            </Modal>
        </div>
    </Layout>

}
// export default Home;