import React, { useState } from "react";

const Widget = ({ title, children, initialPosition, initialSize }) =>{
    const [position, setPosition] = useState(initialPosition);
    const [dragging, setDragging] = useState(false);
    const [offset, setOffset] = useState({ x: 0, y: 0 });
    const [size, setSize] = useState(initialSize);

    const handleMouseDown = (e) => {
        setDragging(true);
        setOffset({
            x: e.clientX - position.x,
            y: e.clientY - position.y,
        });
    };

    const handleMouseMove = (e) => {
        if (dragging) {
            setPosition({
                x: e.clientX - offset.x,
                y: e.clientY - offset.y,
            });
        }
    };

    const handleMouseUp = () => {
        setDragging(false);
    };

    return (
        <div
            className="widget"
            style={{
                top: position.y,
                left: position.x,
                width: size.width,
                height: size.height,
            }}
            onMouseMove={handleMouseMove}
            onMouseUp={handleMouseUp}
            onMouseLeave={handleMouseUp}
        >
            <div className="widget-header" onMouseDown={handleMouseDown}>
                {title}
            </div>

            <div className="widget-content">{children}</div>
        </div>
    );
    
}

export default Widget