import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
    { path: '', redirectTo: '/login', pathMatch: 'full' },
    {
        path: 'login',
        loadComponent: () => import('./components/login/login.component').then(m => m.LoginComponent)
    },
    {
        path: 'register',
        loadComponent: () => import('./components/register/register.component').then(m => m.RegisterComponent)
    },
    {
        path: 'dashboard',
        loadComponent: () => import('./components/dashboard/dashboard.component').then(m => m.DashboardComponent),
        canActivate: [authGuard]
    },
    {
        path: 'validateur',
        loadComponent: () => import('./components/validateur-dashboard/validateur-dashboard.component').then(m => m.ValidateurDashboardComponent),
        canActivate: [authGuard]
    },
    {
        path: 'passeport',
        loadComponent: () => import('./components/passeport-rh/passeport-rh.component').then(m => m.PasseportRHComponent),
        canActivate: [authGuard]
    },
    {
        path: 'admin',
        loadComponent: () => import('./components/admin-dashboard/admin-dashboard.component').then(m => m.AdminDashboardComponent),
        canActivate: [adminGuard]
    },
    {
        path: 'admin/users',
        loadComponent: () => import('./components/admin-users/admin-users.component').then(m => m.AdminUsersComponent),
        canActivate: [adminGuard]
    },
    {
        path: 'admin/referentials',
        loadComponent: () => import('./components/admin-referentials/admin-referentials.component').then(m => m.AdminReferentialsComponent),
        canActivate: [adminGuard]
    },
    {
        path: 'p/:token',
        loadComponent: () => import('./components/public-profile/public-profile.component').then(m => m.PublicProfileComponent)
    },
    {
        path: 'verify/:token',
        loadComponent: () => import('./components/verify-attestation/verify-attestation.component').then(m => m.VerifyAttestationComponent)
    },
    { path: '**', redirectTo: '/login' }
];
