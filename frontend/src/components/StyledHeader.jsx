import React from 'react'
import { Layout, Menu, Dropdown, Input} from 'antd';
import {Link} from 'react-router-dom'
import { DownOutlined, LogoutOutlined } from '@ant-design/icons';
import FetchFunc from './fetchFunc';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import logout from './logout';
import { useHistory } from 'react-router-dom';


const { Header} = Layout;
const { Search } = Input;

// function logout(token) {
//   const result = FetchFunc('logout', 'GET', token, null);
//   result.then(data => {
//     if (data === 200) {
//       alert('log out successfully')
//       localStorage.clear()
//       window.location.href = '/'
//     }
//   })
// }

const Searchresult = (props) => {
    const [userId, setId] = React.useState('123')
    const menu = (
        <Menu>
            <Menu.Item>
                <Link to='/home/chinesefood'>Chinese food</Link>
            </Menu.Item>
            <Menu.Item>
          
                <Link to='/home/janpnesefood'>Japanese food</Link>
            </Menu.Item>
            <Menu.Item>
                <a>Noodle</a>
            </Menu.Item>
            <Menu.Item>
              <Link to='/home/cake'>Cake</Link>
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
                <Link to={'/home/myrecipe/' + userId}>My Recipe</Link>
            </Menu.Item>
            <Menu.Item>
              
                <Link to={'/home/mylikes/'}>My Likes</Link>   
            </Menu.Item>
            <Menu.Item>
                <a onClick = {props.showModal}>Upload Recipe</a>
            </Menu.Item>
        </Menu>
    );



    //                search 发送请求
    const history = useHistory()
    const onSearch = value => {

          
          
      
          console.log(value);

          // axios.get('http://localhost:8080/recipe/recipe_list?pageNum=1&pageSize=9&search=${value}').then(
          //   response =>{console.log('success',response.data);},
          //   error => {console.log('fail',error);}
          // )
    
      // post the request
        const result = FetchFunc(`recipe/recipe_list?pageNum=1&pageSize=9&search=${value}`, 'GET', token, null);
        console.log(result)
        result.then((data) => {
          console.log(data);
          if (data.status === 200) {
            data.json().then(res => {

              console.log('request success');
              // history.push('/home/searchresult/' + value)
              window.location.href = '/home/searchresult/' + value;
            })
          }
        })
        .catch(err => console.error('Caught error: ', err))
      
 
    }
    const token = localStorage.getItem('token')
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
      {token && <ExitToAppIcon style={ { float: 'right' , cursor: 'pointer' } } onClick={() => logout(token)}/>}
    </Header>
  )
}
export default Searchresult;