export const apiAaaBackend = "/api/aaa-customer-backend";
export const apiAaaSelfService = "/api/aaa-customer-self-service";

export const getWebsocketUrlPrefix = () => {
    return ((window.location.protocol === "https:") ? "wss://" : "ws://") + window.location.host
}
