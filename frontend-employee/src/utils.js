export const apiAaaBackend = "/api/aaa-employee-backend";
export const apiAaaSelfService = "/api/aaa-employee-self-service";

export const getWebsocketUrlPrefix = () => {
    return ((window.location.protocol === "https:") ? "wss://" : "ws://") + window.location.host
}
