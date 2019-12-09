var autocomplete;

var componentForm = {
    // 국적
    // locality: 'long_name',
    administrative_area_level_1: 'short_name',
    sublocality_level_1: 'short_name',
    sublocality_level_2: 'short_name',
    sublocality_level_3: 'long_name',
    sublocality_level_4: 'long_name',
    premise: 'long_name'
};

function initAutocomplete() {
    autocomplete = new google.maps.places.Autocomplete(
        document.getElementById('autocomplete'), {types: ['geocode']});

    autocomplete.setFields(['address_component']);

    autocomplete.addListener('place_changed', fillInAddress);
}

function fillInAddress() {
    var fullAddress = '';
    var place = autocomplete.getPlace();

    for (var component in componentForm) {
        if(document.getElementById(component)) {
            document.getElementById(component).value = '';
        }
    }

    for (var i = place.address_components.length - 1; i >= 0; i--) {
        var addressType = place.address_components[i].types[0];

        if (componentForm[addressType]) {
            var val = place.address_components[i][componentForm[addressType]];
            if(document.getElementById(addressType)) {
                document.getElementById(addressType).value = val;
                continue;
            }
            fullAddress += val + " ";

        }
    }
    document.getElementById('detail').value = fullAddress;
}

function geolocate() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var geolocation = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };

            var circle = new google.maps.Circle(
                {center: geolocation, radius: position.coords.accuracy});
            autocomplete.setBounds(circle.getBounds());
        });
    }
}