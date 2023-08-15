import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from 'src/constants/constants';

@Injectable({
  providedIn: 'root'
})
export class WorkerService {
  private baseUrl: string = 'http://localhost:8080/api/v1/worker';

  constructor(private http: HttpClient) {}

  getContainers(page: number, size: number): Observable<any> {
    console.log("Getting workers");

    return this.http.get(`${this.baseUrl}?page=${page}&size=${size}`);
  }

  startContainer(workerId: string): Observable<any> {
    console.log(`Starting worker with id = ${workerId}`);
    
    return this.http.post(`${this.baseUrl}/start?workerId=${workerId}`, null);
  }

  stopContainer(workerId: string): Observable<any> {
    console.log(`Stopping worker with id = ${workerId}`);
    
    return this.http.post(`${this.baseUrl}/stop?workerId=${workerId}`, null);
  }

  addContainer(name: string, image: string, port: string, env: string, hostName: string, cmd: string): Observable<any>{
    console.log("Adding a container");
    
    const requestBody: any = {
      name: name,
      image: image,
      ports: port
    };

    if (env) {
      requestBody.env = env;
    }

    if (hostName) {
      requestBody.hostName = hostName;
    }

    if (cmd) {
      requestBody.cmd = cmd;
    }

    console.log(requestBody);

    return this.http.post(`${this.baseUrl}`, requestBody)
  }

}
