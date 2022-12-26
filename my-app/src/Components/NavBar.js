import React from 'react';
import {  Link } from "react-router-dom";

const NavBar = () => {
    return (
        <div style={{display: 'flex', height:'14.7vh'}}>
            <Link to="/" style={{width:'20%', paddingTop:'5vh', backgroundColor:'gray', color:'white',
                textDecoration:'none', border:'1px solid white'}}>HOME</Link>
            <Link to="/services" style={{width: '20%', paddingTop: '5vh', backgroundColor: 'gray', color:'white',
                textDecoration: 'none', border:'1px solid white'}}>SERVICES</Link>
            <Link to="/clients" style={{width: '20%', paddingTop: '5vh', backgroundColor: 'gray', color:'white',
                textDecoration: 'none', border:'1px solid white'}}>CLIENTS</Link>
            <Link to="/cases" style={{width: '20%', paddingTop: '5vh', backgroundColor: 'gray', color:'white',
                textDecoration: 'none', border:'1px solid white'}}>CASES</Link>
            <Link to="/disbursements" style={{width: '20%', paddingTop: '5vh', backgroundColor: 'gray', color:'white',
                textDecoration: 'none', border:'1px solid white'}}>DISBURSEMENTS</Link>
        </div>
    );
}

export default NavBar;