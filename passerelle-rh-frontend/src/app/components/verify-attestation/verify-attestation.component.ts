import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { PublicService, VerificationResult } from '../../services/public.service';

@Component({
    selector: 'app-verify-attestation',
    standalone: true,
    imports: [CommonModule, RouterModule],
    templateUrl: './verify-attestation.component.html',
    styleUrls: ['./verify-attestation.component.css']
})
export class VerifyAttestationComponent implements OnInit {
    result: VerificationResult | null = null;
    isLoading = true;
    error = '';

    constructor(
        private route: ActivatedRoute,
        private publicService: PublicService
    ) { }

    ngOnInit(): void {
        const missionIdStr = this.route.snapshot.paramMap.get('token'); // reused 'token' param name in routes if needed
        if (missionIdStr) {
            const missionId = parseInt(missionIdStr, 10);
            this.verify(missionId);
        } else {
            this.error = 'Identifiant d\'attestation manquant.';
            this.isLoading = false;
        }
    }

    verify(id: number): void {
        this.isLoading = true;
        this.publicService.verifyAttestation(id).subscribe({
            next: (data) => {
                this.result = data;
                this.isLoading = false;
            },
            error: (err) => {
                this.error = 'Cette attestation est invalide ou n\'a pas pu être vérifiée.';
                this.isLoading = false;
            }
        });
    }
}
