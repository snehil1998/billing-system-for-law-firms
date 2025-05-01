import './App.css';
import DisplayServices from "./pages/services/DisplayServices";
import NavBarComponent from './components/navbar/NavBar';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import DisplayClients from "./pages/clients/DisplayClients";
import DisplayCases from "./pages/cases/DisplayCases";
import DisplayAttorneys from "./pages/attorneys/DisplayAttorneys";
import DisplayDisbursements from "./pages/disbursements/DisplayDisbursements";
import {connect} from "react-redux";
import PropTypes from "prop-types";
import {getMessage} from "./redux/message/MessageSelectors";
import React, {useEffect, useState} from "react"
import AddService from './components/addServices/AddService';
import AddDisbursement from './components/addDisbursements/AddDisbursement';
import AddAttorney from './components/addAttorney/AddAttorney';
import AddServicePricing from './components/addServicePricing/AddServicePricing';
import DeleteServicePricing from './components/deleteServicePricing/DeleteServicePricing';
import AddClient from './components/addClients/AddClient';
import AddCase from './components/addCases/AddCase';

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
                <NavBarComponent/>
                {!!props.message && <div id={'message'} className={'dropdown-translation'} style={{border:'2px solid red',backgroundColor: 'white', color:'red', fontWeight:'bold'}}>
                    {props.message}
                </div>}
            </div>
            <div className={'table-container'}>
                <Routes>
                    <Route path='/' exact element={<DisplayServices />} />
                    <Route path='/attorneys/overview' element={<DisplayAttorneys />} />
                    <Route path='/attorneys/add-attorney' element={<AddAttorney />} />
                    <Route path='/attorneys/add-service-pricing' element={<AddServicePricing />} />
                    <Route path='/attorneys/delete-service-pricing' element={<DeleteServicePricing />} />
                    <Route path='/clients/overview' element={<DisplayClients />} />
                    <Route path='/clients/add-client' element={<AddClient />} />
                    <Route path='/cases/overview' element={<DisplayCases />} />
                    <Route path='/cases/add-case' element={<AddCase />} />
                    <Route path='/disbursements/overview' element={<DisplayDisbursements />} />
                    <Route path='/disbursements/add-disbursement' element={<AddDisbursement />} />
                    <Route path='/services/overview' element={<DisplayServices />} />
                    <Route path='/services/add-service' element={<AddService />} />
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
