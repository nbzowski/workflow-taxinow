//import { green } from "@mui/material/colors";
import { useState } from "react";
import { FaBluetooth, FaStar } from "react-icons/fa";
import './App.css';
//import Rating from '@mui/material/Rating';

const colors = {
  orange: "#FFBA5A",
  grey: "#a9a9a9"
};

function App() {
const [currentValue, setCurrentValue] = useState("0");
const [hoverValue, setHoverValue] = useState(undefined);
const stars = Array(5).fill(0)
const rating = setCurrentValue.currentValue
const [userId, setuserId] = useState('35')
let count = hoverValue

const handleClick = value =>{
  setCurrentValue(value)
 
}

const handleMouseOver = newHoverValue => {
  setCurrentValue(newHoverValue)
  count= newHoverValue
  console.log(count)
}

const handleMouseLeave = () => {
  setHoverValue(undefined)
}


const sendRequest = async () => {
  //console.log("SDASDAS");
  const form = { count, userId };
  //console.log(form.originRef.value);  
  await
   // fetch('http://localhost:8090/api/ratings', {
    fetch('http://taxinowapigateway-env-1.eba-bhfmxdq9.eu-central-1.elasticbeanstalk.com/api/ratings', {  
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      mode: 'no-cors',
      body: new URLSearchParams(form),
    }).then((response) => console.log(response)).catch(error => console.log(error))
}


return (
  <div style={styles.container}>
    <h2> Would you like to rate your driver? </h2>
    <div style={styles.stars}>
      {stars.map((_, index) => {
        return (
          <FaStar
            key={index}
            size={24}
            onClick={() => handleClick(index + 1)}
            onMouseOver={() => handleMouseOver(index + 1)}
            onMouseLeave={handleMouseLeave}
            
            color={(hoverValue || currentValue) > index ? colors.orange : colors.grey}
            style={{
              marginRight: 10,
              cursor: "pointer"
            }}
          />
        )
      })}
    </div>
    <textarea
      placeholder="Please tell us more about your experience"
      style={styles.textarea}
    />

    <button style={styles.button} onClick={sendRequest}  >
      Submit
    </button>
    <button
      style={styles.button}  >
      Cancel
    </button>
  </div>
);
};


const styles = {
container: {
  display: "flex",
  flexDirection: "column",
  alignItems: "center"
},
stars: {
  display: "flex",
  flexDirection: "row",
},
textarea: {
  border: "1px solid #a9a9a9",
  borderRadius: 5,
  padding: 10,
  margin: "20px 0",
  minHeight: 100,
  width: 300
},
button: {
  border: "1px solid #a9a9a9",
  borderRadius: 5,
  width: 300,
  padding: 10,
}

};


export default App;