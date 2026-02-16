import { HttpClient, HttpParams } from '@angular/common/http';
import { computed, inject, Injectable, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs';

export interface User {
  id: number;
  username: string;
}

export interface AuthResponse {
  username: string;
  token: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private readonly API_URL = '/app/api';
  private readonly TOKEN_KEY = 'target_app_token';

  private _user = signal<User | null>(null);
  readonly user = this._user.asReadonly();
  readonly isAuthenticated = computed(() => !!this._user());
  isInitialized = signal(false); 

  constructor() {
    console.log('constructor auth service');
    this.tryAutoLogin();
  }


  login(credentials: { username: string; password: string }) {
    const body = new HttpParams()
      .set('username', credentials.username)
      .set('password', credentials.password);
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, body).pipe(
      tap((Response) => {
        console.log(Response);
        localStorage.setItem(this.TOKEN_KEY, Response.token);
        this._user.set({ id: 0, username: Response.username });
      }),
    );
  }

  registry(credentials: { username: string; password: string }) {
    const body = new HttpParams()
      .set('username', credentials.username)
      .set('password', credentials.password);
    return this.http.post<AuthResponse>(`${this.API_URL}/register`, body);
  }

  logout() {
    console.log('LOGOUT');
    localStorage.removeItem(this.TOKEN_KEY);
    this._user.set(null);
    this.router.navigate(['/login']).then(success => console.log(success));
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  private tryAutoLogin() {
    const token = this.getToken();
    if (token) {
      this.http.get<User>(`${this.API_URL}/login/me`, {headers: {Authorization: `Bearer ${this.getToken()}`, "Skip-Interceptor": 'true'}}).subscribe({
        next: (user) => {
          this._user.set(user);
          this.isInitialized.set(true);
        },
        error: (e) => {
          console.log(e);
          this.isInitialized.set(true);
          this.logout();
        },
      });
    } else {
      this.isInitialized.set(true);
    }
  }
}
