import './App.css';
import DisplayServices from "./Pages/Services/DisplayServices";
import NavBar from './Components/NavBar';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import DisplayClients from "./Pages/Clients/DisplayClients";
import DisplayCases from "./Pages/Cases/DisplayCases";
import DisplayAttorneys from "./Pages/Attorneys/DisplayAttorneys";
import DisplayDisbursements from "./Pages/Disbursements/DisplayDisbursements";


function App() {
  return (
    <div className="App" style={{position: 'absolute', height:'100%', width: '100%', backgroundColor:'maroon'}}>
      <header className="App-header" style={{display:'flex', flexDirection: 'row', width: '100%'}}>
          <div className={'navbar-container'} style={{height:'100%', width: '100%'}}>
              <Router>
                  <NavBar/>
                  <Routes>
                      <Route path='/' exact element={<DisplayServices />} />
                      <Route path='/attorneys' element={<DisplayAttorneys />} />
                      <Route path='/clients' element={<DisplayClients />} />
                      <Route path='/cases' element={<DisplayCases />} />
                      <Route path='/disbursements' element={<DisplayDisbursements />} />
                      <Route path='/services' element={<DisplayServices />} />
                  </Routes>
              </Router>
          </div>
      </header>
        <div className="App-body"/>
    </div>
  );
}

export default App;
