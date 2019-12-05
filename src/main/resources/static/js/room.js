const ROOM_APP = (() => {
    'use strict';

    const RoomController = function () {
        const roomService = new RoomService();

        const signUp = () => {
            const signUpButton = document.getElementById('signUp');
            signUpButton ? signUpButton.addEventListener('click', roomService.saveRoom) : undefined;
        };

        const update = () => {
            const updateButton = document.getElementById('update-room-button');
            updateButton ? updateButton.addEventListener('click', roomService.updateRoom) : undefined;
        }

        const join = () => {
            const roomList = document.getElementById('room-list');
            roomList.addEventListener('click', roomService.joinRoom);
        }

        const init = () => {
            signUp();
            update();
            join();
        };

        return {
            init
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

            const ifSucceed = (response) => {
                response.json().then(data => {
                    window.location.href = `/rooms/${data}`
                })
            };

            connector.fetchTemplate('/rooms',
                connector.POST,
                header,
                JSON.stringify(roomBasicInfo),
                ifSucceed
            );
        };

        const updateRoom = event => {
            event.preventDefault();

            const roomBasicInfo = {
                title: title.value,
                address: {
                    city: city.value,
                    gu: gu.value,
                    detail: detail.value
                },
                startTime: startTime.value,
                endTime: endTime.value,
                playerNumbers: playerNumbers.value,
                intro: intro.value
            };

            const ifSucceed = (response) => {
                response.json().then(data => {
                    window.location.href = `/rooms/${data}`
                })
            };
            const roomId = document.getElementById('roomId').value;


            connector.fetchTemplate('/rooms/' + roomId,
                connector.PUT,
                header,
                JSON.stringify(roomBasicInfo),
                ifSucceed
            );
        };

        const joinRoom = (event) => {
            const targetButton = event.target
            if (targetButton.classList.contains("room-join-button")) {
                let roomId = targetButton.dataset.roomId

                const ifSucceed = (response) => {
                    alert("방에 입장되었습니다!");
                    response.json().then(data => {
                        window.location.href = `/rooms/${data}`
                    })
                };

                connector.fetchTemplateWithoutBody('/rooms/join/' + roomId,
                    connector.POST,
                    ifSucceed
                );

            }
        }

        return {
            saveRoom,
            updateRoom,
            joinRoom
        }
    };

    const init = () => {
        const roomController = new RoomController();
        roomController.init();
    };

    return {
        init,
    }
})();

ROOM_APP.init();