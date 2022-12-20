import './App.css';
import DisplayServices from "./Pages/Services/DisplayServices";
import MyImage from './logo.svg'

function App() {
  return (
    <div className="App">
      <header className="App-header" style={{display:'flex', flexDirection: 'row'}}>
          <div className={'logo-container'} style={{width:'20vw'}}>
              <img src={MyImage} alt="horse" style={{height:'10vh', textAlign:'left'}}/>
          </div>
          <div className={'navbar-container'} style={{width:'80vw', height:'100%', border:'1px solid black'}}>
              NavBar
          </div>
      </header>
        <div className="App-body">
        <DisplayServices />
        </div>
    </div>
  );
}

export default App;
