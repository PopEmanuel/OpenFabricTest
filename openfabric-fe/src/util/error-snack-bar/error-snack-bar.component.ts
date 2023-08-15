import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-error-snack-bar',
  templateUrl: './error-snack-bar.component.html',
  styleUrls: ['./error-snack-bar.component.css']
})
export class ErrorSnackBarComponent {
  constructor(private snackBar: MatSnackBar) {}

  openSnackbar(errorMessage: string): void {
    this.snackBar.open(errorMessage, 'Close', {
      duration: 5000, 
      horizontalPosition: 'end',
      verticalPosition: 'top' 
    });
  }
}
