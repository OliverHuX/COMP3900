import React, { useState } from 'react'
import './index.css'
import { Layout, Modal, Row, Col, Card, Button, Input, Form, Upload, Select, message } from 'antd';
import { UpCircleOutlined, FieldTimeOutlined, HeartOutlined, StarOutlined, UploadOutlined } from '@ant-design/icons';
import FoodList from '../../components/FoodList';
import StyledHeader from '../../components/StyledHeader'
import ChineseFood from '../../components/ChineseFood'
import RecipeDetail from '../RecipeDetail'
import Profile from '../../components/Profile';
import { Switch, Route } from 'react-router-dom';
import Main from '../Main';
import axios from 'axios';
const FormData = require('form-data')


const { Content } = Layout;
const { Option } = Select;
const { TextArea } = Input;
const { Meta } = Card;
const Home = () => {
    const token = localStorage.getItem('token')
    const [fileList, setFileList] = useState()
    const [isModalVisible, setIsModalVisible] = useState(false);

    //var imagedata = document.querySelector('input[type ="file"]').files[0];

    

    const showModal = () => {
        setIsModalVisible(true);
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
    const children = [];
    for (let i = 10; i < 15; i++) {
        children.push(<Option key={ i.toString(36) + i }>{ i.toString(36) + i }</Option>);
    }

    function handleChange(value) {
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
        console.log(fileList['length'])
        var FormData = require('form-data');
        var formData = new FormData();

        // const jsonData = JSON.stringify({
        //     title: 'test title',
        //     introduction: '12131',
        //     ingredients: '1321321',
        //     method: '2321321',
        //   });
        formData.append('uploadPhotos', fileList[0]);
        // formData.append('jsonData',jsonData );
       
      formData.append('jsonData',new Blob ([JSON.stringify({
            title: 'test title',
            introduction: '12131',
            ingredients: '1321321',
            method: '2321321',
          })], {type:"application/json"}));

        formData.forEach((value, key) => {
            console.log(`key ${key}: value ${value}`);
       })
       console.log(formData.get('uploadPhotos'))
       console.log(formData.get('jsonData'))
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
        })
        .catch(err => {
            console.log(err);
        })
    }

    return <Layout className="layout">
        <UpCircleOutlined className='upload' onClick={ showModal } />
        <StyledHeader />
        <div style={ { width: 1200, margin: '0 auto' } }>
            <Content style={ { padding: '0 50px' } }>
                <Switch>
                    <Route path='/home' exact>
                        <Main />
                    </Route>
                    <Route path='/home/foodlist' exact>
                        <FoodList />
                    </Route>
                    <Route path='/home/chinesefood' exact>
                        <ChineseFood /> 
                    </Route>
                    <Route path='/home/recipedetail' exact>
                        <RecipeDetail /> 
                    </Route>
                    <Route path='/home/profile' exact>
                        <Profile />
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
                        rules={ [{ required: true, message: 'recipe title!' }] }
                    >
                        <Input />
                    </Form.Item>
                    <Form.Item
                        label="tag"
                        name="tag"
                        hasFeedback
                        rules={ [{ required: true, message: 'recipe title!' }] }
                    >
                        <Select mode="tags" style={ { width: '100%' } } placeholder="Tags Mode" onChange={ handleChange }>
                            { children }
                        </Select>
                    </Form.Item>
                    <Form.Item
                        label="time use"
                        name="time"
                        hasFeedback
                        rules={ [{ required: true, message: 'recipe title!' }] }
                    >
                        <Input />
                    </Form.Item>
                    <Form.Item
                        label="material"
                        name="time"
                        hasFeedback
                        rules={ [{ required: true, message: 'recipe title!' }] }
                    >
                        <Input />
                    </Form.Item>
                    <Form.Item
                        label="decoration"
                        name="time"
                        hasFeedback
                        rules={ [{ required: true, message: 'recipe title!' }] }
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
export default Home;