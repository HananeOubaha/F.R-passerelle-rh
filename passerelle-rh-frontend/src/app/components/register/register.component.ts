import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, RegisterRequest } from '../../services/auth.service';
import { passwordMatchValidator } from '../../validators/custom.validators';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  errorMessage: string = '';
  isLoading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      nom: ['', [Validators.required, Validators.pattern(/^[A-Za-zÀ-ÿ\s\-]+$/)]],
      prenom: ['', [Validators.required, Validators.pattern(/^[A-Za-zÀ-ÿ\s\-]+$/)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', [Validators.required]],
      ville: [''],
      role: ['USER']
    }, { validators: passwordMatchValidator });
  }

  // Helpers pour récupérer les contrôles facilement
  get nom() { return this.registerForm.get('nom'); }
  get prenom() { return this.registerForm.get('prenom'); }
  get email() { return this.registerForm.get('email'); }
  get password() { return this.registerForm.get('password'); }
  get confirmPassword() { return this.registerForm.get('confirmPassword'); }
  get ville() { return this.registerForm.get('ville'); }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    // Enlever confirmPassword avant d'envoyer au backend
    const { confirmPassword, ...registerData } = this.registerForm.value;

    this.authService.register(registerData as RegisterRequest).subscribe({
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
