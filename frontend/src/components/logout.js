import FetchFunc from './fetchFunc';

export default function logout (token) {
  // const token = localStorage.getItem('token');
  token && FetchFunc('logout', 'GET', token, null)
    .then((data) => {
      if (data.status === 200) {
        console.log('Logged out!');
        localStorage.removeItem('token');
        window.location.href = '/';
      } else {
        data.json().then(result => {
          console.log(result.error);
        })
      }
    })
}
