export const apiAaaBackend = "/api/aaa-customer-backend";
export const apiAaaSelfService = "/api/aaa-customer-self-service";

export const initStompClient = (connectCallback) => {
    const Stomp = require("@stomp/stompjs/bundles/stomp.umd.js").Stomp; // https://github.com/jmesnil/stomp-websocket/issues/119 https://stomp-js.github.io/stomp-websocket/codo/extra/docs-src/Usage.md.html
    const url = ((window.location.protocol === "https:") ? "wss://" : "ws://") + window.location.host + "/api/mortgage-application/stomp";
    const stompClient = Stomp.client(url);
    stompClient.reconnect_delay = 5000; // reconnect after 5 sec

    let connection = stompClient.connect({}, connectCallback);

    return {stompClient, connection};
};

export const closeStompClient = obj => {
    try {
        obj.connection.disconnect();
    } catch (ignored){}
    obj.stompClient.disconnect();
};