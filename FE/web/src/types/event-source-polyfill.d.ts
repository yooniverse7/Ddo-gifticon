declare module 'event-source-polyfill' {
  export class EventSourcePolyfill implements EventSource {
    constructor(url: string, configuration?: EventSourceInitDict);

    readonly CLOSED: number;
    readonly CONNECTING: number;
    readonly OPEN: number;
    readonly readyState: number;
    readonly url: string;

    close(): void;
    addEventListener(type: string, listener: EventListener): void;
    removeEventListener(type: string, listener: EventListener): void;
    dispatchEvent(event: Event): boolean;

    onerror: ((event: Event) => void) | null;
    onmessage: ((event: MessageEvent) => void) | null;
    onopen: ((event: Event) => void) | null;
  }

  export interface EventSourceInitDict {
    headers?: Record<string, string>;
    withCredentials?: boolean;
    heartbeatTimeout?: number;
    lastEventId?: string;
  }
}
