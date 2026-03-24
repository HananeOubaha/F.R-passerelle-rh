import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PublicProfile {
    nom: string;
    prenom: string;
    ville?: string;
    scoreFiabilite: number;
    competences: string[];
    badges: BadgeDTO[];
    missions: PublicMission[];
}

export interface BadgeDTO {
    nom: string;
    niveau: string;
    iconeUrl?: string;
}

export interface PublicMission {
    titre: string;
    description?: string;
    dateDebut?: string;
    dateFin?: string;
    validateurName: string;
    evaluation?: string;
    dateValid?: string;
}

export interface VerificationResult {
    status: string;
    utilisateur: string;
    mission: string;
    dateValidation: string;
    validateur: string;
}

@Injectable({
    providedIn: 'root'
})
export class PublicService {
    private apiUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient) { }

    getPublicProfile(token: string): Observable<PublicProfile> {
        return this.http.get<PublicProfile>(`${this.apiUrl}/users/public/${token}`);
    }

    verifyAttestation(missionId: number): Observable<VerificationResult> {
        return this.http.get<VerificationResult>(`${this.apiUrl}/attestations/verify/${missionId}`);
    }
}
