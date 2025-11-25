import React from "react";
import ReactDOM, {createRoot} from 'react-dom/client'
import App from './App.jsx';

import './styles/main.scss'

//Bootstrap JS (Dropdown, Modal vb..calismasi icin

import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import {AuthProvider}  from "./context/AuthContext.jsx";


ReactDOM,createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
)