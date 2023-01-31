import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { Provider } from 'jotai';
import { BrowserRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { queryClientAtom } from 'jotai-tanstack-query';

const queryClient = new QueryClient({});

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <QueryClientProvider client={queryClient}>
    <Provider initialValues={[[queryClientAtom, queryClient]]}>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </Provider>
  </QueryClientProvider>
);
