import React, { useState } from "react";
import Widget from "./Widget";
import './App.css';

const App = () => {
  const [widgets, setWidgets] = useState([
      {
          id: 1,
          title: "Widget 1",
          content: "This is Widget 1",
          position: { x: 100, y: 100 },
          size: { width: 300, height: 200 },
      },
      {
          id: 2,
          title: "Widget 2",
          content: "This is Widget 2",
          position: { x: 400, y: 150 },
          size: { width: 350, height: 250 },
      },
  ]);


  return (
      <div className="app">
          <h1>Draggable Widgets</h1>
          {widgets.map((widget) => (
              <Widget
                  key={widget.id}
                  title={widget.title}
                  initialPosition={widget.position}
                  initialSize={widget.size}
              >
                  {widget.content}
              </Widget>
          ))}
      </div>
  );
};


export default App;
