import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, RegisterRequest } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  userData: RegisterRequest & { role: string } = {
    nom: '',
    prenom: '',
    email: '',
    password: '',
    ville: '',
    role: 'USER'
  };

  errorMessage: string = '';
  isLoading: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  onSubmit(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.authService.register(this.userData as RegisterRequest).subscribe({
      next: (response) => {
        const role = response.role;
        if (role === 'VALIDATOR') {
          this.router.navigate(['/validateur']);
        } else {
          this.router.navigate(['/dashboard']);
        }
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'Une erreur est survenue lors de l\'inscription';
        console.error('Registration error', error);
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
}
