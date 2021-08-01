import React from 'react';
import CssBaseline from '@material-ui/core/CssBaseline';
import Grid from '@material-ui/core/Grid';
import Container from '@material-ui/core/Container';
import RecipeCard from './RecipeCard';
import FetchFunc from './fetchFunc';
import { useStyles } from './Style';

// const cards = [1, 2, 3, 4, 5, 6, 7, 8, 9];

function getRecipe(token, userId, setRecipes, recipes) {
    const result = FetchFunc('recipe/recipe_list?pageNum=1&pageSize=9&search=' + userId, 'GET', token, null);
    result.then(data => {
        if (data === 200) {
            data.json().then(res => {
                const total = res.total
                for (var i = 0; i < total; i++) {
                    var info = {
                        recipeId: res.recipe_lists[i].recipeId,
                        intro: res.recipe_lists[i].introduction,
                        photo: res.recipe_lists[i].recipePhotos[0],
                        title: res.recipe_lists[i].title,
                    }
                    setRecipes(ele => [...ele, info]);
                }
                console.log(recipes)
            })
        }
    })
}

export default function MyRecipe() {

    const classes = useStyles();
    const url = window.location.href.split('/')
    const recipeId = url[url.length - 1]
    const [recipes, setRecipes] = React.useState([])
    console.log(recipeId)

    return (
        <React.Fragment>
        <CssBaseline />
        <main>
            <Container className={classes.cardGrid} maxWidth="md">
            <Grid container spacing={4}>
                {recipes.map((recipe) => (
                    <RecipeCard recipeId={recipe.recipeId} intro={recipe.intro} title={recipe.title} photo={recipe.photo} />
                ))}
            </Grid>
            </Container>
        </main>
        </React.Fragment>
    );
}
