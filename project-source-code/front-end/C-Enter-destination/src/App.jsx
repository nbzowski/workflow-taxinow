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
import { useRef, useState } from 'react'
import React from "react";
import Geocode from "react-geocode";
import { useEffect } from 'react';
import {Navigate} from 'react-router-dom'

const center = { lat: 48.1987, lng: 16.3685 }
const libraries = ['places'];


function App() {
  const { isLoaded } = useJsApiLoader({
    googleMapsApiKey: "AIzaSyBw1zPmfMUfiO2BozI3Z_YgEqg-bl8yTk0",
    libraries,
  })

  const [map, setMap] = useState(/** @type google.maps.Map */(null))
  const [directionsResponse, setDirectionsResponse] = useState(null)
  const [distance, setDistance] = useState('')
  const [duration, setDuration] = useState('')
  const [lat, setLat] = useState('')  
  const [lng, setLng] = useState('')
  const [userId, setuserId] = useState('35')

  /** @type React.MutableRefObject<HTMLInputElement> */
  const originRef = useRef({})
  useEffect(() => {
    originRef.current.value = " "  
  }, []);
 
  /** @type React.MutableRefObject<HTMLInputElement> */
  const destinationRef = useRef()
  
  const origin = JSON.stringify(originRef.current.value)
  Geocode.fromAddress(origin, "AIzaSyBw1zPmfMUfiO2BozI3Z_YgEqg-bl8yTk0", "en", "at").then(
    (response) => {
      setLat(response.results[0].geometry.location.lat);
      setLng(response.results[0].geometry.location.lng);
      console.log(lat,lng);
    },
  );
  
  if (!isLoaded) {
    return <SkeletonText />
  }

  async function calculateRoute() {
    if (originRef.current.value === '' || destinationRef.current.value === '') {
      return originRef.currentValue
    }
    // eslint-disable-next-line no-undef
    const directionsService = new google.maps.DirectionsService()
    const results = await directionsService.route({
      origin: originRef.current.value,
      destination: destinationRef.current.value,
      // eslint-disable-next-line no-undef
      travelMode: google.maps.TravelMode.DRIVING,

    })
    setDirectionsResponse(results)
    setDistance(results.routes[0].legs[0].distance.text)
    setDuration(results.routes[0].legs[0].duration.text)
    const form = {userId}
  //  await fetch('http://localhost:8080/api/load', {
    await fetch('http://taxinowapigateway-env-1.eba-bhfmxdq9.eu-central-1.elasticbeanstalk.com/api/load', {   
        method: 'POST',
        headers: {
          'Content-Type': 'application/json' },
        mode: 'no-cors',
        body: new URLSearchParams(form)
      }).then((response) => console.log(response)).catch(error => console.log(error))

  }

  const refresh = async () => {
    
    const form = {userId}
   //  await fetch('http://localhost:8080/api/load', {
    await fetch('http://taxinowapigateway-env-1.eba-bhfmxdq9.eu-central-1.elasticbeanstalk.com/api/load', {
         method: 'POST',
         headers: {
           'Content-Type': 'application/json' },
         mode: 'no-cors',
         body: new URLSearchParams(form)
       }).then((response) => console.log(response)).catch(error => console.log(error))
  }
  const sendRequest = async () => {
    //console.log("SDASDAS");
    const form = { userId, lat, lng };
    //console.log(form.originRef.value);  

    await
      //fetch('http://localhost:8080/api/message', {
        fetch('http://taxinowapigateway-env-1.eba-bhfmxdq9.eu-central-1.elasticbeanstalk.com/api/message', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        mode: 'no-cors',
        body: new URLSearchParams(form),
      }).then((response) => console.log(response)).catch(error => console.log(error))


  }

  //const getCircularReplacer = () => {
  // const seen = new WeakSet();
  //   return (key, value) => {
  //    if (typeof value === "object" && value !== null) {
  //      if (seen.has(value)) {
  //       return;
  //     }
  //      seen.add(value);
  //   }
  // return value;
  // };
  //  };


  //JSON.stringify(form, getCircularReplacer());
  //  fetch('https://reqres.in/api/posts', requestOptions) //link to API endpoint that Petar will generate
  //    .then(response => response.json())
  // .then(data => setPostId(data.id));
  //  .catch(err => {
  //console.log("Error: " + err)
  //  })}
  // empty dependency array means this effect will only run once (like componentDidMount in classes)


  function clearRoute() {
    setDirectionsResponse(null)
    setDistance('')
    setDuration('')
    originRef.current.value = ''
    destinationRef.current.value = ''
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
                ref={destinationRef}
              />
            </Autocomplete>
          </Box>

          <ButtonGroup>
            <Button colorScheme='blue' type='submit' onClick={calculateRoute}  >
              Calculate Route
            </Button>
            <Button colorScheme='blue' type='submit' onClick={sendRequest} >
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