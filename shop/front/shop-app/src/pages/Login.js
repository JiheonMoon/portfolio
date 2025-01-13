import React, { useEffect, useState } from 'react';
import axios from 'axios';

const Login = () => {
    const [credentials, setCredentials] = useState({ username: '', password: '' });
    const [userDetails, setUserDetails] = useState(null);

    const getUserDetail = async () => {
        try {
            const response = await axios.get('http://localhost:9090/user/private/details', { withCredentials: true });
            setUserDetails(response.data);
        } catch (error) {
            if (error.response.status === 401) {
                alert('Session expired. You will be logged out.');
                await axios.post('http://localhost:9090/user/private/details')
                setUserDetails(null);
            }
            console.error('Failed to fetch user details:', error);
        }
    }

    const handleChange = (e) => {
        const { name, value } = e.target;
        setCredentials({ ...credentials, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:9090/user/signin', credentials, {
                withCredentials: true, // 쿠키를 포함한 요청
            });
            setUserDetails(response.data);
            alert(`Welcome, ${response.data.nickname}`);
        } catch (error) {
            alert(error.response.data);
        }
    };

    useEffect(() => {
        getUserDetail()
    }, [])

    return (
        <div>
            <h2>Login</h2>

            {userDetails ? (
                <div>
                    <h3>Welcome, {userDetails.nickname}!</h3>
                    <p>Email: {userDetails.email}</p>
                </div>
            ) : (
                <form onSubmit={handleSubmit}>
                    <input type="text" name="username" placeholder="Username" onChange={handleChange} required />
                    <input type="password" name="password" placeholder="Password" onChange={handleChange} required />
                    <button type="submit">Login</button>
                </form>
            )}
        </div>
    );
};

export default Login;

