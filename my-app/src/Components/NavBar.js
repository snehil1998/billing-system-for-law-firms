import React from 'react';
import {  Link } from "react-router-dom";
import MyImage from "../logo.jpg";

const NavBar = () => {
    return (
        <div style={{display: 'flex', height:'15vh'}}>
            <img src={MyImage} alt="logo" style={{height:'15vh', width:'10vw', textAlign:'left'}}/>
            <Link to="/attorneys" style={{width:'20%', paddingTop:'5vh', backgroundColor:'gray', color:'white',
                textDecoration:'none', border:'1px solid white'}}>ATTORNEYS</Link>
            <Link to="/clients" style={{width: '20%', paddingTop: '5vh', backgroundColor: 'gray', color:'white',
                textDecoration: 'none', border:'1px solid white'}}>CLIENTS</Link>
            <Link to="/cases" style={{width: '20%', paddingTop: '5vh', backgroundColor: 'gray', color:'white',
                textDecoration: 'none', border:'1px solid white'}}>CASES</Link>
            <Link to="/disbursements" style={{width: '20%', paddingTop: '5vh', backgroundColor: 'gray', color:'white',
                textDecoration: 'none', border:'1px solid white'}}>DISBURSEMENTS</Link>
            <Link to="/services" style={{width: '20%', paddingTop: '5vh', backgroundColor: 'gray', color:'white',
                textDecoration: 'none', border:'1px solid white'}}>SERVICES</Link>
        </div>
    );
}

export default NavBar;