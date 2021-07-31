import React from 'react';
import Button from '@material-ui/core/Button';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import CssBaseline from '@material-ui/core/CssBaseline';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import RecipeCard from './RecipeCard';
import { useStyles } from './Style';

const cards = [1, 2, 3, 4, 5, 6, 7, 8, 9];

export default function MyRecipe() {

    const classes = useStyles();
    const url = window.location.href.split('/')
    const recipeId = url[url.length - 1]
    console.log(recipeId)

    const handleEdit = () => {
        window.location.href = '/home/editrecipe/' + recipeId;
    }

    const handleView = () => {
        window.location.href = '/home/recipedetail/' + recipeId;
    }


    return (
        <React.Fragment>
        <CssBaseline />
        <main>
            <Container className={classes.cardGrid} maxWidth="md">
            <Grid container spacing={4}>
                {cards.map((card) => (
                    <RecipeCard recipeId={card} />
                ))}
            </Grid>
            </Container>
        </main>
        </React.Fragment>
    );
}
