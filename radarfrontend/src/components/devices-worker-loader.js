export const createDevicesWorker = () => {
    return new Worker(new URL('./devices-sse-worker.js', import.meta.url), {
        type: 'module',
    })
}
