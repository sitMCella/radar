export const createSignalsWorker = () => {
    return new Worker(new URL('./signals-sse-worker.js', import.meta.url), {
        type: 'module',
    })
}
