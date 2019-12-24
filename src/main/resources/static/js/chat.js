const connector = FETCH_APP.FetchApi();
const socket = new SockJS("/stomp-connect");
const stompClient = Stomp.over(socket);
const chatBox = document.getElementById("chat-box");
const chatInput = document.getElementById("chat-input");
const sendButton = document.getElementById("chat-send-button");
const roomId = document.getElementById("room-id").value;
const playerId = document.getElementById("player-id").value;
const playerNickname = document.getElementById("player-nickname").value;

const makeChatMessageBlock = (messageBody) =>
`<div class="comment item">
    <div class="content">
        <a class="author">${messageBody.nickname}</a>
        <span class="date">${messageBody.timestamp}</span>
        <div class="text">${messageBody.message}</div>
    </div>
</div>`;

const makeMyChatMessageBlock = (messageBody) =>
`<div class="my-comment item">
    <div class="content">
        <span class="date">${messageBody.timestamp}</span>
        <a class="author">${messageBody.nickname}</a>
        <div class="text">${messageBody.message}</div>
    </div>
</div>`
;

const appendMessage = (messageBody) => {
    let chatMessage;

    if (playerNickname === messageBody.nickname) {
        chatMessage = makeMyChatMessageBlock(messageBody);
    } else {
        chatMessage = makeChatMessageBlock(messageBody);
    }
    chatBox.insertAdjacentHTML("beforeend", chatMessage);
    chatBoxScrollToBottom();
};

const sendMessage = (event) => {
    if (chatInput.value === "") {
        return;
    }

    const requestMessage = {
        roomId,
        playerId,
        contents: chatInput.value
    };

    stompClient.send(`/chat/${roomId}`, {}, JSON.stringify(requestMessage));
    chatInput.value = "";
};

const inputEnter = (event) => {
    if (event.key === "Enter") {
        sendMessage(event);
    }
};

const showPreviousMessages = () => {
    connector.fetchTemplateWithoutBody(
        `/chat/rooms/${roomId}`,
        connector.GET,
            response => response.json()
                .then(result => result.forEach(item => appendMessage(item))));
};

const chatBoxScrollToBottom = () => {
    chatBox.scrollTop = chatBox.scrollHeight - chatBox.clientHeight;
};

sendButton.addEventListener("click", sendMessage);
chatInput.addEventListener("keyup", inputEnter);

stompClient.connect({}, () =>
    stompClient.subscribe(`/subscribe/chat/${roomId}`,
            message => appendMessage(JSON.parse(message.body))));

showPreviousMessages();