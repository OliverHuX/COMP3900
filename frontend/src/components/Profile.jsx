import React from 'react';
// import Skeleton from '@material-ui/lab/Skeleton';
import Avatar from '@material-ui/core/Avatar';
import { useStyles } from './Style';
import Container from '@material-ui/core/Container';
import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Input from '@material-ui/core/Input';
import Grid from '@material-ui/core/Grid';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';

// function handleChange(e) {
//     setNickName(e.target)
// }

function update(nickName, gender, BOD) {
    console.log('nickname is ' + nickName + ' gender is ' + gender + ' BOD is ' + BOD)
}

export default function Profile () {
    const [nickName, setNickName] = React.useState('X');
    const [BOD, setBOD] = React.useState('2000-01-01');
    const [email, setEmail] = React.useState('12345678@gmail.com');
    const [avatar, setAvatar] = React.useState(require("../test.jpg"));
    const [gender, setGender] = React.useState(Number(-1));
    const [passWordOld, setPassWordOld] = React.useState('')
    const [passWordNew, setPassWordNew] = React.useState('')
    const classes = useStyles();

    const handleChange = (event) => {
        setNickName(event.target.value);
    };

    return (
        <Container className={classes.paper} maxWidth="xs">
            <Avatar className={classes.large} src={avatar} />

            {/* <FormControl>
                <InputLabel htmlFor="component-simple">Nick Name</InputLabel>
                <Input fullWidth='true' value={nickName} onChange={e => handleChange(e)} />
            </FormControl> */}
            <h2>{email}</h2>
            {/* <div className={classes.form}>
            </div> */}
            <form className={classes.form} noValidate>
                <Grid item xs={12} className={classes.form}>
                    <TextField
                        label="Nick Name"
                        // variant="filled"
                        style={{ margin: 8 }}
                        value={nickName}
                        fullWidth
                        margin="normal"
                        onChange={e => handleChange(e)}
                    />
                </Grid>
                
                <Grid item xs={12} className={classes.form}>
                    <TextField
                        id="date"
                        label="Birthday"
                        type="date"
                        style={{ marginLeft: 7, marginBottom: 4}}
                        fullWidth
                        defaultValue={BOD}
                        InputLabelProps={{
                            shrink: true,
                        }}
                        onChange={(e) => setBOD(e.target.value)}
                    />
                </Grid>
                
                <FormControl fullWidth={true} style={{ marginLeft: 7, marginBottom: 4}}>
                    <InputLabel>Gender</InputLabel>
                    <Select
                        label="Gender"
                        value={gender}
                        onChange={(e) => setGender(e.target.value)}
                    >
                        <MenuItem value={Number(-1)}>
                        <em>None</em>
                        </MenuItem>
                        <MenuItem value={Number(0)}>Male</MenuItem>
                        <MenuItem value={Number(1)}>Female</MenuItem>
                    </Select>
                </FormControl>
                
                <Button
                    name='signup'
                    fullWidth
                    variant="contained"
                    color="primary"
                    className={classes.submit}
                    onClick={() => update(nickName, gender, BOD)}
                >
                Save
                </Button>
            </form>

        </Container>
    )
}