import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, tap } from 'rxjs';
import { WorkerService } from 'src/service/worker.service';
import { ErrorSnackBarComponent } from 'src/util/error-snack-bar/error-snack-bar.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  containers: any[] = [];
  selectedContainerId: string = "";
  constructor(private workerService: WorkerService, private snackBar: MatSnackBar) {}

  
  ngOnInit(): void{
    this.loadData();
  }

  loadData(): void{
    this.getContainers(0, 10);
  }

  selectContainer(containerId: string): void {
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

    this.workerService.getContainers(0, 10).subscribe(
      (data: any) => {
        this.containers = data;
      },
      (error: any) => {
        console.error(error);
      }
    );
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

  openErrorSnackbar(error: any): void {
    console.error(error);
    this.snackBar.open(error.error.message, 'Close', {
      duration: 5000, 
      horizontalPosition: 'end',
      verticalPosition: 'top' 
    });
  }
}
