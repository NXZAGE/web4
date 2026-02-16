import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable, OnInit, signal } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';

export interface Hit {
  id: number
  x: number;
  y: number;
  r: number;
  isHit: boolean;
  exec: number;
  registredAt: string;
}

@Injectable({
  providedIn: 'root',
})
export class HitService {
  private http = inject(HttpClient);
  private readonly _hits = signal<Hit[]>([]);
  readonly hits = this._hits.asReadonly();

  constructor() {
    this.loadHits();
  }

  addHit(hit: Hit) {
    this._hits.update((prev) => [...prev, hit]);
    console.log(`hit service updates hits. hits:`);
    this._hits().forEach((hit_) => console.log(hit_));
  }

  hit(x: number, y: number, r: number) {
    console.log('hit() method of hitService');
    const body = new HttpParams().set('x', x).set('y', y).set('r', r);
    this.http
      .post<Hit>('/app/api/hits/hit', body, { timeout: 1000 })
      .pipe(
        tap((response) => console.log(response)),
        catchError((error) => {
          console.log(error);
          return throwError(() => new Error('Ошибка сервера'));
        }),
      )
      .subscribe({
        next: (hit) => {
          this.addHit(hit);
        },
        error: (err) => {
          console.log(err);
        },
      });
  }


  loadHits() {
    this.http
      .get<Hit[]>('/app/api/hits/get')
      .pipe(
        tap((response) => console.log(response)),
        catchError((error) => {
          console.log(error);
          return throwError(() => new Error('Ошибка сервера'));
        }),
      )
      .subscribe({
        next: (hits) => {
          console.log("User hits loded successfully");
          this._hits.set(hits);
        },
        error: (err) => {
          alert("Failed to load user hits");
          console.log(err);
        },
      });
  }
}
