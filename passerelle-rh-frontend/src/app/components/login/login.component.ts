import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Subject, combineLatest } from 'rxjs';
import { filter, takeUntil } from 'rxjs/operators';
import { AuthFacade } from '../../store/auth/auth.facade';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit, OnDestroy {
  loginForm!: FormGroup;
  errorMessage: string = '';
  isLoading: boolean = false;
  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private authFacade: AuthFacade,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    this.authFacade.loading$
      .pipe(takeUntil(this.destroy$))
      .subscribe((loading) => (this.isLoading = loading));

    this.authFacade.error$
      .pipe(takeUntil(this.destroy$))
      .subscribe((error) => (this.errorMessage = error || ''));

    combineLatest([this.authFacade.user$, this.authFacade.loading$])
      .pipe(
        takeUntil(this.destroy$),
        filter(([user, loading]) => !!user && !loading)
      )
      .subscribe(([user]) => {
        const role = user!.role;
        if (role === 'VALIDATOR') {
          this.router.navigate(['/validateur']);
        } else if (role === 'ADMIN') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/dashboard']);
        }
      });
  }

  // Helper pour récupérer facilement les contrôles
  get email() { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.errorMessage = '';
    this.authFacade.login(this.loginForm.value);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
