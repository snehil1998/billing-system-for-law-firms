import React from 'react';
import {  Link } from "react-router-dom";

const NavBar = () => {
    return (
        <div>
            <li>
                <Link to="/">Home</Link>
            </li>
            <li>
                <Link to="/services">Services</Link>
            </li>
            <li>
                <Link to="/clients">Clients</Link>
            </li>
            <li>
                <Link to="/cases">Cases</Link>
            </li>
            <li>
                <Link to="/disbursements">Disbursements</Link>
            </li>
        </div>
    );
}

export default NavBar;