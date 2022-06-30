import React, {useState, useEffect, useRef} from 'react';
import Button from './Components/Button.jsx';
import Modal, { ModalBody, ModalFooter, ModalHeader } from './Components/modal.jsx';
//import {useJsApiLoader, GoogleMap, Marker,  Autocomplete, DirectionsRenderer} from '@react-google-maps/api';
import './App.css';
//import {Box, ButtonGroup, HStack, IconButton,Input, SkeletonText, Text} from '@chakra-ui/react'
//const libraries = ['places'];

function App() {
  


 
  const [showModal, setShowModal] = useState(true);
  //const originRef = useRef()
  
  
  useEffect(() => {
    document.title = "Please click 'Accept' if you would like to take the customer"
  });
  const declineAlert = () => {
    alert("This ride was declined. Thank you!")
  }

  return (
  
       <div>
         <Modal
                show={showModal}
                setShow={setShowModal}
            // hideCloseButton
            >
                <ModalHeader>
                    <h2>There is a customer who needs a ride next to you. </h2>
                </ModalHeader>
                <ModalBody>
                    <p style={{ textAlign: 'justify' }}>
                    Please click 'Accept' if you would like to take the customer
                    </p>
                </ModalBody>
                <ModalFooter>
                    <Button classname="accept" onClick={() => setShowModal(false)}>
                        Accept
                    </Button>
                    <Button classname="decline" onClick={declineAlert}>
                        Decline
                    </Button>
                </ModalFooter>
            </Modal>
 
         </div>

      
    
      );
    }
    
export default App;