import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { AuthService } from "../services/auth_service";
import { toObservable } from "@angular/core/rxjs-interop";
import { filter, map, take } from "rxjs";
import { FilterService } from "primeng/api";

export const AuthGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);


  return toObservable(authService.isInitialized).pipe(
    filter(initialized => initialized), 
    take(1), 
    map(() => {
      if (authService.isAuthenticated()) {
        return true;
      }
      
      console.log("AuthGuard blocked request");
      router.navigate(['/login']);
      return false;
    })
  );
};