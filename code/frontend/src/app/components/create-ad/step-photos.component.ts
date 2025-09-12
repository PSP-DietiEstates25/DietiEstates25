import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AdDraftService } from '../../vecchioService/ad-draft.service';

@Component({
  selector: 'app-step-photos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './step-photos.component.html',
})
export class StepPhotosComponent {
  private draft = inject(AdDraftService);
  private router = inject(Router);

  previews = signal<string[]>([]);

  onFiles(e: Event) {
    const input = e.target as HTMLInputElement;
    const files = Array.from(input.files ?? []);
    this.draft.setPhotos(files);
    this.previews.set([]);
    files.forEach((f) => {
      const r = new FileReader();
      r.onload = () =>
        this.previews.update((arr) => [...arr, r.result as string]);
      r.readAsDataURL(f);
    });
  }

  back() {
    this.router.navigateByUrl('/agent/ads/new/details');
  }
  next() {
    this.router.navigateByUrl('/agent/ads/new/review');
  }
}
