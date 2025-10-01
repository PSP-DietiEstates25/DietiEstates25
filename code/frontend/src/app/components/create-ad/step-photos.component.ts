// src/app/components/create-ad/step-photos.component.ts
import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';

@Component({
  selector: 'app-step-photos',
  standalone: true,
  templateUrl: './step-photos.component.html',
})
export class StepPhotosComponent {
  private router = inject(Router);
  private facade = inject(CreateAdFacade);

  previews = signal<string[]>(this.facade.draft().imagesBase64 ?? []);

  async onFiles(ev: Event) {
    const input = ev.target as HTMLInputElement | null;
    const files = input?.files;
    if (!files || files.length === 0) return;

    const toBase64 = (f: File) =>
      new Promise<string>((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => resolve(reader.result as string);
        reader.onerror = reject;
        reader.readAsDataURL(f);
      });

    const newImages: string[] = [];
    for (const f of Array.from(files)) {
      if (!f.type.startsWith('image/')) continue;
      try {
        const b64 = await toBase64(f);
        newImages.push(b64);
      } catch {}
    }

    if (newImages.length > 0) {
      const merged = [...this.previews(), ...newImages];
      this.previews.set(merged);
      this.facade.setImagesBase64(merged);
    }

    if (input) input.value = '';
  }

  back() {
    this.router.navigateByUrl('/agent/ads/new/details');
  }

  next() {
    this.router.navigateByUrl('/agent/ads/new/review');
  }
}
