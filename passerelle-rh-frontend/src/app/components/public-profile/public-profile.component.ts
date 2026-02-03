import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { PublicService, PublicProfile } from '../../services/public.service';

@Component({
    selector: 'app-public-profile',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './public-profile.component.html',
    styleUrls: ['./public-profile.component.css']
})
export class PublicProfileComponent implements OnInit {
    profile: PublicProfile | null = null;
    isLoading = true;
    error = '';

    constructor(
        private route: ActivatedRoute,
        private publicService: PublicService
    ) { }

    ngOnInit(): void {
        const token = this.route.snapshot.paramMap.get('token');
        if (token) {
            this.loadProfile(token);
        } else {
            this.error = 'Lien de profil invalide.';
            this.isLoading = false;
        }
    }

    loadProfile(token: string): void {
        this.isLoading = true;
        this.publicService.getPublicProfile(token).subscribe({
            next: (data) => {
                this.profile = data;
                this.isLoading = false;
            },
            error: () => {
                this.error = 'Ce profil est privé ou n\'existe pas.';
                this.isLoading = false;
            }
        });
    }
}
