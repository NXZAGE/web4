import { Component, inject } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { CardModule } from 'primeng/card';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { CascadeSelectModule } from 'primeng/cascadeselect';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth_service';

@Component({
  selector: 'login',
  templateUrl: 'login.html',
  styleUrl: 'login.scss',
  imports: [
    ButtonModule,
    InputTextModule,
    PasswordModule,
    CardModule,
    ReactiveFormsModule,
    IconFieldModule,
    InputIconModule,
    CascadeSelectModule,
  ],
})
export class Login {
  private router = inject(Router);
  private authService = inject(AuthService);
  loginForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.authService.logout();
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(4)]],
    });
  }

  onSubmit() {
    const creds = this.loginForm.getRawValue();
    console.log(`submitting creds... ${creds}`);
    console.log(creds);
    this.authService.login(creds).subscribe({
      next: (response) => {
        alert('successfully logged in');
        this.router.navigate(['/home']);
      },
      error: (err) => {
        alert('Login failed');
      },
    });
  }

  goToRegister() {
    this.router.navigate(['/registry']);
  }
}
