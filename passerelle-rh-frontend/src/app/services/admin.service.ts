import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AdminStats {
    totalUsers: number;
    totalMissions: number;
    totalValidations: number;
    usersByRole: { [key: string]: number };
    missionsByStatus: { [key: string]: number };
    topCompetences: { [key: string]: number };
}

@Injectable({
    providedIn: 'root'
})
export class AdminService {
    private apiUrl = 'http://localhost:8080/api/admin';

    constructor(private http: HttpClient) { }

    getStats(): Observable<AdminStats> {
        return this.http.get<AdminStats>(`${this.apiUrl}/stats`);
    }

    // --- User Management ---
    getAllUsers(): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiUrl}/users`);
    }

    updateUserRole(userId: number, role: string): Observable<void> {
        return this.http.put<void>(`${this.apiUrl}/users/${userId}/role?role=${role}`, {});
    }

    deleteUser(userId: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/users/${userId}`);
    }

    // --- Referential Management ---
    private refUrl = `${this.apiUrl}/referential`;

    getCompetences(): Observable<any[]> {
        return this.http.get<any[]>(`${this.refUrl}/competences`);
    }

    saveCompetence(comp: any): Observable<any> {
        return this.http.post<any>(`${this.refUrl}/competences`, comp);
    }

    deleteCompetence(id: number): Observable<void> {
        return this.http.delete<void>(`${this.refUrl}/competences/${id}`);
    }

    getBadges(): Observable<any[]> {
        return this.http.get<any[]>(`${this.refUrl}/badges`);
    }

    saveBadge(badge: any): Observable<any> {
        return this.http.post<any>(`${this.refUrl}/badges`, badge);
    }

    deleteBadge(id: number): Observable<void> {
        return this.http.delete<void>(`${this.refUrl}/badges/${id}`);
    }
}
