import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../services/admin.service';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-admin-referentials',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterModule],
    templateUrl: './admin-referentials.component.html',
    styleUrls: ['./admin-referentials.component.css']
})
export class AdminReferentialsComponent implements OnInit {
    competences: any[] = [];
    badges: any[] = [];

    newCompetenceName = '';

    newBadge = {
        nom: '',
        seuilScore: 0,
        iconeUrl: ''
    };

    activeTab: 'competences' | 'badges' = 'competences';
    isLoading = false;

    constructor(private adminService: AdminService) { }

    ngOnInit(): void {
        this.loadData();
    }

    loadData(): void {
        this.isLoading = true;
        this.adminService.getCompetences().subscribe(data => this.competences = data);
        this.adminService.getBadges().subscribe(data => {
            this.badges = data;
            this.isLoading = false;
        });
    }

    addCompetence(): void {
        if (!this.newCompetenceName.trim()) return;
        this.adminService.saveCompetence({ nom: this.newCompetenceName }).subscribe(() => {
            this.newCompetenceName = '';
            this.loadData();
        });
    }

    deleteCompetence(id: number): void {
        if (confirm('Supprimer cette compétence ?')) {
            this.adminService.deleteCompetence(id).subscribe(() => this.loadData());
        }
    }

    addBadge(): void {
        if (!this.newBadge.nom || !this.newBadge.iconeUrl) return;
        this.adminService.saveBadge(this.newBadge).subscribe(() => {
            this.newBadge = { nom: '', seuilScore: 0, iconeUrl: '' };
            this.loadData();
        });
    }

    deleteBadge(id: number): void {
        if (confirm('Supprimer ce badge ?')) {
            this.adminService.deleteBadge(id).subscribe(() => this.loadData());
        }
    }
}
