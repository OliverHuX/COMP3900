import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Button from '@material-ui/core/Button';
import CameraIcon from '@material-ui/icons/PhotoCamera';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import CssBaseline from '@material-ui/core/CssBaseline';
import Grid from '@material-ui/core/Grid';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import PropTypes from 'prop-types';
import { useStyles } from './Style';

export default function RecipeCard ({ recipeId }) {

    const classes = useStyles();

    const handleEdit = () => {
        window.location.href = '/home/editrecipe/' + recipeId;
    }

    const handleView = () => {
        window.location.href = '/home/recipedetail/' + recipeId;
    }


    return (
        <Grid item key={recipeId} xs={12} sm={6} md={4}>
            <Card className={classes.card}>
            <CardMedia
                className={classes.cardMedia}
                image={require('../test.jpg')}
                title="Image title"
            />
            <CardContent className={classes.cardContent}>
                <Typography gutterBottom variant="h5" component="h2">
                Title
                </Typography>
                <Typography>
                This is a my recipe card.
                </Typography>
            </CardContent>
            <CardActions>
                <Button
                    size="small"
                    color="primary"
                    onClick={() => handleView()}
                >
                View
                </Button>
                <Button
                    size="small"
                    color="primary"
                    onClick={() => handleEdit()}
                >
                Edit
                </Button>
            </CardActions>
            </Card>
        </Grid>
    )
}

RecipeCard.propTypes = {
    recipeId: PropTypes.string
};