import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject, Injector } from '@angular/core';
import { AuthService } from '../services/auth_service';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);


  if (req.headers.has('Skip-Interceptor')) {
    console.log("Interceptor skipped");
    return next(req.clone({ headers: req.headers.delete('Skip-Interceptor') }));
  }

  const authService = inject(AuthService);
  const token = authService.getToken();

  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log("Interceptor token set")
    return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        console.warn('Invalid token/ logout');
        
        authService.logout();
      }

      return throwError(() => error);
    })
  );;
  }

   console.log("Interceptor token unset")
  return next(req);
};