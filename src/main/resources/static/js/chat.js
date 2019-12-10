const socket = new SockJS('/stomp-connect');
const stompClient = Stomp.over(socket);
let roomId = 1; // TODO: 현재 들어온 방 정보를 대입
stompClient.connect({}, () => stompClient.subscribe("/subscribe/chat/" + roomId, message => console.log(message)));

// stompClient.send("/chat/1", {}, JSON.stringify({"roomId":1,"sender":"a","message":"abcd"}));