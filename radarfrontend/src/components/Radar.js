import React, { useEffect, useRef, useState } from 'react'
import { Canvas, useFrame } from '@react-three/fiber'
import { Grid } from '@react-three/drei'

function Radar() {
    const [devices, setDevices] = useState([])
    const [signals, setSignals] = useState([])
    const [pulseRadius, setPulseRadius] = useState([])

    function Device(device) {
        const ref = useRef()

        useFrame(() => {
            ref.current.position.x = device.device.longitude
            ref.current.position.y = device.device.latitude
        })

        return (
            <mesh ref={ref}>
                <circleGeometry args={[5, 32]} />
                <meshStandardMaterial color="hotpink" />
            </mesh>
        )
    }

    function Pulse(device) {
        const ref = useRef()

        useFrame(({ clock }) => {
            const t = clock.getElapsedTime()
            const scale = 1 + Math.sin(t * 2) * 60
            ref.current.scale.set(scale, scale, scale)
            ref.current.position.x = device.device.longitude
            ref.current.position.y = device.device.latitude
        })

        return (
            <mesh ref={ref}>
                <ringGeometry args={[0.98, 1, 64]} />
                <meshStandardMaterial color="hotpink" />
            </mesh>
        )
    }

    function Signal(signal) {
        const ref = useRef()

        useFrame(() => {
            ref.current.position.x = signal.signal.longitude
            ref.current.position.y = signal.signal.latitude
        })

        return (
            <mesh ref={ref}>
                <circleGeometry args={[5, 32]} />
                <meshStandardMaterial color="lime" />
            </mesh>
        )
    }

    function removeOldSignals() {
        const now = Date.now()
        const threshold = 2000
        setSignals((prevSignals) =>
            prevSignals.filter((item) => now - item.creationtime <= threshold)
        )
    }

    useEffect(() => {
        const interval = setInterval(() => removeOldSignals(), 2000)

        // Development environment prefix: http://localhost:9090
        const devicesEventSource = new EventSource('/api/devices')
        devicesEventSource.onmessage = (event) => {
            if (event.data) {
                const device = JSON.parse(event.data)
                if (!devices.some((item) => device.id === item.id)) {
                    setDevices((prevDevices) => [...prevDevices, device])
                    setPulseRadius((prevPulseRadius) => [...prevPulseRadius, 0])
                }
            }
        }

        devicesEventSource.onerror = (error) => {
            console.error('Devices EventSource failed:', error)
            devicesEventSource.close()
        }

        // Development environment prefix: http://localhost:9090
        const signalsEventSource = new EventSource('/api/signals')
        signalsEventSource.onmessage = (event) => {
            if (event.data) {
                const signal = JSON.parse(event.data)
                if (!signals.some((item) => signal.id === item.id)) {
                    if (
                        !signals.some(
                            (item) =>
                                signal.id < item.id &&
                                signal.deviceId === item.deviceId &&
                                signal.objId === item.objId
                        )
                    ) {
                        if (
                            !signals.some(
                                (item) =>
                                    signal.deviceId === item.deviceId &&
                                    signal.objId === item.objId
                            )
                        ) {
                            setSignals((prevSignals) => [
                                ...prevSignals,
                                signal,
                            ])
                        } else {
                            const cleanedSignals = signals.filter(
                                (item) =>
                                    signal.deviceId !== item.deviceId ||
                                    (signal.deviceId === item.deviceId &&
                                        signal.objId !== item.objId)
                            )
                            setSignals([...cleanedSignals, signal])
                        }
                    }
                }
            }
        }

        signalsEventSource.onerror = (error) => {
            console.error('Signals EventSource failed:', error)
            signalsEventSource.close()
        }

        return () => {
            devicesEventSource.close()
            signalsEventSource.close()
            clearInterval(interval)
        }
    }, [devices, signals, pulseRadius])

    return (
        <div>
            <div style={{ width: '100vw', height: '100vh' }}>
                <Canvas camera={{ position: [0, 0, 5] }} orthographic>
                    <ambientLight intensity={0.5} />
                    <pointLight position={[10, 10, 10]} />

                    <Grid
                        args={[100, 100]}
                        infiniteGrid
                        cellSize={20}
                        sectionSize={20}
                        fadeDistance={5000}
                        fadeStrength={0}
                        rotation={[Math.PI / 2, 0, 0]}
                        cellColor="green"
                        sectionColor="green"
                    />

                    {devices.map((device, index) => (
                        <Device key={index} device={device} />
                    ))}

                    {devices.map((device, index) => (
                        <Pulse key={index} device={device} />
                    ))}

                    {signals.map((signal, index) => (
                        <Signal key={index} signal={signal} />
                    ))}
                </Canvas>
            </div>
        </div>
    )
}

export default Radar
