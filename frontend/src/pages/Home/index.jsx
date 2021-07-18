import React, { useState } from 'react'
import './index.css'
import { Layout, Modal, Row, Col, Card, Button, Input, Form, Upload, Select } from 'antd';
import { UpCircleOutlined, FieldTimeOutlined, HeartOutlined, StarOutlined, UploadOutlined } from '@ant-design/icons';
import FoodList from '../../components/FoodList';
import StyledHeader from '../../components/StyledHeader'
import ChineseFood from '../../components/ChineseFood'
import { Switch, Route } from 'react-router-dom';
import Main from '../Main';
const { Content } = Layout;
const { Option } = Select;
const { TextArea } = Input;
const { Meta } = Card;
const Home = () => {
    const [isModalVisible, setIsModalVisible] = useState(false);

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
                        <Upload name="logo" action="/upload.do" listType="picture">
                            <Button icon={ <UploadOutlined /> }>Click to upload</Button>
                        </Upload>
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
                        <Button type="primary" htmlType="submit">
                            submit
                        </Button>
                    </Form.Item>

                </Form>
            </Modal>
        </div>
    </Layout>

}
export default Home;