const divPosition = document.getElementById("coordinates");
let latitude=0;
let longitude=0;
function getLocation() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(getPosition);
  } else {
    divPosition.innerHTML = "Geolocation is not supported by this browser.";
  }
}
function getPosition(position) {
  latitude=position.coords.latitude;
  longitude=position.coords.longitude;
}

//now running all the functions
getLocation();