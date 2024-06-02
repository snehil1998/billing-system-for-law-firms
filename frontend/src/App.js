import './App.css';
import DisplayServices from "./Pages/Services/DisplayServices";
import NavBar from './Components/NavBar';
import {HashRouter as Router, Routes, Route} from 'react-router-dom';
import DisplayClients from "./Pages/Clients/DisplayClients";
import DisplayCases from "./Pages/Cases/DisplayCases";
import DisplayAttorneys from "./Pages/Attorneys/DisplayAttorneys";
import DisplayDisbursements from "./Pages/Disbursements/DisplayDisbursements";
import {connect} from "react-redux";
import PropTypes from "prop-types";
import {getMessage} from "./Redux/Message/MessageSelectors";
import React, {useEffect, useState} from "react"

function App(props) {
    const [ isMobile, setIsMobile ] = useState(window.innerWidth < 600)
    useEffect(() => {
        const setIsMobileValue = () => setIsMobile(window.innerWidth < 600);
        window.addEventListener("resize", setIsMobileValue);
        return () => window.removeEventListener("resize", setIsMobileValue);
      }, []);
  return (
    <div className="App">
        {isMobile ? <div style={{color:'white'}}>Please use the portrait mode the view application</div> :
        <div className="App-body">
        <Router>
            <div className={'navbar-container'}>
            <NavBar/>
                {!!props.message && <div id={'message'} className={'dropdown-translation'} style={{border:'2px solid red',backgroundColor: 'white', color:'red', fontWeight:'bold'}}>
                    {props.message}
                </div>}
            </div>
            <div className={'table-container'}>
                <Routes>
                    <Route path='/' exact element={<DisplayServices />} />
                    <Route path='/attorneys' element={<DisplayAttorneys />} />
                    <Route path='/clients' element={<DisplayClients />} />
                    <Route path='/cases' element={<DisplayCases />} />
                    <Route path='/disbursements' element={<DisplayDisbursements />} />
                    <Route path='/services' element={<DisplayServices />} />
                </Routes>
            </div>
        </Router>
    </div>}
    </div>
  );
}

App.propTypes = {
    message: PropTypes.string,
}

const mapStateToProps = state => {
    return {
        message: getMessage(state),
    }
}

export default connect(mapStateToProps, null)(App);
