// Development environment prefix: http://localhost:9090
const eventSource = new EventSource('/api/signals')

eventSource.onmessage = (event) => {
    postMessage(event.data)
}

eventSource.onerror = (err) => {
    postMessage({ error: 'SSE error', details: err })
}
