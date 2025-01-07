import React, { useState, useEffect, useRef  } from "react";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import axios from "axios";

const ChatRoom = () => {
    const [nickname, setNickname] = useState(""); // 닉네임 입력 값
    const [fixedNickname, setFixedNickname] = useState(""); // 고정된 닉네임
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState("");
    const [stompClient, setStompClient] = useState(null);

    const messagesEndRef = useRef(null); // 스크롤을 위한 ref

    // 스크롤을 가장 아래로 이동하는 함수
    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    const setNicknameHandler = () => {
        if (nickname.trim()) {
            setFixedNickname(nickname); // 닉네임 고정
        } else {
            alert("[익명]을 닉네임으로 사용합니다");
            setFixedNickname("익명");
        }
    };

    const sendMessage = () => {
        if (stompClient && message.trim() && fixedNickname) {
            const chatMessage = {
                sender: fixedNickname,
                content: message,
                timestamp: new Date().toISOString(),
            };
            stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
            setMessage("");
        } else if (!fixedNickname) {
            alert("닉네임을 먼저 설정해주세요.");
        }
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    useEffect(() => {
        axios.get("http://localhost:9090/messages").then((response) => {
            setMessages(response.data);
            console.log(response.data)
        });

        const socket = new SockJS("http://localhost:9090/ws");
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, () => {
            stompClient.subscribe("/topic/messages", (msg) => {
                const newMessage = JSON.parse(msg.body);
                setMessages((prev) => [...prev, newMessage]);
            });
        });

        setStompClient(stompClient);

        return () => {
            stompClient.disconnect();
        };
    }, []);

    return (
        <div>
            <h1>Chat Room</h1>
            {!fixedNickname ? (
                <div>
                    <label>
                        닉네임:
                        <input
                            type="text"
                            value={nickname}
                            onChange={(e) => setNickname(e.target.value)}
                            placeholder="닉네임을 입력하세요"
                        />
                    </label>
                    <button onClick={setNicknameHandler}>닉네임 설정</button>
                </div>
            ) : (
                <p>닉네임: <strong>{fixedNickname}</strong></p>
            )}
            <div style={{ border: "1px solid #ccc", height: "300px", overflow: "auto" }}>
                {messages.map((msg, index) => (
                    <div key={index}>
                        <strong>{msg.sender}</strong>: {msg.content} <em>({msg.timestamp})</em>
                    </div>
                ))}
                <div ref={messagesEndRef} />
            </div>
            <input
                type="text"
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                onKeyPress={(e) => e.key === "Enter" && sendMessage()}
                placeholder="메시지를 입력하세요"
            />
            <button onClick={sendMessage}>Send</button>
        </div>
    )
}

export default ChatRoom