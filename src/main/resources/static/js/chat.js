const socket = new SockJS("/stomp-connect");
const stompClient = Stomp.over(socket);
const chatBox = document.getElementById("chat-box");
const chatInput = document.getElementById("chat-input");
const sendButton = document.getElementById("chat-send-button");
const roomId = document.getElementById("roomId").value;
const nickname = document.getElementById("nickname").value;

const makeChatMessageBlock = (messageBody) =>
`<div class="comment">
    <div class="content">
        <a class="author">${messageBody.sender}</a>
        <div class="text">${messageBody.message}</div>
    </div>
</div>`;

const appendMessage = (messageBody) => {
    chatBox.insertAdjacentHTML("beforeend", makeChatMessageBlock(messageBody));
    chatBoxScrollToBottom();
};

const sendMessage = (event) => {
    const message = {
        roomId,
        sender: nickname,
        message: chatInput.value
    };

    stompClient.send(`/chat/${roomId}`, {}, JSON.stringify(message));
    chatInput.value = "";
};

const inputEnter = (event) => {
    if (event.key === "Enter") {
        sendMessage(event);
    }
};

const chatBoxScrollToBottom = () => {
    chatBox.scrollTop = chatBox.scrollHeight - chatBox.clientHeight;
};

sendButton.addEventListener("click", sendMessage);
chatInput.addEventListener("keyup", inputEnter);

stompClient.connect({}, () =>
    stompClient.subscribe(`/subscribe/chat/${roomId}`,
            message => appendMessage(JSON.parse(message.body))));
