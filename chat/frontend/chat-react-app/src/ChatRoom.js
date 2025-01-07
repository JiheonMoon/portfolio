import React, { useState, useEffect, useRef } from "react";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import axios from "axios";

const ChatRoom = () => {
    const [nickname, setNickname] = useState(""); // 닉네임 입력 값
    const [fixedNickname, setFixedNickname] = useState(""); // 고정된 닉네임
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState("");
    const [stompClient, setStompClient] = useState(null);
    const [visitCounts, setVisitCounts] = useState({ todayVisits: 0, totalVisits: 0 });

    const messagesEndRef = useRef(null); // 스크롤을 위한 ref

    // 스크롤을 가장 아래로 이동하는 함수
    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    const formatTimestamp = (date) => {
        return new Date(date).toLocaleString("ko-KR", {
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit",
        }).replace(", ", " ");
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
                timestamp: formatTimestamp(new Date()),
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
        }).catch((error) => {
            console.error("Fetching Messages Error: ", error);
        })

        const socket = new SockJS("http://localhost:9090/ws");
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, () => {
            stompClient.subscribe("/topic/messages", (msg) => {
                const newMessage = JSON.parse(msg.body).body;
                setMessages((prev) => [...prev, newMessage]);
            });
        });

        setStompClient(stompClient);

        return () => {
            stompClient.disconnect();
        };
    }, []);

    useEffect(() => {
        const fetchVisitData = async () => {
            try {
                const recordResponse = await axios.post("http://localhost:9090/visit/record");
                console.log(recordResponse.data);

                const countResponse = await axios.get("http://localhost:9090/visit/count");
                console.log(countResponse.data);
                setVisitCounts(countResponse.data)
            } catch (error) {
                console.error('Error fetching visit data: ' + error.message);
            }
        };

        fetchVisitData(); // 비동기 함수 호출
    }, []);

    return (
        <div className="container">
            <div className="header">
                <h1>Chat Room</h1>
                <div className="visit-info">
                    <span>오늘 방문자: <strong>{visitCounts.todayVisits}</strong></span>
                    <span>총 방문자: <strong>{visitCounts.totalVisits}</strong></span>
                </div>
            </div>
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
            <div className="chat-container">
                {messages.map((msg, index) => (
                    <div
                        key={index}
                        className={`chat-bubble ${msg.sender === fixedNickname ? "right" : "left"
                            }`}
                    >
                        <strong>{msg.sender}</strong>: {msg.content} <em>({msg.timestamp})</em>
                    </div>
                ))}
                <div ref={messagesEndRef} />
            </div>
            <div className="chat-input-container">
                <input
                    type="text"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    onKeyPress={(e) => e.key === "Enter" && sendMessage()}
                    placeholder="메시지를 입력하세요"
                />
                <button onClick={sendMessage}>Send</button>
            </div>
        </div>
    )
}

export default ChatRoom