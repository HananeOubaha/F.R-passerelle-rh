import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, filter, switchMap, take } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    private isRefreshing = false;
    private refreshTokenSubject = new BehaviorSubject<string | null>(null);

    constructor(private authService: AuthService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const authReq = this.addAuthHeader(req, this.authService.getToken());

        return next.handle(authReq).pipe(
            catchError((error: HttpErrorResponse) => {
                if (!this.shouldHandle401(error, req.url)) {
                    return throwError(() => error);
                }
                return this.handle401Error(authReq, next);
            })
        );
    }

    private addAuthHeader(request: HttpRequest<any>, token: string | null): HttpRequest<any> {
        if (!token) {
            return request;
        }
        return request.clone({
            setHeaders: {
                Authorization: `Bearer ${token}`
            }
        });
    }

    private shouldHandle401(error: HttpErrorResponse, url: string): boolean {
        if (error.status !== 401) {
            return false;
        }

        // Eviter boucle infinie sur endpoints auth
        if (url.includes('/api/auth/login') || url.includes('/api/auth/register') || url.includes('/api/auth/refresh')) {
            return false;
        }

        return !!this.authService.getRefreshToken();
    }

    private handle401Error(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!this.isRefreshing) {
            this.isRefreshing = true;
            this.refreshTokenSubject.next(null);

            return this.authService.refreshAccessToken().pipe(
                switchMap((response) => {
                    this.isRefreshing = false;
                    this.refreshTokenSubject.next(response.accessToken);
                    return next.handle(this.addAuthHeader(request, response.accessToken));
                }),
                catchError((refreshError) => {
                    this.isRefreshing = false;
                    this.authService.logout();
                    return throwError(() => refreshError);
                })
            );
        }

        return this.refreshTokenSubject.pipe(
            filter((token) => token != null),
            take(1),
            switchMap((token) => next.handle(this.addAuthHeader(request, token)))
        );
    }
}
