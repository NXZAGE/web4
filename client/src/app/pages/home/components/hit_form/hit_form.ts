import { Component, inject, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';

// Импорт модулей PrimeNG
import { SelectButtonModule } from 'primeng/selectbutton';
import { SliderModule } from 'primeng/slider';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { HitService } from '../../../../services/hit_service';
import { map } from 'rxjs';

@Component({
  selector: 'hit-form',
  templateUrl: 'hit_form.html',
  styleUrl: 'hit_form.scss',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SelectButtonModule,
    SliderModule,
    ButtonModule,
    InputTextModule,
  ],
})
export class HitForm {
  private _hitService = inject(HitService);

  onFormSubmit = output<{ x: number; y: number; r: number }>();

  readonly coordinateOptions = [
    { label: '-4', value: -4 },
    { label: '-3', value: -3 },
    { label: '-2', value: -2 },
    { label: '-1', value: -1 },
    { label: '0', value: 0 },
    { label: '1', value: 1 },
    { label: '2', value: 2 },
    { label: '3', value: 3 },
    { label: '4', value: 4 },
  ];

  readonly radiusOptions = [
    { label: '0.5', value: 0.5 },
    { label: '1', value: 1 },
    { label: '1.5', value: 1.5 },
    { label: '2', value: 2 },
    { label: '2.5', value: 2.5 },
    { label: '3', value: 3 },
    { label: '3.5', value: 3.5 },
    { label: '4', value: 4 },
  ];

  hitForm = new FormGroup({
    x: new FormControl<number>(0, [Validators.required]),
    y: new FormControl<number>(0, [Validators.required, Validators.min(-3), Validators.max(5)]),
    r: new FormControl<number>(1, [Validators.required]),
  });

  readonly currentR = toSignal(this.hitForm.controls.r.valueChanges.pipe(map((v) => v ?? 1)), {
    initialValue: 1,
  });

  submit() {
    if (this.hitForm.valid) {
      const rawValue = this.hitForm.getRawValue();

      console.log('Данные формы:', rawValue);

      this._hitService.hit(rawValue.x ?? 0, rawValue.y ?? 0, rawValue.r ?? 0);
    }
  }
}
