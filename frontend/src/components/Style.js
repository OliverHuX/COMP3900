import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
        '& > *': {
          margin: theme.spacing(1),
        },
    },
    small: {
      width: theme.spacing(3),
      height: theme.spacing(3),
    },
    large: {
      width: theme.spacing(30),
      height: theme.spacing(30),
      marginBottom: theme.spacing(3),
    },
    paper: {
        marginTop: theme.spacing(8),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    form: {
        width: '100%', // Fix IE 11 issue.
        marginTop: theme.spacing(2),
        marginBottom: theme.spacing(2),
    },
    submit: {
        margin: theme.spacing(3, 1, 2, 0.8),
    },
    done: {
        margin: theme.spacing(3, 0, 2),
    },
}));

export { useStyles }