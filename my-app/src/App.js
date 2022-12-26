import './App.css';
import DisplayServices from "./Pages/Services/DisplayServices";
import NavBar from './Components/NavBar';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import DisplayClients from "./Pages/Clients/DisplayClients";

function App() {
  return (
    <div className="App">
      <header className="App-header" style={{display:'flex', flexDirection: 'row'}}>
          <div className={'navbar-container'} style={{height:'15vh'}}>
              <Router style={{display:'flex'}}>
                  <NavBar style={{width:'90vw'}}/>
                  <Routes>
                      <Route path='/' exact element={<DisplayServices />} />
                      <Route path='/attorneys' element={<DisplayServices />} />
                      <Route path='/clients' element={<DisplayClients />} />
                      <Route path='/cases' element={<DisplayServices />} />
                      <Route path='/disbursements' element={<DisplayServices />} />
                      <Route path='/services' element={<DisplayServices />} />
                  </Routes>
              </Router>
          </div>
      </header>
        <div className="App-body" />
    </div>
  );
}

export default App;
