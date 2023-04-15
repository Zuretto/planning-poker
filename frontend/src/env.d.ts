interface ImportMetaEnv extends Readonly<Record<string, string>> {
    readonly VITE_BASE_URL: string;
    readonly VITE_WEBSOCKET_BASE_URL: string;
}

interface ImportMeta {
    readonly env: ImportMetaEnv
}
