import './App.css';
import DisplayServices from "./Pages/Services/DisplayServices";
import MyImage from './logo.svg'

function App() {
  return (
    <div className="App">
      <header className="App-header">
          {/*<img src={MyImage} alt="horse" style={{height:'10vh', textAlign:'left'}}/>*/}
        Logo + NavBar
      </header>
        <div className="App-body">
        <DisplayServices />
        </div>
    </div>
  );
}

export default App;
