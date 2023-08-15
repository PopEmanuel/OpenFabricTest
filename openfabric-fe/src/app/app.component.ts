import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  containerStatus: string = 'running'; // Set the initial status, or get it from your data

  getStatusClass(): string {
    return this.containerStatus === 'running' ? 'green' : 'red';
  }
}
