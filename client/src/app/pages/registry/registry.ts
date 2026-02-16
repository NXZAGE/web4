import { Component, inject, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { CardModule } from 'primeng/card';
import { ReactiveFormsModule } from '@angular/forms';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth_service';

@Component({
  selector: 'registry',
  templateUrl: 'registry.html',
  styleUrl: 'registry.scss',
  imports: [
    ButtonModule,
    InputTextModule,
    PasswordModule,
    CardModule,
    ReactiveFormsModule,
    IconFieldModule,
    InputIconModule,
  ],
})
export class Registry implements OnInit {
  private router = inject(Router);
  private authService = inject(AuthService);
  registerForm!: FormGroup;

  constructor(private fb: FormBuilder) {

  }

  ngOnInit() {

    if(this.authService.isAuthenticated()) this.authService.logout();
    
    this.registerForm = this.fb.group(
      {
        username: ['', [Validators.required]],
        password: ['', [Validators.required, Validators.minLength(4)]],
        confirmPassword: ['', [Validators.required]],
      },
      { validators: this.passwordMatchValidator },
    );
  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');
    return password && confirmPassword && password.value !== confirmPassword.value
      ? { mismatch: true }
      : null;
  }

  onRegister() {
    if (this.registerForm.valid) {
      const formData = this.registerForm.getRawValue();
      this.authService
        .registry({ username: formData.username, password: formData.password })
        .subscribe({
          next: (response) => {
            alert('SuccessfullyRegistred');
            this.goToLogin();
          },
          error: (err) => {
            alert(`Registry filed: ${err.toString()}`);
          },
        });
    }
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
