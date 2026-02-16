import { ChangeDetectorRef, Component, computed, effect, inject } from '@angular/core';
import { AuthService } from '../../../services/auth_service';
import { Button } from 'primeng/button';

@Component({
  selector: 'main-header',
  templateUrl: 'header.html',
  styleUrl: 'header.scss',
  imports: [Button],
})
export class Header {
  private authService = inject(AuthService);
  authenticated = computed(() => this.authService.isAuthenticated());
  currentUser = computed(() => this.authService.user()?.username);

  constructor(private cdr: ChangeDetectorRef) {
    effect(() => {
      this.authenticated();
      this.cdr.markForCheck();
    });
  }

  logout() {
    this.authService.logout();
  }
}
