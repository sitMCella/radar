import React, { useEffect, useMemo, useRef, useState } from 'react'
import { Observable } from 'rxjs'
import { bufferTime } from 'rxjs/operators'
import { Canvas, useFrame } from '@react-three/fiber'
import { Grid } from '@react-three/drei'
import { createDevicesWorker } from './devices-worker-loader'
import { createSignalsWorker } from './signals-worker-loader'

function Radar() {
    const [devices, setDevices] = useState([])
    const [signals, setSignals] = useState([])

    const renderedDevices = useMemo(() => {
        return devices.map((device, index) => (
            <Device key={index} device={device} />
        ))
    }, [devices])

    const renderedPulses = useMemo(() => {
        return devices.map((device, index) => (
            <Pulse key={index} device={device} />
        ))
    }, [devices])

    const renderedSignals = useMemo(() => {
        return signals.map((signal, index) => (
            <Signal key={index} signal={signal} />
        ))
    }, [signals])

    const createDevicesObservable = () => {
        return new Observable((subscriber) => {
            const worker = createDevicesWorker()

            worker.onmessage = (w) => {
                if (w.data?.error) {
                    subscriber.error(w.data)
                    return
                }
                subscriber.next(w.data)
            }

            return () => {
                worker.terminate()
            }
        })
    }

    const createSignalsObservable = () => {
        return new Observable((subscriber) => {
            const worker = createSignalsWorker()

            worker.onmessage = (w) => {
                if (w.data?.error) {
                    subscriber.error(w.data)
                    return
                }
                subscriber.next(w.data)
            }

            return () => {
                worker.terminate()
            }
        })
    }

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
            const scale = 1 + Math.sin(t * 2) * device.device.radius
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

    useEffect(() => {
        let updatedDevices = []
        let updatedSignal = []

        const interval = setInterval(() => {
            const now = Date.now()
            const threshold = 1500
            updatedSignal = updatedSignal.filter((item) => now - item.receivedtime < threshold)
            setSignals(updatedSignal)
        }, 1500)

        const devicesObservable = createDevicesObservable().pipe(
            bufferTime(500)
        )

        const devicesSubscription = devicesObservable.subscribe({
            next: (batch) => {
                batch.forEach((data) => {
                    if (!data) return
                    const device = JSON.parse(data)
                    if (!updatedDevices.some((item) => device.id === item.id)) {
                        updatedDevices = [...updatedDevices, device]
                    }
                })
                setDevices(updatedDevices)
            },
            error: (err) => console.error('Devices SSE error:', err),
        })

        const signalsObservable = createSignalsObservable().pipe(
            bufferTime(250)
        )

        const signalsSubscription = signalsObservable.subscribe({
            next: (batch) => {
                batch.forEach((data) => {
                    if (!data) return
                    const signal = JSON.parse(data)
                    signal.receivedtime = Date.now()
                    if (
                        !updatedSignal.some(
                            (item) =>
                                signal.deviceId === item.deviceId &&
                                signal.objId === item.objId
                        )
                    ) {
                        updatedSignal = [...updatedSignal, signal]
                    } else {
                        updatedSignal = [
                            ...updatedSignal.filter(
                                (item) =>
                                    signal.deviceId !== item.deviceId ||
                                    (signal.deviceId === item.deviceId &&
                                        signal.objId !== item.objId)
                            ),
                            signal,
                        ]
                    }
                })
                setSignals(updatedSignal)
            },
            error: (err) => console.error('Signals SSE error:', err),
        })

        return () => {
            devicesSubscription.unsubscribe()
            signalsSubscription.unsubscribe()
            clearInterval(interval)
        }
    }, [])

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

                    {renderedDevices}

                    {renderedPulses}

                    {renderedSignals}
                </Canvas>
            </div>
        </div>
    )
}

export default Radar
