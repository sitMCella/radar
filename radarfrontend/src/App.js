import { BrowserRouter, Route, Routes } from 'react-router-dom'
import Radar from './components/Radar'
import './App.css'

function App() {
    return (
        <div className="App">
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Radar />} />
                </Routes>
            </BrowserRouter>
        </div>
    )
}

export default App
