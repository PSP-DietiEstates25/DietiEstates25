import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { VisitControllerService } from '../../services/services/visit-controller.service';
import { VisitRequest } from '../../services/models/visit-request';

@Component({
  selector: 'app-visit-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './visit-form.component.html',
})
export class VisitFormComponent {
  private fb = inject(FormBuilder);
  private api = inject(VisitControllerService);

  @Input({ required: true }) adId!: number;
  @Input() isLoggedIn = false;

  @Output() loginRequired = new EventEmitter<void>();
  @Output() success = new EventEmitter<void>();

  loading = false;
  ok: string | null = null;
  err: string | null = null;

  form = this.fb.nonNullable.group({
    date: ['', Validators.required],
    time: ['', Validators.required],
  });

  submit() {
    if (!this.isLoggedIn) {
      this.loginRequired.emit();
      return;
    }
    if (this.form.invalid) return;

    this.loading = true;
    this.ok = this.err = null;

    const body: VisitRequest = {
      category: 'SALE',
      status: 'PENDING',
      date: this.form.value.date!,
      time: this.form.value.time!,
      userEmail: 'guest@public.local',
    };

    this.api.createVisit({ realestateid: this.adId, body }).subscribe({
      next: () => (this.ok = 'Richiesta inviata!'),
      error: () => (this.err = 'Errore durante l’invio della richiesta.'),
      complete: () => {
        this.loading = false;
        if (this.ok) this.success.emit();
      },
    });
  }
}
