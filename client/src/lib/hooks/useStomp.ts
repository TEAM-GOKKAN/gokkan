import React from 'react';
import SockJS from 'sockjs-client';
import { Client, IMessage } from '@stomp/stompjs';

interface ISubscription {
  destination: string;
  callback: (message: IMessage) => void;
}

const useStomp = (
  client: React.MutableRefObject<Client | undefined>,
  subscriptionList: ISubscription[]
) => {
  const connect = () => {
    client.current = new Client({
      webSocketFactory: () =>
        new SockJS(
          'http://3.38.59.40:8080/gokkan',
          {},
          {
            transports: ['xhr-polling'],
          }
        ),
      brokerURL: 'ws://3.38.59.40:8080/gokkan',
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        console.log('0 stomp onConnect: ');

        subscriptionList?.forEach((subs) => {
          client.current?.subscribe(subs.destination, subs.callback);
        });
      },
      onStompError: (frame) => {
        console.log('1 stomp error: ' + frame.headers['message']);
        console.log('Additional details: ' + frame.body);
      },
      onDisconnect: (frame) => {
        console.log('2 disconnect: ' + frame);
      },
      onWebSocketClose: (frame) => {
        console.log('3 Stomp WebSocket Closed' + frame);
      },
      debug: (str) => {
        console.log('4 debug:' + str);
      },
      onUnhandledMessage: (msg) => {
        console.log('5 unhandled Message' + msg);
      },
    });

    client.current?.activate();
  };

  const disconnect = () => {
    client.current?.deactivate();
  };

  return [connect, disconnect];
};

export default useStomp;
