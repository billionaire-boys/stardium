const connector = FETCH_APP.FetchApi();
const socket = new SockJS("/stomp-connect");
const stompClient = Stomp.over(socket);
const chatBox = document.getElementById("chat-box");
const chatInput = document.getElementById("chat-input");
const sendButton = document.getElementById("chat-send-button");
const roomId = document.getElementById("room-id").value;
const playerId = document.getElementById("player-id").value;

const makeChatMessageBlock = (messageBody) =>
`<div class="comment">
    <div class="content">
        <a class="author">${messageBody.nickname}</a>
        <div class="metadata">
            <span class="date">${messageBody.timestamp}</span>
        </div>
        <div class="text">${messageBody.message}</div>
    </div>
</div>`;

const appendMessage = (messageBody) => {
    chatBox.insertAdjacentHTML("beforeend", makeChatMessageBlock(messageBody));
    chatBoxScrollToBottom();
};

const sendMessage = (event) => {
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