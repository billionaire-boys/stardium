const ROOM_APP = (() => {
    'use strict';

    const RoomController = function () {
        const roomService = new RoomService();

        const signUp = () => {
            const signUpButton = document.getElementById('signUp');
            signUpButton ? signUpButton.addEventListener('click', roomService.saveRoom) : undefined;
        };

        const init = () => {
            signUp();
        };

        return {
            init: init
        }
    };

    const RoomService = function () {
        const connector = FETCH_APP.FetchApi();
        const header = {
            'Content-Type': 'application/json; charset=UTF-8',
            'Accept': 'application/json'
        };

        const title = document.getElementById('title');
        const city = document.getElementById('city');
        const section = document.getElementById('section');
        const detail = document.getElementById('detail');
        const startTime = document.getElementById('startTime');
        const endTime = document.getElementById('endTime');
        const playerNumbers = document.getElementById('playerNumbers');
        const intro = document.getElementById('intro');


        const saveRoom = event => {
            event.preventDefault();

            const roomBasicInfo = {
                title: title.value,
                address: {
                    city: city.value,
                    section: section.value,
                    detail: detail.value
                },
                startTime: startTime.value,
                endTime: endTime.value,
                playerNumbers: playerNumbers.value,
                intro: intro.value
            };

            const ifSucceed = () => {
                alert("방 만들기 성공!");
                window.location.href = `/rooms/1`;
            };

            connector.fetchTemplate('/rooms',
                connector.POST,
                header,
                JSON.stringify(roomBasicInfo),
                ifSucceed
            );
        };

        return {
            saveRoom: saveRoom,
        }
    };

    const init = () => {
        const roomController = new RoomController();
        roomController.init();
    };

    return {
        init: init,
    }
})();

ROOM_APP.init();