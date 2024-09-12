import React, { useEffect, useState, useRef } from "react";
import { Client } from "@stomp/stompjs";

function ChatApp() {
    const [messages, setMessages] = useState([]);
    const [inputMessage, setInputMessage] = useState("");
    const stompClient = useRef(null); // Use useRef to persist stompClient across re-renders

    useEffect(() => {
        stompClient.current = new Client({
            brokerURL: "ws://localhost:8080/ws", // Your WebSocket server URL
            reconnectDelay: 1000,
            onConnect: () => {
                console.log("Connected");
                stompClient.current.subscribe("/topic/messages", (message) => {
                    setMessages((prevMessages) => [
                        ...prevMessages,
                        message.body,
                    ]);
                });
            },
            onDisconnect: () => {
                console.log("Disconnected");
            },
            onStompError: (error) => {
                console.error("STOMP Error: ", error);
            },
        });

        stompClient.current.activate();

        // Clean up the connection when the component unmounts
        return () => {
            if (stompClient.current) {
                stompClient.current.deactivate();
            }
        };
    }, []); // Empty dependency array means this effect runs once on mount and cleanup on unmount

    const sendMessage = () => {
        if (
            inputMessage.trim() !== "" &&
            stompClient.current &&
            stompClient.current.connected
        ) {
            stompClient.current.publish({
                destination: "/app/chat",
                body: inputMessage,
            });
            setInputMessage(""); // Clear input after sending
        } else {
            console.error("STOMP client is not connected");
        }
    };

    return (
        <div>
            <div>
                {messages.map((msg, index) => (
                    <p key={index}>{msg}</p>
                ))}
            </div>
            <input
                type="text"
                value={inputMessage}
                onChange={(e) => setInputMessage(e.target.value)}
            />
            <button onClick={sendMessage}>Send</button>
        </div>
    );
}

export default ChatApp;
