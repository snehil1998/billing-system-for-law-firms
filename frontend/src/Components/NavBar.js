import React from 'react';
import {  Link } from "react-router-dom";
import MyImage from "../logo.jpg";
import './NavBar.css';

const NavBar = () => {
    return (
        <div className={'navbars'} style={{display: 'flex', width: '100%'}}>
            <img className={'navbar-logo'} src={MyImage} alt="logo"/>
            <Link to="/attorneys" id={'navbar-attorneys'} style={{justifyContent: 'center', alignItems:'center', display:'flex', backgroundColor:'gray', color:'white',
                textDecoration:'none', border:'1px solid white'}}>ATTORNEYS</Link>
            <Link to="/clients" id={'navbar-clients'} style={{justifyContent: 'center', alignItems:'center', display:'flex', backgroundColor: 'gray', color:'white',
                textDecoration: 'none', border:'1px solid white'}}>CLIENTS</Link>
            <Link to="/cases" id={'navbar-cases'} style={{justifyContent: 'center', alignItems:'center', display:'flex', backgroundColor: 'gray', color:'white',
                textDecoration: 'none', border:'1px solid white'}}>CASES</Link>
            <Link to="/disbursements" id={'navbar-disbursements'} style={{justifyContent: 'center', alignItems:'center', display:'flex', backgroundColor: 'gray', color:'white',
                textDecoration: 'none', border:'1px solid white'}}>DISBURSEMENTS</Link>
            <Link to="/services" id={'navbar-services'} style={{justifyContent: 'center', alignItems:'center', display:'flex', backgroundColor: 'gray', color:'white',
                textDecoration: 'none', border:'1px solid white'}}>SERVICES</Link>
        </div>
    );
}

export default NavBar;