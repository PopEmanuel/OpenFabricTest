export interface Container {
    id: string;
    name: string;
    image: string;
    hostName: string;
    cmd: string;
    env: string;
    port: string;
    status: number;
  }