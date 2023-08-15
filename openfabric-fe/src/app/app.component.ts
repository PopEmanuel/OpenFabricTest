import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, tap } from 'rxjs';
import { Container } from 'src/model/container';
import { Statistic } from 'src/model/statistics';
import { WorkerService } from 'src/service/worker.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  containers: Container[] = [];
  containerStatistics: Statistic[] = [];
  selectedContainerId: string = "";
  viewMode: 'inputs' | 'statistics' = 'inputs';
  constructor(private workerService: WorkerService, private snackBar: MatSnackBar) {}

  
  ngOnInit(): void{
    this.loadData();
  }

  loadData(): void{
    this.getContainers(0, 10);
  }

  selectContainer(containerId: string): void {
    if(this.selectedContainerId === containerId){
      this.getContainerStatistics(this.selectedContainerId);
      this.toggleStatisticsView();
    }
    this.selectedContainerId = containerId;
  }

  getContainers(page: number, size: number): void{
    this.workerService.getContainers(0, 10)
    .pipe(
      catchError(error => {
        console.error(error);
        return [];
      })
    )
    .subscribe((data: any) => {
      console.log(data);
      this.containers = data;
    })
  }

  getContainerStatistics(containerId: string): void{
    this.workerService.getContainerStatistics(containerId)
    .pipe(
      catchError(error => {
        console.error(error);
        return [];
      })
    )
    .subscribe((data: any) => {
      console.log(data);
      this.containerStatistics = data;
    })
  }

  startContainer(workerId: string): void{
    this.workerService.startContainer(workerId)
    .pipe(
      tap(response => {
        this.loadData();
      }),
      catchError(error => {
        this.openErrorSnackbar(error);
        throw error;
      })
    )
    .subscribe();
  }

  stopContainer(workerId: string): void{
    this.workerService.stopContainer(workerId)
    .pipe(
      tap(response => {
        this.loadData();
      }),
      catchError(error => {
        this.openErrorSnackbar(error);
        throw error;
      })
    )
    .subscribe();
  }

  addContainer(name: string, image: string, portStart: string, portEnd: string, env: string, hostName: string, cmd: string): void{
    let port: string = portStart + ':' + portEnd;
    this.workerService.addContainer(name, image, port, env, hostName, cmd)
    .pipe(
      tap(response => {
        this.loadData();
      }),
      catchError(error => {
        this.openErrorSnackbar(error);
        throw error;
      })
    )
    .subscribe();
  }

  getStatusClass(status: number): string {
    return status === 1 ? 'green' : 'red';
  }

  getStatusText(status: number): string {
    return status === 1 ? 'running' : 'stopped';
  }

  toggleStatisticsView(): void {
    this.viewMode = this.viewMode === 'inputs' ? 'statistics' : 'inputs';

  }

  openErrorSnackbar(error: any): void {
    console.error(error);
    this.snackBar.open(error.error.message, 'Close', {
      duration: 5000, 
      horizontalPosition: 'end',
      verticalPosition: 'top' 
    });
  }
}
