import React from 'react';
import { useHistory } from 'react-router-dom';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { myStyles } from './Login.style';
import FetchFunc from '../../components/fetchFunc';
import StyledHeader from '../../components/StyledHeader'
import { TextPopup } from '../../components/TextPopup';

function signin(email, password, history) {
  // history.push('./home')   // 跳过登录， 测试
  // return;         // 跳过登录 测试
  // console.log('incomplete' + email + password);
  // const path = 'login'
  const payload = JSON.stringify({
    email: email,
    password: password
  });
  const result = FetchFunc('login', 'POST', null, payload);
  console.log(result)
  result.then((data) => {
    console.log(data);
    if (data.status === 200) {
      data.json().then(res => {
        console.log(res.token);
        localStorage.setItem('token', result.token);
        history.push('./home')
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

export default function SignIn() {
  const classes = myStyles();
  const [email, setEmailInputs] = React.useState('');
  const [passWord, setPasswordInputs] = React.useState('');
  const [errorMsg, setErrorMsg] = React.useState('');
  const [error, setError] = React.useState(false);
  const history = useHistory();
  return (
    // <div className={classes.size}>
    <React.Fragment>
      <CssBaseline />
      <StyledHeader/> 
      <main>
        <Container component="main" maxWidth="xs" className={ classes.backg }>
          {/* <img src='https://coolwallpapers.me/th700/3056229-cooking_delicious-food_dining_eat_food_fusion-cuisine_morning-bread_platter_restaurant.jpg'/> */ }
          <div className={ classes.paper }>
            <Avatar className={ classes.avatar }>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Sign in
            </Typography>
            <form className={ classes.form } noValidate>
              <TextField
                variant="outlined"
                margin="normal"
                required
                fullWidth
                id="email"
                label="Email Address"
                name="email"
                autoComplete="email"
                autoFocus
                onChange={ (e) => setEmailInputs(e.target.value) }
              />
              <TextField
                variant="outlined"
                margin="normal"
                required
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
                autoComplete="current-password"
                onChange={ (e) => setPasswordInputs(e.target.value) }
              />
              <FormControlLabel
                control={ <Checkbox value="remember" color="primary" /> }
                label="Remember me"
              />
              <Button
                fullWidth
                variant="contained"
                color="primary"
                className={ classes.submit }
                onClick={ () => signin(email, passWord, history) }
              >
                Sign In
              </Button>
              <TextPopup
                open={ error }
                setOpen={ setError }
                title={ errorMsg }
                handleOnClick={ () => setError(false) }
              />
              <Grid container>
                <Grid item className={ classes.marginBtm }>
                  <Link href="/register" variant="body2">
                    { "Don't have an account? Sign Up" }
                  </Link>
                </Grid>
              </Grid>
            </form>
          </div>
        </Container>
      </main>
    </React.Fragment>
    // </div>
  );
}
