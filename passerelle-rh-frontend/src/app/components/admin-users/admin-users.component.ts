import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../services/admin.service';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-admin-users',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterModule],
    templateUrl: './admin-users.component.html',
    styleUrls: ['./admin-users.component.css']
})
export class AdminUsersComponent implements OnInit {
    users: any[] = [];
    filteredUsers: any[] = [];
    searchTerm = '';
    selectedRole = '';
    isLoading = true;

    roles = ['ADMIN', 'UTILISATEUR', 'VALIDATEUR'];

    constructor(private adminService: AdminService) { }

    ngOnInit(): void {
        this.loadUsers();
    }

    loadUsers(): void {
        this.isLoading = true;
        this.adminService.getAllUsers().subscribe({
            next: (data) => {
                this.users = data;
                this.applyFilters();
                this.isLoading = false;
            },
            error: () => {
                this.isLoading = false;
                alert('Erreur lors du chargement des utilisateurs.');
            }
        });
    }

    applyFilters(): void {
        this.filteredUsers = this.users.filter(user => {
            const matchSearch = (user.nom + ' ' + user.prenom + ' ' + user.email)
                .toLowerCase().includes(this.searchTerm.toLowerCase());
            const matchRole = !this.selectedRole || user.role === this.selectedRole;
            return matchSearch && matchRole;
        });
    }

    changeRole(userId: number, newRole: string): void {
        if (confirm(`Changer le rôle de cet utilisateur en ${newRole} ?`)) {
            this.adminService.updateUserRole(userId, newRole).subscribe({
                next: () => {
                    this.loadUsers();
                },
                error: () => alert('Erreur lors du changement de rôle.')
            });
        }
    }

    deleteUser(userId: number): void {
        if (confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ? Cette action est irréversible.')) {
            this.adminService.deleteUser(userId).subscribe({
                next: () => {
                    this.loadUsers();
                },
                error: () => alert('Erreur lors de la suppression.')
            });
        }
    }
}
