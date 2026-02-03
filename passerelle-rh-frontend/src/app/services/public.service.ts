import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PublicProfile {
    nom: string;
    prenom: string;
    scoreFiabilite: number;
    competences: any[];
    badges: any[];
    missions: any[];
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
