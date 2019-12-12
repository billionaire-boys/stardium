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
        };

        const join = () => {
            const roomList = document.getElementById('room-list');
            roomList ? roomList.addEventListener('click', roomService.joinRoom) : undefined;
        };

        const quit = () => {
            const quitButton = document.getElementById('quit-button');
            quitButton ? quitButton.addEventListener('click', roomService.quitRoom) : undefined;
        };

        const init = () => {
            signUp();
            update();
            join();
            quit();
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

        const saveRoom = event => {
            const title = document.getElementById('title').value;
            const city = document.getElementById('city').value;
            const section = document.getElementById('section').value;
            const detail = document.getElementById('detail').value;
            const startTime = document.getElementById('startTime').value;
            const endTime = document.getElementById('endTime').value;
            const playersLimit = document.getElementById('playersLimit').value;
            const intro = document.getElementById('intro').value;

            event.preventDefault();
            console.log(title, city, section, detail, startTime, endTime, playersLimit, intro);

            if (title === "" || city === "" || section === "" || detail === "" ||
                startTime === "" || endTime === "" || playersLimit === "" && intro === "") {
                alert('모든 항목을 입력해주세요!');
                return;
            }

            const roomBasicInfo = {
                title: title,
                address: {
                    city: city,
                    section: section,
                    detail: detail,
                },
                startTime: startTime,
                endTime: endTime,
                playersLimit: playersLimit,
                intro: intro,
            };

            const ifSucceed = (response) => {
                response.json().then(data => {
                    window.location.href = `/room/${data}`
                })
            };

            console.log(roomBasicInfo);

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
                playersLimit: playersLimit.value,
                intro: intro.value
            };

            const ifSucceed = (response) => {
                response.json().then(data => {
                    window.location.href = `/room/${data}`
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
            const targetButton = event.target;
            if (targetButton.classList.contains("room-join-button")) {
                let roomId = targetButton.dataset.roomId;

                const ifSucceed = (response) => {
                    alert("방에 입장되었습니다!");
                    response.json().then(data => {
                        window.location.href = `/room/${data}`
                    });
                };

                connector.fetchTemplateWithoutBody('/rooms/join/' + roomId,
                    connector.POST,
                    ifSucceed
                );

            }
        };

        const quitRoom = (event) => {

            const ifSucceed = (response) => {
                response.json().then(data => {
                    alert("나가는 데 성공했습니다!");
                    window.location.href = `/room/`
                })
            };
            const roomId = document.getElementById('roomId').value;

            connector.fetchTemplateWithoutBody('/rooms/quit/' + roomId,
                connector.POST,
                ifSucceed
            );
        }

        return {
            saveRoom,
            updateRoom,
            joinRoom,
            quitRoom
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