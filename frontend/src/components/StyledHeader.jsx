import React from 'react'
import { Layout, Menu, Dropdown, Input} from 'antd';
import {Link} from 'react-router-dom'
import { DownOutlined, LogoutOutlined } from '@ant-design/icons';
const { Header} = Layout;
const { Search } = Input;
const StyledHeader = () => {
    const menu = (
        <Menu>
            <Menu.Item>
                <Link to='/home/chinesefood'>Chinese food</Link>
            </Menu.Item>
            <Menu.Item>
                <a>Japaness food</a>
            </Menu.Item>
            <Menu.Item>
                <a>Noodle</a>
            </Menu.Item>
            <Menu.Item>
                <a>Cake</a>
            </Menu.Item>
            <Menu.Item>
                <a>Seafood</a>
            </Menu.Item>
            <Menu.Item>
                <a>View All</a>
            </Menu.Item>
        </Menu>
    );
    const menu2 = (
        <Menu>
            <Menu.Item>
              <Link to='/home/profile'>Customise Profile</Link>
                {/* <a>Customise Profile</a> */}
            </Menu.Item>
            <Menu.Item>
              <Link to='/home/password'>Change Password</Link>
                {/* <a>Customise Profile</a> */}
            </Menu.Item>
            <Menu.Item>
                <a>My Recipe</a>
            </Menu.Item>
            <Menu.Item>
                <a>My collection</a>
            </Menu.Item>
            <Menu.Item>
                <a>Upload Recipe</a>
            </Menu.Item>
        </Menu>
    );
    const onSearch = value => console.log(value);
  return (
    <Header style={ { backgroundColor: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'space-between' } } >
      <div style={ { display: 'flex', alignItems: 'center' } }>
        <div className="logo" > <Link to='/home'>YYDS</Link></div>
        <Dropdown overlay={ menu }>
          <span className="dropdown" onClick={ e => e.preventDefault() }>
            Recipe <DownOutlined />
          </span>
        </Dropdown>
        <Dropdown overlay={ menu2 }>
          <span className="dropdown" onClick={ e => e.preventDefault() }>
            My profile <DownOutlined />
          </span>
        </Dropdown>
        <Search style={ { width: 200 } } placeholder="input search text" onSearch={ onSearch } enterButton />
      </div>
      <LogoutOutlined style={ { float: 'right' } } />
    </Header>
  )
}
export default StyledHeader;