import {
  Box,
  Button,
  ButtonGroup,
  Flex,
  HStack,
  IconButton,
  Input,
  SkeletonText,
  Text,
} from '@chakra-ui/react'
import { FaLocationArrow, FaTimes } from 'react-icons/fa'

import {
  useJsApiLoader,
  GoogleMap,
  Marker,
  Autocomplete,
  DirectionsRenderer,
} from '@react-google-maps/api'
import {useEffect, useRef, useState } from 'react'
import React from "react";
import axios from 'axios';


const center = { lat: 48.1987, lng: 16.3685 }
//48.1987° N, 16.3685° E
//const center = { lat: 48.8584, lng: 2.2945 }
const libraries = ['places'];

function App() {
  const [ip, setIP] = useState('');
  //const [postId, setPostId] = useState('');
 
  //creating function to load ip address from the API 
    async function getData() {
    const res = await axios.get('https://geolocation-db.com/json/');
    console.log(res.data);
    setIP(res.data.IPv4);
  }

useEffect(() => {
    // POST request using fetch inside useEffect React hook
    
    const requestOptions = {
        method: 'POST',
        headers: {'Content-Type': 'application/json' },
        body: JSON.stringify(getData())
    }; 
    fetch('https://reqres.in/api/posts', requestOptions) //link to API endpoint that Petar will generate
        .then(response => response.json())
       // .then(data => setPostId(data.id));
        .catch(err => {
         console.log("Error: " + err)
    })
// empty dependency array means this effect will only run once (like componentDidMount in classes)
}, [ip]);
      



  const { isLoaded } = useJsApiLoader({
   googleMapsApiKey: "AIzaSyBw1zPmfMUfiO2BozI3Z_YgEqg-bl8yTk0",
  // googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY,
   libraries,
  })

  const [map, setMap] = useState(/** @type google.maps.Map */ (null))
  const [directionsResponse, setDirectionsResponse] = useState(null)
  const [distance, setDistance] = useState('')
  const [duration, setDuration] = useState('')

  /** @type React.MutableRefObject<HTMLInputElement> */
  const originRef = useRef()
  /** @type React.MutableRefObject<HTMLInputElement> */
  const destiantionRef = useRef()

  if (!isLoaded) {
    return <SkeletonText />
  }

  async function calculateRoute() {
    if (originRef.current.value === '' || destiantionRef.current.value === '') {
      return
    }
    // eslint-disable-next-line no-undef
    const directionsService = new google.maps.DirectionsService()
    const results = await directionsService.route({
      origin: originRef.current.value,
      destination: destiantionRef.current.value,
      // eslint-disable-next-line no-undef
      travelMode: google.maps.TravelMode.DRIVING,
    })
    setDirectionsResponse(results)
    setDistance(results.routes[0].legs[0].distance.text)
    setDuration(results.routes[0].legs[0].duration.text)
  }

 // const handleRequest = () => {
 //  const response =  fetch ('', {
   //   method: 'POST',
    //  headers: { 'Content-Type': 'application/json' },
    //  body: JSON.stringify({ title: 'React POST Request Example' })})
     // const data = response.json();
    //  this.setState({ postId: data.id });
  ;

  function clearRoute() {
    setDirectionsResponse(null)
    setDistance('')
    setDuration('')
    originRef.current.value = ''
    destiantionRef.current.value = ''
  }

  return (
    <Flex
      position='relative'
      flexDirection='column'
      alignItems='center'
      h='100vh'
      w='100vw'
    >
      <Box position='absolute' left={0} top={0} h='100%' w='100%'>
    
       {
        process.env.REACT_APP_GOOGLE_MAPS_API_KEY
       }
       
        {/* Google Map Box */}
        <GoogleMap
          center={center}
          zoom={15}
          mapContainerStyle={{ width: '100%', height: '100%' }}
          options={{
            zoomControl: false,
            streetViewControl: false,
            mapTypeControl: false,
            fullscreenControl: false,
          }}
          onLoad={map => setMap(map)}
        >
          <Marker position={center} />
          {directionsResponse && (
            <DirectionsRenderer directions={directionsResponse} />
          )}
        </GoogleMap>
      </Box>
      <Box
        p={4}
        borderRadius='lg'
        m={4}
        bgColor='white'
        shadow='base'
        minW='container.md'
        zIndex='1'
      >
        <HStack spacing={2} justifyContent='space-between'>
          <Box flexGrow={1}>
            <Autocomplete>
              <Input type='text' placeholder='Origin' ref={originRef} />
            </Autocomplete>
          </Box>
          <Box flexGrow={1}>
            <Autocomplete>
              <Input
                type='text'
                placeholder='Destination'
                ref={destiantionRef}
              />
            </Autocomplete>
          </Box>

          <ButtonGroup>
            <Button colorScheme='blue' type='submit' onClick={calculateRoute}>
              Calculate Route
            </Button>
            <Button colorScheme='blue' type='submit' onClick={calculateRoute}>
              Request a ride
            </Button>
            <IconButton
              aria-label='center back'
              icon={<FaTimes />}
              onClick={clearRoute}
            />
          </ButtonGroup>
        </HStack>
        <HStack spacing={4} mt={4} justifyContent='space-between'>
          <Text>Distance: {distance} </Text>
          <Text>Duration: {duration} </Text>
          <IconButton
            aria-label='center back'
            icon={<FaLocationArrow />}
            isRound
            onClick={() => {
              map.panTo(center)
              map.setZoom(15)
            }}
          />
        </HStack>
      </Box>
    </Flex>
  )
}

export default App