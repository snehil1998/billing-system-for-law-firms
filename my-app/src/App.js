import './App.css';
import DisplayServices from "./Pages/Services/DisplayServices";
import MyImage from './logo.jpg';
import NavBar from './Components/NavBar';
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';

function App() {
  return (
    <div className="App">
      <header className="App-header" style={{display:'flex', flexDirection: 'row'}}>
          <div className={'logo-container'} style={{width:'15vw', textAlign:'left'}}>
              <img src={MyImage} alt="horse" style={{height:'15vh', width:'10vw', textAlign:'left'}}/>
          </div>
          <div className={'navbar-container'} style={{width:'135vw', height:'15vh'}}>
              <Router>
                  <NavBar />
                  <Routes>
                      <Route path='/' exact component={DisplayServices} />
                      <Route path='/services' component={DisplayServices} />
                      <Route path='/clients' component={DisplayServices} />
                      <Route path='/cases' component={DisplayServices} />
                      <Route path='/disbursements' component={DisplayServices} />
                  </Routes>
              </Router>
          </div>
      </header>
        <div className="App-body">
        <DisplayServices />
        </div>
    </div>
  );
}

export default App;
