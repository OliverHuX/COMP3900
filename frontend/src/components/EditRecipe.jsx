import React from 'react';
import CssBaseline from '@material-ui/core/CssBaseline';
import Paper from '@material-ui/core/Paper';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import FetchFunc from './fetchFunc';
import { useStyles } from './Style';

function getRecipe(token, recipeId, setTitle, setTime, setIntro, setIngre) {
    const result = FetchFunc('recipe/recipe_list?pageNum=1&pageSize=9&search=' + recipeId, 'GET', token, null);
    result.then(data => {
        if (data === 200) {
            data.json().then(res => {
                const recipe = res.recipe_lists[0];
                setTitle(recipe.title);
                setTime(recipe.timeDuration);
                setIntro(recipe.introduction);
                setIngre(recipe.ingredients);
            })
        }
    })
}

// function updateRecipe() {}

export default function EditRecipe () {
    const url = window.location.href.split('/')
    const recipeId = url[url.length - 1]
    const token = localStorage.getItem('token')
    const classes = useStyles();
    const [title, setTitle] = React.useState('');
    const [time, setTime] = React.useState('');
    const [intro, setIntro] = React.useState('');
    const [method, setMethod] = React.useState('');
    const [ingre, setIngre] = React.useState('');

    const handleTitle = (e) => {
        setTitle(e.target.value)
    }
    const handleTime = (e) => {
        setTime(e.target.value)
    }
    const handleIntro = (e) => {
        setIntro(e.target.value)
    }
    const handleIngre = (e) => {
        setIngre(e.target.value)
    }
    const handleMethod = (e) => {
        setMethod(e.target.value)
    }

    React.useEffect(() => {
        getRecipe(token, recipeId, setTitle, setTime, setIntro, setIngre);
    }, [])

    return (
        <React.Fragment>
        <CssBaseline />
        <main className={classes.layout}>
          <Paper className={classes.paper2}>
            <Typography component="h1" variant="h4" align="center">
              My Recipe
            </Typography>
            <React.Fragment>
                <Grid container spacing={4}>
                    <Grid item xs={12} sm={6}>
                    <TextField
                        required
                        label="Title"
                        value={title}
                        fullWidth
                        onChange={(e) => handleTitle(e)}
                    />
                    </Grid>
                    <Grid item xs={12} sm={6}>
                    <TextField
                        required
                        value={time}
                        label="Time"
                        fullWidth
                        type='number'
                        onChange={(e) => handleTime(e)}
                    />
                    </Grid>
                    <Grid item xs={12}>
                    <TextField
                        required
                        label="Introduction"
                        fullWidth
                        multiline
                        value={intro}
                        rows={4}
                        variant="outlined"
                        onChange={(e) => handleIntro(e)}
                    />
                    </Grid>
                    <Grid item xs={12}>
                    <TextField
                        required
                        label="Method"
                        fullWidth
                        multiline
                        value={method}
                        rows={4}
                        variant="outlined"
                        onChange={(e) => handleMethod(e)}
                    />
                    </Grid>
                    <Grid item xs={12}>
                    <TextField
                        required
                        label="Ingredients"
                        fullWidth
                        multiline
                        value={ingre}
                        rows={4}
                        variant="outlined"
                        onChange={(e) => handleIngre(e)}
                    />
                    </Grid>
                </Grid>
            <React.Fragment>
                <Button
                    variant="contained"
                    color="primary"
                    fullWidth
                    className={classes.button}
                    >
                    Save
                </Button>
            </React.Fragment>
            </React.Fragment>
          </Paper>
        </main>
      </React.Fragment>
    )
}