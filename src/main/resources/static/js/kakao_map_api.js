const MAP_API = (() => {

    const MapController = function (LOCATION) {
        const mapContainer = document.querySelector('#map'),
            mapOption = {
                center: new kakao.maps.LatLng(37.515412, 127.103040),
                level: 3
            };
        const mapService = new MapService(mapContainer, mapOption);

        const readLocation = () => {
            mapService.search(LOCATION);
        };

        const init = () => {
            readLocation();
        };

        return {
            init
        }
    };

    const MapService = function (mapContainer, mapOption) {
        const map = new kakao.maps.Map(mapContainer, mapOption);
        const geocoder = new kakao.maps.services.Geocoder();

        const searchAddress = (location) => {
            geocoder.addressSearch(location, (result, status) => {
                if (status === kakao.maps.services.Status.OK) {
                    statusOK(map, result, location);
                } else {
                    statusFailed(map);
                }
            });
        };

        const statusOK = (map, result, location) => {
            const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
            const marker = new kakao.maps.Marker({
                map: map,
                position: coords,
            });
            const infoWindow = new kakao.maps.InfoWindow({
                content: `<div class="info_window">${location}</div>`,
            });

            infoWindow.open(map, marker);
            map.setCenter(coords);
        };

        const statusFailed = (map) => {
            const centerPosition = new kakao.maps.LatLng(37.515412, 127.103040);
            const infoWindow = new kakao.maps.InfoWindow({
                content: '<div class="info_window"> 위치를 찾을 수 없습니다.</div>',
                position: centerPosition
            });

            infoWindow.open(map);
            map.setCenter(centerPosition);
        };

        return {
            search: searchAddress
        }
    };

    const init = (LOCATION) => {
        const mapController = new MapController(LOCATION);
        mapController.init();
    };

    return {
        init
    }

})();